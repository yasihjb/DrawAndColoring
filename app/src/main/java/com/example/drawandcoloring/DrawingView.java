package com.example.drawandcoloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    Paint       mPaint;
    Bitmap  mBitmap;
    Canvas  mCanvas;
    Path    mPath;
    Paint   mBitmapPaint;

    public void setColor( int r, int g, int b){
        mPaint.setColor(Color.rgb(r,g,b));
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

    public DrawingView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
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
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        // kill this so we don't double draw
        mPath.reset();
        // mPath= new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}
