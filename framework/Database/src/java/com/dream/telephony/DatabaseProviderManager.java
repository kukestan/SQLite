package com.dream.telephony;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Telephony;
import android.telephony.Rlog;

import java.util.ArrayList;
import java.util.List;

public class DatabaseProviderManager implements IDatabaseManager{
    private static final String AUTHORITY = "dream";
    private static final String BASE_PATH = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private final ContentResolver mContentResolver;
    private Context mContext;
    private DatabaseObserver mDatabaseObserver;
    private HandlerThread mHandlerThread;
    private final Handler mHandler;

    public DatabaseProviderManager(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mHandlerThread = new HandlerThread("DatabaseProviderManager");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        registerContentObserver();
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
        Rlog.d("DatabaseProviderManager", "deleteData uri = " + uri.toString());
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

    public List<User> getDataByIdUri(int id) {
        Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));

        // 直接查询该 URI
        Cursor cursor = mContentResolver.query(uri,null, null, null, null);

        List<User> userList = new ArrayList<>();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        User user = new User();
                        user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                        user.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
                        user.setAge(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AGE)));
                        userList.add(user);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close(); // 确保 Cursor 被关闭
            }
        }
        return userList;
    }
    @Override
    public String getPath() {
        return "";
    }

    @Override
    public void close() {

    }

    private void registerContentObserver() {
        mDatabaseObserver = new DatabaseObserver(mContext, mHandler);

        mContentResolver.registerContentObserver(
                CONTENT_URI,
                true,
                mDatabaseObserver
        );
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