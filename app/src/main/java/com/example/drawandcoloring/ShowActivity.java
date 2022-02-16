package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ShowActivity extends AppCompatActivity implements StatusBarColor, View.OnClickListener {
    ImageView button_back,button_edit,button_delete,button_save_in_gallery,show;
    DatabaseHelper databaseHelper;
    String selected_id,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        setStatusBarColor(R.color.gallery);
        selected_id=getIntent().getExtras().getString("selected_id");
        databaseHelper=new DatabaseHelper(this);
        type=databaseHelper.getType(selected_id);

        button_back =findViewById(R.id.back);
        button_edit=findViewById(R.id.button_edit);
        button_delete=findViewById(R.id.button_delete);
        button_save_in_gallery=findViewById(R.id.button_save_in_gallery);
        show=findViewById(R.id.show_view);

        ShowDataInView();

        button_back.setOnClickListener(this);
        button_edit.setOnClickListener(this::onClick);
        button_delete.setOnClickListener(this::onClick);
        button_save_in_gallery.setOnClickListener(this::onClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Status: ","onResume");
        ShowDataInView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Status: ","onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Status: ","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Status: ","onStop");
    }



    private void ShowDataInView() {
        byte[] data=databaseHelper.getViewData(selected_id);
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
        }else if (view.getId()==button_edit.getId()){
            if(type.equals("draw")){
                Intent intent=new Intent(this, DrawingActivity.class);
                intent.putExtra("previous","show");
                intent.putExtra("selected_id",selected_id);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }else if (type.equals("paint")){
                Intent intent=new Intent(this,ColoringActivity.class);
                intent.putExtra("previous","show");
                intent.putExtra("selected_id",selected_id);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }

        }else if (view.getId()==button_delete.getId()){
            databaseHelper.DeleteData(selected_id);
            finish();
        }
        else if (view.getId()==button_save_in_gallery.getId()){
            Save(selected_id);
            Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
        }
    }

    private void Save(String image_name){
        BitmapDrawable bitmapDrawable= (BitmapDrawable) show.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        FileOutputStream fos=null;
        File file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File directory=new File(file.getAbsolutePath()+"/Draw And Paint/");
        if (!directory.exists()){
            directory.mkdir();
        }
        String filename=image_name+".png";
        File outFile=new File(directory,filename);
        try {
            fos=new FileOutputStream(outFile);

        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
        try {
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}