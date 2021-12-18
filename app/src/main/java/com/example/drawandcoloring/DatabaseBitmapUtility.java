package com.example.drawandcoloring;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DatabaseBitmapUtility {
    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,baos);
        return baos.toByteArray();
    }

    public static Bitmap getView(byte[] view){
        return BitmapFactory.decodeByteArray(view,0,view.length);
    }
}
