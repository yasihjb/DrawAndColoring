package com.example.drawandcoloring;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;
import java.util.Locale;

public class DrawingActivity extends AppCompatActivity implements View.OnClickListener, StatusBarColor, View.OnTouchListener {
    ImageView back,save, palette,pencil,undo,redo,eraser,eyedropper,clear_view;
    Bitmap bitmap;
    DatabaseHelper databaseHelper;
    String previous;
    String selected_id;
    GradientDrawable gradientDrawable;
    public static RelativeLayout color_palette;
    Toolbar toolbar;
    public static ConstraintLayout pencil_toolbox,eraser_toolbox;
    public static SeekBar pencil_seekbar,eraser_seekbar;
    public static RelativeLayout pencil_round_line, pencil_square_line,select_round,select_square,eraser_round_line;
    public static int pencil_last_color;
    int eraser_size=10;
    int pencil_size=10;
    public static DrawView drawView;
    public static String MODE="draw";
    public static Bitmap eyedropper_bitmap=null;

    ImageView dark_red,red,crimson,light_coral,salmon,light_salmon,orange,golden_rod,yellow,
            moccasin,khaki,dark_khaki,dark_green,islamic_green,chartreuse,spring_green,screaming_green,
            olive_drab,midnight_blue,blue,deep_sky_blue,turquoise,aquamarine,light_cyan,medium_violet_red,
            hot_pink,pink,violet,medium_orchid,purple,black,saddle_brown,white,gray,dim_gray,dark_slate_gray;
    ImageView openRainbow,openPalette,rainbow_range,selected_color_frame;
    RelativeLayout palette_layout,rainbow_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        drawView=findViewById(R.id.just_draw);
        drawView.setDrawingCacheEnabled(true);
        drawView.buildDrawingCache(true);

        setStatusBarColor(R.color.white);
        previous=getIntent().getStringExtra("previous");

        databaseHelper=new DatabaseHelper(this);

        findViews();
        
        setViewsVisibilityStatus();


        rainbow_range.getDrawingCache(true);
        rainbow_range.setDrawingCacheEnabled(true);
        rainbow_range.buildDrawingCache(true);
        rainbow_range.setOnTouchListener(this);

      


        pencil_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) pencil_size));
        pencil_square_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) pencil_size));
        eraser_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,eraser_size));



        setStrokeWidth(pencil_size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pencil_seekbar.setMin(1);
        }
        pencil_seekbar.setMax(100);
        pencil_seekbar.setProgress(pencil_size);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            eraser_seekbar.setMin(1);
        }
        eraser_seekbar.setMax(100);
        eraser_seekbar.setProgress(eraser_size);

        setPencilColor(getResources().getColor(R.color.toolbox));
        pencil_last_color=getResources().getColor(R.color.toolbox);

        eraser_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setStrokeWidth(progress);
                eraser_size=progress;
                eraser_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pencil_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setStrokeWidth(progress);
                pencil_size=progress;
                pencil_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) progress));
                pencil_square_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (previous.equals("main")){
            drawView.setBackgroundColor(Color.WHITE);
        }else if (previous.equals("show")){
            selected_id=getIntent().getStringExtra("selected_id");
            System.out.println(selected_id);
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(databaseHelper.getViewData(selected_id)));
            drawView.setBackgroundDrawable(drawable);

        }

        clickListener();

        select_round.callOnClick();

    }

    private void clickListener() {
        back.setOnClickListener(this::onClick);
        undo.setOnClickListener(this::onClick);
        redo.setOnClickListener(this::onClick);
        palette.setOnClickListener(this::onClick);
        pencil.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        eyedropper.setOnClickListener(this::onClick);
        select_round.setOnClickListener(this::onClick);
        select_square.setOnClickListener(this::onClick);
        clear_view.setOnClickListener(this::onClick);

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

    private void setViewsVisibilityStatus() {
        openRainbow.setVisibility(View.VISIBLE);
        palette_layout.setVisibility(View.VISIBLE);
        rainbow_layout.setVisibility(View.GONE);
        openPalette.setVisibility(View.GONE);

        eraser_toolbox.setVisibility(View.GONE);
        pencil_toolbox.setVisibility(View.GONE);

        pencil_round_line.setVisibility(View.VISIBLE);
        pencil_square_line.setVisibility(View.GONE);
        eraser_round_line.setVisibility(View.VISIBLE);
        color_palette.setVisibility(View.GONE);


    }

    private void findViews() {
        toolbar=findViewById(R.id.toolbar);
        back=findViewById(R.id.button_back);
        save=findViewById(R.id.button_save);
        palette =findViewById(R.id.pallet);
        pencil=findViewById(R.id.pencil);
        eraser=findViewById(R.id.eraser);
        eyedropper=findViewById(R.id.eyedropper);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);

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

        palette_layout=findViewById(R.id.layout_palette);
        rainbow_layout=findViewById(R.id.layout_rainbow);
        openPalette=findViewById(R.id.open_palette);

        select_round=findViewById(R.id.select_circle);
        select_square=findViewById(R.id.select_square);
        eraser_toolbox=findViewById(R.id.eraser_toolbox);

        pencil_toolbox=findViewById(R.id.pencil_toolbox);

        pencil_seekbar=findViewById(R.id.pencil_seekbar);
        eraser_seekbar=findViewById(R.id.eraser_seekbar);
        pencil_round_line =findViewById(R.id.round_line);

        pencil_square_line =findViewById(R.id.square_line);
        eraser_round_line=findViewById(R.id.eraser_round_line);
        clear_view=findViewById(R.id.clear_board);
        color_palette=findViewById(R.id.color_palette_bubble);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        pencil_seekbar.setProgress(pencil_size);
        eraser_seekbar.setProgress(eraser_size);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_save:
                drawView.setDrawingCacheEnabled(true);
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
                drawView.setDrawingCacheEnabled(false);
                break;
            case R.id.button_back:
                hideEraserToolbox();
                hidePencilToolbox();
                hideColorPalette();
                finish();
                break;
            case R.id.redo:
                hidePencilToolbox();
                hideEraserToolbox();
                hideColorPalette();
                drawView.redo();
                break;
            case R.id.undo:
                hidePencilToolbox();
                hideEraserToolbox();
                hideColorPalette();
                drawView.undo();
                break;
            case R.id.pallet:
                MODE="draw";
                hidePencilToolbox();
                hideEraserToolbox();
                setStrokeWidth(pencil_size);
                pencil_seekbar.setProgress(pencil_size);
                if (color_palette.getVisibility()==View.GONE){
                    color_palette.setVisibility(View.VISIBLE);
                    color_palette.bringToFront();
                    color_palette.setTranslationZ(0);
                }else if (color_palette.getVisibility()==View.VISIBLE){
                    color_palette.setVisibility(View.GONE);
                }
                break;
            case R.id.pencil:
                MODE="draw";
                setStrokeWidth(pencil_size);
                pencil_seekbar.setProgress(pencil_size);
                hideEraserToolbox();
                hideColorPalette();
                setPencilColor(pencil_last_color);
                if (pencil_toolbox.getVisibility()==View.GONE){
                    pencil_toolbox.setVisibility(View.VISIBLE);
                    pencil_toolbox.bringToFront();
                    pencil_toolbox.setTranslationZ(0);
                }else if (pencil_toolbox.getVisibility()==View.VISIBLE){
                    pencil_toolbox.setVisibility(View.GONE);
                }
                break;
            case R.id.eraser:
                MODE="draw";
                setStrokeWidth(eraser_size);
                eraser_seekbar.setProgress(eraser_size);
                hidePencilToolbox();
                hideColorPalette();
                drawView.setColor(Color.WHITE);
                if (eraser_toolbox.getVisibility()==View.GONE){
                    eraser_toolbox.setVisibility(View.VISIBLE);
                    eraser_toolbox.bringToFront();
                    eraser_toolbox.setTranslationZ(0);
                }else if (eraser_toolbox.getVisibility()==View.VISIBLE){
                    eraser_toolbox.setVisibility(View.GONE);
                }
                break;
            case R.id.eyedropper:
                hideColorPalette();
                hideEraserToolbox();
                hidePencilToolbox();
                MODE="eyedropper";
                drawView.setDrawingCacheEnabled(true);
                eyedropper_bitmap=drawView.getDrawingCache();
                break;
            case R.id.select_circle:
                drawView.setRound();
                drawView.setStrokeWidth(pencil_seekbar.getProgress());

                gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.round);
                gradientDrawable.setStroke(5,getApplicationContext().getResources().getColor(R.color.cute_blue));
                select_round.setBackgroundDrawable(gradientDrawable);

                gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
                gradientDrawable.setStroke(0,pencil_last_color);
                select_square.setBackgroundDrawable(gradientDrawable);

                pencil_square_line.setVisibility(View.GONE);
                pencil_round_line.setVisibility(View.VISIBLE);
                break;
            case R.id.select_square:
                drawView.setSquare();
                drawView.setStrokeWidth(pencil_seekbar.getProgress());

                gradientDrawable = (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
                gradientDrawable.setStroke(5, getApplicationContext().getResources().getColor(R.color.cute_blue));
                select_square.setBackgroundDrawable(gradientDrawable);

                gradientDrawable = (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.round);
                gradientDrawable.setStroke(0, pencil_last_color);
                select_round.setBackgroundDrawable(gradientDrawable);

                pencil_square_line.setVisibility(View.VISIBLE);
                pencil_round_line.setVisibility(View.GONE);
                break;
            case R.id.clear_board:
                hideEraserToolbox();
                drawView.clearCanvas();
                setPencilColor(pencil_last_color);
                setStrokeWidth(pencil_size);
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
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.dark_slate_gray));
                break;
            case R.id.dim_gray:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.dim_gray));
                break;
            case R.id.gray:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.gray));
                break;
            case R.id.white:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.white));
                break;
            case R.id.saddle_brown:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.saddle_brown));
                break;
            case R.id.black:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.black));
                break;
            case R.id.purple:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.purple));
                break;
            case R.id.medium_orchid:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.medium_orchid));
                break;
            case R.id.violet:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.violet));
                break;
            case R.id.pink:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.pink));
                break;
            case R.id.dark_red:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.dark_red));
                break;
            case R.id.red:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.red));
                break;
            case R.id.crimson:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.crimson));
                break;
            case R.id.light_coral:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.light_coral));
                break;
            case R.id.salmon:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.salmon));
                break;
            case R.id.light_salmon1:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.light_salmon));
                break;
            case R.id.orange:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.orange));
                break;
            case R.id.golden_rod:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.golden_rod));
                break;
            case R.id.yellow:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.yellow));
                break;
            case R.id.moccasin:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.moccasin));
                break;
            case R.id.khaki1:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.khaki));
                break;
            case R.id.dark_khaki1:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.dark_khaki));
                break;
            case R.id.dark_green:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.dark_green));
                break;
            case R.id.islamic_green:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.islamic_green));
                break;
            case R.id.chartreuse:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.chartreuse));
                break;
            case R.id.spring_green:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.spring_green));
                break;
            case R.id.screaming_green1:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.screaming_green));
                break;
            case R.id.olive_drab1:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.olive_drab));
                break;
            case R.id.midnight_blue:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.midnight_blue));
                break;
            case R.id.blue:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.blue));
                break;
            case R.id.deep_sky_blue:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.deep_sky_blue));

                break;
            case R.id.turquoise:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.turquoise));
                break;
            case R.id.aquamarine:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.aquamarine));
                break;
            case R.id.light_cyan:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.light_cyan));
                hideColorPalette();
                break;
            case R.id.medium_violet_red:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.medium_violet_red));
                break;
            case R.id.hot_pink:
                setPencilFullColor(getApplicationContext().getResources().getColor(R.color.hot_pink));
                break;

        }

    }

    private void setPencilFullColor(int color){
        setPencilColor(color);
        pencil_last_color=color;
        hideColorPalette();
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

    private void setPencilColor(int color){
        drawView.setColor(color);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.round_line);
        gradientDrawable.setColor(color);
        pencil_round_line.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square_line);
        gradientDrawable.setColor(color);
        pencil_square_line.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.round);
        gradientDrawable.setColor(color);
        select_round.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
        gradientDrawable.setColor(color);
        select_square.setBackgroundDrawable(gradientDrawable);
    }

    private void setStrokeWidth(int size){
        drawView.setStrokeWidth(size);

    }

    private void hidePencilToolbox(){
        pencil_toolbox.setVisibility(View.GONE);
    }

    private void hideEraserToolbox(){
        eraser_toolbox.setVisibility(View.GONE);
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
                    setPencilColor(pixel);
                    pencil_last_color=pixel;
                    selected_color_frame.setBackgroundColor(pixel);
                }
            }
            rainbow_range.setDrawingCacheEnabled(false);
        }
        return true;
    }
}