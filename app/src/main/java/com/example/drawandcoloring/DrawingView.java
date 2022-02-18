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

import static com.example.drawandcoloring.DrawingActivity.MODE;
import static com.example.drawandcoloring.DrawingActivity.WIDTH;
import static com.example.drawandcoloring.DrawingActivity.HEIGHT;
import static com.example.drawandcoloring.DrawingActivity.view_array;


import java.util.LinkedList;
import java.util.Queue;

public class DrawingView extends View {
    Paint   mPaint;
    Bitmap  mBitmap;
    Canvas  mCanvas;
    Path    mPath;
    Paint   mBitmapPaint;
    Context context;
    RelativeLayout layout;

    public void setColor( int r, int g, int b){
        mPaint.setColor(Color.rgb(r,g,b));
    }

    public void setColor(int color){
        mPaint.setColor(color);
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
//        mPath.lineTo(x, y);
        if (x>=0 && y>=0 && x<=WIDTH && y<=HEIGHT) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                float x2=(x+mX)/2;
                float y2=(y+mY)/2;
               // mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mPath.quadTo(mX, mY, x2, y2);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            int int_x = (int) (x / 1);
            int int_y = (int) (y / 1);
            if (MODE.equals("draw")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("START:");
                        touch_start(x, y);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("MOVE:");
                        touch_move(x, y);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("END:");
                        touch_up();
                        invalidate();
                        break;
                }
                Log.i("STATUS : ", MODE + " x :" + int_x + "| y :" + int_y);
            }
        return true;
    }
}
