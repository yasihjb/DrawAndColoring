package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView back;
    RecyclerView gallery_recycler_view;
    TextView empty;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        databaseHelper=new DatabaseHelper(this);
        back=findViewById(R.id.back);
        gallery_recycler_view=findViewById(R.id.gallery_recycler_view);
        gallery_recycler_view.setVisibility(View.VISIBLE);
        empty=findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        setStatusBarColor(R.color.cute_blue);
        getWindow().setBackgroundDrawableResource(R.color.white);
        back.setOnClickListener(this);

        ShowData();
    }

    private void ShowData() {
        Cursor cursor=databaseHelper.getAllData();
        if (cursor.getCount()==0){
            IfDbIsEmptyDoThis();
        }else {
            empty.setVisibility(View.GONE);
            gallery_recycler_view.setVisibility(View.VISIBLE);
            RecyclerViewAdapter_Gallery adapter_gallery=new RecyclerViewAdapter_Gallery(this,cursor);
            gallery_recycler_view.setLayoutManager(new GridLayoutManager(this,2));
            gallery_recycler_view.setAdapter(adapter_gallery);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowData();

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

    public void IfDbIsEmptyDoThis(){
        gallery_recycler_view.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
    }
}