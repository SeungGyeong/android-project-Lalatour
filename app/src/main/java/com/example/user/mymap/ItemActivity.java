package com.example.user.mymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by SeungGyeong Chung on 2016-10-18. (Previous Activity : City List Activity / 3rd Activity)
 * 도시명 클릭시 나오는 액티비티. 정보를 주제별로 분류할 수 있게 한다.
 */

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getIntent();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_pre:
                Intent it = new Intent(this, CityListActivity.class);
                startActivity(it);
                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void reservation (View v) {
        Intent it = new Intent(this, ListOfEachItemActivity.class);
        it.putExtra("whichBtn","reservation");
        startActivity(it);
       finish();
    }

    public void thingstodo (View v) {
        Intent it = new Intent(this, ListOfEachItemActivity.class);
        it.putExtra("whichBtn","thingstodo");
        startActivity(it);
        finish();
    }

    public void transportation (View v) {
        Intent it = new Intent(this, ListOfEachItemActivity.class);
        it.putExtra("whichBtn","transportation");
        startActivity(it);
        finish();
    }

    public void accomodation (View v) {
        Intent it = new Intent(this, ListOfEachItemActivity.class);
        it.putExtra("whichBtn","accomodation");
        startActivity(it);
        finish();
    }

    public void food (View v) {
        Intent it = new Intent(this, ListOfEachItemActivity.class);
        it.putExtra("whichBtn","food");
        startActivity(it);
        finish();
    }
}
