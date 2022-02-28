package com.example.drawandcoloring;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static com.example.drawandcoloring.DrawingActivity.MODE;
import static com.example.drawandcoloring.DrawingActivity.WIDTH;
import static com.example.drawandcoloring.DrawingActivity.HEIGHT;
import static com.example.drawandcoloring.DrawingActivity.pencil_toolbox;
import static com.example.drawandcoloring.DrawingActivity.tool_box;
import static com.example.drawandcoloring.DrawingActivity.round_line;
import static com.example.drawandcoloring.DrawingActivity.square_line;
import static com.example.drawandcoloring.DrawingActivity.select_round;
import static com.example.drawandcoloring.DrawingActivity.select_square;
import static com.example.drawandcoloring.DrawingActivity.pencil_seekbar;
import static com.example.drawandcoloring.DrawingActivity.redo_array_stack;
import static com.example.drawandcoloring.DrawingActivity.undo_array_stack;

public class DrawingView extends View {
    Paint   mPaint;
    Bitmap  mBitmap;
    Canvas  mCanvas;
    Path    mPath;
    Paint   mBitmapPaint;
    Context context;
    RelativeLayout layout;
    GradientDrawable gradientDrawable;
    int[] array_undo;
    int[] array_redo;
    Bitmap layout_bitmap;


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
    public float getStrokeWidth(){
        return mPaint.getStrokeWidth();
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
        mPaint.setColor(Color.parseColor("#DEDEE0"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        mPath = new Path();
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.RED);
        this.layout=layout;
        this.layout.setDrawingCacheEnabled(true);
        this.layout.buildDrawingCache(true);

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

    public void unDo(){
        new unDo().execute();
    }

    public void reDo(){
        new reDo().execute();
    }

    class reDo extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            array_undo=new int[WIDTH*HEIGHT];
            array_redo=new int[WIDTH*HEIGHT];
            System.out.println("a redo Size="+redo_array_stack.size());
            array_redo=redo_array_stack.pop();
            System.out.println("b redo Size="+redo_array_stack.size());
            layout.setDrawingCacheEnabled(true);
            layout_bitmap =layout.getDrawingCache(true);
            layout_bitmap.getPixels(array_undo,0, layout_bitmap.getWidth(),0,0, layout_bitmap.getWidth(), layout_bitmap.getHeight());
            System.out.println("a undo Size="+undo_array_stack.size());
            undo_array_stack.push(array_undo);
            System.out.println("b undo Size="+undo_array_stack.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            layout_bitmap.setPixels(array_redo,0, layout_bitmap.getWidth(),0,0, layout_bitmap.getWidth(), layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
            layout.setDrawingCacheEnabled(false);
        }
    }

    class unDo extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            array_undo=new int[WIDTH*HEIGHT];
            array_redo=new int[WIDTH*HEIGHT];
            System.out.println("a undo Size="+undo_array_stack.size());
            array_undo=undo_array_stack.pop();
            System.out.println("b undo Size="+undo_array_stack.size());
            layout.setDrawingCacheEnabled(true);
            layout_bitmap =layout.getDrawingCache();
            layout_bitmap.getPixels(array_redo,0, layout_bitmap.getWidth(),0,0, layout_bitmap.getWidth(), layout_bitmap.getHeight());
            System.out.println("a redo Size="+redo_array_stack.size());
            redo_array_stack.push(array_redo);
            System.out.println("b redo Size="+redo_array_stack.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            layout_bitmap.setPixels(array_undo,0, layout_bitmap.getWidth(),0,0, layout_bitmap.getWidth(), layout_bitmap.getHeight());
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(DatabaseBitmapUtility.getBytes(layout_bitmap)));
            layout.setBackgroundDrawable(drawable);
            invalidate();
            layout.setDrawingCacheEnabled(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        float x = event.getX();
        float y = event.getY();
        int int_x = (int) (x / 1);
        int int_y = (int) (y / 1);
        Log.i("STATUS : ", MODE + " x :" + int_x + "| y :" + int_y);
        if (action==MotionEvent.ACTION_DOWN){
            if (pencil_toolbox.getVisibility()==VISIBLE){
                pencil_toolbox.setVisibility(GONE);
            }
            if (pencil_toolbox.getVisibility()==GONE){
                if (MODE.equals("draw")){
                    layout.setDrawingCacheEnabled(true);
                    layout_bitmap =layout.getDrawingCache();
                    array_undo=new int[WIDTH*HEIGHT];
                    layout_bitmap.getPixels(array_undo,0, layout_bitmap.getWidth(),0,0, layout_bitmap.getWidth(), layout_bitmap.getHeight());
                    layout.setDrawingCacheEnabled(false);
                    System.out.println("draw :");
                    System.out.println("a undo Size="+undo_array_stack.size());
                    undo_array_stack.push(array_undo);
                    System.out.println("b undo Size="+undo_array_stack.size());
                    System.out.println("START");
                    touch_start(x, y);
                    invalidate();
                }
                if (MODE.equals("eyedropper")){
                    int[] pixels=new int[WIDTH*HEIGHT];

                    layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap =layout.getDrawingCache();
                    bitmap.getPixels(pixels,0,WIDTH,0,0,WIDTH,HEIGHT);
                    layout.setDrawingCacheEnabled(false);

                    int pixel= pixels[int_x+int_y*WIDTH];

                    System.out.println("this is color="+pixel);

                    if (pixel==0){
                        pixel=getResources().getColor(R.color.white);
                    }

                    setStrokeWidth(pencil_seekbar.getProgress());

                    setColor(pixel);

                    gradientDrawable= (GradientDrawable) context.getApplicationContext().getResources().getDrawable(R.drawable.toolbox_style);
                    gradientDrawable.setColor(getColor());
                    tool_box.setBackgroundDrawable(gradientDrawable);

                    gradientDrawable= (GradientDrawable) context.getApplicationContext().getResources().getDrawable(R.drawable.round_line);
                    gradientDrawable.setColor(getColor());
                    round_line.setBackgroundDrawable(gradientDrawable);

                    gradientDrawable= (GradientDrawable) context.getApplicationContext().getResources().getDrawable(R.drawable.square_line);
                    gradientDrawable.setColor(getColor());
                    square_line.setBackgroundDrawable(gradientDrawable);

                    gradientDrawable= (GradientDrawable) context.getApplicationContext().getResources().getDrawable(R.drawable.circle);
                    gradientDrawable.setColor(getColor());
                    select_round.setBackgroundDrawable(gradientDrawable);

                    gradientDrawable= (GradientDrawable) context.getApplicationContext().getResources().getDrawable(R.drawable.square);
                    gradientDrawable.setColor(getColor());
                    select_square.setBackgroundDrawable(gradientDrawable);

                    MODE="draw";
                    return false;
                }
            }

        }
        if (action==MotionEvent.ACTION_MOVE){
            if (pencil_toolbox.getVisibility()==GONE) {
                System.out.println("MOVE");
                touch_move(x, y);
                invalidate();
            }
        }
        if (action==MotionEvent.ACTION_UP){
            if (pencil_toolbox.getVisibility()==GONE) {
                System.out.println("END");
                touch_up();
                invalidate();
            }
        }
        return true;
    }
}
