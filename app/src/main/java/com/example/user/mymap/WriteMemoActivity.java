package com.example.user.mymap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.user.mymap.R.id.here;
import static com.example.user.mymap.R.id.heree;
import static com.example.user.mymap.R.id.memotext;

//import static com.example.user.mymap.R.id.here;

/**
 * Created by SeungGyeong Chung on 2016-11-04. 5th Activity / Previous : List Of Each Item Activity
 * 메모를 입력하는 액티비티
 */

public class WriteMemoActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PICKALBUM = 101;
    Bitmap image_bitmap;

    public static final int MENU_ONE = 1;
    public static final int MENU_TWO = 2;
    private int currMenu = MENU_ONE;

    MyView m = null;
    MyView mm = null;

    String whichh;
    private TimerTask mTask;
    private Timer mTimer;

    int screenshotHeightPx = 0;
    Canvas screenshotCanvas = null;
    Bitmap screenshotBitmap = null;
    Rect cropRect;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writememo);

        Intent itit = getIntent();
        if(itit.hasExtra("whichBtn")) {
            whichh = itit.getStringExtra("whichBtn");
        }

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        alert_confirm.setMessage("손으로 그리고, 하이라이트를 칠할 수 있습니다.\t" +
                "단, 가장 마지막 단계에 하셔야합니다. \n\n이미지나 텍스트 삭제시에,\n 그리기와 하이라이트 기능을 사용하셨다면, \t" +
                "이 두 기능을 지우고(undo) 삭제가 가능합니다.");
        alert_confirm.setPositiveButton("확인", null);
        AlertDialog alert = alert_confirm.create();
        alert.setIcon(R.drawable.warning);
        //alert.setTitle("제목");
        alert.show();

        FloatingActionButton but = (FloatingActionButton) findViewById(R.id.fab);           // 사진 추가 부분
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent it3 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                it3.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(Intent.createChooser(it3, "앨범에서 불러오기"), REQUEST_CODE_PICKALBUM);          // 응답처리가 필요한 경우 사용.
                changeMenu(view);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        EditText memo = (EditText)findViewById(memotext);
        memo.setTextColor(Color.GRAY);
        int st_index = memo.getSelectionStart();
        //memo.getText().insert(st_index, "\n");
        int et_index = memo.getSelectionEnd();

        int width= getWindowManager().getDefaultDisplay().getWidth();
            if (requestCode == REQUEST_CODE_PICKALBUM) {
                if (resultCode == RESULT_OK) {

                try {
                    // 앨범인 경우
                    Uri mImageUri = data.getData();
                    String realPaths = getPath(mImageUri);

                    // image option 설정 및 resizing
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    image_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri), null, options);
                    int resizedWidth = width;
                    int resizedHeight = width + image_bitmap.getHeight() / image_bitmap.getWidth();
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, resizedWidth, resizedHeight, true);

                    // 이미지 회전
                    ExifInterface exif = new ExifInterface(realPaths);
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees (realPaths);
                    image_bitmap = rotate(image_bitmap, exifDegree);

                    /*Drawable myDrawable = new BitmapDrawable(getResources(), image_bitmap);
                    myDrawable.setBounds(0,0,myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
                    memo.setCompoundDrawables(null,null,myDrawable,null);*/

                    //ImageSpan span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);
                    //Editable m = memo.getEditableText();
                   // m.setSpan(span, st_index, et_index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                  /*Spannable span = memo.getText();
                    span.setSpan(new ImageSpan(this,image_bitmap, ImageSpan.ALIGN_BASELINE), st_index, et_index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
                    // memo.append(span);

                    /*inLayout = (LinearLayout)findViewById(here);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    route_info_tab = (LinearLayout) inflater.inflate(R.layout.activity_image, inLayout, true);
                    inImage = (ImageView)findViewById(memoimage);
                    inImage.setImageBitmap(image_bitmap);*/

                    LinearLayout inLayout = (LinearLayout)findViewById(here);
                    ImageView image = new ImageView(this);
                    image.setImageBitmap(image_bitmap);
                    inLayout.addView(image);

                    EditText test = new EditText(this);
                    test.setTextColor(Color.GRAY);
                    test.setBackground(null);
                    test.setHint("여기도 입력 가능합니다");
                    inLayout.addView(test);

                   image.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            Toast.makeText(getApplicationContext(),"이미지가 삭제됩니다.", Toast.LENGTH_LONG).show();
                            v.setVisibility(View.GONE);
                            return true;
                        }
                    });


                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "파일을 찾을 수 없음", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(this, "IO exception", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "exception", Toast.LENGTH_LONG).show();
                }
            }
        } else {
                Toast.makeText(this, "사진 불러오기만 가능합니다.", Toast.LENGTH_LONG).show();
            }
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public  int exifOrientationToDegrees(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        }
        catch (IOException e) {
            Log.e("TAG", "cannot read exif");
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    public synchronized static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2 );
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            }
            catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }

        return bitmap;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        FrameLayout linearLayout = (FrameLayout)findViewById(heree);


        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_pre:
                Intent it2 = new Intent(getApplicationContext(), ListOfEachItemActivity.class);
                it2.putExtra("whichBtn", whichh);
                startActivity(it2);
                finish();
                break;

            case R.id.menu_add:

                SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();

                EditText ett = (EditText)findViewById(R.id.memotext);
                String fname = ett.getText().toString();
                if(fname.isEmpty()) {
                    fname = day.format(date);
                }

                ScrollView capture = (ScrollView) findViewById(R.id.scroll);        //캡쳐할영역(프레임레이아웃)
                int totalHeight = capture.getChildAt(0).getHeight();
                int totalWidth = capture.getChildAt(0).getWidth();

                Bitmap b = getBitmapFromView(capture,totalHeight,totalWidth);


                capture.buildDrawingCache();
                Bitmap captureView = capture.getDrawingCache();
                FileOutputStream fos;

                String dirPath = Environment.getExternalStorageDirectory().toString();
                if(whichh.equals("reservation")) {
                    dirPath += "/reservation";
                } else if (whichh.equals("thingstodo")) {
                    dirPath += "/thingstodo";
                } else if (whichh.equals("transportation")) {
                    dirPath += "/transportation";
                } else if (whichh.equals("accomodation")) {
                    dirPath += "/accomodation";
                } else if (whichh.equals("food")) {
                    dirPath += "/food";
                }

                File file = new File(dirPath);

                if(!file.exists()) {
                    file.mkdirs();
                }
                try{
                    fos = new FileOutputStream(dirPath+"/"+fname+".png");

                    b.compress(Bitmap.CompressFormat.PNG, 90, fos);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Captured!", Toast.LENGTH_SHORT).show();


                Handler handler = new Handler();

                handler.postDelayed(new Runnable () {
                    public  void run() {
                        Intent it3 = new Intent(getApplicationContext(), ListOfEachItemActivity.class);
                        it3.putExtra("whichBtn", whichh);
                        startActivity(it3);
                        finish();
                    }
                }, 2000);

                break;

            case R.id.menu_highlight:
                MyView.color = Color.YELLOW;
                MyView.alpha = 80;
                mm = new MyView(this);
                linearLayout.addView(mm);
                break;

            case R.id.menu_pen:
                MyView.color = Color.BLACK;
                MyView.alpha = 255;
                //Toast.makeText(this, "undo후에, 이미지 삭제가 가능합니다.", Toast.LENGTH_LONG).show();
                m = new MyView(this);
                linearLayout.addView(m);
                break;

           case R.id.menu_undo:
               //m.setVisibility(View.INVISIBLE);
               linearLayout.removeView(m);
               linearLayout.removeView(mm);
                break;
        }

        return true;
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        int menuResourceId = -1;

        switch(currMenu) {
            case MENU_ONE :
                menuResourceId = R.menu.menu_writememo;
                break;
            case MENU_TWO :
                menuResourceId = R.menu.menu_writememo2;
                break;
        }

        menu.clear();           // remove all menu existing
        getMenuInflater().inflate(menuResourceId, menu);
        return true;
    }

    public void changeMenu(View view) {
        int newMenu = -1;
        newMenu = MENU_TWO;

        if(currMenu != newMenu) {
            // force system to call onPrepareOptionsMenu()
            invalidateOptionsMenu();
            currMenu = newMenu;
        }
    }
    static class MyView extends View {

        Paint paint = new Paint();
        Path path = new Path();

        static int color ;
        static int alpha ;


        public MyView(Context context) {
            super(context);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setAlpha(alpha);
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN :
                    path.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE :
                    path.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP :
                    break;
            }

            invalidate();

            return true;
        }

    }

    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }
}