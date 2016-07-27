package com.zzu.ehome.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by dell on 2016/6/1.
 */
public class MyScrollListView extends ListView {

    public MyScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }


}
