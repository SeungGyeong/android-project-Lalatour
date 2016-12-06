package com.example.user.mymap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SeungGyeong Chung on 2016-11-27.
 */

public class MarkerDB extends SQLiteOpenHelper {
    public MarkerDB(Context context) {
        super (context, "marker.db", null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table marker( Id integer PRIMARY KEY autoincrement, lat real, lng real);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

