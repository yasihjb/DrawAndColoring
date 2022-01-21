package com.example.drawandcoloring;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.Calendar;
import java.util.Locale;

import dev.sasikanth.colorsheet.ColorSheet;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener ,StatusBarColor{
    ImageView back,save,pallet,pencil,paint_roller,eraser;
    ColorPicker colorPicker;
    int selectedColor;
    int color_alpha,color_red,color_green,color_blue;
    RelativeLayout drawView;
    Bitmap bitmap,fill_bitmap;
    DatabaseHelper databaseHelper;
    String previous;
    String selected_id;
    DrawingView dv;
    private Paint mPaint;
    ColorSheet colorSheet;

    public static String STATUS="draw";
    public static int WIDTH,HEIGHT;
    public static int[][] view_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);
        drawView=findViewById(R.id.draw_view);
        dv =new DrawingView(this,drawView);
        drawView.addView(dv);
        drawView.setDrawingCacheEnabled(true);
        drawView.buildDrawingCache(true);

        databaseHelper=new DatabaseHelper(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        setStatusBarColor(R.color.draw);
        previous=getIntent().getStringExtra("previous");

        color_red=255;
        color_blue=255;
        color_green=255;
        colorSheet=new ColorSheet();

        back=findViewById(R.id.button_back);
        save=findViewById(R.id.button_save);
        pallet=findViewById(R.id.pallet);
        pencil=findViewById(R.id.pencil);
        paint_roller=findViewById(R.id.paint_roller);
        eraser=findViewById(R.id.eraser);

        drawView.post(new Runnable() {
            @Override
            public void run() {
                WIDTH=drawView.getWidth();
                HEIGHT=drawView.getHeight();
                System.out.println("MAIN:"+"WIDTH="+WIDTH+" HEIGHT="+HEIGHT);
                view_array=new int[WIDTH+1][HEIGHT+1];

                if (previous.equals("main")){

                    for (int i=0;i<(WIDTH+1);i++){
                        for (int j=0;j<(HEIGHT+1);j++){
                            view_array[i][j]=0;
                        }
                    }

                }else if (previous.equals("show")){
                    System.out.println("FELAN HICHI");
                }


            }
        });


        if (previous.equals("main")){
//            drawView.setBackgroundColor(Color.parseColor("#ffffff"));
//            drawView.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_bg));

        }else if (previous.equals("show")){
            selected_id=getIntent().getStringExtra("selected_id");
            System.out.println(selected_id);
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(databaseHelper.getViewData(selected_id)));
            drawView.setBackgroundDrawable(drawable);
        }



        int defaultColorR=color_red;
        int defaultColorG=color_green;
        int defaultColorB=color_blue;
        colorPicker=new ColorPicker(this,defaultColorR, defaultColorG, defaultColorB);
        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                color_alpha=Color.alpha(color);
                color_red=Color.red(color);
                color_green=Color.green(color);
                color_blue=Color.blue(color);
//                selectedColor=Color.argb();
                dv.setColor(color_red,color_green,color_blue);
                colorPicker.dismiss();
            }
        });

        back.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        pencil.setOnClickListener(this::onClick);
        paint_roller.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);


    }


    @Override
    public void onClick(View view) {
        if (view.getId()==back.getId()){
            finish();
        }else if (view.getId()==save.getId()){
            bitmap=drawView.getDrawingCache();
            if(previous.equals("main")){
//                int height=drawView.getHeight();
//                int width=drawView.getWidth();
//                Log.i("Height",String.valueOf(height));
//                Log.i("Width",String.valueOf(width));
//                for (int i=0;i<width;i++){
//                    for (int j=0;j<height;j++){
//                        int pixel=bitmap.getPixel(i,j);
//                        int red=Color.red(pixel);
//                        int green=Color.green(pixel);
//                        int blue=Color.blue(pixel);
//                        int alpha=Color.alpha(pixel);
//                    }
//                }
                System.out.println(bitmap);
                Calendar calendar=Calendar.getInstance(Locale.getDefault());
                StringBuilder sb=new StringBuilder();
                sb.append(calendar.get(Calendar.YEAR));
                sb.append(calendar.get(Calendar.MONTH));
                sb.append(calendar.get(Calendar.DAY_OF_MONTH));
                sb.append(calendar.get(Calendar.HOUR_OF_DAY));
                sb.append(calendar.get(Calendar.MINUTE));
                sb.append(calendar.get(Calendar.SECOND));
                System.out.println(sb);
                databaseHelper.InsertData(DatabaseBitmapUtility.getBytes(bitmap),sb.toString());
                finish();
            }else if (previous.equals("show")){
                databaseHelper.UpdateViewData(DatabaseBitmapUtility.getBytes(bitmap),selected_id);
//                Intent intent=new Intent(this,ShowActivity.class);
//                intent.putExtra("selected_id",selected_id);
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }


        }else if (view.getId()==pallet.getId()){
            colorPicker.show();
        }else if (view.getId()==pencil.getId()){
            STATUS="draw";
            dv.setDefault();
        }else if (view.getId()==paint_roller.getId()){
            STATUS="fill";
            dv.setDisable();
        }else if (view.getId()==eraser.getId()){
            dv.setColor(255,255,255);
            STATUS="draw";
            dv.setDefault();
        }
    }


    @Override
    public void setStatusBarColor(int color) {

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(color));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sth() {
        System.out.println("OPSSSS");
        WIDTH=drawView.getWidth();
        HEIGHT=drawView.getHeight();
        System.out.println("MAIN:"+"WIDTH="+WIDTH+" HEIGHT="+HEIGHT);
        view_array=new int[WIDTH+1][HEIGHT+1];
    }
}