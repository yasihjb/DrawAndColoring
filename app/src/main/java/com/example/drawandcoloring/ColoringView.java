package com.example.drawandcoloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;



import static com.example.drawandcoloring.ColoringActivity.HEIGHT;
import static com.example.drawandcoloring.ColoringActivity.WIDTH;
import static com.example.drawandcoloring.ColoringActivity.MODE;
import static com.example.drawandcoloring.ColoringActivity.fucking_redo;
import static com.example.drawandcoloring.ColoringActivity.fucking_undo;
import static com.example.drawandcoloring.ColoringActivity.redo_array_stack;
import static com.example.drawandcoloring.ColoringActivity.redo_stack;
import static com.example.drawandcoloring.ColoringActivity.undo_array_stack;
import static com.example.drawandcoloring.ColoringActivity.undo_stack;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ColoringView extends View {
    RelativeLayout layout;
    int selected_color;
    int BORDER_COLOR=Color.BLACK;
    int BACKGROUND_COLOR;
    Bitmap layout_bitmap;
    List<Point> Queue=new ArrayList<>();
    Context context;
    int[] array_layout_pixels;
    int[] pixels_for_redo,pixels_for_undo;
    public static int undo_size=1;
    int redo_size=0;

    public int getColor(){
        return selected_color;
    }

    public void setColor(int r,int g,int b){
        selected_color= Color.rgb(r,g,b);
    }

    public ColoringView(Context context, RelativeLayout layout) {
        super(context);
        this.layout=layout;
        this.layout.setDrawingCacheEnabled(true);
        this.layout.buildDrawingCache(true);
        this.context=context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        int int_x=(int)(x/1);
        int int_y=(int) (y/1);
        Point p=new Point(int_x,int_y);
        int action=event.getAction();
        if (action==MotionEvent.ACTION_DOWN){
            if (MODE.equals("fill")){
                new Fill(p,selected_color).execute();
            }
            if (MODE.equals("eraser")){
                new Fill(p,Color.parseColor("#ffffff")).execute();
            }
        }
        return true;
    }


    public void unDo(){
//        if (undo_size==undo_array_stack.size()){
//            new unDo().execute();
//            new unDo().execute();
//        }else {
//
//        }
        new unDo().execute();
    }

    public void reDo(){
        new reDo().execute();
    }

    public void fill(int x, int y, int color) {
        Queue.clear();
        BACKGROUND_COLOR=array_layout_pixels[x + y * WIDTH];
        fillUnit(x, y, color);
        while (!Queue.isEmpty()) {
            Point p = Queue.remove(0);
            if (p.x+1<WIDTH){
                fillUnit(p.x + 1, p.y, color);
            }
            if (p.x-1>=0){
                fillUnit(p.x - 1, p.y, color);
            }
            if (p.y+1<HEIGHT){
                fillUnit(p.x, p.y + 1, color);
            }
            if (p.y-1>=0){
                fillUnit(p.x, p.y - 1, color);
            }

            if (p.y+1<HEIGHT && p.x-1>=0 ){
                fillUnit(p.x-1, p.y + 1, color);
            }
            if (p.y-1>=0 && p.x+1<WIDTH ){
                fillUnit(p.x+1, p.y - 1, color);
            }
            if (p.x+1<WIDTH && p.y+1<HEIGHT){
                fillUnit(p.x + 1, p.y+1, color);
            }
            if (p.x-1>=0 && p.y-1>=0){
                fillUnit(p.x - 1, p.y-1, color);
            }

        }
    }

    private void fillUnit(int x, int y, int color) {
        if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT ) {
            return;
        }
        int pixelColor = array_layout_pixels[x + y * WIDTH];
        if (pixelColor == color || pixelColor==BORDER_COLOR || pixelColor!=BACKGROUND_COLOR) {
            return;
        }
        Queue.add(new Point(x, y));
        array_layout_pixels[x + y * WIDTH] = color;
    }


    class reDo extends AsyncTask<Void,Integer,Void>{
        public reDo() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("reDo");
            pixels_for_undo=new int[WIDTH*HEIGHT];
            pixels_for_redo=new int[WIDTH*HEIGHT];
            pixels_for_redo=redo_array_stack.pop();
            Log.i("Event1","reDo Size After Pop"+String.valueOf(undo_stack.size()));
            layout_bitmap.getPixels(pixels_for_undo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            undo_array_stack.push(pixels_for_undo);
            Log.i("Event1","unDo Size After Push ="+String.valueOf(undo_array_stack.size()));
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            layout_bitmap.setPixels(pixels_for_redo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
        }
    }



    class unDo extends AsyncTask<Void,Integer,Void>{
        public unDo() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pixels_for_undo=new int[WIDTH*HEIGHT];
            pixels_for_redo=new int[WIDTH*HEIGHT];
            pixels_for_undo=undo_array_stack.pop();
            Log.i("Event1","unDo Size After Pop ="+String.valueOf(undo_array_stack.size()));
            layout_bitmap.getPixels(pixels_for_redo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            redo_array_stack.push(pixels_for_redo);
            Log.i("Event1","reDo Size After Push="+String.valueOf(redo_array_stack.size()));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            layout_bitmap.setPixels(pixels_for_undo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
        }
    }


    class Fill extends AsyncTask<Void,Integer,Void>{
        Point point;
        int selectedColor;
        public Fill(Point p, int sc) {
            this.point=p;
            this.selectedColor =sc;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Event1","Pre");
            layout_bitmap = layout.getDrawingCache();
            array_layout_pixels=new int[layout_bitmap.getWidth()*layout_bitmap.getHeight()];
            layout_bitmap.getPixels(array_layout_pixels,0,layout_bitmap.getWidth(),0,0,WIDTH,HEIGHT);
//            if (undo_array_stack.size()==0){
//                undo_array_stack.push(array_layout_pixels);
//            }
            undo_array_stack.push(array_layout_pixels);
            undo_size++;
            Log.i("Event1","unDo Size After Push="+String.valueOf(undo_array_stack.size()));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            fill(point.x,point.y,selectedColor);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            layout_bitmap.setPixels(array_layout_pixels,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
        }
    }

}

