package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class PaintActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView save,back,pallet,pencil,paint_roller,eraser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setStatusBarColor(R.color.paint);

        save=findViewById(R.id.button_save);
        back=findViewById(R.id.button_back);
        pallet=findViewById(R.id.pallet);
        pencil=findViewById(R.id.pencil);
        paint_roller=findViewById(R.id.paint_roller);
        eraser=findViewById(R.id.eraser);

        save.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        paint_roller.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        pencil.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);



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
        }else if (view.getId()==back.getId()){
            Intent intent_back=new Intent(this,MainActivity.class);
            startActivity(intent_back.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            finish();
        }else if (view.getId()==pallet.getId()){
            Toast.makeText(this, "Pallet", Toast.LENGTH_SHORT).show();
        }else if (view.getId()==pencil.getId()){
            Toast.makeText(this, "Pencil", Toast.LENGTH_SHORT).show();
        }else if (view.getId()==paint_roller.getId()){
            Toast.makeText(this, "Paint Roller", Toast.LENGTH_SHORT).show();
        }else if (view.getId()==eraser.getId()){
            Toast.makeText(this, "Eraser", Toast.LENGTH_SHORT).show();
        }
    }
}