/*
 *  Android ScaleMarkView.
 *  
 *  Copyright 2015 YangJun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.zzu.ehome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.ScaleMarkScroller.ScrollingListener;

import java.math.BigDecimal;


public class ScaleMarkView extends SurfaceView implements SurfaceHolder.Callback, ScrollingListener {
    /** 每一个小刻度对应的int值 */
    public int mPerValue = 1;
    private Paint mBorderFillPaint = new Paint();
    private Paint mBorderPaint = new Paint();
    private RectF mBorderRectF = new RectF();

    private Paint mCurrentMarkPaint = new Paint();
    /** 当前的大刻度值 */
    private int mCurrentBigMarkValue = 0;
    /** 当前的小刻度值 */
    private int mCurrentSmallMarkValue = 0;

    /** 默认的值 */
    private double mDefaultValue = 60.0;
    private SurfaceHolder mHolder;

    private int mMaxMarkValue = 3000;
    private int mMinMarkValue = 0;
    private float mPerMM2PX;
    /** 每一个大刻度包括小刻度数 */
    private int mPerScaleMark = 10;
    /** 一个大刻度对应的真实值  */
    private float mScale = 1f;
    private float mScaleMarkHeight;

    private Paint mScaleMarkPaint = new Paint();
    private ScaleMarkScroller mScroller;
    private float mScrollingOffset;
    private int mLastShownBigMarkValue;
    private int mLastShownSmallMarkValue;
    private float mLastShownOffset;
    //    private Paint mTest = new Paint();
    private Path mClearPath = new Path();
    private float mTextHeight;
    private Rect mTextRect = new Rect();
    private Paint mClearPaint;
    private int mPadding;
    private OnValueChangedListener mOnValueChangedListener;
    private boolean mHasInitValue;

    public ScaleMarkView(Context context) {
        super(context);
        init();
    }

    public ScaleMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleMarkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 获取当前刻度对应的尺寸值
     *
     * @return 尺寸值
     */
    public BigDecimal getCurrentValue() {
        computeMarkValue();
        return getValueFormat(mCurrentBigMarkValue, mCurrentSmallMarkValue, 0);
    }

    private float getMarkValue(int bigMark, int smallMark, float offset) {
        return bigMark + (smallMark + offset) / mPerScaleMark;
    }

    private float getMarkValue() {
        return getMarkValue(mCurrentBigMarkValue, mCurrentSmallMarkValue, mScrollingOffset);
    }

    @Override
    public void onFinished() {
    }

    @Override
    public void onJustify() {
        computeMarkValue();
        mScrollingOffset = 0.0f;
        refreshCanvas(mCurrentBigMarkValue, mCurrentSmallMarkValue, mScrollingOffset);
    }

    @Override
    public void onScroll(int distance) {
        float markValueDistance = -distance / mPerMM2PX;
        if (getMarkValue() > mMaxMarkValue) {
            mCurrentBigMarkValue = mMaxMarkValue;
            mCurrentSmallMarkValue = 0;
            mScrollingOffset = 0;
            mScroller.stopScrolling();
        } else if (getMarkValue() < mMinMarkValue) {
            mCurrentBigMarkValue = mMinMarkValue;
            mCurrentSmallMarkValue = 0;
            mScrollingOffset = 0;
            mScroller.stopScrolling();
        } else {
            offsetBy(markValueDistance);
        }
    }

    @Override
    public void onStarted() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }

        return mScroller.onTouchEvent(event);
    }

    /**
     * 刷新视图
     */
    public void refreshCanvas() {
        refreshCanvas(mCurrentBigMarkValue, mCurrentSmallMarkValue, 0);
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        this.mOnValueChangedListener = listener;
    }

    /**
     * 设置默认值
     *
     * @param defaultValue
     */
    public void setDefaultValue(float defaultValue) {
        mDefaultValue = defaultValue;
        refreshCanvas();
    }

    /**
     * 设置最大值
     *
     * @param maxMarkValue
     */
    public void setMaxValue(int maxValue) {
        mMaxMarkValue = Math.round(maxValue / mScale);
        refreshCanvas();
    }

    /**
     * 设置最小值
     *
     * @param maxValue
     */
    public void setMinValue(int minValue) {
        mMinMarkValue = Math.round(minValue / mScale);
        refreshCanvas();
    }

    /**
     * 设置每个大刻度包含的小刻度数量
     *
     * @param perScaleMark
     */
    public void setPerScaleMark(int perScaleMark) {
        mPerScaleMark = perScaleMark;
        refreshCanvas();
    }

    /**
     * 设置当前的尺寸值
     *
     * @param value
     */
    public void setValue(double value) {
        int bigMarkValue = (int) Math.round(value / mScale);
        int smallMarkValue = (int) Math.round((value / mScale - bigMarkValue) * mPerScaleMark);
        setMarkValue(bigMarkValue, smallMarkValue);
        mHasInitValue = true;
    }

    /**
     * 设置一个大刻度对应的值
     *
     * @param value
     */
    public void setPreMarkValue(float value) {
        mScale = value;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mBorderRectF.set(mBorderPaint.getStrokeWidth() + mPadding, mBorderPaint.getStrokeWidth() + mPadding,
                width - mBorderPaint.getStrokeWidth() - mPadding, height - mBorderPaint.getStrokeWidth() - mPadding);
        mScaleMarkHeight = mBorderRectF.height() * 0.6f - mTextHeight;
        refreshCanvas();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasInitValue) {
            setValue(mDefaultValue);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mBorderRectF.setEmpty();
        mHasInitValue = false;
    }

    public void stopScrolling() {
        mScroller.stopScrolling();
        onJustify();
    }

    private void computeMarkValue() {
        while (Math.round(Math.abs(mScrollingOffset)) >= 1) {
            if (mScrollingOffset > 0) {
                mCurrentSmallMarkValue++;
                mScrollingOffset -= 1;
            } else {
                mCurrentSmallMarkValue--;
                mScrollingOffset += 1;
            }
        }

        mCurrentBigMarkValue += mCurrentSmallMarkValue / mPerScaleMark;
        mCurrentSmallMarkValue = mCurrentSmallMarkValue % mPerScaleMark;
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawRoundRect(mBorderRectF, 8, 8, mBorderFillPaint);
        canvas.drawRoundRect(mBorderRectF, 8, 8, mBorderPaint);
    }

    private void drawMarkName(Canvas canvas, boolean isBigMark, float offsetX, int markValue, int offsetBigMark) {
        if (!isBigMark) {
            return;
        }

        float centerX = mBorderRectF.centerX();
        float centerY = mBorderRectF.centerY();
        if (offsetX != 0) {
            String text = String.valueOf(markValue + offsetBigMark);
            mScaleMarkPaint.getTextBounds(text, 0, text.length(), mTextRect);
            canvas.drawText(text, centerX + offsetX - mTextRect.centerX(), mBorderRectF.height()*0.8f+4, mScaleMarkPaint);
        }

        String text = String.valueOf(markValue - offsetBigMark);
        mScaleMarkPaint.getTextBounds(text, 0, text.length(), mTextRect);
        canvas.drawText(text, centerX - offsetX - mTextRect.centerX(), mBorderRectF.height()*0.8f+4, mScaleMarkPaint);
    }

    private void drawOneMark(Canvas canvas, boolean isBigMark, float offsetX) {
        float centerX = mBorderRectF.centerX();

        float scaleMarkHeight;
        if (isBigMark) {
            scaleMarkHeight = mScaleMarkHeight;
        } else {
            scaleMarkHeight = mScaleMarkHeight * 0.4f;
        }

        if (offsetX != 0) {

            canvas.drawLine(centerX + offsetX, mBorderRectF.top, centerX + offsetX, mBorderRectF.top + scaleMarkHeight,
                    mScaleMarkPaint);
        }

        canvas.drawLine(centerX - offsetX, mBorderRectF.top, centerX - offsetX, mBorderRectF.top + scaleMarkHeight,
                mScaleMarkPaint);
    }

    /**
     * 画出刻度以及刻度值
     *
     * @param canvas
     * @param bigMarkValue
     *            大刻度值
     * @param smallMarkValue
     *            小刻度值
     * @param offMark
     *            刻度偏移度
     */
    private void drawScaleMark(Canvas canvas, int bigMarkValue, int smallMarkValue, float offMark) {
        int saveCount = canvas.save();

        // 中心大刻度对应的值
        int centerMarkValue = (int) (bigMarkValue * mScale);
        float scalePoint = smallMarkValue + offMark;
        canvas.translate(-mPerMM2PX * scalePoint, 0);
        // 与中心大刻度的px距离
        float offsetX = 0;
        // 与中心大刻度相隔的刻度数
        int offsetMark = 0;
        boolean isBigMark = true;
        // 画一个屏幕的刻度
        while (offsetX < mBorderRectF.centerX() + Math.abs(mPerMM2PX * scalePoint)) {
            isBigMark = offsetMark % mPerScaleMark == 0;
            drawOneMark(canvas, isBigMark, offsetX);
            drawMarkName(canvas, isBigMark, offsetX, centerMarkValue, offsetMark / mPerScaleMark);

            offsetX += mPerMM2PX;
            offsetMark++;
        }

        canvas.restoreToCount(saveCount);
    }

    private void init() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        mPerMM2PX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 2, dm);
        mTextHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm);
        mScroller = new ScaleMarkScroller(getContext(), this);
        mHolder = this.getHolder();
        mHolder.addCallback(this);

        initPaints();
    }

    private void initPaints() {
        mBorderPaint.setColor(0xffffffff);
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setStrokeWidth(2);

        mBorderFillPaint.setColor(0xffffffff);
        mBorderFillPaint.setStyle(Style.FILL);
        mBorderFillPaint.setStrokeWidth(2);

        mScaleMarkPaint.setColor(0xff0f2536);
        mScaleMarkPaint.setStyle(Style.FILL);
        mScaleMarkPaint.setStrokeWidth(3);
        mScaleMarkPaint.setTextSize(mTextHeight);

        mClearPaint = new Paint();
        mClearPaint.setColor(Color.WHITE);
//        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

//        mTest.setColor(Color.RED);
//        mTest.setStyle(Style.FILL);
//        mTest.setStrokeWidth(3);
//        mTest.setTextSize(mTextHeight);

        mCurrentMarkPaint.setColor(Color.RED);
        mCurrentMarkPaint.setStyle(Style.FILL);
        mCurrentMarkPaint.setStrokeWidth(3);

        mPadding = 0;
    }

    /**
     * 偏移刻度尺
     *
     * @param x
     *            偏移的px距离
     */
    private void offsetBy(float offsetMarkValue) {
        mScrollingOffset += offsetMarkValue;
        computeMarkValue();

        refreshCanvas(mCurrentBigMarkValue, mCurrentSmallMarkValue, mScrollingOffset);
    }

    /**
     * 根据给予的刻度值和刻度偏移值来刷新视图
     *
     * @param markValue
     *            需要显示的刻度值
     * @param offMark
     *            需要偏度的刻度值
     * @param mScrollingOffset2
     */
    private void refreshCanvas(final int bigMarkValue, final int smallMarkValue, final float offMark) {
        if (mBorderRectF.isEmpty()) {
            return;
        }

        Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        drawBorder(canvas);
        drawScaleMark(canvas, bigMarkValue, smallMarkValue, offMark);
        clearOutMarks(canvas);
        drawMarkPoint(canvas);

//        canvas.drawText(String.valueOf((float) Math.round(getCurrentValue() * 100) / 100), mBorderRectF.centerX(),
//                mBorderRectF.centerY(), mTest);
        mHolder.unlockCanvasAndPost(canvas);
        if (mOnValueChangedListener != null && (mLastShownBigMarkValue != bigMarkValue || mLastShownSmallMarkValue != smallMarkValue || !getBigDecimalFormat(offMark).equals(getBigDecimalFormat(mLastShownOffset)))) {
            mOnValueChangedListener.onValueChanged(this, getValueFormat(mLastShownBigMarkValue, mLastShownSmallMarkValue, mLastShownOffset), getValueFormat(bigMarkValue, smallMarkValue, offMark));
            mLastShownBigMarkValue = bigMarkValue;
            mLastShownSmallMarkValue = smallMarkValue;
            mLastShownOffset = offMark;
        }
    }

    private BigDecimal getBigDecimalFormat(float value) {
        BigDecimal b1 = new BigDecimal(Float.toString(value));
        return b1.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal getValueFormat(int bigMark, int smallMark, float offset) {
        float markValue = getMarkValue(bigMark, smallMark, offset);
        BigDecimal b2 = new BigDecimal(Float.toString(mScale));
        return getBigDecimalFormat(markValue).multiply(b2);
    }

    private void clearOutMarks(Canvas canvas) {

        mClearPath.reset();
        mClearPath.addRect(0, 0, canvas.getWidth(), canvas.getHeight(), Path.Direction.CW);
        mClearPath.addRoundRect(mBorderRectF, 8, 8, Path.Direction.CCW);
        canvas.drawPath(mClearPath, mClearPaint);
    }

    private void drawMarkPoint(Canvas canvas) {


        int centerX = (int) mBorderRectF.centerX();
        int centerY = (int) mBorderRectF.centerX();

        Drawable markPoint = getContext().getResources().getDrawable(R.drawable.scale_mark_point);

        markPoint.setBounds(centerX - markPoint.getIntrinsicWidth() / 2, 0, centerX + markPoint.getIntrinsicWidth() / 2, canvas.getHeight()/2);
        markPoint.draw(canvas);

    }

    /**
     * 设置当前刻度值
     *
     * @param bigMarkValue
     * @param smallMarkValue
     */
    private void setMarkValue(int bigMarkValue, int smallMarkValue) {
        if (bigMarkValue != mCurrentBigMarkValue || smallMarkValue != mCurrentSmallMarkValue || mScrollingOffset != 0) {
            mCurrentBigMarkValue = bigMarkValue;
            mCurrentSmallMarkValue = smallMarkValue;
            mScrollingOffset = 0;
            refreshCanvas(mCurrentBigMarkValue, mCurrentSmallMarkValue, 0);
        }
    }

    public static interface OnValueChangedListener {
        void onValueChanged(ScaleMarkView view, BigDecimal oldValue, BigDecimal newValue);
    }

}
