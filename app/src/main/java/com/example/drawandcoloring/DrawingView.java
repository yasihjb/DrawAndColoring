package com.example.drawandcoloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static com.example.drawandcoloring.DrawActivity.STATUS;
import static com.example.drawandcoloring.DrawActivity.WIDTH;
import static com.example.drawandcoloring.DrawActivity.HEIGHT;
import static com.example.drawandcoloring.DrawActivity.view_array;

public class DrawingView extends View {
    Paint   mPaint;
    Bitmap  mBitmap;
    Canvas  mCanvas;
    Path    mPath;
    Paint   mBitmapPaint;
    Context context;
    int targetColor;
    RelativeLayout layout;
    Bitmap layout_bitmap;
    Point point=new Point();
    //if value equal 0 means it is empty
    //else if value equal 1 means it is fill
    //else if value equal 2 means it is wall


    public void setColor( int r, int g, int b){
        mPaint.setColor(Color.rgb(r,g,b));
    }

    public int getColor(){
        return mPaint.getColor();
    }

    public void setStrokeWidth(float width){
        mPaint.setStrokeWidth(width);
    }

    public void setStroke(){
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setFill(){
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setStrokeJoinRound(){
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setStrokeJoinBevel(){
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
    }

    public void setStrokeJoinMiter(){
        mPaint.setStrokeJoin(Paint.Join.MITER);
    }

    public void setStrokeCapRound(){
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setStrokeCapButt(){
        mPaint.setStrokeCap(Paint.Cap.BUTT);
    }

    public void setStrokeSquare(){
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    public void setDisable(){
        mPaint.setStrokeWidth(0);
    }
    public void setDefault(){
        mPaint.setStrokeWidth(10);
    }

    public DrawingView(Context context, RelativeLayout layout) {
        super(context);
        this.context=context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        mPath = new Path();
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.RED);
        this.layout=layout;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        if (x>=0 && y>=0 && x<=WIDTH && y<=HEIGHT) {
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
    }

    private void touch_move(float x, float y) {
        if (x>=0 && y>=0 && x<=WIDTH && y<=HEIGHT) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }
    }

    private void touch_up() {
        if (mX>=0 && mY>=0 && mX<=WIDTH && mY<=HEIGHT) {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
    }

    private void import_wall_in_view_array(float x,float y){
        if ( x>=0 && y>=0 && x<=WIDTH && y<=HEIGHT) {
            System.out.println("WIDTH=" + WIDTH + " HEIGHT=" + HEIGHT);
            int int_x = (int) (x/1);
            int int_y = (int) (y/1);
            view_array[int_x][int_y]=(-1); //this is wall
            System.out.println("VALUE OF X="+int_x+" Y="+int_y+" |"+ view_array[int_x][int_y]);
        }
    }

    public void flood_fill(float x,float y ,int targetColor ){
        if (x>=0 && y>=0 && x<=WIDTH && y<=HEIGHT) {
            System.out.println("WIDTH=" + WIDTH + " HEIGHT=" + HEIGHT);
            int int_x = (int) (x/1);
            int int_y = (int) (y/1);
            System.out.println("X="+int_x+" Y="+int_y);
//            mPath.moveTo(int_x, int_y);
//            mPaint.setColor(targetColor);
            layout_bitmap = layout.getDrawingCache();
            System.out.println("VALUE OF THIS FUCKING ARRAY BEFORE="+view_array[int_x][int_y]);
            if (view_array[int_x][int_y]==(-1)){//if pixel is wall
                System.out.println("this is fucking wall");
                return;
            }
            else if (view_array[int_x][int_y]==1){//if pixel is fill
                return;
            }
            //    point=new Point(int_x,int_y);
//                mPath.lineTo(int_x + 1, int_y );
//                mCanvas.drawPath(mPath, mPaint);
            view_array[int_x][int_y] = 1;
            flood_fill(int_x+1, int_y, targetColor);
            flood_fill(int_x, int_y+1, targetColor);
            flood_fill(int_x-1, int_y, targetColor);
            flood_fill(int_x, int_y-1, targetColor);
            System.out.println("VALUE OF THIS FUCKING ARRAY AFTER="+view_array[int_x][int_y]);

//            else if (view_array[int_x][int_y]==0){
////              point=new Point(int_x,int_y);
//                view_array[int_x][int_y] = 1;
////                mPath.lineTo(int_x + 1, int_y );
////                mCanvas.drawPath(mPath, mPaint);
//                flood_fill(int_x+1, int_y, targetColor);
//                flood_fill(int_x, int_y+1, targetColor);
//                flood_fill(int_x-1, int_y, targetColor);
//                flood_fill(int_x, int_y-1, targetColor);
//              System.out.println("VALUE OF THIS FUCKING ARRAY AFTER="+view_array[int_x][int_y]);
//            }
        }
    }

    private void fill(float x,float y){
        mPath.moveTo(x,y);
        mPath.lineTo(x+1,y+1);
        int xx= (int) (x/1);
        int yy= (int) (y/1);
        System.out.println("X: "+xx+" Y: "+yy);
        layout_bitmap=layout.getDrawingCache();
        int b=layout_bitmap.getPixel(xx+1,yy+1);
        System.out.println("Value :"+b);
        int blue=Color.blue(b);
        int green=Color.green(b);
        int red=Color.red(b);
        int alpha=Color.alpha(b);
        System.out.println("COLORS: BLUE="+blue+" RED="+red+" GREEN="+green+" ALPHA="+alpha);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
        targetColor =getColor();

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int int_x=(int) (x/1);
        int int_y=(int) (y/1);
        if(STATUS.equals("draw")){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    System.out.println("START:");
                    import_wall_in_view_array(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    import_wall_in_view_array(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    System.out.println("END:");
                    invalidate();
                    break;
            }
            Log.i("STATUS : ",STATUS+" x :"+int_x+"| y :"+int_y);
        }else if (STATUS.equals("fill")){
            Log.i("STATUS : ",STATUS+" x :"+int_x+"| y :"+int_y);
            targetColor=getColor();
            flood_fill(x,y,targetColor);
            invalidate();
        }


        return true;
    }
}
