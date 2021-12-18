package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class GalleryActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView back;
    RecyclerView gallery_recycler_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        back=findViewById(R.id.back);
        gallery_recycler_view=findViewById(R.id.gallery_recycler_view);
        setStatusBarColor(R.color.gallery);
        getWindow().setBackgroundDrawableResource(R.color.white);
        back.setOnClickListener(this);
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
        switch (view.getId()){

            case R.id.back:
                finish();
                break;

        }
    }
}