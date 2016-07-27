package com.zzu.ehome.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager {
	float mDownX,mDownY;
	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent motionEvent ) {
//
//		return true;
//	}

@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			mDownX=ev.getX();
			mDownY=ev.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(ev.getX()-mDownX)>Math.abs(ev.getY()-mDownY)){
				getParent().requestDisallowInterceptTouchEvent(true);
			}else{
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		if (this.getCurrentItem() == 0) {
			getParent().requestDisallowInterceptTouchEvent(false);
		} else {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		return super.dispatchTouchEvent(ev);
	}
//	@Override

  /*  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = 0;
		// 下面遍历所有child的高度
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec,
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			// 采用最大的view的高度
			if (h > height) {
				height = h;
			}
		}

		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }*/
	


}
