package com.example.user.mymap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SeungGyeong Chung on 2016-11-27.
 */

public class CountryListDB extends SQLiteOpenHelper {

    public CountryListDB(Context context) {
        super(context, "countrylist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table countrylist( Id integer PRIMARY KEY autoincrement, countryname varchar(50));"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
