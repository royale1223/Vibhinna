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

	protected Context context;

	public DataBaseHelper(Context context) {
		super(context, VFS_DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
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

}

