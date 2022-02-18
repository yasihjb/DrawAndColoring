package com.example.drawandcoloring;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.Calendar;
import java.util.Locale;

import dev.sasikanth.colorsheet.ColorSheet;

public class DrawingActivity extends AppCompatActivity implements View.OnClickListener, StatusBarColor{
    ImageView back,save,pallet,pencil,undo,redo,eraser,eyedropper;
    RelativeLayout drawView;
    Bitmap bitmap;
    DatabaseHelper databaseHelper;
    String previous;
    String selected_id;
    DrawingView dv;
    private Paint mPaint;
    public static String MODE ="draw";
    public static int WIDTH,HEIGHT;
    public static int[] view_array;
    GradientDrawable gradientDrawable;
    RelativeLayout tool_box;
    Bitmap layout_bitmap;
    Toolbar toolbar;
    ColorPickerDialog colorPickerDialog;

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

        toolbar=findViewById(R.id.toolbar);
        back=findViewById(R.id.button_back);
        save=findViewById(R.id.button_save);
        pallet=findViewById(R.id.pallet);
        pencil=findViewById(R.id.pencil);
        eraser=findViewById(R.id.eraser);
        tool_box=findViewById(R.id.toolbox);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);
        eyedropper=findViewById(R.id.eyedropper);


        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.toolbox_style);
        gradientDrawable.setColor(getResources().getColor(R.color.toolbox));
        tool_box.setBackgroundDrawable(gradientDrawable);

        drawView.post(new Runnable() {
            @Override
            public void run() {
                WIDTH=drawView.getWidth();
                HEIGHT=drawView.getHeight();
                System.out.println("MAIN:"+"WIDTH="+WIDTH+" HEIGHT="+HEIGHT);
//                if (previous.equals("main")){
//                    System.out.println("FELAN HICHI");
//                }else if (previous.equals("show")){
//                    System.out.println("FELAN HICHI");
//                }

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

        colorPickerDialog=new ColorPickerDialog(this);
        colorPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                int color=colorPickerDialog.getSelectedColor();
                dv.setColor(color);
                gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.toolbox_style);
                gradientDrawable.setColor(color);
                tool_box.setBackgroundDrawable(gradientDrawable);
            }
        });


        back.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        pencil.setOnClickListener(this::onClick);
        undo.setOnClickListener(this::onClick);
        redo.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);
        toolbar.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==toolbar.getId()){

        } else if (view.getId()==back.getId()){
            finish();
        }else if (view.getId()==save.getId()){
            bitmap=drawView.getDrawingCache();
            if(previous.equals("main")){
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
                databaseHelper.InsertData(DatabaseBitmapUtility.getBytes(bitmap),sb.toString(),"draw");
                finish();
            }else if (previous.equals("show")){
                databaseHelper.UpdateViewData(DatabaseBitmapUtility.getBytes(bitmap),selected_id);
                finish();
            }


        }else if (view.getId()==pallet.getId()){
            colorPickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            colorPickerDialog.show();
        }else if (view.getId()==pencil.getId()){
            MODE ="draw";
            dv.setDefault();
        }else if (view.getId()==eraser.getId()){
            dv.setColor(Color.WHITE);
            MODE ="draw";
            dv.setDefault();
        }else if (view.getId()==undo.getId()){
            System.out.println("unDo");
        }else if (view.getId()==redo.getId()){
            System.out.println("reDo");
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




}