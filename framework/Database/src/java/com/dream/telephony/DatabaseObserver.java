package com.dream.telephony;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.telephony.TelephonyManager;

public class DatabaseObserver extends ContentObserver {
    private final Context mContext;

    public DatabaseObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }
}
