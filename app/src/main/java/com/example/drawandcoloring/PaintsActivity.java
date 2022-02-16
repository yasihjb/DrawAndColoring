package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PaintsActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView back;
    RecyclerView recycler_view_paints;
    List<String> listOfPaints=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paints);
        setStatusBarColor(R.color.paint);
        back=findViewById(R.id.back);
        recycler_view_paints =findViewById(R.id.recycler_view_paints);

        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p2).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p4).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.cute_bird).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.snake).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.train).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.air_plane).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.bus).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.bee).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.kitty).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.shorti2).toString());

        RecyclerViewAdapter_Paints paints_adapter=new RecyclerViewAdapter_Paints(this,listOfPaints);
        recycler_view_paints.setAdapter(paints_adapter);
        recycler_view_paints.setLayoutManager(new GridLayoutManager(this,2));

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
    public void onClick(View v) {
        if (v.getId()==back.getId()){
            finish();
        }
    }
}