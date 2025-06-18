package com.dream.providers.telephony;

import android.annotation.NonNull;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DreamTelephonyProvider extends ContentProvider {
    private static final String AUTHORITY = "dream";
    private static final String BASE_PATH = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int USERS = 1;
    private static final int USER_ID = 2;
    private Context mContext;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, USERS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", USER_ID);
    }

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDatabaseHelper = new DatabaseHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                cursor = db.query(DatabaseHelper.TABLE_USERS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_ID:
                selection = DatabaseHelper.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(DatabaseHelper.TABLE_USERS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                return "vnd.android.cursor.dir/vnd.com.dream.telephony.users";
            case USER_ID:
                return "vnd.android.cursor.item/vnd.com.dream.telephony.users";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new android.database.SQLException("Failed to insert row into " + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                rowsDeleted = db.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs);
                break;
            case USER_ID:
                selection = DatabaseHelper.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsDeleted = db.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                rowsUpdated = db.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs);
                break;
            case USER_ID:
                selection = DatabaseHelper.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsUpdated = db.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}