package com.dream.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.Rlog;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager implements IDatabaseManager {
    private final DatabaseHelper mHelper;
    private SQLiteDatabase mDataBase;
    public DatabaseManager(Context context) {
        mHelper = new DatabaseHelper(context);
        createTable();
    }

    @Override
    public void createTable() {
        mDataBase = mHelper.getWritableDatabase();
    }

    @Override
    public long insertData(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_AGE, user.getAge());
        return mDataBase.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    @Override
    public int deleteData(int userId) {
        return mDataBase.delete(
                DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)});
    }

    @Override
    public int updateData(int age, int id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AGE, age);
        return mDataBase.update(
                DatabaseHelper.TABLE_USERS,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    @Override
    public List<User> getAllData(String selection, String[] selectionArgs) {
        List<User> userList = new ArrayList<>();
        Cursor cursor = mDataBase.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE},
                selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setAge(cursor.getInt(2));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    @Override
    public List<User> getData(int id) {
        return getAllData("id = ?", new String[]{Integer.toString(id)});
    }

    @Override
    public List<User> getData(String name) {
        return getAllData("name = ?", new String[]{name});
    }

    @Override
    public String getPath() {
        return mDataBase.getPath();
    }

    @Override
    public void close() {
        mHelper.close();
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
        updateData(40 ,2);

        //query
        List<User> u = getAllData(null, null);
        for (User user : u) {
            Rlog.d("DatabaseManager", "all =" + user.toString());
        }
        List<User> u1 = getData(1);
        for (User user : u1) {
            Rlog.d("DatabaseManager", "id 1 = " + user.toString());
        }
        List<User> u2 = getData("Mike");
        for (User user : u2) {
            Rlog.d("DatabaseManager", "name Mike = " + user.toString());
        }

        Rlog.d("DatabaseManager", "path = " + getPath());
    }
}
