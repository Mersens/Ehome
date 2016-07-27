package com.zzu.ehome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.zzu.ehome.bean.TreatmentSearch;
import com.zzu.ehome.utils.MessageItem;

public class ListViewCompat extends ListView  {

    private static final String TAG = "ListViewCompat";
    private int mListViewFirstItem = 0;
    //listView中第一项的在屏幕中的位置
    private int mScreenY = 0;
    //是否向上滚动
    private boolean mIsScrollToUp = false;
    private SlideView mFocusedItemView;
    View footer;// 底部布局；
    int totalItemCount;// 总数量；
    int lastVisibleItem;// 最后一个可见的item；
    boolean isLoading;// 正在加载；

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    TreatmentSearch item = (TreatmentSearch) getItemAtPosition(position);
                    if(MessageItem.isContainsKey(item.getDoctorId()+item.getTime())){
                        mFocusedItemView=(SlideView)MessageItem.getMsg(item.getDoctorId()+item.getTime());
                    }
                }
            }
            default:
                break;
        }
        if (mFocusedItemView != null) {
            mFocusedItemView.onRequireTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


}