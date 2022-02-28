package com.example.drawandcoloring;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;

import dev.sasikanth.colorsheet.ColorSheet;

public class DrawingActivity extends AppCompatActivity implements View.OnClickListener, StatusBarColor{
    ImageView back,save,pallet,pencil,undo,redo,eraser,eyedropper;
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
    public static ConstraintLayout pencil_toolbox;
    public static SeekBar pencil_seekbar;
    public static RelativeLayout round_line,square_line,select_round,select_square;
    public static Stack<int[]> undo_array_stack;
    public static Stack<int[]> redo_array_stack;
    int pencil_last_color;

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
        pencil_toolbox=findViewById(R.id.pencil_toolbox);
        pencil_toolbox.setVisibility(View.GONE);
        pencil_seekbar=findViewById(R.id.pencil_seekbar);
        round_line=findViewById(R.id.round_line);
        round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dv.getStrokeWidth()));
        round_line.setVisibility(View.VISIBLE);
        square_line=findViewById(R.id.square_line);
        square_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dv.getStrokeWidth()));
        square_line.setVisibility(View.GONE);

//        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.circle);
//        gradientDrawable.setColor(getResources().getColor(R.color.toolbox));
//        select_round.setBackgroundDrawable(gradientDrawable);

//        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
//        gradientDrawable.setColor(getResources().getColor(R.color.toolbox));
//        select_square.setBackgroundDrawable(gradientDrawable);

//        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.toolbox_style);
//        gradientDrawable.setColor(getResources().getColor(R.color.toolbox));
//        tool_box.setBackgroundDrawable(gradientDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pencil_seekbar.setMin(1);
        }
        pencil_seekbar.setMax(100);
        pencil_seekbar.setProgress((int) dv.getStrokeWidth());

        setPencilColor(getResources().getColor(R.color.toolbox));

        drawView.post(new Runnable() {
            @Override
            public void run() {
                WIDTH=drawView.getWidth();
                HEIGHT=drawView.getHeight();
                System.out.println("MAIN:"+"WIDTH="+WIDTH+" HEIGHT="+HEIGHT);
                pushBackgroundIntoUndoStack();
            }
        });

        pencil_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("SeekSize",""+progress);
                dv.setStrokeWidth(progress);
                round_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dv.getStrokeWidth()));
                square_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dv.getStrokeWidth()));
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

        select_round.callOnClick();


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==toolbar.getId()){
            hidePencilToolbox();
        } else if (view.getId()==back.getId()){
            hidePencilToolbox();
            finish();
        }else if (view.getId()==save.getId()){
            hidePencilToolbox();
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
            colorPickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            colorPickerDialog.show();
        }else if (view.getId()==pencil.getId()){
            setPencilColor(pencil_last_color);
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
        }else if (view.getId()==undo.getId()){
            hidePencilToolbox();
            System.out.println("unDo");
            if (undo_array_stack.size()!=0){
                dv.unDo();
            }else if (undo_array_stack.size()==0){
                Toast.makeText(this, "Stack is Empty", Toast.LENGTH_SHORT).show();
            }

        }else if (view.getId()==redo.getId()){
            hidePencilToolbox();
            System.out.println("reDo");
            if (redo_array_stack.size()!=0){
                dv.reDo();
            }else if (redo_array_stack.size()==0){
                Toast.makeText(this, "Stack is Empty", Toast.LENGTH_SHORT).show();
            }

        }else if(view.getId()==eyedropper.getId()){
            hidePencilToolbox();
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

            square_line.setVisibility(View.GONE);
            round_line.setVisibility(View.VISIBLE);

        }else if(view.getId()==select_square.getId()){
            dv.setStrokeSquare();
            dv.setStrokeWidth(pencil_seekbar.getProgress());

            gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square);
            gradientDrawable.setStroke(5,Color.parseColor("#FFDC04"));
            select_square.setBackgroundDrawable(gradientDrawable);

            gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.circle);
            gradientDrawable.setStroke(0,dv.getColor());
            select_round.setBackgroundDrawable(gradientDrawable);

            square_line.setVisibility(View.VISIBLE);
            round_line.setVisibility(View.GONE);

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

    private void setPencilColor(int color){
        dv.setColor(color);
        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.toolbox_style);
        gradientDrawable.setColor(color);
        tool_box.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.round_line);
        gradientDrawable.setColor(color);
        round_line.setBackgroundDrawable(gradientDrawable);

        gradientDrawable= (GradientDrawable) getApplicationContext().getResources().getDrawable(R.drawable.square_line);
        gradientDrawable.setColor(color);
        square_line.setBackgroundDrawable(gradientDrawable);

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