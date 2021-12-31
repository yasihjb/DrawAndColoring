package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ShowActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView button_back,button_edit,button_delete,button_save_in_gallery,show;
    DatabaseHelper databaseHelper;
    String selected_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        setStatusBarColor(R.color.gallery);
        selected_id=getIntent().getExtras().getString("selected_id");
        databaseHelper=new DatabaseHelper(this);



        button_back =findViewById(R.id.back);
        button_edit=findViewById(R.id.button_edit);
        button_delete=findViewById(R.id.button_delete);
        button_save_in_gallery=findViewById(R.id.button_save_in_gallery);
        show=findViewById(R.id.show_view);

        ShowDataInView();

        button_back.setOnClickListener(this);



    }

    private void ShowDataInView() {
        byte[] data=databaseHelper.getViewData(selected_id);
//        Cursor selected=databaseHelper.getData(selected_id);
//        selected.moveToPosition(0);
//        byte[] data=selected.getBlob(0);
        show.setImageBitmap(DatabaseBitmapUtility.getView(data));
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
        if (view.getId()==button_back.getId()){
            finish();
        }
    }
}