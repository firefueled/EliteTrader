package com.firefueled.elitetrader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pablo on 24/08/2014.
 */
public class MarketOpenHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MARKET";

    // STATION
    public static final String STATION_TABLE_NAME = "STATION";
    public static final String KEY_STATION_ID = "ID";
    public static final String KEY_STATION_NAME = "NAME";
    public static final String KEY_STATION_SYSTEM = "SYSTEM";
    private static final String STATION_TABLE_CREATE =
            "CREATE TABLE " + STATION_TABLE_NAME + " (" +
                    KEY_STATION_ID + " INTEGER PRIMARY KEY ASC, " +
                    KEY_STATION_NAME + " TEXT, " +
                    KEY_STATION_SYSTEM + " TEXT" +
            ");";

    // TRADE ITEM
    public static final String TRADE_ITEM_TABLE_NAME = "TRADE_ITEM";
    public static final String KEY_TRADE_ITEM_ID = "ID";
    public static final String KEY_TRADE_ITEM_STATION_ID = "ID_STATION";
    public static final String KEY_TRADE_ITEM_NAME = "NAME";
    public static final String KEY_TRADE_ITEM_BUY = "BUY";
    public static final String KEY_TRADE_ITEM_SELL = "SELL";
    private static final String KEY_TRADE_ITEM_ID_CONSTRAINT = "CONSTRAINT TRADE_ITEM_STATION " +
            "FOREIGN KEY " + KEY_TRADE_ITEM_STATION_ID + " REFERENCES " +
            STATION_TABLE_NAME + " (" + KEY_TRADE_ITEM_ID + ") " +
            "";
    private static final String TRADE_ITEM_TABLE_CREATE =
            "CREATE TABLE " + TRADE_ITEM_TABLE_NAME + " (" +
                    KEY_TRADE_ITEM_ID + " INTEGER PRIMARY KEY ASC, " +
                    KEY_TRADE_ITEM_STATION_ID + " INTEGER " + KEY_TRADE_ITEM_ID_CONSTRAINT + ", " +
                    KEY_TRADE_ITEM_NAME + " TEXT, " +
                    KEY_TRADE_ITEM_BUY + " REAL, " +
                    KEY_TRADE_ITEM_SELL + " REAL" +
            ");";

    public MarketOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STATION_TABLE_CREATE);
        db.execSQL(TRADE_ITEM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}