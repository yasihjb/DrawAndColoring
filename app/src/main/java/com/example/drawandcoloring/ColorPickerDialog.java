package com.example.drawandcoloring;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class ColorPickerDialog extends Dialog implements View.OnClickListener {
    int selected_color;
    Context context;
    ImageView dark_red,red,crimson,light_coral,salmon,light_salmon,orange,golden_rod,yellow,
            moccasin,khaki,dark_khaki,dark_green,islamic_green,chartreuse,spring_green,screaming_green,
            olive_drab,midnight_blue,blue,deep_sky_blue,turquoise,aquamarine,light_cyan,medium_violet_red,
            hot_pink,pink,violet,medium_orchid,purple,black,saddle_brown,white,gray,dim_gray,dark_slate_gray;
    public ColorPickerDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_colors_palette);
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

    @SuppressLint("Range")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dark_slate_gray:
                selected_color=Color.parseColor("#2F4F4F");
                dismiss();
                break;
            case R.id.dim_gray:
                selected_color=Color.parseColor("#696969");
                dismiss();
                break;
            case R.id.gray:
                selected_color=Color.parseColor("#808080");
                dismiss();
                break;
            case R.id.white:
                selected_color=Color.parseColor("#ffffff");
                dismiss();
                break;
            case R.id.saddle_brown:
                selected_color=Color.parseColor("#804000");
                dismiss();
                break;
            case R.id.black:
                selected_color=Color.parseColor("#000000");
                dismiss();
                break;
            case R.id.purple:
                selected_color=Color.parseColor("#800080");
                dismiss();
                break;
            case R.id.medium_orchid:
                selected_color=Color.parseColor("#BA55D3");
                dismiss();
                break;
            case R.id.violet:
                selected_color=Color.parseColor("#FF99FF");
                dismiss();
                break;
            case R.id.pink:
                selected_color=Color.parseColor("#FFC0CB");
                dismiss();
                break;
            case R.id.dark_red:
                selected_color=Color.parseColor("#8B0000");
                dismiss();
                break;
            case R.id.red:
                selected_color=Color.parseColor("#ff0000");
                dismiss();
                break;
            case R.id.crimson:
                selected_color=Color.parseColor("#DC143C");
                dismiss();
                break;
            case R.id.light_coral:
                selected_color=Color.parseColor("#F08080");
                dismiss();
                break;
            case R.id.salmon:
                selected_color=Color.parseColor("#FA8072");
                dismiss();
                break;
            case R.id.light_salmon1:
                selected_color=Color.parseColor("#FFA07A");
                dismiss();
                break;
            case R.id.orange:
                selected_color=Color.parseColor("#FFA500");
                dismiss();
                break;
            case R.id.golden_rod:
                selected_color=Color.parseColor("#DAA520");
                dismiss();
                break;
            case R.id.yellow:
                selected_color=Color.parseColor("#ffff00");
                dismiss();
                break;
            case R.id.moccasin:
                selected_color=Color.parseColor("#FFE4B5");
                dismiss();
                break;
            case R.id.khaki1:
                selected_color=Color.parseColor("#BDB76B");
                dismiss();
                break;
            case R.id.dark_khaki1:
                selected_color=Color.parseColor("#F0E68C");
                dismiss();
                break;
            case R.id.dark_green:
                selected_color=Color.parseColor("#006400");
                dismiss();
                break;
            case R.id.islamic_green:
                selected_color=Color.parseColor("#00B300");
                dismiss();
                break;
            case R.id.chartreuse:
                selected_color=Color.parseColor("#80ff00");
                dismiss();
                break;
            case R.id.spring_green:
                selected_color=Color.parseColor("#00ff80");
                dismiss();
                break;
            case R.id.screaming_green:
                selected_color=Color.parseColor("#66FF66");
                dismiss();
                break;
            case R.id.olive_drab1:
                selected_color=Color.parseColor("#6B8E23");
                dismiss();
                break;
            case R.id.midnight_blue:
                selected_color=Color.parseColor("#191970");
                dismiss();
                break;
            case R.id.blue:
                selected_color=Color.parseColor("#0000FF");
                dismiss();
                break;
            case R.id.deep_sky_blue:
                selected_color=Color.parseColor("#00BFFF");
                dismiss();
                break;
            case R.id.turquoise:
                selected_color=Color.parseColor("#40E0D0");
                dismiss();
                break;
            case R.id.aquamarine:
                selected_color=Color.parseColor("#7FFFD4");
                dismiss();
                break;
            case R.id.light_cyan:
                selected_color=Color.parseColor("#E0FFFF");
                dismiss();
                break;
            case R.id.medium_violet_red:
                selected_color=Color.parseColor("#C71585");
                dismiss();
                break;
            case R.id.hot_pink:
                selected_color=Color.parseColor("#FF69B4");
                dismiss();
                break;


        }
    }

    public int getSelectedColor(){
        return selected_color;
    }

}
