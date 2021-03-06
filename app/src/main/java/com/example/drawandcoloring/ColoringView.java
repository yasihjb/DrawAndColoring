package com.example.drawandcoloring;

import static com.example.drawandcoloring.ColoringActivity.HEIGHT;
import static com.example.drawandcoloring.ColoringActivity.MODE;
import static com.example.drawandcoloring.ColoringActivity.WIDTH;
import static com.example.drawandcoloring.ColoringActivity.redo_array_stack;
import static com.example.drawandcoloring.ColoringActivity.undo_array_stack;
import static com.example.drawandcoloring.ColoringActivity.color_palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ColoringView extends View {
    RelativeLayout layout;
    public static int selected_color;
    int BORDER_COLOR=Color.BLACK;
    int BACKGROUND_COLOR;
    Bitmap layout_bitmap;
    List<Point> Queue=new ArrayList<>();
    Context context;
    int[] array_layout_pixels;
    int[] pixels_for_redo,pixels_for_undo;

    public int getColor(){
        return selected_color;
    }

    public void setColor(int r,int g,int b){
        selected_color= Color.rgb(r,g,b);
    }
    public void setColor(int color){
        selected_color=color;
    }
    public void setColor(String color){
        selected_color=Color.parseColor(color);
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
        Point p=new Point((int)x,(int)y);
        int action=event.getAction();
        if (action==MotionEvent.ACTION_DOWN){
            if (MODE.equals("fill") || MODE.equals("eraser")){
                layout.setDrawingCacheEnabled(true);
                layout_bitmap = layout.getDrawingCache();
                array_layout_pixels=new int[layout_bitmap.getWidth()*layout_bitmap.getHeight()];
                layout_bitmap.getPixels(array_layout_pixels,0,layout_bitmap.getWidth(),0,0,WIDTH,HEIGHT);
                undo_array_stack.push(array_layout_pixels);
                layout.destroyDrawingCache();
                new Fill(p,selected_color).execute();
            }
            if (MODE.equals("eyedropper")){
                layout.setDrawingCacheEnabled(true);
                Bitmap bitmap=layout.getDrawingCache();
                int pixel=bitmap.getPixel(p.x,p.y);
                layout.setDrawingCacheEnabled(false);
                setColor(pixel);
                MODE="fill";
            }
            if (color_palette.getVisibility()==VISIBLE){
                color_palette.setVisibility(GONE);
            }
        }
        return true;
    }

//    public void innerFloodFill(int[] layout_bitmap_array,int x,int y,int selected_color,int width,int height){
//        if (layout_bitmap_array[x+(y*width)]!=selected_color){
//            MyFloodFill(layout_bitmap_array,x,y,width,height,selected_color);
//        }
//    }
//
//    public  void MyFloodFill(int[] layout_bitmap_array, int x, int y, int width, int height,int selected_color) {
//        while (true){
//            int ox=x,oy=y;
//            while (y!=0 && layout_bitmap_array[x+((y-1)*width)]!=BORDER_COLOR && y<height && y>0){
//                y--;
//            }
//            while (x!=0 && layout_bitmap_array[(x-1)+(y*width)]!=BORDER_COLOR && x<width && x>0){
//                x--;
//            }
//            if (x==ox && y==oy){
//                break;
//            }
//        }
//        FillCore(layout_bitmap_array,x,y,width,height,selected_color);
//    }
//
//    public void FillCore(int[] layout_bitmap_array, int x, int y, int width, int height, int selected_color) {
//        int lastRowLength = 0;
//        do {
//            int rowLength = 0, sx = x;
//            if(lastRowLength != 0 && layout_bitmap_array[x+(y*width)]==BORDER_COLOR){
//                do {
//                    if(--lastRowLength == 0){
//                        return;
//                    }
//                }while( layout_bitmap_array[(++x)+(y*width)]==BORDER_COLOR);
//                sx=x;
//            }else {
//                for (; x != 0 && layout_bitmap_array[(x-1)+(y*width)]!=BORDER_COLOR; rowLength++, lastRowLength++){
//                    layout_bitmap_array[(--x)+(y*width)]=selected_color;
//                    if(y != 0 && layout_bitmap_array[x+((y-1)*width)]!=BORDER_COLOR){
//                        MyFloodFill(layout_bitmap_array,x,y-1,width,height,selected_color);
//                    }
//                }
//            }
//            for(; sx < width && layout_bitmap_array[sx+(y*width)]!=BORDER_COLOR; rowLength++, sx++) {
//                layout_bitmap_array[sx+(y*width)]=selected_color;
//            }
//            if (rowLength<lastRowLength){
//                for (int end=x+lastRowLength;++sx<end;){
//                    if (layout_bitmap_array[sx+(y*width)]!=BORDER_COLOR){
//                        MyFloodFill(layout_bitmap_array,sx,y,width,height,selected_color);
//                    }
//                }
//            }else if (rowLength>lastRowLength && y!=0){
//                for (int ux=x+lastRowLength;++ux<sx;){
//                    if (layout_bitmap_array[ux+((y-1)*width)]!=BORDER_COLOR ){
//                        MyFloodFill(layout_bitmap_array,ux,y-1,width,height,selected_color);
//                    }
//                }
//            }
//            lastRowLength=rowLength;
//        }while (lastRowLength!=0 && ++y<height);
//    }

    public void unDo(){
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
        int pixelColor = array_layout_pixels[x + y * WIDTH];
        if (pixelColor == color || pixelColor==BORDER_COLOR) {
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
            pixels_for_undo=new int[WIDTH*HEIGHT];
            pixels_for_redo=new int[WIDTH*HEIGHT];
            pixels_for_redo=redo_array_stack.pop();
            layout.setDrawingCacheEnabled(true);
            layout_bitmap=layout.getDrawingCache();
            layout_bitmap.getPixels(pixels_for_undo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            undo_array_stack.push(pixels_for_undo);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            layout_bitmap.setPixels(pixels_for_redo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
            layout.setDrawingCacheEnabled(false);
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
            layout.setDrawingCacheEnabled(true);
            layout_bitmap=layout.getDrawingCache();
            layout_bitmap.getPixels(pixels_for_redo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            redo_array_stack.push(pixels_for_redo);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            layout_bitmap.setPixels(pixels_for_undo,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
            layout.setDrawingCacheEnabled(false);
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
            layout.setDrawingCacheEnabled(true);
            layout_bitmap = layout.getDrawingCache();
            array_layout_pixels=new int[layout_bitmap.getWidth()*layout_bitmap.getHeight()];
            layout_bitmap.getPixels(array_layout_pixels,0,layout_bitmap.getWidth(),0,0,WIDTH,HEIGHT);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            fill(point.x,point.y,selectedColor);
//            innerFloodFill(array_layout_pixels,point.x,point.y,selectedColor,WIDTH,HEIGHT);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            layout_bitmap.setPixels(array_layout_pixels,0,layout_bitmap.getWidth(),0,0,layout_bitmap.getWidth(),layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            layout.setDrawingCacheEnabled(false);
            invalidate();

        }
    }

}

