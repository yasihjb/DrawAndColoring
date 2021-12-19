package com.example.drawandcoloring;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.divyanshu.draw.widget.DrawView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener ,StatusBarColor{
    ImageView back,save,pallet,pencil,paint_roller,eraser;
    ColorPicker colorPicker;
    int selectedColor;
    int color_alpha,color_red,color_green,color_blue;
    DrawView drawView;
    Bitmap bitmap;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        databaseHelper=new DatabaseHelper(this);
        setStatusBarColor(R.color.draw);

        color_red=255;
        color_blue=255;
        color_green=255;

        back=findViewById(R.id.button_back);
        save=findViewById(R.id.button_save);
        pallet=findViewById(R.id.pallet);
        pencil=findViewById(R.id.pencil);
        paint_roller=findViewById(R.id.paint_roller);
        eraser=findViewById(R.id.eraser);
        drawView=findViewById(R.id.draw_view);
        drawView.setDrawingCacheEnabled(true);
        drawView.buildDrawingCache(true);

        int defaultColorR=color_red;
        int defaultColorG=color_green;
        int defaultColorB=color_blue;
        colorPicker=new ColorPicker(this,defaultColorR, defaultColorG, defaultColorB);
        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
                color_alpha=Color.alpha(color);
                color_red=Color.red(color);
                color_green=Color.green(color);
                color_blue=Color.blue(color);
                selectedColor=Color.argb(color_alpha,color_red,color_green,color_blue);
                drawView.setColor(selectedColor);
                colorPicker.dismiss();
            }
        });

        back.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        pencil.setOnClickListener(this::onClick);
        paint_roller.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==back.getId()){
            Intent intent_back=new Intent(this,MainActivity.class);
            startActivity(intent_back.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            finish();
        }else if (view.getId()==save.getId()){
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
            bitmap=drawView.getDrawingCache();
            int height=drawView.getHeight();
            int width=drawView.getWidth();
            Log.i("Height",String.valueOf(height));
            Log.i("Width",String.valueOf(width));
            for (int i=0;i<width;i++){
                for (int j=0;j<height;j++){
                    int pixel=bitmap.getPixel(i,j);
                    int red=Color.red(pixel);
                    int green=Color.green(pixel);
                    int blue=Color.blue(pixel);
                    int alpha=Color.alpha(pixel);
                }
            }
            System.out.println(bitmap);
            Calendar calendar=Calendar.getInstance(Locale.getDefault());
            StringBuilder sb=new StringBuilder();
            sb.append(calendar.get(Calendar.YEAR));
            sb.append(calendar.get(Calendar.MONTH));
            sb.append(calendar.get(Calendar.DAY_OF_MONTH));
            sb.append(calendar.get(Calendar.HOUR_OF_DAY));
            sb.append(calendar.get(Calendar.MINUTE));
            sb.append(calendar.get(Calendar.SECOND));
            System.out.println(sb);
            databaseHelper.InsertData(DatabaseBitmapUtility.getBytes(bitmap),sb.toString());
        }else if (view.getId()==pallet.getId()){
//            Toast.makeText(this, "Pallet", Toast.LENGTH_SHORT).show();
            colorPicker.show();
        }else if (view.getId()==pencil.getId()){
            Toast.makeText(this, "Pencil", Toast.LENGTH_SHORT).show();
        }else if (view.getId()==paint_roller.getId()){
            Toast.makeText(this, "Paint Roller", Toast.LENGTH_SHORT).show();
        }else if (view.getId()==eraser.getId()){
            drawView.setColor(Color.parseColor("#ffffff"));
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}