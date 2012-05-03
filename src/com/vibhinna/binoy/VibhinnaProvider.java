package com.vibhinna.binoy;

import java.io.File;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class VibhinnaProvider extends ContentProvider {
	private DataBaseHelper mDB;
	private Context context;
	private static final String AUTHORITY = "com.manager.boot.free.MultiBootProvider";
	public static final int TUTORIALS = 0;
	public static final int TUTORIAL_ID = 1;
	private static final int TUTORIAL_PATH = 2;

	private static final String TUTORIALS_BASE_PATH = "vfs";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TUTORIALS_BASE_PATH);

	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/mt-vfs";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/mt-vfs";

	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(DataBaseHelper.VFS_DATABASE_TABLE);
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TUTORIAL_ID:
			queryBuilder.appendWhere(BaseColumns._ID + "="
					+ uri.getLastPathSegment());
			break;
		case TUTORIALS:
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TUTORIAL_ID:
			break;
		case TUTORIALS:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		context = getContext();
		mDB = new DataBaseHelper(context);
		return true;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(DataBaseHelper.VFS_DATABASE_TABLE);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TUTORIAL_ID:
			queryBuilder.appendWhere(BaseColumns._ID + "="
					+ uri.getLastPathSegment());
			break;
		case TUTORIALS:
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(context.getContentResolver(), uri);
		return cursor;
	}

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	private static final String TAG = null;

	static {
		sURIMatcher.addURI(AUTHORITY, TUTORIALS_BASE_PATH, TUTORIALS);
		sURIMatcher.addURI(AUTHORITY, TUTORIALS_BASE_PATH + "/#", TUTORIAL_ID);
		sURIMatcher.addURI(AUTHORITY, TUTORIALS_BASE_PATH + "/path/*",
				TUTORIAL_PATH);
	}

	public Cursor getNameList(Context mContext) {
		String[] columnNames = { "_id", "name", "desc", "family", "folder",
				"status", "vdstatus", "path" };
		context = mContext;
		mDB = new DataBaseHelper(context);
		Cursor c = query(CONTENT_URI, DataSource.allColumns, null, null, null);
		MatrixCursor cursor = new MatrixCursor(columnNames);
		if (c.moveToFirst()) {
			do {
				File root = new File(c.getString(2));
				for (int i = 0; i < c.getColumnCount(); i++) {
					Log.d(TAG,
							"c.getString(" + i + ")" + c.getString(i));
				}
				if (root.canRead()) {
					Object[] fsii = new Object[8];
					String cache = null;
					String data = null;
					String system = null;
					String vdstatus = "0";
					File cacheimg = new File(root, "cache.img");
					if (cacheimg.exists()) {
						cache = cacheimg.length() / 1048576
								+ mContext.getString(R.string.smiB);
					} else
						cache = mContext.getString(R.string.error);
					File dataimg = new File(root, "data.img");
					if (dataimg.exists()) {
						data = dataimg.length() / 1048576
								+ mContext.getString(R.string.smiB);
					} else
						data = mContext.getString(R.string.error);
					File systemimg = new File(root, "system.img");
					if (systemimg.exists()) {
						system = systemimg.length() / 1048576
								+ mContext.getString(R.string.smiB);
					} else
						system = mContext.getString(R.string.error);
					if (systemimg.exists() && cacheimg.exists()
							&& dataimg.exists()) {
						vdstatus = "1";
					} else
						vdstatus = "0";
					fsii[0] = Integer.parseInt(c.getString(0));
					fsii[1] = c.getString(1);
					fsii[2] = c.getString(4);
					fsii[3] = null;
					fsii[4] = c.getString(3);
					fsii[5] = mContext.getString(R.string.caches) + cache
							+ mContext.getString(R.string.datas) + data
							+ mContext.getString(R.string.systems) + system;
					fsii[6] = vdstatus;
					fsii[7] = c.getString(2);
					cursor.addRow(fsii);
				}
			} while (c.moveToNext());
		}
		c.close();
		return cursor;

	}

}
