package com.example.drawandcoloring;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;

public class ColoringActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener, View.OnTouchListener {
    ImageView save,back,pallet,undo,redo,paint_roller,eraser,eyedropper;
    RelativeLayout paint_board;
    public static RelativeLayout color_palette;
    String paint_uri,previous,selected_id;
    ColoringView cw;
    static int WIDTH,HEIGHT;
    DatabaseHelper databaseHelper;
    Bitmap bitmap;
    public static String MODE="fill";
    public static Stack<int[]> undo_array_stack;
    public static Stack<int[]> redo_array_stack;

    ImageView dark_red,red,crimson,light_coral,salmon,light_salmon,orange,golden_rod,yellow,
            moccasin,khaki,dark_khaki,dark_green,islamic_green,chartreuse,spring_green,screaming_green,
            olive_drab,midnight_blue,blue,deep_sky_blue,turquoise,aquamarine,light_cyan,medium_violet_red,
            hot_pink,pink,violet,medium_orchid,purple,black,saddle_brown,white,gray,dim_gray,dark_slate_gray;
    ImageView openRainbow,openPalette,rainbow_range,selected_color_frame;
    RelativeLayout palette_layout,rainbow_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);
        setStatusBarColor(R.color.white);
        previous=getIntent().getStringExtra("previous");
        paint_board=findViewById(R.id.draw_board);
        cw=new ColoringView(this,paint_board);
        paint_board.addView(cw);

        paint_board.setDrawingCacheEnabled(true);
        paint_board.buildDrawingCache(true);

        databaseHelper=new DatabaseHelper(this);

        findViews();

        setViewsVisibilityStatus();
        
        rainbow_range.getDrawingCache(true);
        rainbow_range.setDrawingCacheEnabled(true);
        rainbow_range.buildDrawingCache(true);
        rainbow_range.setOnTouchListener(this);

        undo_array_stack=new Stack<>();
        redo_array_stack=new Stack<>();

        setColor_palette(R.color.toolbox);

        paint_board.post(new Runnable() {
            @Override
            public void run() {
                WIDTH=paint_board.getWidth();
                HEIGHT=paint_board.getHeight();
            }
        });

        if (previous.equals("show")){
            selected_id=getIntent().getStringExtra("selected_id");
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

        clickListener();

    }

    private void setViewsVisibilityStatus() {
        color_palette.setVisibility(View.GONE);

        openRainbow.setVisibility(View.VISIBLE);
        palette_layout=findViewById(R.id.layout_palette);
        palette_layout.setVisibility(View.VISIBLE);

        rainbow_layout.setVisibility(View.GONE);
        openPalette.setVisibility(View.GONE);
    }

    private void findViews() {
        color_palette=findViewById(R.id.color_palette_bubble);
        save=findViewById(R.id.button_save);
        back=findViewById(R.id.button_back);
        pallet=findViewById(R.id.pallet);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);
        paint_roller=findViewById(R.id.paint_roller);
        eraser=findViewById(R.id.eraser);
        eyedropper=findViewById(R.id.eyedropper);

        dark_red=findViewById(R.id.dark_red);
        red=findViewById(R.id.red);
        crimson=findViewById(R.id.crimson);
        light_coral=findViewById(R.id.light_coral);
        salmon=findViewById(R.id.salmon);
        light_salmon=findViewById(R.id.light_salmon1);
        orange=findViewById(R.id.orange);
        golden_rod=findViewById(R.id.golden_rod);
        yellow=findViewById(R.id.yellow);
        moccasin=findViewById(R.id.moccasin);
        khaki=findViewById(R.id.khaki1);
        dark_khaki=findViewById(R.id.dark_khaki1);
        dark_green=findViewById(R.id.dark_green);
        islamic_green=findViewById(R.id.islamic_green);
        chartreuse=findViewById(R.id.chartreuse);
        spring_green=findViewById(R.id.spring_green);
        screaming_green=findViewById(R.id.screaming_green1);
        olive_drab=findViewById(R.id.olive_drab1);
        midnight_blue=findViewById(R.id.midnight_blue);
        blue=findViewById(R.id.blue);
        deep_sky_blue=findViewById(R.id.deep_sky_blue);
        turquoise=findViewById(R.id.turquoise);
        aquamarine=findViewById(R.id.aquamarine);
        light_cyan=findViewById(R.id.light_cyan);
        medium_violet_red=findViewById(R.id.medium_violet_red);
        hot_pink=findViewById(R.id.hot_pink);
        pink=findViewById(R.id.pink);
        violet=findViewById(R.id.violet);
        medium_orchid=findViewById(R.id.medium_orchid);
        purple=findViewById(R.id.purple);
        black=findViewById(R.id.black);
        saddle_brown=findViewById(R.id.saddle_brown);
        white=findViewById(R.id.white);
        gray=findViewById(R.id.gray);
        dim_gray=findViewById(R.id.dim_gray);
        dark_slate_gray=findViewById(R.id.dark_slate_gray);

        rainbow_range=findViewById(R.id.rainbow_range);
        selected_color_frame=findViewById(R.id.sc);

        openRainbow=findViewById(R.id.open_rainbow);

        rainbow_layout=findViewById(R.id.layout_rainbow);
        openPalette=findViewById(R.id.open_palette);

    }

    private void clickListener() {
        save.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        paint_roller.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);
        paint_board.setOnClickListener(this::onClick);
        undo.setOnClickListener(this::onClick);
        redo.setOnClickListener(this::onClick);
        eyedropper.setOnClickListener(this::onClick);

        openPalette.setOnClickListener(this::onClick);
        openRainbow.setOnClickListener(this::onClick);
        dark_slate_gray.setOnClickListener(this::onClick);
        dim_gray.setOnClickListener(this::onClick);
        gray.setOnClickListener(this::onClick);
        white.setOnClickListener(this::onClick);
        saddle_brown.setOnClickListener(this::onClick);
        black.setOnClickListener(this::onClick);
        dark_red.setOnClickListener(this::onClick);
        red.setOnClickListener(this::onClick);
        crimson.setOnClickListener(this::onClick);
        light_coral.setOnClickListener(this::onClick);
        salmon.setOnClickListener(this::onClick);
        light_salmon.setOnClickListener(this::onClick);
        orange.setOnClickListener(this::onClick);
        golden_rod.setOnClickListener(this::onClick);
        yellow.setOnClickListener(this::onClick);
        moccasin.setOnClickListener(this::onClick);
        khaki.setOnClickListener(this::onClick);
        dark_khaki.setOnClickListener(this::onClick);
        dark_green.setOnClickListener(this::onClick);
        islamic_green.setOnClickListener(this::onClick);
        chartreuse.setOnClickListener(this::onClick);
        spring_green.setOnClickListener(this::onClick);
        screaming_green.setOnClickListener(this::onClick);
        olive_drab.setOnClickListener(this::onClick);
        midnight_blue.setOnClickListener(this::onClick);
        blue.setOnClickListener(this::onClick);
        deep_sky_blue.setOnClickListener(this::onClick);
        turquoise.setOnClickListener(this::onClick);
        aquamarine.setOnClickListener(this::onClick);
        light_cyan.setOnClickListener(this::onClick);
        medium_violet_red.setOnClickListener(this::onClick);
        hot_pink.setOnClickListener(this::onClick);
        pink.setOnClickListener(this::onClick);
        violet.setOnClickListener(this::onClick);
        medium_orchid.setOnClickListener(this::onClick);
        purple.setOnClickListener(this::onClick);
    }

    @Override
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(color));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                hideColorPalette();
                paint_board.setDrawingCacheEnabled(true);
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
                paint_board.setDrawingCacheEnabled(false);
                break;
            case R.id.button_back:
                hideColorPalette();
                finish();
                break;
            case R.id.pallet:
                if (color_palette.getVisibility()==View.GONE){
                    color_palette.setVisibility(View.VISIBLE);
                }else if (color_palette.getVisibility()==View.VISIBLE){
                    hideColorPalette();
                }
                break;
            case R.id.paint_roller:
                hideColorPalette();
                MODE="fill";
                break;
            case R.id.eraser:
                hideColorPalette();
                cw.setColor(Color.WHITE);
                MODE="eraser";
                break;
            case R.id.undo:
                hideColorPalette();
                if (undo_array_stack.size()!=0){
                    cw.unDo();
                }
                break;
            case R.id.redo:
                hideColorPalette();
                if (redo_array_stack.size()!=0){
                    cw.reDo();
                }
                break;
            case R.id.eyedropper:
                hideColorPalette();
                MODE="eyedropper";
                break;
            case R.id.open_palette:
                palette_layout.setVisibility(View.VISIBLE);
                rainbow_layout.setVisibility(View.GONE);
                openPalette.setVisibility(View.GONE);
                openRainbow.setVisibility(View.VISIBLE);
                break;
            case R.id.open_rainbow:
                palette_layout.setVisibility(View.GONE);
                rainbow_layout.setVisibility(View.VISIBLE);
                openRainbow.setVisibility(View.GONE);
                openPalette.setVisibility(View.VISIBLE);
                break;
            case R.id.dark_slate_gray:
                setColor_palette(R.color.dark_slate_gray);
                break;
            case R.id.dim_gray:
                setColor_palette(R.color.dim_gray);
                break;
            case R.id.gray:
                setColor_palette(R.color.gray);
                break;
            case R.id.white:
                setColor_palette(R.color.white);
                break;
            case R.id.saddle_brown:
                setColor_palette(R.color.saddle_brown);
                break;
            case R.id.black:
                setColor_palette(R.color.cute_brown);
                break;
            case R.id.purple:
                setColor_palette(R.color.purple);
                break;
            case R.id.medium_orchid:
                setColor_palette(R.color.medium_orchid);
                break;
            case R.id.violet:
                setColor_palette(R.color.violet);
                break;
            case R.id.pink:
                setColor_palette(R.color.pink);
                break;
            case R.id.dark_red:
                setColor_palette(R.color.dark_red);
                break;
            case R.id.red:
                setColor_palette(R.color.red);
                break;
            case R.id.crimson:
                setColor_palette(R.color.crimson);
                break;
            case R.id.light_coral:
                setColor_palette(R.color.light_coral);
                break;
            case R.id.salmon:
                setColor_palette(R.color.salmon);
                break;
            case R.id.light_salmon1:
                setColor_palette(R.color.light_salmon);
                break;
            case R.id.orange:
                setColor_palette(R.color.orange);
                break;
            case R.id.golden_rod:
                setColor_palette(R.color.golden_rod);
                break;
            case R.id.yellow:
                setColor_palette(R.color.yellow);
                break;
            case R.id.moccasin:
                setColor_palette(R.color.moccasin);
                break;
            case R.id.khaki1:
                setColor_palette(R.color.khaki);
                break;
            case R.id.dark_khaki1:
                setColor_palette(R.color.dark_khaki);
                break;
            case R.id.dark_green:
                setColor_palette(R.color.dark_green);
                break;
            case R.id.islamic_green:
                setColor_palette(R.color.islamic_green);
                break;
            case R.id.chartreuse:
                setColor_palette(R.color.chartreuse);
                break;
            case R.id.spring_green:
                setColor_palette(R.color.spring_green);
                break;
            case R.id.screaming_green1:
                setColor_palette(R.color.screaming_green);
                break;
            case R.id.olive_drab1:
                setColor_palette(R.color.olive_drab);
                break;
            case R.id.midnight_blue:
                setColor_palette(R.color.midnight_blue);
                break;
            case R.id.blue:
                setColor_palette(R.color.blue);
                break;
            case R.id.deep_sky_blue:
                setColor_palette(R.color.deep_sky_blue);
                break;
            case R.id.turquoise:
                setColor_palette(R.color.turquoise);
                break;
            case R.id.aquamarine:
                setColor_palette(R.color.aquamarine);
                break;
            case R.id.light_cyan:
                setColor_palette(R.color.light_cyan);
                break;
            case R.id.medium_violet_red:
                setColor_palette(R.color.medium_violet_red);
                break;
            case R.id.hot_pink:
                setColor_palette(R.color.hot_pink);
                break;

        }
    }

    private void setColor_palette(int color_id){
        cw.setColor(getApplicationContext().getResources().getColor(color_id));
        hideColorPalette();
    }

    private void hideColorPalette(){
        color_palette.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId()==rainbow_range.getId()){
            rainbow_range.setDrawingCacheEnabled(true);
            Bitmap b = rainbow_range.getDrawingCache();
            int x= (int) motionEvent.getX();
            int y= (int) motionEvent.getY();

            if (x>=0 && x<b.getWidth() && y>=0 && y<b.getHeight()){
                int pixel=b.getPixel(x,y);
                if (pixel!=0){
                    cw.setColor(pixel);
                    selected_color_frame.setBackgroundColor(pixel);
                }
            }
            rainbow_range.setDrawingCacheEnabled(false);
        }
        return true;
    }
}