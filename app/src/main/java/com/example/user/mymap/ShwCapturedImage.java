package com.example.user.mymap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by SeungGyeong Chung on 2016-11-25. 5th Activity / Previous : List Of Each Item Activity
 * List Of Each Item Activity의 리스트 하나를 클릭하면 자세한 내용을 볼 수 있다.
 */

public class ShwCapturedImage extends AppCompatActivity {
    String file_name;
    String which;
    String path;
    int pos;        // 자세히 보려고 클릭된 이미지의 리스트뷰 위치

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcapturedimage);

        Intent it = getIntent();
        file_name = it.getStringExtra("file_name");
        which =it.getStringExtra("whichBtn");
        pos = it.getIntExtra("position", -1);

        if(which.equals("reservation")) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/reservation";//+"/"+ file_name + ".png";
        } else if (which.equals("thingstodo")) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/thingstodo";//+"/"+ file_name + ".png";
        } else if (which.equals("transportation")) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/transportation";//+"/"+ file_name + ".png";
        } else if (which.equals("accomodation")) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/accomodation";//+"/"+ file_name + ".png";
        } else if (which.equals("food")) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/food";//+"/"+ file_name + ".png";
        }

        ImageView imgv = (ImageView)findViewById(R.id.showImage);
        String imageFname = null;
        File imageFiles[] = new File(path).listFiles();
        imageFname = imageFiles[pos].toString();
        Bitmap bm = BitmapFactory.decodeFile(imageFname);
        imgv.setImageBitmap(bm);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_showcapturedimage, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_pre:
                Intent it2 = new Intent(getApplicationContext(), ListOfEachItemActivity.class);
                it2.putExtra("whichBtn", which);
                startActivity(it2);
                finish();
                break;

        }
        return true;
    }
}
