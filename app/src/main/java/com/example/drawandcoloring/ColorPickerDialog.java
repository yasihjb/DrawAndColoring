package com.example.drawandcoloring;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class ColorPickerDialog extends Dialog implements View.OnClickListener {
    Button cancel,select;
    int selected_color;
    Context context;
    String activity;
   ImageView red,dark_orange,yellow,chartreuse,spring_green,aqua,blue,electric_indigo,electric_purple,
            fuchsia,razzmatazz,islamic_green,saddle_brown,maroon,mardi_gras,violet,screaming_green,
            laser_lemon,bittersweet,palette,black;
    public ColorPickerDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_picker_dialog);
        cancel=findViewById(R.id.button_cancel);
        select=findViewById(R.id.button_select);
        red=findViewById(R.id.red);
        dark_orange=findViewById(R.id.dark_orange);
        yellow=findViewById(R.id.yellow);
        chartreuse=findViewById(R.id.chartreuse);
        spring_green=findViewById(R.id.spring_green);
        aqua=findViewById(R.id.aqua);
        blue=findViewById(R.id.blue);
        electric_indigo=findViewById(R.id.electric_indigo);
        electric_purple=findViewById(R.id.electric_purple);
        fuchsia=findViewById(R.id.fuchsia);
        islamic_green=findViewById(R.id.islamic_green);
        saddle_brown=findViewById(R.id.saddle_brown);
        maroon=findViewById(R.id.maroon);
        mardi_gras=findViewById(R.id.mardi_gras);
        violet=findViewById(R.id.violet);
        screaming_green=findViewById(R.id.screaming_green);
        laser_lemon=findViewById(R.id.laser_lemon);
        bittersweet=findViewById(R.id.bittersweet);
        palette=findViewById(R.id.open_palette);
        black=findViewById(R.id.black);
        razzmatazz=findViewById(R.id.razzmatazz);
        cancel.setVisibility(View.GONE);
        select.setVisibility(View.GONE);



        cancel.setOnClickListener(this::onClick);
        select.setOnClickListener(this::onClick);


        red.setOnClickListener(this);
        dark_orange.setOnClickListener(this);
        yellow.setOnClickListener(this);
        chartreuse.setOnClickListener(this);
        spring_green.setOnClickListener(this);
        aqua.setOnClickListener(this);
        blue.setOnClickListener(this);
        electric_indigo.setOnClickListener(this);
        electric_purple.setOnClickListener(this);
        fuchsia.setOnClickListener(this);
        razzmatazz.setOnClickListener(this::onClick);
        islamic_green.setOnClickListener(this);
        saddle_brown.setOnClickListener(this);
        maroon.setOnClickListener(this);
        mardi_gras.setOnClickListener(this);
        violet.setOnClickListener(this);
        screaming_green.setOnClickListener(this);
        laser_lemon.setOnClickListener(this);
        bittersweet.setOnClickListener(this);
        black.setOnClickListener(this);

        palette.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.button_cancel:
//                dismiss();
//                break;
//            case R.id.button_select:
//                dismiss();
//                break;
            case R.id.red:
                selected_color=Color.parseColor("#ff0000");
                dismiss();
                break;
            case R.id.aqua:
                selected_color=Color.parseColor("#00FFFF");
                dismiss();
                break;
            case R.id.dark_orange:
                selected_color=Color.parseColor("#ff8000");
                dismiss();
                break;
            case R.id.yellow:
                selected_color=Color.parseColor("#ffff00");
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
            case R.id.blue:
                selected_color=Color.parseColor("#0000FF");
                dismiss();
                break;
            case R.id.electric_indigo:
                selected_color=Color.parseColor("#8000FF");
                dismiss();
                break;
            case R.id.electric_purple:
                selected_color=Color.parseColor("#BF00FF");
                dismiss();
                break;
            case R.id.fuchsia:
                selected_color=Color.parseColor("#FF00FF");
                dismiss();
                break;
            case R.id.razzmatazz:
                selected_color=Color.parseColor("#FF0066");
                dismiss();
                break;
            case R.id.islamic_green:
                selected_color=Color.parseColor("#00B300");
                dismiss();
                break;
            case R.id.saddle_brown:
                selected_color=Color.parseColor("#804000");
                dismiss();
                break;
            case R.id.maroon:
                selected_color=Color.parseColor("#800000");
                dismiss();
                break;
            case R.id.mardi_gras:
                selected_color=Color.parseColor("#1A001A");
                dismiss();
                break;
            case R.id.violet:
                selected_color=Color.parseColor("#FF99FF");
                dismiss();
                break;
            case R.id.screaming_green:
                selected_color=Color.parseColor("#66FF66");
                dismiss();
                break;
            case R.id.laser_lemon:
                selected_color=Color.parseColor("#FFFF66");
                dismiss();
                break;
            case R.id.bittersweet:
                selected_color=Color.parseColor("#FF6666");
                dismiss();
                break;
            case R.id.black:
                selected_color=Color.parseColor("#000000");
                dismiss();
                break;
        }
    }

    public int getSelectedColor(){
        return selected_color;
    }

}
