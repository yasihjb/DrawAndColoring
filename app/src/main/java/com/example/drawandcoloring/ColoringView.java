package com.example.drawandcoloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;



import static com.example.drawandcoloring.ColoringActivity.HEIGHT;
import static com.example.drawandcoloring.ColoringActivity.WIDTH;
import static com.example.drawandcoloring.ColoringActivity.MODE;

import java.util.ArrayList;
import java.util.List;

public class ColoringView extends View {
    RelativeLayout layout;
    int selected_color;
    int BORDER_COLOR=Color.BLACK;
    int BACKGROUND_COLOR=Color.WHITE;
    Bitmap layout_bitmap;
    List<Point> Queue=new ArrayList<>();
    Context context;
    int[] array;


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


    public void recursive_flood_fill(int x, int y, int selected_color, Bitmap layout_bitmap){
        int old_color=layout_bitmap.getPixel(x,y);
        if (old_color==selected_color){
            return;
        }
        FloodFillUnit(layout_bitmap,old_color,selected_color,x,y);
    }

    private void FloodFillUnit(Bitmap layout_bitmap, int old_color, int selected_color, int x, int y) {
        if (x<0 || x>WIDTH || y<0 || y>HEIGHT){
            return;
        }
        if (layout_bitmap.getPixel(x,y)!=old_color){
            return;
        }
        layout_bitmap.setPixel(x,y,selected_color);
        FloodFillUnit(layout_bitmap,old_color,selected_color,x+1,y);
        FloodFillUnit(layout_bitmap,old_color,selected_color,x-1,y);
        FloodFillUnit(layout_bitmap,old_color,selected_color,x,y+1);
        FloodFillUnit(layout_bitmap,old_color,selected_color,x,y-1);
    }

    public void flood_fill(int x,int y,int selected_color,Bitmap layout_bitmap){
        Queue.clear();
        int bit_color=layout_bitmap.getPixel(x,y);
        if (bit_color==selected_color){
            return ;
        }
        Queue.add(new Point(x,y));
        while (!Queue.isEmpty()){
            Point selected_point=Queue.remove(0);
            int i=selected_point.x;
            int j=selected_point.y;
            int bit_ij_color=layout_bitmap.getPixel(i,j);
            if (i<0 || i>WIDTH || j<0 || j>HEIGHT || bit_ij_color!=bit_color || bit_ij_color==BORDER_COLOR){
                continue;
            }
            else {
                layout_bitmap.setPixel(i,j,selected_color);
                if (i+1<WIDTH ){
                    Queue.add(new Point(i+1,j));
                }
                if (i-1>=0){
                    Queue.add(new Point(i-1,j));
                }
                if (j+1<HEIGHT){
                    Queue.add(new Point(i,j+1));
                }
                if (j-1>=0){
                    Queue.add(new Point(i,j-1));
                }

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=event.getX();
        float y=event.getY();
        int int_x=(int)(x/1);
        int int_y=(int) (y/1);
        Point p=new Point(int_x,int_y);
        layout_bitmap=layout.getDrawingCache();
        if (MODE.equals("fill")){
            new Fill(layout_bitmap,p,BORDER_COLOR,selected_color).execute();
        }
        if (MODE.equals("eraser")){
            new Fill(layout_bitmap,p,BORDER_COLOR,Color.parseColor("#ffffff")).execute();
        }

        return true;
    }

    class Fill extends AsyncTask<Void,Integer,Void>{
        Bitmap bitmap;
        Point point;
        int selectedColor,targetColor;
        public Fill(Bitmap bm,Point p, int tc, int sc) {
            this.bitmap =bm;
            this.point=p;
            this.selectedColor =sc;
            this.targetColor=tc;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Void doInBackground(Void... params) {
        flood_fill(point.x,point.y, selectedColor,bitmap);
        //recursive_flood_fill(point.x,point.y, selectedColor,bitmap);
//            innerFloodFill innerFloodFill=new innerFloodFill();
//            innerFloodFill.innerFloodFill(bitmap,point.x,point.y,selected_color);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
        }
    }

    class innerFloodFill{
        public void innerFloodFill(Bitmap layout_bitmap,int x,int y,int selected_color){
            if (layout_bitmap.getPixel(x,y)!=selected_color){
                MyFloodFill(layout_bitmap,x,y,WIDTH,HEIGHT,selected_color);
            }
        }

        public  void MyFloodFill(Bitmap layout_bitmap, int x, int y, int width, int height,int selected_color) {
            while (true){
                int ox=x,oy=y;
                while (y!=0 && layout_bitmap.getPixel(x,y-1)!=BORDER_COLOR && y<height && y>0){
                    y--;
                }
                while (x!=0 && layout_bitmap.getPixel(x-1,y)!=BORDER_COLOR && x<width && x>0){
                    x--;
                }
                if (x==ox && y==oy){
                    break;
                }
            }
            FillCore(layout_bitmap,x,y,width,height,selected_color);
        }

        public void FillCore(Bitmap layout_bitmap, int x, int y, int width, int height, int selected_color) {
            int lastRowLength = 0;
            do {
                int rowLength = 0, sx = x;
                if(lastRowLength != 0 && layout_bitmap.getPixel(x,y)==BORDER_COLOR){
                    do {
                        if(--lastRowLength == 0){
                            return;
                        }
                    }while(layout_bitmap.getPixel(++x,y)==BORDER_COLOR);
                    sx=x;
                }else {
                    for (; x != 0 && layout_bitmap.getPixel(x-1,y)!=BORDER_COLOR; rowLength++, lastRowLength++){
                        layout_bitmap.setPixel(--x,y,selected_color);
                        if(y != 0 && layout_bitmap.getPixel(x,y-1)!=BORDER_COLOR){
                            MyFloodFill(layout_bitmap,x,y-1,width,height,selected_color);
                        }
                    }
                }
                for(; sx < width && layout_bitmap.getPixel(sx,y)!=BORDER_COLOR; rowLength++, sx++) {
                    layout_bitmap.setPixel(sx,y,selected_color);
                }
                if (rowLength<lastRowLength){
                    for (int end=x+lastRowLength;++sx<end;){
                        if (layout_bitmap.getPixel(sx,y)!=BORDER_COLOR){
                            MyFloodFill(layout_bitmap,sx,y,width,height,selected_color);
                        }
                    }
                }else if (rowLength>lastRowLength && y!=0){
                    for (int ux=x+lastRowLength;++ux<sx;){
                        if (layout_bitmap.getPixel(ux,y-1)!=BORDER_COLOR ){
                            MyFloodFill(layout_bitmap,ux,y-1,width,height,selected_color);
                        }
                    }
                }
                lastRowLength=rowLength;
            }while (lastRowLength!=0 && ++y<height);
        }
    }

   

}

