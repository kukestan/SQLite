package com.dream.telephony;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.Rlog;

import java.util.ArrayList;
import java.util.List;

public class DatabaseProviderManager implements IDatabaseManager{
    private static final String AUTHORITY = "dream";
    private static final String BASE_PATH = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private final ContentResolver mContentResolver;

    public DatabaseProviderManager(Context context) {
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void createTable() {

    }

    @Override
    public long insertData(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_AGE, user.getAge());
        Uri uri = mContentResolver.insert(CONTENT_URI, values);
        if (uri != null) {
            return Long.parseLong(uri.getLastPathSegment());
        }
        return -1;
    }

    @Override
    public int deleteData(int userId) {
        Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(userId));
        return mContentResolver.delete(uri, null, null);
    }

    @Override
    public int updateData(int age, int id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AGE, age);
        Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
        return mContentResolver.update(uri, values, null, null);
    }

    @Override
    public List<User> getAllData(String selection, String[] selectionArgs) {
        List<User> userList = new ArrayList<>();
        Cursor cursor = mContentResolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
                    user.setAge(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AGE)));
                    userList.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return userList;
    }

    @Override
    public List<User> getData(int id) {
        return getAllData(DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public List<User> getData(String name) {
        return getAllData(DatabaseHelper.COLUMN_NAME + " = ?", new String[]{name});
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public void close() {

    }

    public void test() {
        //add
        User user1 = new User("Tom", 25);
        User user2 = new User("Mike", 30);
        User user3 = new User("Mary", 35);

        insertData(user1);
        insertData(user2);
        insertData(user3);

        //delete
        deleteData(3);

        //update
        updateData(40, 2);

        //query
        List<User> u = getAllData(null, null);
        for (User user : u) {
            Rlog.d("DatabaseProviderManager", "all = " + user.toString());
        }
        List<User> u1 = getData(1);
        for (User user : u1) {
            Rlog.d("DatabaseProviderManager", "id 1 = " + user.toString());
        }
        List<User> u2 = getData("Mike");
        for (User user : u2) {
            Rlog.d("DatabaseProviderManager", "name Mike = " + user.toString());
        }
    }
}