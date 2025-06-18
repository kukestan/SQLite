package com.dream.diag.framework;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

public class DatabaseAppManager {
    private Context mContext;
    private static final String AUTHORITY = "dream";
    private static final String BASE_PATH = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private ContentResolver mContentResolver;

    public DatabaseAppManager(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        testDatabaseOperations();
    }

    private void insertData() {
        ContentValues values = new ContentValues();
        values.put("name", "John");
        values.put("age", 28);

        Uri newUri = mContentResolver.insert(CONTENT_URI, values);
        if (newUri != null) {
            Toast.makeText(mContext, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryData() {
        String[] projection = {"id", "name", "age"};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        Cursor cursor = mContentResolver.query(CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        if (cursor != null) {
            List<String> userList = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                userList.add("ID: " + id + ", Name: " + name + ", Age: " + age);
            }
            cursor.close();
            for (String user : userList) {
                Toast.makeText(mContext, user, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateData() {
        ContentValues values = new ContentValues();
        values.put("age", 30);
        String selection = "name = ?";
        String[] selectionArgs = {"John"};

        int rowsUpdated = mContentResolver.update(CONTENT_URI, values, selection, selectionArgs);
        if (rowsUpdated > 0) {
            Toast.makeText(mContext, "Data updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Failed to update data", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteData() {
        String selection = "name = ?";
        String[] selectionArgs = {"John"};

        int rowsDeleted = mContentResolver.delete(CONTENT_URI, selection, selectionArgs);
        if (rowsDeleted > 0) {
            Toast.makeText(mContext, "Data deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Failed to delete data", Toast.LENGTH_SHORT).show();
        }
    }

    private void testDatabaseOperations() {
        insertData();

        queryData();

        updateData();

        deleteData();
    }
}
