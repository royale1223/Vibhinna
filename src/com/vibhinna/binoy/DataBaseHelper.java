package com.vibhinna.binoy;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	public static final String VFS_DATABASE_NAME = "vs_db";
	public static final String VFS_DATABASE_TABLE = "vs_table";
	public static final String VIRTUAL_SYSTEM_COLUMN_DESCRIPTION = "vsdesc";
	public static final String VIRTUAL_SYSTEM_COLUMN_NAME = "vsname";
	public static final String VIRTUAL_SYSTEM_COLUMN_PATH = "vspath";
	public static final String VIRTUAL_SYSTEM_COLUMN_TYPE = "vstype";
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ VFS_DATABASE_TABLE + " ( " + BaseColumns._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
			+ VIRTUAL_SYSTEM_COLUMN_NAME + " VARCHAR(50), "
			+ VIRTUAL_SYSTEM_COLUMN_PATH + " VARCHAR(50) UNIQUE, "
			+ VIRTUAL_SYSTEM_COLUMN_TYPE + " INTEGER, "
			+ VIRTUAL_SYSTEM_COLUMN_DESCRIPTION + " VARCHAR(200))";
	private static final String TAG = null;

	protected Context context;

	public DataBaseHelper(Context mContext) {
		super(mContext, VFS_DATABASE_NAME, null, DATABASE_VERSION);
		this.context = mContext;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Toast.makeText(context, "Creating DataBase for first time...", 2000)
				.show();
		db.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + VFS_DATABASE_TABLE);
		onCreate(db);

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// tbd
		if (db.isReadOnly()) {
			Log.d(TAG, "db is readonly");
			return;
		}
		Log.d(TAG, "db is writable");
		String extState = Environment.getExternalStorageState();
		if (extState.equals(Environment.MEDIA_MOUNTED)) {
			FileFilter filterDirectoriesOnly = new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory();
				}
			};
			Cursor pathcursora = db.query(DataBaseHelper.VFS_DATABASE_TABLE,
					new String[] { DataBaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH,
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
						db.delete(DataBaseHelper.VFS_DATABASE_TABLE,
								BaseColumns._ID + " IS ?",
								new String[] { pathcursora.getString(1) });
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
				Cursor pathcursorb = db
				// compare with db entries
						.rawQuery("SELECT " + BaseColumns._ID + " FROM "
								+ DataBaseHelper.VFS_DATABASE_TABLE + " WHERE "
								+ DataBaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH
								+ "=?", new String[] { vspathi });
				// filter out those with a . as prefix
				if (!vsname.startsWith(".")) {
					// if cursor is empty, the vs is not registerd
					if (pathcursorb.getCount() == 0) {
						ContentValues values = new ContentValues();
						values.put(DataBaseHelper.VIRTUAL_SYSTEM_COLUMN_NAME,
								vsname);
						try {
							values.put(
									DataBaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH,
									file.getCanonicalPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							values.put(
									DataBaseHelper.VIRTUAL_SYSTEM_COLUMN_DESCRIPTION,
									context.getString(R.string.newvfsi)
											+ file.getCanonicalPath() + ")");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						values.put(DataBaseHelper.VIRTUAL_SYSTEM_COLUMN_TYPE,
								"2");
						db.insert(DataBaseHelper.VFS_DATABASE_TABLE, null,
								values);
					}
				}
				pathcursorb.close();
			}
			return;
		} else if (extState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			return;
		} else {
			return;
		}
	}
}
