package com.example.drawandcoloring;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DB_NAME="draw_and_paint.db";
    public final static String TABLE_NAME="views_data";
    public final static String KEY_VIEW_DATA ="view_data";
    public final static String KEY_VIEW_ID ="view_id";
    public DatabaseHelper(Context context) {
        super(context,DB_NAME,null,1);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" (view_data BLOB NOT NULL," +
                "    view_id   TEXT PRIMARY KEY NOT NULL )" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean InsertData(byte[] bitmap, String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(KEY_VIEW_DATA,bitmap);
        cv.put(KEY_VIEW_ID,id);
        long result=sqLiteDatabase.insert(TABLE_NAME,null,cv);

        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean DeleteData(String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        long result=sqLiteDatabase.delete(TABLE_NAME,"Id=?",new String[]{id});
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean UpdateViewData(byte[] bitmap, String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(KEY_VIEW_DATA,bitmap);
        long result=sqLiteDatabase.update(TABLE_NAME,cv,"Id=?",new String[]{id});
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean UpdateViewDataAndId(byte[] bitmap, String oldId, String newID){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(KEY_VIEW_DATA,bitmap);
        cv.put(KEY_VIEW_ID,newID);
        long result=sqLiteDatabase.update(TABLE_NAME,cv,"Id=?",new String[]{oldId});
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor result=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return result;
    }

    public Cursor getData(String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor result=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_VIEW_ID+" ="+id,null);
        return result;
    }

    public byte[] getViewData(String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT "+KEY_VIEW_DATA+" FROM "+TABLE_NAME+" WHERE "+KEY_VIEW_ID+" ="+id,null);
        cursor.moveToPosition(0);
        byte[] data=cursor.getBlob(0);
        return data;
    }



}
