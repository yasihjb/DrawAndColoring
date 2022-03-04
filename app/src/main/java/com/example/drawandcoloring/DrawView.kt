package com.example.drawandcoloring

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
//import android.support.annotation.ColorInt
//import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.divyanshu.draw.widget.MyPath
import java.util.LinkedHashMap
import com.example.drawandcoloring.DrawingActivity.MODE
import com.example.drawandcoloring.DrawingActivity.drawView
import com.example.drawandcoloring.DrawingActivity.eyedropper_bitmap
import kotlin.math.roundToInt
import com.example.drawandcoloring.DrawingActivity.pencil_round_line
import com.example.drawandcoloring.DrawingActivity.pencil_square_line
import com.example.drawandcoloring.DrawingActivity.select_round
import com.example.drawandcoloring.DrawingActivity.select_square
import com.example.drawandcoloring.DrawingActivity.tool_box
import com.example.drawandcoloring.DrawingActivity.pencil_toolbox
import com.example.drawandcoloring.DrawingActivity.eraser_toolbox


class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var mPaths = LinkedHashMap<MyPath, PaintOptions>()

    private var mLastPaths = LinkedHashMap<MyPath, PaintOptions>()
    private var mUndonePaths = LinkedHashMap<MyPath, PaintOptions>()

    private var mPaint = Paint()
    private var mPath = MyPath()
    private var mPaintOptions = PaintOptions()

    private var mCurX = 0f
    private var mCurY = 0f
    private var mStartX = 0f
    private var mStartY = 0f
    private var mIsSaving = false
    private var mIsStrokeWidthBarEnabled = false

    init {
        mPaint.apply {
            color = mPaintOptions.color
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
//            strokeCap = Paint.Cap.ROUND
            strokeCap = mPaintOptions.strokeCap
            strokeWidth = mPaintOptions.strokeWidth
            isAntiAlias = true
        }
    }

    fun undo() {
        if (mPaths.isEmpty() && mLastPaths.isNotEmpty()) {
            mPaths = mLastPaths.clone() as LinkedHashMap<MyPath, PaintOptions>
            mLastPaths.clear()
            invalidate()
            return
        }
        if (mPaths.isEmpty()) {
            return
        }
        val lastPath = mPaths.values.lastOrNull()
        val lastKey = mPaths.keys.lastOrNull()

        mPaths.remove(lastKey)
        if (lastPath != null && lastKey != null) {
            mUndonePaths[lastKey] = lastPath
        }
        invalidate()
    }

    fun redo() {
        if (mUndonePaths.keys.isEmpty()) {
            return
        }

        val lastKey = mUndonePaths.keys.last()
        addPath(lastKey, mUndonePaths.values.last())
        mUndonePaths.remove(lastKey)
        invalidate()
    }

    fun setColor(newColor: Int) {
        @ColorInt
        val alphaColor = ColorUtils.setAlphaComponent(newColor, mPaintOptions.alpha)
        mPaintOptions.color = alphaColor
        if (mIsStrokeWidthBarEnabled) {
            invalidate()
        }
    }

    fun setSquare(){
        mPaintOptions.strokeCap=Paint.Cap.SQUARE
        if (mIsStrokeWidthBarEnabled) {
            invalidate()
        }
    }

    fun setRound(){
        mPaintOptions.strokeCap=Paint.Cap.ROUND
        if (mIsStrokeWidthBarEnabled) {
            invalidate()
        }
    }

    fun setAlpha(newAlpha: Int) {
        val alpha = (newAlpha*255)/100
        mPaintOptions.alpha = alpha
        setColor(mPaintOptions.color)
    }

    fun setStrokeWidth(newStrokeWidth: Float) {
        mPaintOptions.strokeWidth = newStrokeWidth
        if (mIsStrokeWidthBarEnabled) {
            invalidate()
        }
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        mIsSaving = true
        draw(canvas)
        mIsSaving = false
        return bitmap
    }

    fun addPath(path: MyPath, options: PaintOptions) {
        mPaths[path] = options
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for ((key, value) in mPaths) {
            changePaint(value)
            canvas.drawPath(key, mPaint)
        }

        changePaint(mPaintOptions)
        canvas.drawPath(mPath, mPaint)
    }

    private fun changePaint(paintOptions: PaintOptions) {
        mPaint.color = paintOptions.color
        mPaint.strokeWidth = paintOptions.strokeWidth
        mPaint.strokeCap=paintOptions.strokeCap
    }

    fun clearCanvas() {
        mLastPaths = mPaths.clone() as LinkedHashMap<MyPath, PaintOptions>
        mPath.reset()
        mPaths.clear()
        invalidate()
    }

    private fun actionDown(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mCurX = x
        mCurY = y
    }

    private fun actionMove(x: Float, y: Float) {
        mPath.quadTo(mCurX, mCurY, (x + mCurX) / 2, (y + mCurY) / 2)
        mCurX = x
        mCurY = y
    }

    private fun actionUp() {
        mPath.lineTo(mCurX, mCurY)

        // draw a dot on click
        if (mStartX == mCurX && mStartY == mCurY) {
            mPath.lineTo(mCurX, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY)
        }

        mPaths.put(mPath, mPaintOptions)
        mPath = MyPath()
        mPaintOptions = PaintOptions(mPaintOptions.color, mPaintOptions.strokeWidth, mPaintOptions.alpha,mPaintOptions.strokeCap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (pencil_toolbox.visibility== VISIBLE){
                    pencil_toolbox.visibility= GONE
                }
                if (eraser_toolbox.visibility== VISIBLE){
                    eraser_toolbox.visibility= GONE
                }
                if(MODE.equals("draw")){
                mStartX = x
                mStartY = y
                actionDown(x, y)
                mUndonePaths.clear()
                }
                if (MODE.equals("eyedropper")){
                    val selected_pixel= eyedropper_bitmap.getPixel(x.roundToInt(), y.roundToInt())
                    drawView.destroyDrawingCache()
                    drawView.setColor(selected_pixel)
                    var gradientDrawable=(context.resources.getDrawable(R.drawable.toolbox_style) as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(selected_pixel)
                    tool_box.setBackgroundDrawable(gradientDrawable)

                    gradientDrawable=(context.resources.getDrawable(R.drawable.round_line) as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(selected_pixel)
                    pencil_round_line.setBackgroundDrawable(gradientDrawable)

                    gradientDrawable=(context.resources.getDrawable(R.drawable.square_line) as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(selected_pixel)
                    pencil_square_line.setBackgroundDrawable(gradientDrawable)

                    gradientDrawable=(context.resources.getDrawable(R.drawable.circle) as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(selected_pixel)
                    select_round.setBackgroundDrawable(gradientDrawable)

                    gradientDrawable=(context.resources.getDrawable(R.drawable.square) as GradientDrawable).mutate()
                    (gradientDrawable as GradientDrawable).setColor(selected_pixel)
                    select_square.setBackgroundDrawable(gradientDrawable)

                    MODE="draw"
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> actionMove(x, y)
            MotionEvent.ACTION_UP -> actionUp()
        }

        invalidate()
        return true
    }
}