package com.binoy.vibhinna;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TasksProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.binoy.vibhinna.TasksProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/tasks");
    public static final String _ID = "_id";
    private static final int TASKS = 1;
    private static final int TASK_ID = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "tasks", TASKS);
        uriMatcher.addURI(PROVIDER_NAME, "tasks/#", TASK_ID);
    }

    private SQLiteDatabase mDatabase;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                count = mDatabase.delete(DatabaseHelper.TASK_DATABASE_TABLE, selection,
                        selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getPathSegments().get(1);
                count = mDatabase.delete(DatabaseHelper.TASK_DATABASE_TABLE, _ID
                        + " = "
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                                : Constants.EMPTY), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
        // ---get all tasks---
            case TASKS:
                return "vnd.android.cursor.dir/vnd.vibhinna.binoy.tasks ";
                // ---get a particular task---
            case TASK_ID:
                return "vnd.android.cursor.item/vnd.vibhinna.binoy.tasks ";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // ---add a new task---
        long rowID = mDatabase.insert(DatabaseHelper.TASK_DATABASE_TABLE, Constants.EMPTY, values);
        // ---if added successfully---
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        return (mDatabase == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DatabaseHelper.TASK_DATABASE_TABLE);
        if (uriMatcher.match(uri) == TASK_ID)
            // ---if getting a particular task---
            sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
        if (sortOrder == null || sortOrder == Constants.EMPTY)
            sortOrder = DatabaseHelper.TASK_STATUS;
        Cursor c = sqlBuilder.query(mDatabase, projection, selection, selectionArgs, null, null,
                sortOrder);
        // ---register to watch a content URI for changes---
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                count = mDatabase.update(DatabaseHelper.TASK_DATABASE_TABLE, values, selection,
                        selectionArgs);
                break;
            case TASK_ID:
                count = mDatabase.update(DatabaseHelper.TASK_DATABASE_TABLE, values, _ID
                        + " = "
                        + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                                : Constants.EMPTY), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
