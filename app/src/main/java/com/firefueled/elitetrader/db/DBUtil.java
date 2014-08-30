package com.firefueled.elitetrader.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pablo on 24/08/2014.
 */
public class DBUtil {

    private SQLiteOpenHelper db;

    public void init (Context context) {
        this.db = new MarketOpenHelper(context);
    }
}
