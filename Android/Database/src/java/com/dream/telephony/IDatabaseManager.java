package com.dream.telephony;

import java.util.List;

public interface IDatabaseManager {
    void createTable();
    long insertData(User user);
    int deleteData(int userId);
    int updateData(int age, int id);
    List<User> getAllData(String selection, String[] selectionArgs);
    List<User> getData(int id);
    List<User> getData(String name);
    String getPath();
    void close();
}
