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
        setStatusBarColor(R.color.white);
        back=findViewById(R.id.back);
        recycler_view_paints =findViewById(R.id.recycler_view_paints);

        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_cute_bird).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_snake).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_train).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_air_plane).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_bus).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_bee).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_kitty).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_tree).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_mountain).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_sun).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_grape).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_car).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_flying_saucer).toString());
        listOfPaints.add(Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.p_my_sticker).toString());

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
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==back.getId()){
            finish();
        }
    }
}