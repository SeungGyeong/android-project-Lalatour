package com.example.user.mymap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by SeungGyeong Chung on 2016-10-14. (1st_2 Activity)
 */

public class AddCountryActivity extends AppCompatActivity {

    SQLiteDatabase sqlitedb;
    CountryListDB db;
    int a;
    int aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcountry);

        Intent it = getIntent();
        a = it.getIntExtra("pk", -1);                       // 기본키
        aa = it.getIntExtra("position", -1);                // 리스트뷰 위치
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addcountry, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_check:
                if (a == -1) {
                    EditText et = (EditText) findViewById(R.id.input);
                    String country_name = et.getText().toString();
                    try {
                        db = new CountryListDB(this);
                        sqlitedb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("countryname", country_name);
                        sqlitedb.insert("countrylist", null, values);
                        sqlitedb.close();
                        db.close();
                    } catch (SQLiteException e) {
                        Toast.makeText(this, "DB에 저장 에러", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(this, "DB에 저장", Toast.LENGTH_LONG).show();
                    Intent it3 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it3);
                    finish();
                    break;

                } else {
                    ////////////////////////////////////////////////////////////////////////////////////////// 수정인 경우 해야 함.
                    EditText et_name = (EditText) findViewById(R.id.input);
                    String str_name = et_name.getText().toString();

                    try {
                        db = new CountryListDB(this);
                        sqlitedb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("countryname", str_name);

                        sqlitedb.update("countrylist", values, "Id =" + a, null);        //Id가 num인 것만을 찾아서 업데이트, Id는
                        Toast.makeText(this, "DB 수정 잘 됨", Toast.LENGTH_LONG).show();
                        sqlitedb.close();
                        db.close();

                    } catch (SQLiteException e) {
                        Toast.makeText(getApplicationContext(), "DB 수정 에러", Toast.LENGTH_LONG).show();
                    }
                    Intent it3 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it3);
                    finish();

                    break;
                }

            case R.id.menu_close :
                    finish();
                    Intent it2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it2);
                break;

            default :
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}