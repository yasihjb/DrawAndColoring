package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button_draw,button_paint,button_gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        button_draw=findViewById(R.id.button_draw);
        button_paint=findViewById(R.id.button_paint);
        button_gallery=findViewById(R.id.button_gallery);

    }
}