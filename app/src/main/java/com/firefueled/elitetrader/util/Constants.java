package com.firefueled.elitetrader.util;

import android.content.Context;

import com.google.gson.Gson;

/**
 * Created by Pablo on 24/08/2014.
 */
public class Constants {

    public static Database DB;
    public static final String DB_NAME = "MARKET";

    public static final Gson GSON = new Gson();

    public static void initCotextStuff(Context context) {
        DB = new Database(context);
    }
}
