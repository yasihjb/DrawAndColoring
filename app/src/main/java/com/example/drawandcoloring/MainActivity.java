package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , StatusBarColor {
    Button button_draw,button_paint,button_gallery,check;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper=new DatabaseHelper(this);

        button_draw=findViewById(R.id.button_draw);
        button_paint=findViewById(R.id.button_paint);
        button_gallery=findViewById(R.id.button_gallery);
        setStatusBarColor(R.color.white);
        button_draw.setOnClickListener(this::onClick);
        button_paint.setOnClickListener(this::onClick);
        button_gallery.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==button_draw.getId()){
            Intent intent_draw=new Intent(this, DrawingActivity.class);
            intent_draw.putExtra("previous","main");
            intent_draw.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_draw.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent_draw);
        }else if (view.getId()==button_paint.getId()){
            Intent intent_paint=new Intent(this, PaintsActivity.class);
            intent_paint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent_paint.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent_paint);
        }else if (view.getId()==button_gallery.getId()){
            Intent intent_gallery=new Intent(this,GalleryActivity.class);
            intent_gallery.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent_gallery.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent_gallery);
        }
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
}