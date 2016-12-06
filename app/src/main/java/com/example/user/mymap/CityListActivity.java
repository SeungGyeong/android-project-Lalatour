package com.example.user.mymap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SeungGyeong Chung on 2016-10-15. (2nd Activity)
 */

public class CityListActivity extends AppCompatActivity {

    static  public ArrayList<String> mDatas = new ArrayList<String>();                              // 데이터 베이스로부터 이미 저장되어 값들을 읽어와 담아두는 배열.
    ListView listview;
    int ff;
    int c=0;
    //ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist);

        getIntent();        // 현재 액티비티를 호출한 액티비티(Main Activity) 인식.

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0x000000));

        mDatas.clear();
        try {
            CityListDB db = new CityListDB(this);
            SQLiteDatabase sqlitedb = db.getReadableDatabase();
            Cursor cursor = sqlitedb.query("cityListtttt", null, "cityname is not null", null, null, null, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("cityname"));
                String stay = cursor.getString(cursor.getColumnIndex("stay"));
                String namestay = name + " ( " + stay + " )";
                mDatas.add(namestay);
                c++;
            }
            cursor.close();
            sqlitedb.close();
            db.close();

        } catch (SQLiteException e) {
            //Toast.makeText(this, "DB로부터 정보 읽어오기 실패", Toast.LENGTH_LONG).show();
        }
       final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDatas);         // DB로부터 정보를 읽어와 리스트뷰에 보여줌.
        listview = (ListView) findViewById(R.id.citylist);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {                     // 리스트뷰의 아이템 클릭시 아이템 액티비티로 넘어감.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itt = new Intent(getApplicationContext(),ItemActivity.class);
                startActivity(itt);
                finish();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {             // 리스트뷰의 아이템 길게 클릭시 삭제, 수정 할 수 있다.
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                PopupMenu popup= new PopupMenu(CityListActivity.this, view);            //view는 오래 눌러진 뷰를 의미
                getMenuInflater().inflate(R.menu.menu_listview, popup.getMenu());
                final int index= position;                                              // index는 리스트뷰에서 선택된 아이템의 위치를 알려준다.

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modify:                                                                                           // 수정
                                String y = mDatas.get(index).toString();                // 선택된 리스트뷰의 위치에 있는 데이터를 가져온다.                                                             //////////////////////
                                int yy = y.indexOf(" ");
                                String yyy = y.substring(0,yy);
                                String nameee=null;

                                CityListDB dbb = new CityListDB(getApplicationContext());
                                SQLiteDatabase sqlitedbb = dbb.getWritableDatabase();
                                Cursor curs = sqlitedbb.query("cityListtttt", null, "cityname is not null", null, null, null, null);

                                while (curs.moveToNext()) {
                                    int f = curs.getInt(curs.getColumnIndex("Id"));
                                    nameee = curs.getString(curs.getColumnIndex("cityname"));
                                    if(yyy.equals(nameee)) {
                                        ff = f;				                                            // 리스트뷰에서 선택된 아이템의 Id(기본키)를 가져온다.
                                    }
                                }

                                Intent itt = new Intent(CityListActivity.this, AddCityActivity.class);
                                itt.putExtra("pk",ff);                                                      //////////
                                itt.putExtra("position", index);
                                itt.putExtra("cname",nameee);
                                startActivity(itt);
                                finish();
                                break;

                            case R.id.delete:                                                                                           //삭제
                                String xxx = Integer.toString(index);        // 선택된 리스트뷰의 위치를 가져온다.
                                String k = mDatas.get(index).toString();                // 그 위치에 있는 데이터를 가져온다.                                                             //////////////////////
                                int kk = k.indexOf(" ");
                                String kkk = k.substring(0,kk);
                                String namee=null;

                                CityListDB db = new CityListDB(getApplicationContext());
                                SQLiteDatabase sqlitedb = db.getWritableDatabase();
                                Cursor cursor = sqlitedb.query("cityListtttt", null, "cityname is not null", null, null, null, null);

                                while (cursor.moveToNext()) {
                                    int f = cursor.getInt(cursor.getColumnIndex("Id"));                         // DB내의 기본키와 도시명을 가져와 k와 같으면 삭제한다.
                                    namee = cursor.getString(cursor.getColumnIndex("cityname"));
                                    if(kkk.equals(namee)) {
                                        ff = f;
                                    }
                                }

                                sqlitedb.delete("cityListtttt", "Id =" + ff, null);        //Id는 데이터베이스의 기본키.
                                Toast.makeText(getApplicationContext(), "DB 삭제 잘됨" , Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), ff+"/"+xxx+"/"+kkk+"/"+namee , Toast.LENGTH_LONG).show();
                                cursor.close();

                                mDatas.clear();
                                Cursor cur = sqlitedb.query("cityListtttt", null, "cityname is not null", null, null, null, null);
                                while (cur.moveToNext()) {
                                    String name = cur.getString(cur.getColumnIndex("cityname"));
                                    String stay = cur.getString(cur.getColumnIndex("stay"));
                                    String namestay = name + "  ( " + stay + " )";
                                    mDatas.add(namestay);
                                }
                                adapter.notifyDataSetInvalidated();
                                cur.close();
                                sqlitedb.close();
                                db.close();
                                break;
                        }
                    return false;
                    }
                });
                popup.show();
                return false;
            }
         });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citylist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
                case R.id.menu_add:
                Intent itt = new Intent(this, AddCityActivity.class);
                startActivity(itt);
                finish();
                break;

            case R.id.menu_pre:
                Intent ittt = new Intent (this, MainActivity.class);
                startActivity(ittt);
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
