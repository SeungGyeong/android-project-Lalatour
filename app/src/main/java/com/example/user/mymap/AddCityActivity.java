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
 * Created by SeungGyeong Chung on 2016-10-15. (2nd_2 Activity)
 */

public class AddCityActivity extends AppCompatActivity {

    SQLiteDatabase sqlitedb;
    CityListDB db;
    int a;
    int aa;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcity);

        Intent it = getIntent();
        a = it.getIntExtra("pk", -1);                       // 기본키
        aa = it.getIntExtra("position", -1);                // 리스트뷰 위치
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addcity, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_pre:         // cityListActivity로 돌아가는 기능
                Intent it2 = new Intent(getApplicationContext(), CityListActivity.class);
                startActivity(it2);
                finish();
                break;

            case R.id.menu_check:         // cityListActivity에 도시 추가
                if (a == -1) {
                    EditText et_name = (EditText) findViewById(R.id.cityname);
                    String str_name = et_name.getText().toString();

                    EditText et_day = (EditText) findViewById(R.id.dayday);
                    String str_day = et_day.getText().toString();

                    try {
                        db = new CityListDB(this);
                        sqlitedb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("cityname", str_name);
                        values.put("stay", str_day);
                        sqlitedb.insert("cityListtttt", null, values);
                        sqlitedb.close();
                        db.close();

                    } catch (SQLiteException e) {
                        Toast.makeText(this, "DB에 저장 에러", Toast.LENGTH_LONG).show();
                    }

                    Intent it3 = new Intent(getApplicationContext(), CityListActivity.class);
                    startActivity(it3);
                    finish();
                } else {
                    ////////////////////////////////////////////////////////////////////////////////////////// 수정인 경우 해야 함.
                    EditText et_name = (EditText) findViewById(R.id.cityname);
                    String str_name = et_name.getText().toString();

                    EditText et_day = (EditText) findViewById(R.id.dayday);
                    String str_day = et_day.getText().toString();

                    try {
                        db = new CityListDB(getApplicationContext());
                        sqlitedb = db.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("cityname", str_name);
                        values.put("stay", str_day);

                        sqlitedb.update("cityListtttt", values, "Id =" + a, null);        //Id가 num인 것만을 찾아서 업데이트, Id는
                        Toast.makeText(this, "DB 수정 잘 됨", Toast.LENGTH_LONG).show();
                        sqlitedb.close();
                        db.close();

                    } catch (SQLiteException e) {
                        Toast.makeText(getApplicationContext(), "DB 수정 에러", Toast.LENGTH_LONG).show();
                    }
                    Intent it3 = new Intent(getApplicationContext(), CityListActivity.class);
                    startActivity(it3);
                    finish();

                    break;
                }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
