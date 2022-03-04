package com.example.drawandcoloring;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;

public class DrawingActivity extends AppCompatActivity implements View.OnClickListener, StatusBarColor{
    ImageView back,save,pallet,pencil,undo,redo,eraser,eyedropper,clear_view;
    RelativeLayout drawView;
    Bitmap bitmap;
    DatabaseHelper databaseHelper;
    String previous;
    String selected_id;
    DrawingView dv;
    private Paint mPaint;
    public static String MODE ="draw";
    public static int WIDTH,HEIGHT;
    public static int[] view_array;
    GradientDrawable gradientDrawable;
    public static RelativeLayout tool_box;
    Bitmap layout_bitmap;
    Toolbar toolbar;
    ColorPickerDialog colorPickerDialog;
    public static ConstraintLayout pencil_toolbox,eraser_toolbox;
    public static SeekBar pencil_seekbar,eraser_seekbar;
    public static RelativeLayout pencil_round_line, pencil_square_line,select_round,select_square,eraser_round_line;
    public static Stack<int[]> undo_array_stack;
    public static Stack<int[]> redo_array_stack;
    int pencil_last_color;
    int eraser_size=10;
    int pencil_size=10;
    int[] array_undo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);
        drawView=findViewById(R.id.draw_view);
        dv =new DrawingView(this,drawView);
        drawView.addView(dv);
        drawView.setDrawingCacheEnabled(true);
        drawView.buildDrawingCache(true);

        databaseHelper=new DatabaseHelper(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        setStatusBarColor(R.color.draw);
        previous=getIntent().getStringExtra("previous");

        redo_array_stack=new Stack<>();
        undo_array_stack=new Stack<>();


        toolbar=findViewById(R.id.toolbar);
        back=findViewById(R.id.button_back);
        save=findViewById(R.id.button_save);
        pallet=findViewById(R.id.pallet);
        pencil=findViewById(R.id.pencil);
        eraser=findViewById(R.id.eraser);
        tool_box=findViewById(R.id.toolbox);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);
        eyedropper=findViewById(R.id.eyedropper);
        select_round=findViewById(R.id.select_circle);
        select_square=findViewById(R.id.select_square);
        eraser_toolbox=findViewById(R.id.eraser_toolbox);
        eraser_toolbox.setVisibility(View.GONE);
        pencil_toolbox=findViewById(R.id.pencil_toolbox);
        pencil_toolbox.setVisibility(View.GONE);
        pencil_seekbar=findViewById(R.id.pencil_seekbar);
        eraser_seekbar=findViewById(R.id.eraser_seekbar);
        pencil_round_line =findViewById(R.id.round_line);
        pencil_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) pencil_size));
        pencil_round_line.setVisibility(View.VISIBLE);
        pencil_square_line =findViewById(R.id.square_line);
        pencil_square_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) pencil_size));
        pencil_square_line.setVisibility(View.GONE);
        eraser_round_line=findViewById(R.id.eraser_round_line);
        eraser_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,eraser_size));
        eraser_round_line.setVisibility(View.VISIBLE);
        clear_view=findViewById(R.id.clear_bg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pencil_seekbar.setMin(1);
        }
        pencil_seekbar.setMax(100);
        pencil_seekbar.setProgress((int) dv.getStrokeWidth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            eraser_seekbar.setMin(1);
        }
        eraser_seekbar.setMax(100);
        eraser_seekbar.setProgress(eraser_size);

        setPencilColor(getResources().getColor(R.color.toolbox));
        pencil_last_color=getResources().getColor(R.color.toolbox);

        drawView.post(new Runnable() {
            @Override
            public void run() {
                WIDTH=drawView.getWidth();
                HEIGHT=drawView.getHeight();
                System.out.println("MAIN:"+"WIDTH="+WIDTH+" HEIGHT="+HEIGHT);
                pushBackgroundIntoUndoStack();
            }
        });

        eraser_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("EraserSeekSize",""+progress);
                dv.setStrokeWidth(progress);
                eraser_size=progress;
                eraser_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pencil_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("PencilSeekSize",""+progress);
                dv.setStrokeWidth(progress);
                pencil_size=progress;
                pencil_round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dv.getStrokeWidth()));
                pencil_square_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dv.getStrokeWidth()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        if (previous.equals("main")){
            drawView.setBackgroundColor(Color.WHITE);
        }else if (previous.equals("show")){
            selected_id=getIntent().getStringExtra("selected_id");
            System.out.println(selected_id);
            Drawable drawable=new BitmapDrawable(DatabaseBitmapUtility.getView(databaseHelper.getViewData(selected_id)));
            drawView.setBackgroundDrawable(drawable);

        }

        colorPickerDialog=new ColorPickerDialog(this);
        colorPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                int color=colorPickerDialog.getSelectedColor();
                setPencilColor(color);
                pencil_last_color=color;
                dv.setStrokeWidth(pencil_seekbar.getProgress());
            }
        });


        back.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        pallet.setOnClickListener(this::onClick);
        pencil.setOnClickListener(this::onClick);
        undo.setOnClickListener(this::onClick);
        redo.setOnClickListener(this::onClick);
        eraser.setOnClickListener(this::onClick);
        toolbar.setOnClickListener(this::onClick);
        eyedropper.setOnClickListener(this::onClick);
        select_round.setOnClickListener(this::onClick);
        select_square.setOnClickListener(this::onClick);
        clear_view.setOnClickListener(this::onClick);

        select_round.callOnClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==toolbar.getId()){
            hidePencilToolbox();
            hideEraserToolbox();
        } else if (view.getId()==back.getId()){
            hidePencilToolbox();
            hideEraserToolbox();
            finish();
        }else if (view.getId()==save.getId()){
            hidePencilToolbox();
            hideEraserToolbox();
            drawView.setDrawingCacheEnabled(true);
            bitmap=drawView.getDrawingCache();
            if(previous.equals("main")){
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
                databaseHelper.InsertData(DatabaseBitmapUtility.getBytes(bitmap),sb.toString(),"draw");
                finish();
            }else if (previous.equals("show")){
                databaseHelper.UpdateViewData(DatabaseBitmapUtility.getBytes(bitmap),selected_id);
                finish();
            }
            drawView.setDrawingCacheEnabled(false);
        }else if (view.getId()==pallet.getId()){
            MODE ="draw";
            hidePencilToolbox();
            hideEraserToolbox();
            colorPickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            colorPickerDialog.show();
        }else if (view.getId()==pencil.getId()){
            hideEraserToolbox();
            setPencilColor(pencil_last_color);
            dv.setStrokeWidth(pencil_size);
            if (pencil_toolbox.getVisibility()==View.GONE){
                pencil_toolbox.setVisibility(View.VISIBLE);
                pencil_toolbox.bringToFront();
                pencil_toolbox.setTranslationZ(0);
            }else if (pencil_toolbox.getVisibility()==View.VISIBLE){
                hidePencilToolbox();
            }
            MODE ="draw";
//            dv.setDefault();
        }else if (view.getId()==eraser.getId()){
            pencil_last_color=dv.getColor();
            hidePencilToolbox();
            dv.setColor(Color.WHITE);
            MODE ="draw";
            dv.setDefault();
            if (eraser_toolbox.getVisibility()==View.GONE){
                eraser_toolbox.setVisibility(View.VISIBLE);
                eraser_toolbox.bringToFront();
                eraser_toolbox.setTranslationZ(0);
            }else if(eraser_toolbox.getVisibility()==View.VISIBLE){
                hideEraserToolbox();
            }
        }else if (view.getId()==undo.getId()){
            hidePencilToolbox();
            hideEraserToolbox();
            System.out.println("unDo");
            if (undo_array_stack.size()!=0){
                dv.unDo();
            }else if (undo_array_stack.size()==0){
                Toast.makeText(this, ":(", Toast.LENGTH_SHORT).show();
            }

        }else if (view.getId()==redo.getId()){
            hidePencilToolbox();
            hideEraserToolbox();
            System.out.println("reDo");
            if (redo_array_stack.size()!=0){
                dv.reDo();
            }else if (redo_array_stack.size()==0){
                Toast.makeText(this, ":(", Toast.LENGTH_SHORT).show();
            }

        }else if(view.getId()==eyedropper.getId()){
            hidePencilToolbox();
            hideEraserToolbox();
            System.out.println("eyedropper");
            MODE="eyedropper";
        }else if (view.getId()==select_round.getId()){
            dv.setStrokeCapRound();
            dv.setStrokeWidth(pencil_seekbar.getProgress());

            gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.circle);
            gradientDrawable.setStroke(5,Color.parseColor("#FFDC04"));
            select_round.setBackgroundDrawable(gradientDrawable);

            gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
            gradientDrawable.setStroke(0,dv.getColor());
            select_square.setBackgroundDrawable(gradientDrawable);

            pencil_square_line.setVisibility(View.GONE);
            pencil_round_line.setVisibility(View.VISIBLE);

        }else if(view.getId()==select_square.getId()) {
            dv.setStrokeSquare();
            Paint paint = new Paint();
            dv.setStrokeWidth(pencil_seekbar.getProgress());

            gradientDrawable = (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
            gradientDrawable.setStroke(5, Color.parseColor("#FFDC04"));
            select_square.setBackgroundDrawable(gradientDrawable);

            gradientDrawable = (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.circle);
            gradientDrawable.setStroke(0, dv.getColor());
            select_round.setBackgroundDrawable(gradientDrawable);

            pencil_square_line.setVisibility(View.VISIBLE);
            pencil_round_line.setVisibility(View.GONE);
        }else if (view.getId()==clear_view.getId()){
            array_undo=new int[WIDTH*HEIGHT];
            drawView.setDrawingCacheEnabled(true);
            bitmap=drawView.getDrawingCache();
            bitmap.getPixels(array_undo,0,WIDTH,0,0,WIDTH,HEIGHT);
            Log.i("redo-undo","undo size BEFORE push= "+undo_array_stack.size());
            undo_array_stack.push(array_undo);
            Log.i("redo-undo","undo size AFTER push= "+undo_array_stack.size());
            drawView.removeAllViews();
            dv=new DrawingView(this,drawView);
            drawView.setBackgroundColor(Color.WHITE);
            drawView.addView(dv);
            drawView.invalidate();
        }
    }


    @Override
    public void setStatusBarColor(int color) {

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(color));
            window.setNavigationBarColor(this.getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void hidePencilToolbox(){
        pencil_toolbox.setVisibility(View.GONE);
    }
    private void hideEraserToolbox(){
        eraser_toolbox.setVisibility(View.GONE);
    }

    private void setPencilColor(int color){
        dv.setColor(color);
        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.toolbox_style);
        gradientDrawable.setColor(color);
        tool_box.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.round_line);
        gradientDrawable.setColor(color);
        pencil_round_line.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square_line);
        gradientDrawable.setColor(color);
        pencil_square_line.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.circle);
        gradientDrawable.setColor(color);
        select_round.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
        gradientDrawable.setColor(color);
        select_square.setBackgroundDrawable(gradientDrawable);
    }

    private void pushBackgroundIntoUndoStack(){
        drawView.setDrawingCacheEnabled(true);
        Bitmap bitmap=drawView.getDrawingCache();
        System.out.println("width= "+bitmap.getWidth());
        int[] array=new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(array,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        undo_array_stack.push(array);
        drawView.setDrawingCacheEnabled(false);
    }


}