package com.binoy.vibhinna;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;
import android.util.Xml;
import com.binoy.vibhinna.R;

public class DatabaseUtils {
	private static final String TAG = "DatabaseUtils";
	private static final String XML_DUMP_PATH = "/mnt/sdcard/multiboot/.mbm/vfsdbdump.xml";
	private static final String MBM_CONFIG_FOLDER = "/mnt/sdcard/multiboot/.mbm";
	private static final File xmldumpfile = new File(XML_DUMP_PATH);
	private static final File mbmconfigdir = new File(MBM_CONFIG_FOLDER);
	private static final String VFS_ELEMENT_ROOT = "vfs";

	public static int[] scanFolder(SQLiteDatabase database) {
		int added = 0, deleted = 0;
		String extState = Environment.getExternalStorageState();
		readXML(database);
		if (extState.equals(Environment.MEDIA_MOUNTED)) {
			FileFilter filterDirectoriesOnly = new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory();
				}
			};
			Cursor pathcursora = database.query(
					DatabaseHelper.VFS_DATABASE_TABLE, new String[] {
							DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH,
							BaseColumns._ID }, null, null, null, null, null);
			if (!Constants.MBM_ROOT.exists()) {
				Constants.MBM_ROOT.mkdir();
			}
			if (pathcursora.getCount() > 0) {
				// scan whole db and get path to cursor
				pathcursora.moveToFirst();
				do {
					File cfile = new File(pathcursora.getString(0));
					if (!cfile.exists()) {
						// remove invalid db files
						Log.d(TAG, pathcursora.getString(0)
								+ " does not exist, db entry removed");
						database.delete(DatabaseHelper.VFS_DATABASE_TABLE,
								BaseColumns._ID + " IS ?",
								new String[] { pathcursora.getString(1) });
						deleted = deleted++;
						// writeXML();
					}
				} while (pathcursora.moveToNext());
			}
			pathcursora.close();
			File[] sdDirectories = Constants.MBM_ROOT
					.listFiles(filterDirectoriesOnly);
			// get all dirs in /mnt/sdcard/multiboot
			for (int i = 0; i < sdDirectories.length; i++) {
				File file = sdDirectories[i];
				String vspathi = null;
				try {
					vspathi = file.getCanonicalPath();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String vsname = file.getName();
				Cursor pathcursorb = database
				// compare with db entries
						.rawQuery("SELECT " + BaseColumns._ID + " FROM "
								+ DatabaseHelper.VFS_DATABASE_TABLE + " WHERE "
								+ DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH
								+ "=?", new String[] { vspathi });
				// filter out those with a . as prefix
				if (!vsname.startsWith(".")) {
					// if cursor is empty, the vs is not registerd
					if (pathcursorb.getCount() == 0) {
						ContentValues values = new ContentValues();
						values.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_NAME,
								vsname);
						values.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH,
								file.getPath());
						values.put(
								DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_DESCRIPTION,
								R.string.default_vfs_description);
						values.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_TYPE,
								"1");
						database.insert(DatabaseHelper.VFS_DATABASE_TABLE,
								null, values);
						added = added++;
					}
				}
				pathcursorb.close();
			}
			return new int[] { added, deleted };
		} else {
			return new int[] { 0, 0 };
		}
	}

	public static void writeXML(SQLiteDatabase database) {
		Log.d(TAG, "writeXML()");
		if (!mbmconfigdir.exists()) {
			mbmconfigdir.mkdirs();
		}
		if (xmldumpfile.exists()) {
			xmldumpfile.delete();
		}
		try {
			xmldumpfile.createNewFile();
		} catch (IOException e) {
			Log.w(TAG, "exception in createNewFile() method");
		}
		// we have to bind the new file with a FileOutputStream
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(xmldumpfile);
		} catch (FileNotFoundException e) {
			Log.w(TAG, "can't create FileOutputStream");
		}
		// we create a XmlSerializer in order to write xml data
		Cursor cursor = database.query(DatabaseHelper.VFS_DATABASE_TABLE, null,
				null, null, null, null, null);
		XmlSerializer serializer = Xml.newSerializer();
		try {
			// we set the FileOutputStream as output for the serializer, using
			// UTF-8 encoding
			serializer.setOutput(fileos, "UTF-8");
			// Write <?xml declaration with encoding (if encoding not null) and
			// standalone flag (if standalone not null)
			serializer.startDocument(null, Boolean.valueOf(true));
			// set indentation option
			serializer.setFeature(
					"http://xmlpull.org/v1/doc/features.html#indent-output",
					true);
			serializer.startTag(null, "xmldump");
			// i indent code just to have a view similar to xml-tree
			// read from cursor and write child tags
			cursor.moveToFirst();
			do {
				serializer.startTag(null, VFS_ELEMENT_ROOT);
				serializer.attribute(null, "name", cursor.getString(1));
				serializer.attribute(null, "path", cursor.getString(2));
				serializer.attribute(null, "type", cursor.getString(3));
				serializer.attribute(null, "desc", cursor.getString(4));
				serializer.endTag(null, VFS_ELEMENT_ROOT);
			} while (cursor.moveToNext());
			serializer.endTag(null, "xmldump");
			serializer.endDocument();
			// write xml data into the FileOutputStream
			serializer.flush();
			// finally we close the file stream
			fileos.close();
		} catch (Exception e) {
			Log.w(TAG, "error occurred while creating xml file");
		}
		cursor.close();
	}

	private static Document getDumpDoc() {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse using builder to get DOM representation of the XML file
			dom = db.parse("file:" + XML_DUMP_PATH);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return dom;
	}

	public static void readXML(SQLiteDatabase database) {
		// get the root element
		Log.d(TAG, "readXML()");
		try {
			Element docEle = getDumpDoc().getDocumentElement();
			// get a nodelist of elements
			NodeList nl = docEle.getElementsByTagName("vfs");
			// Log.d(Tag.getTag(this),"nl length : " + nl.getLength());
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element el = (Element) nl.item(i);
					String name = el.getAttribute("name");
					String path = el.getAttribute("path");
					String type = el.getAttribute("type");
					String desc = el.getAttribute("desc");
					database.delete(
							DatabaseHelper.VFS_DATABASE_TABLE,
							DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH + " IS ?",
							new String[] { path });
					ContentValues values = new ContentValues();
					values.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_NAME, name);
					values.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH, path);
					values.put(
							DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_DESCRIPTION,
							desc);
					values.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_TYPE, type);
					database.insert(DatabaseHelper.VFS_DATABASE_TABLE, null,
							values);
				}
			}
		} catch (Exception e) {
			Log.w(TAG, "exception in readXML() method");
		}
	}
}
