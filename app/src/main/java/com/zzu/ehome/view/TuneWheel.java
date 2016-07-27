package com.zzu.ehome.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zzu.ehome.R;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TuneWheel extends View {

    public interface OnValueChangeListener {
        public void onValueChange(float value);
    }

    public static final int MOD_TYPE_HALF = 2;
    public static final int MOD_TYPE_ONE = 10;

    private static final int ITEM_HALF_DIVIDER = 40;
    private static final int ITEM_ONE_DIVIDER = 10;

    private static  int ITEM_MAX_HEIGHT = 30;
    private static final int ITEM_MIN_HEIGHT = 20;

    private static final int TEXT_SIZE = 14;

    private float mDensity;
//    private int mValue = 50, mMaxValue = 100, mModType = MOD_TYPE_HALF, mLineDivider = ITEM_HALF_DIVIDER;
     private int mValue = 50, mMaxValue = 500, mModType = MOD_TYPE_ONE,
     mLineDivider = ITEM_ONE_DIVIDER;

    private int mLastX, mMove;
    private int mWidth, mHeight;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;

    @SuppressWarnings("deprecation")
    public TuneWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        // setBackgroundResource(R.drawable.bg_wheel);
        setBackgroundDrawable(createBackground());
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyTuneWheel);
        int defaultValue = typedArray.getInt(R.styleable.MyTuneWheel_DefaltValue,0);
        int maxValue = typedArray.getInt(R.styleable.MyTuneWheel_MaxValue,2);
        int model = typedArray.getInt(R.styleable.MyTuneWheel_Model,MOD_TYPE_ONE);
        initViewParam(defaultValue,maxValue,model);
    }

    private GradientDrawable createBackground() {
        float strokeWidth = 4 * mDensity; // 边框宽度
        float roundRadius = 6 * mDensity; // 圆角半径
        int strokeColor = Color.parseColor("#FFFFFFFF");// 边框颜色
        // int fillColor = Color.parseColor("#DFDFE0");// 内部填充颜色

        setPadding((int)strokeWidth, (int)strokeWidth, (int)strokeWidth, 0);

        int colors[] = { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF };// 分别为开始颜色，中间夜色，结束颜色
        GradientDrawable bgDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);// 创建drawable
        // bgDrawable.setColor(fillColor);
        bgDrawable.setCornerRadius(roundRadius);
        bgDrawable.setStroke((int)strokeWidth, strokeColor);
        // setBackgroundDrawable(gd);
        return bgDrawable;
    }

    /**
     *
     * 考虑可扩展，但是时间紧迫，只可以支持两种类型效果图中两种类型
     *
     * @param value
     *            初始值
     * @param maxValue
     *            最大值
     * @param model
     *            刻度盘精度：<br>
     *            {@link MOD_TYPE_HALF}<br>
     *            {@link MOD_TYPE_ONE}<br>
     */
    public void initViewParam(int defaultValue, int maxValue, int model) {
        switch (model) {
            case MOD_TYPE_HALF:
                mModType = MOD_TYPE_HALF;
                mLineDivider = ITEM_HALF_DIVIDER;
                mValue = defaultValue * 2;
                mMaxValue = maxValue * 2;
                break;
            case MOD_TYPE_ONE:
                mModType = MOD_TYPE_ONE;
                mLineDivider = ITEM_ONE_DIVIDER;
                mValue = defaultValue;
                mMaxValue = maxValue;
                break;

            default:
                break;
        }
        invalidate();

        mLastX = 0;
        mMove = 0;
        notifyValueChange();
    }

    /**
     * 设置用于接收结果的监听器
     *
     * @param listener
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    /**
     * 获取当前刻度值
     *
     * @return
     */
    public float getValue() {
        return mValue;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawScaleLine(canvas);
        // drawWheel(canvas);
        drawMiddleLine(canvas);
    }

    private void drawWheel(Canvas canvas) {
        Drawable wheel = getResources().getDrawable(R.drawable.bg_wheel);
        wheel.setBounds(0, 0, getWidth(), getHeight());
        wheel.draw(canvas);
    }

    /**
     * 从中间往两边开始画刻度线
     *
     * @param canvas
     */
    private void drawScaleLine(Canvas canvas) {
        canvas.save();

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE * mDensity);

        int width = mWidth, drawCount = 0;
        float xPosition = 0, textWidth = Layout.getDesiredWidth("0", textPaint);

        for (int i = 0; drawCount <= 4 * width; i++) {
            int numSize = String.valueOf(mValue + i).length();

            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
            if (xPosition + getPaddingRight() < mWidth) {
                if ((mValue + i) % mModType == 0) {
//                    canvas.drawLine(xPosition, getPaddingTop(), xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                    canvas.drawLine(xPosition, getPaddingTop(), xPosition, (int)(mDensity *  canvas.getHeight()*0.16), linePaint);
                    if (mValue + i <= mMaxValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue + i) / 2), countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:

                                canvas.drawText(String.valueOf(mValue + i), xPosition - (textWidth * numSize / 2), getHeight() -(int)(textWidth*0.7), textPaint);
                                break;

                            default:
                                break;
                        }
                    }
                } else {

                    canvas.drawLine(xPosition, getPaddingTop(), xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }

            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
            if (xPosition > getPaddingLeft()) {
                if ((mValue - i) % mModType == 0) {
//                    canvas.drawLine(xPosition, getPaddingTop(), xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                    canvas.drawLine(xPosition, getPaddingTop(), xPosition, (int)(mDensity *  canvas.getHeight()*0.16), linePaint);
                    if (mValue - i >= 0) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue - i) / 2), countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:
                                canvas.drawText(String.valueOf(mValue - i), xPosition - (textWidth * numSize / 2), getHeight() - (int)(textWidth*0.7), textPaint);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    canvas.drawLine(xPosition, getPaddingTop(), xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }

            drawCount += 2 * mLineDivider * mDensity;
        }

        canvas.restore();
    }

    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp = 0f;
        if (value < 20) {
            xp = xPosition - (textWidth * 1 / 2);
        } else {
            xp = xPosition - (textWidth * 2 / 2);
        }
        return xp;
    }

    /**
     * 画中间的红色指示线、阴影等。指示线两端简单的用了两个矩形代替
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {
        // TOOD 常量太多，暂时放这，最终会放在类的开始，放远了怕很快忘记
        int gap = 12, indexWidth = 3, indexTitleWidth = 24, indexTitleHight = 10, shadow = 6;
        String color = "#66999999";

        canvas.save();

        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(indexWidth);
        redPaint.setColor(Color.RED);

        canvas.drawLine(mWidth / 2, 0, mWidth / 2,(int)(mHeight*0.6), redPaint);

//        Paint ovalPaint = new Paint();
//        ovalPaint.setColor(Color.RED);
//        ovalPaint.setStrokeWidth(indexTitleWidth);
//        canvas.drawLine(mWidth / 2, 0, mWidth / 2, indexTitleHight, ovalPaint);
//        canvas.drawLine(mWidth / 2, mHeight - indexTitleHight, mWidth / 2, mHeight, ovalPaint);

        // RectF ovalRectF = new RectF(mWidth / 2 - 10, 0, mWidth / 2 + 10, 4 *
        // mDensity); //TODO 椭圆
        // canvas.drawOval(ovalRectF, ovalPaint);
        // ovalRectF.set(mWidth / 2 - 10, mHeight - 8 * mDensity, mWidth / 2 +
        // 10, mHeight); //TODO

//        Paint shadowPaint = new Paint();
//        shadowPaint.setStrokeWidth(shadow);
//        shadowPaint.setColor(Color.parseColor(color));
//        canvas.drawLine(mWidth / 2 + gap, 0, mWidth / 2 + gap, mHeight, shadowPaint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mScroller.forceFinished(true);

                    mLastX = xPosition;
                    mMove = 0;
                }


                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker(event);
                return false;
            // break;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker(MotionEvent event) {
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }


    private void changeMoveAndValue() {
        int tValue = (int) (mMove / (mLineDivider * mDensity));
        if (Math.abs(tValue) > 0) {
            mValue += tValue;
            mMove -= tValue * mLineDivider * mDensity;
            if (mValue <= 0 || mValue > mMaxValue) {
                mValue = mValue <= 0 ? 0 : mMaxValue;
                mMove = 0;
                mScroller.forceFinished(true);
            }
            notifyValueChange();
        }
        postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (mLineDivider * mDensity));
        mValue = mValue + roundMove;
        mValue = mValue <= 0 ? 0 : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;

        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            if (mModType == MOD_TYPE_ONE) {
                mListener.onValueChange(mValue);
            }
            if (mModType == MOD_TYPE_HALF) {
                mListener.onValueChange(mValue / 2f);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }
}
