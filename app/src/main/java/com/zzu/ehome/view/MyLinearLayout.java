package com.zzu.ehome.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.zzu.ehome.utils.ScreenUtils;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs,
                                 int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initView() {

    }
    private boolean mScrolling;
    private float touchDownX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop()) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    float x1 = 0;
    float x2 = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (touchDownX - x2 > ScreenUtils.dip2px(getContext(), 40)) {
                    if(mSetOnSlideListener!=null){
                        mSetOnSlideListener.onRightToLeftSlide();
                    }
                }
                if (touchDownX - x2 < -ScreenUtils.dip2px(getContext(), 40)) {
                    if(mSetOnSlideListener!=null){
                        mSetOnSlideListener.onLeftToRightSlide();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private setOnSlideListener mSetOnSlideListener;

    public setOnSlideListener getmSetOnSlideListener() {
        return mSetOnSlideListener;
    }

    public void setmSetOnSlideListener(setOnSlideListener mSetOnSlideListener) {
        this.mSetOnSlideListener = mSetOnSlideListener;
    }

    public interface setOnSlideListener{
        void onRightToLeftSlide();
        void onLeftToRightSlide();
    }

}
