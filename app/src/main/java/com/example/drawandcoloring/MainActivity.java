package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_draw,button_paint,button_gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        button_draw=findViewById(R.id.button_draw);
        button_paint=findViewById(R.id.button_paint);
        button_gallery=findViewById(R.id.button_gallery);

        button_draw.setOnClickListener(this::onClick);
        button_paint.setOnClickListener(this::onClick);
        button_gallery.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==button_draw.getId()){
            Intent intent_draw=new Intent(this,DrawActivity.class);
            startActivity(intent_draw.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }else if (view.getId()==button_paint.getId()){
            Intent intent_paint=new Intent(this,PaintActivity.class);
            startActivity(intent_paint.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }else if (view.getId()==button_gallery.getId()){
            Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
        }
    }
}