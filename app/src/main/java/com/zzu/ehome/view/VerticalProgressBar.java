package com.zzu.ehome.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
/**
 * Created by Administrator on 2016/6/4.
 */
public class VerticalProgressBar extends ProgressBar{
    private Paint mPaint;
    private Rect mRect;
    private String text;







    public VerticalProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
//        TextInit();
    }

    public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
//        TextInit();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
//        TextInit();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
//        text = getProgress() + "%";
        //求文字的宽高
//        mPaint.getTextBounds(text, 0, text.length(), mRect);
//        int x = (getWidth() / 2) - mRect.centerX();
//        int y = (getHeight() / 2) - mRect.centerY();
//        canvas.drawText(text, x, y, mPaint);
    }

//    private void TextInit(){
//        mPaint = new Paint();
//        mRect = new Rect();
//        mPaint.setColor(Color.RED);
//        mPaint.setTextSize(60);
//    }
}
