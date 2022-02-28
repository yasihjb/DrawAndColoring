package com.example.drawandcoloring;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

public class ColorPickerDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {
    int selected_color;
    Context context;
    ImageView dark_red,red,crimson,light_coral,salmon,light_salmon,orange,golden_rod,yellow,
            moccasin,khaki,dark_khaki,dark_green,islamic_green,chartreuse,spring_green,screaming_green,
            olive_drab,midnight_blue,blue,deep_sky_blue,turquoise,aquamarine,light_cyan,medium_violet_red,
            hot_pink,pink,violet,medium_orchid,purple,black,saddle_brown,white,gray,dim_gray,dark_slate_gray;
    ImageView openRainbow,openPalette,rainbow_range,selected_color_frame;
    RelativeLayout palette_layout,rainbow_layout;
    public ColorPickerDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.colors_palette_dialog);

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

        openRainbow=findViewById(R.id.rainbow);
        openRainbow.setVisibility(View.VISIBLE);
        palette_layout=findViewById(R.id.palette_layout);
        palette_layout.setVisibility(View.VISIBLE);

        rainbow_layout=findViewById(R.id.rainbow_layout);
        rainbow_layout.setVisibility(View.GONE);
        openPalette=findViewById(R.id.palette);
        openPalette.setVisibility(View.GONE);
        rainbow_range.getDrawingCache(true);
        rainbow_range.setDrawingCacheEnabled(true);
        rainbow_range.buildDrawingCache(true);

        rainbow_range.setOnTouchListener(this::onTouch);


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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.palette:
                System.out.println("this is palette");
                palette_layout.setVisibility(View.VISIBLE);
                rainbow_layout.setVisibility(View.GONE);
                openPalette.setVisibility(View.GONE);
                openRainbow.setVisibility(View.VISIBLE);
                break;
            case R.id.rainbow:
                System.out.println("this is rainbow");
                palette_layout.setVisibility(View.GONE);
                rainbow_layout.setVisibility(View.VISIBLE);
                openRainbow.setVisibility(View.GONE);
                openPalette.setVisibility(View.VISIBLE);
                break;
            case R.id.dark_slate_gray:
                selected_color=context.getResources().getColor(R.color.dark_slate_gray);
                dismiss();
                break;
            case R.id.dim_gray:
                selected_color=context.getResources().getColor(R.color.dim_gray);
                dismiss();
                break;
            case R.id.gray:
                selected_color=context.getResources().getColor(R.color.gray);
                dismiss();
                break;
            case R.id.white:
                selected_color=context.getResources().getColor(R.color.white);
                dismiss();
                break;
            case R.id.saddle_brown:
                selected_color=context.getResources().getColor(R.color.saddle_brown);
                dismiss();
                break;
            case R.id.black:
                selected_color=context.getResources().getColor(R.color.black);
                dismiss();
                break;
            case R.id.purple:
                selected_color=context.getResources().getColor(R.color.purple);
                dismiss();
                break;
            case R.id.medium_orchid:
                selected_color=context.getResources().getColor(R.color.medium_orchid);
                dismiss();
                break;
            case R.id.violet:
                selected_color=context.getResources().getColor(R.color.violet);
                dismiss();
                break;
            case R.id.pink:
                selected_color=context.getResources().getColor(R.color.pink);
                dismiss();
                break;
            case R.id.dark_red:
                selected_color=context.getResources().getColor(R.color.dark_red);
                dismiss();
                break;
            case R.id.red:
                selected_color=context.getResources().getColor(R.color.red);
                dismiss();
                break;
            case R.id.crimson:
                selected_color=context.getResources().getColor(R.color.crimson);
                dismiss();
                break;
            case R.id.light_coral:
                selected_color=context.getResources().getColor(R.color.light_coral);
                dismiss();
                break;
            case R.id.salmon:
                selected_color=context.getResources().getColor(R.color.salmon);
                dismiss();
                break;
            case R.id.light_salmon1:
                selected_color=context.getResources().getColor(R.color.light_salmon);
                dismiss();
                break;
            case R.id.orange:
                selected_color=context.getResources().getColor(R.color.orange);
                dismiss();
                break;
            case R.id.golden_rod:
                selected_color=context.getResources().getColor(R.color.golden_rod);
                dismiss();
                break;
            case R.id.yellow:
                selected_color=context.getResources().getColor(R.color.yellow);
                dismiss();
                break;
            case R.id.moccasin:
                selected_color=context.getResources().getColor(R.color.moccasin);
                dismiss();
                break;
            case R.id.khaki1:
                selected_color=context.getResources().getColor(R.color.khaki);
                dismiss();
                break;
            case R.id.dark_khaki1:
                selected_color=context.getResources().getColor(R.color.dark_khaki);
                dismiss();
                break;
            case R.id.dark_green:
                selected_color=context.getResources().getColor(R.color.dark_green);
                dismiss();
                break;
            case R.id.islamic_green:
                selected_color=context.getResources().getColor(R.color.islamic_green);
                dismiss();
                break;
            case R.id.chartreuse:
                selected_color=context.getResources().getColor(R.color.chartreuse);
                dismiss();
                break;
            case R.id.spring_green:
                selected_color=context.getResources().getColor(R.color.spring_green);
                dismiss();
                break;
            case R.id.screaming_green1:
                selected_color=context.getResources().getColor(R.color.screaming_green);
                dismiss();
                break;
            case R.id.olive_drab1:
                selected_color=context.getResources().getColor(R.color.olive_drab);
                dismiss();
                break;
            case R.id.midnight_blue:
                selected_color=context.getResources().getColor(R.color.midnight_blue);
                dismiss();
                break;
            case R.id.blue:
                selected_color=context.getResources().getColor(R.color.blue);
                dismiss();
                break;
            case R.id.deep_sky_blue:
                selected_color=context.getResources().getColor(R.color.deep_sky_blue);
                dismiss();
                break;
            case R.id.turquoise:
                selected_color=context.getResources().getColor(R.color.turquoise);
                dismiss();
                break;
            case R.id.aquamarine:
                selected_color=context.getResources().getColor(R.color.aquamarine);
                dismiss();
                break;
            case R.id.light_cyan:
                selected_color=context.getResources().getColor(R.color.light_cyan);
                dismiss();
                break;
            case R.id.medium_violet_red:
                selected_color=context.getResources().getColor(R.color.medium_violet_red);
                dismiss();
                break;
            case R.id.hot_pink:
                selected_color=context.getResources().getColor(R.color.hot_pink);
                dismiss();
                break;
        }
    }

    public int getSelectedColor(){
        return selected_color;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId()==rainbow_range.getId()){
            rainbow_range.setDrawingCacheEnabled(true);
            Bitmap b = rainbow_range.getDrawingCache();
            int x= (int) event.getX();
            int y= (int) event.getY();

            if (x>=0 && x<b.getWidth() && y>=0 && y<b.getHeight()){
                int pixel=b.getPixel(x,y);
                if (pixel!=0){
                    selected_color=pixel;
                    selected_color_frame.setBackgroundColor(selected_color);
                }
            }
            rainbow_range.setDrawingCacheEnabled(false);
        }
        return true;
    }
}
