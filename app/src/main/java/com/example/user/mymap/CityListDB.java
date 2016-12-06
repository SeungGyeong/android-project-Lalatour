package com.example.user.mymap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SeungGyeong Chung on 2016-10-16.
 */

public class CityListDB extends SQLiteOpenHelper {

    public CityListDB(Context context) {
        super (context, "cityListtttt.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cityListtttt( Id integer PRIMARY KEY autoincrement, cityname varchar(50), stay varchar(50));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}