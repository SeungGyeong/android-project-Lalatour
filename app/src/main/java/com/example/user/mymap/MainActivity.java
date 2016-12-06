package com.example.user.mymap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by SeungGyeong Chung on 2016-10-14. (1st activity)
 */

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    MarkerDB db;
    SQLiteDatabase dbb;
    ArrayList<LatLng> tt = new ArrayList<LatLng>();
    int c = 0;
    LatLng latLngg;

    static  public ArrayList<String> mDatas = new ArrayList<String>();
    ListView listview;
    int ff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getIntent();

        mDatas.clear();
        try {
            CountryListDB cdb = new CountryListDB(this);
            SQLiteDatabase sqlitedb = cdb.getReadableDatabase();
            Cursor cursor = sqlitedb.query("countrylist", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("countryname"));
                mDatas.add(name);
                c++;
            }
            cursor.close();
            sqlitedb.close();
            cdb.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, "DB로부터 정보 읽어오기 실패", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapterr = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDatas);         // DB로부터 정보를 읽어와 리스트뷰에 보여줌.
        listview = (ListView) findViewById(R.id.countrylistt);
        listview.setAdapter(adapterr);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {                     // 리스트뷰의 아이템 클릭시 도시 리스트 액티비티로 넘어감.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itt = new Intent(getApplicationContext(),CityListActivity.class);
                startActivity(itt);
                finish();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {             // 리스트뷰의 아이템 길게 클릭시 삭제, 수정 할 수 있다.
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                PopupMenu popup= new PopupMenu(MainActivity.this, view);            //view는 오래 눌러진 뷰를 의미
                getMenuInflater().inflate(R.menu.menu_listview, popup.getMenu());
                final int index= position;                                              // index는 리스트뷰에서 선택된 아이템의 위치를 알려준다.

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modify:                                                                                           // 수정
                                String y = mDatas.get(index).toString();                // 선택된 리스트뷰의 위치에 있는 데이터를 가져온다.                                                             //////////////////////
                                String nameee=null;

                                CountryListDB dbb = new CountryListDB(getApplicationContext());
                                SQLiteDatabase sqlitedbb = dbb.getWritableDatabase();
                                Cursor curs = sqlitedbb.query("countrylist", null, null, null, null, null, null);

                                while (curs.moveToNext()) {
                                    int f = curs.getInt(curs.getColumnIndex("Id"));
                                    nameee = curs.getString(curs.getColumnIndex("countryname"));
                                    if(y.equals(nameee)) {
                                        ff = f;				                                            // 리스트뷰에서 선택된 아이템의 Id(기본키)를 가져온다.
                                    }
                                }

                                Intent itt = new Intent(MainActivity.this, AddCountryActivity.class);
                                itt.putExtra("pk",ff);
                                itt.putExtra("position", index);
                                itt.putExtra("countryname",nameee);
                                startActivity(itt);
                                finish();
                                break;

                            case R.id.delete:                                                                                           //삭제
                                String xxx = Integer.toString(index);        // 선택된 리스트뷰의 위치를 가져온다.
                                String k = mDatas.get(index).toString();                // 그 위치에 있는 데이터를 가져온다.                                                             //////////////////////
                                String namee=null;

                                CountryListDB db = new CountryListDB(getApplicationContext());
                                SQLiteDatabase sqlitedb = db.getWritableDatabase();
                                Cursor cursor = sqlitedb.query("countrylist", null, null, null, null, null, null);

                                while (cursor.moveToNext()) {
                                    int f = cursor.getInt(cursor.getColumnIndex("Id"));                         // DB내의 기본키와 도시명을 가져와 k와 같으면 삭제한다.
                                    namee = cursor.getString(cursor.getColumnIndex("countryname"));
                                    if(k.equals(namee)) {
                                        ff = f;
                                    }
                                }

                                sqlitedb.delete("countrylist", "Id =" + ff, null);        //Id는 데이터베이스의 기본키.
                                Toast.makeText(getApplicationContext(), "DB 삭제 잘됨" , Toast.LENGTH_LONG).show();
                                cursor.close();

                                mDatas.clear();
                                Cursor cur = sqlitedb.query("countrylist", null, null, null, null, null, null);
                                while (cur.moveToNext()) {
                                    String name = cur.getString(cur.getColumnIndex("countryname"));
                                    mDatas.add(name);
                                }
                                adapterr.notifyDataSetInvalidated();
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

        FloatingActionButton butt = (FloatingActionButton) findViewById(R.id.fabb);           // 나라 추가 부분
        butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent it9 = new Intent(getApplicationContext(), AddCountryActivity.class);
                startActivity(it9);
                finish();
            }
        });
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {                     // 길게 클릭시 마커 지정
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng); //마커위치설정
                insert(latLng.latitude, latLng.longitude);              // db에 저장
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.star));
                mMap.addMarker(markerOptions).showInfoWindow(); //마커 생성
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));   // 마커생성위치로 이동
            }
        });

        db = new MarkerDB(this);                                                                   // 저장 된 마커 디비에서 불러오기
        dbb = db.getReadableDatabase();
        Cursor cursor = dbb.query("marker", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            double latt = cursor.getDouble(cursor.getColumnIndex("lat"));
            double lngg = cursor.getDouble(cursor.getColumnIndex("lng"));
            latLngg = new LatLng(latt,lngg);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngg);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.star));
            mMap.addMarker(markerOptions).showInfoWindow(); //마커 생성
        }
        cursor.close();
        dbb.close();
        db.close();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng x =marker.getPosition();
                double lattt = x.latitude;
                double lnggg = x.longitude;

                //String id = marker.getId();
                //marker.setVisible(false);

                db = new MarkerDB(getApplicationContext());
                dbb = db.getWritableDatabase();

                dbb.delete("marker", "lat =" + lattt, null);
                Toast.makeText(getApplicationContext(), "DB 삭제 잘됨" , Toast.LENGTH_LONG).show();

                mMap.clear();
                Cursor cursor = dbb.query("marker", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    double latt = cursor.getDouble(cursor.getColumnIndex("lat"));
                    double lngg = cursor.getDouble(cursor.getColumnIndex("lng"));
                    latLngg = new LatLng(latt,lngg);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLngg);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.star));
                    mMap.addMarker(markerOptions).showInfoWindow(); //마커 생성
                }
                cursor.close();
                dbb.close();
                db.close();

                return true;
            }
        });
    }

    public void insert(double lat, double lng){
        db = new MarkerDB(this);
        dbb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lat", lat);
        values.put("lng", lng);
        dbb.insert("marker", null, values);
        dbb.close();
        db.close();
    }
}
