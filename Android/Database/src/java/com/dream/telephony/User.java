package com.dream.telephony;

public class User {
    private int mId;
    private String mName;
    private int mAge;

    public User() {

    }

    public User(String name, int age) {
        mName = name;
        mAge = age;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    public void setId(int setid) {
        mId = setid;
    }

    public void setName(String setname) {
        mName = setname;
    }

    public void setAge(int setage) {
        mAge = setage;
    }

    public String toString() {
        return "USER [ID = " + mId + ", NAME = " + mName + ", AGE = " + mAge + "]";
    }
}