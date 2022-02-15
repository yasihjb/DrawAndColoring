package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import static com.example.drawandcoloring.ColoringView.undo_size;

public class ColoringActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView save,back,pallet,undo,redo,paint_roller,eraser;
    RelativeLayout paint_board;
    String paint_uri,previous,selected_id;
    ColoringView cw;
    ColorPicker colorPicker;
    int color_alpha,color_red,color_green,color_blue;
    static int WIDTH,HEIGHT;
    DatabaseHelper databaseHelper;
    Bitmap bitmap;
    public static String MODE="fill";//1-fill 2-eraser
    public static Stack<int[]> undo_array_stack;
    public static Stack<int[]> redo_array_stack;
    public static Stack<Bitmap> undo_stack;
    public static Stack<Bitmap> redo_stack;
    public static List<int[]> fucking_undo,fucking_redo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);
        setStatusBarColor(R.color.paint);
        previous=getIntent().getStringExtra("previous");
        paint_board=findViewById(R.id.draw_board);
        cw=new ColoringView(this,paint_board);
        paint_board.addView(cw);

        paint_board.setDrawingCacheEnabled(true);
        paint_board.buildDrawingCache(true);

        databaseHelper=new DatabaseHelper(this);

        save=findViewById(R.id.button_save);
        back=findViewById(R.id.button_back);
        pallet=findViewById(R.id.pallet);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);
        paint_roller=findViewById(R.id.paint_roller);
        eraser=findViewById(R.id.eraser);

        undo_stack=new Stack<>();
        redo_stack=new Stack<>();

        undo_array_stack=new Stack<>();
        redo_array_stack=new Stack<>();


        fucking_undo=new ArrayList<>();
        fucking_redo=new ArrayList<>();

        paint_board.post(new Runnable() {
            @Override
            public void run() {
                WIDTH=paint_board.getWidth();
                HEIGHT=paint_board.getHeight();
            }
        });

        if (previous.equals("show")){
            selected_id=getIntent().getStringExtra("selected_id");
//            image_bitmap =DatabaseBitmapUtility.getView(databaseHelper.getViewData(selected_id));
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(databaseHelper.getViewData(selected_id)));
            paint_board.setBackground(drawable);
        }else if (previous.equals("main")){
            paint_uri=getIntent().getStringExtra("paint");
            try {
                InputStream inputStream=getContentResolver().openInputStream(Uri.parse(paint_uri));
                Drawable drawable=Drawable.createFromStream(inputStream,paint_uri);
                paint_board.setBackground(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }



        color_red=255;
        color_blue=255;
        color_green=255;
        int defaultColorR=color_red;
        int defaultColorG=color_green;
        int defaultColorB=color_blue;
        colorPicker=new ColorPicker(this,defaultColorR, defaultColorG, defaultColorB);
        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                MODE="fill";
                color_alpha= Color.alpha(color);
                color_red=Color.red(color);
                color_green=Color.green(color);
                color_blue=Color.blue(color);
                cw.setColor(color_red,color_green,color_blue);
                colorPicker.dismiss();
            }
        });

        save.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        paint_roller.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);
        paint_board.setOnClickListener(this::onClick);
        undo.setOnClickListener(this::onClick);
        redo.setOnClickListener(this::onClick);

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
    public void onClick(View view) {
        if (view.getId()==save.getId()){
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
            bitmap=paint_board.getDrawingCache();
            if (previous.equals("main")){
                Calendar calendar=Calendar.getInstance(Locale.getDefault());
                StringBuilder sb=new StringBuilder();
                sb.append(calendar.get(Calendar.YEAR));
                sb.append(calendar.get(Calendar.MONTH));
                sb.append(calendar.get(Calendar.DAY_OF_MONTH));
                sb.append(calendar.get(Calendar.HOUR_OF_DAY));
                sb.append(calendar.get(Calendar.MINUTE));
                sb.append(calendar.get(Calendar.SECOND));
                System.out.println(sb);
                databaseHelper.InsertData(DatabaseBitmapUtility.getBytes(bitmap),sb.toString(),"paint");
                finish();
            }else if (previous.equals("show")){
                databaseHelper.UpdateViewData(DatabaseBitmapUtility.getBytes(bitmap),selected_id);
                finish();
            }

        }else if (view.getId()==back.getId()){
            finish();
        }else if (view.getId()==pallet.getId()){
            colorPicker.show();
        }else if (view.getId()==paint_roller.getId()){
            MODE="fill";
        }else if (view.getId()==eraser.getId()){
            MODE="eraser";
        }else if (view.getId()==paint_board.getId()){

        }else if(view.getId()==undo.getId()){
            Log.i("Event1","UNDO");
            if (undo_array_stack.size()!=0) {
                cw.unDo();
            }else {
                undo_size=1;
                Toast.makeText(this, "Stack is Empty ", Toast.LENGTH_SHORT).show();
            }

        }else if (view.getId()==redo.getId()){
            Log.i("Event1","reDo");
            if (redo_array_stack.size()!=0){
                cw.reDo();
            }else {
                Toast.makeText(this, "Stack is Empty", Toast.LENGTH_SHORT).show();
            }

        }
    }

}