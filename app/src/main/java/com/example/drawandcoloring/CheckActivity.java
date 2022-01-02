package com.example.drawandcoloring;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.Locale;

public class CheckActivity extends AppCompatActivity {
    DrawingView dv ;
    private Paint mPaint;
    LinearLayout paint_view;
    DatabaseHelper databaseHelper;
    Button save;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        databaseHelper=new DatabaseHelper(this);
        setContentView(R.layout.activity_check);
        save=findViewById(R.id.save);
        mPaint = new Paint();
        paint_view=findViewById(R.id.paint_view);
        paint_view.addView(dv);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(databaseHelper.getViewData("20211131215130")));
        paint_view.setBackgroundDrawable(drawable);
        paint_view.setDrawingCacheEnabled(true);
        paint_view.buildDrawingCache(true);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap=paint_view.getDrawingCache();
                int height=paint_view.getHeight();
                int width=paint_view.getWidth();
                Log.i("Height",String.valueOf(height));
                Log.i("Width",String.valueOf(width));
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
            }
        });

    }
}