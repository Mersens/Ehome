//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.support.percent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentLayoutHelper.PercentLayoutInfo;
import android.support.percent.PercentLayoutHelper.PercentLayoutParams;
import android.util.AttributeSet;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;

public class PercentFrameLayout extends FrameLayout {
    private final PercentLayoutHelper mHelper = new PercentLayoutHelper(this);

    public PercentFrameLayout(Context context) {
        super(context);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PercentFrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PercentFrameLayout.LayoutParams(this.getContext(), attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(this.mHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mHelper.restoreOriginalParams();
    }

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams implements PercentLayoutParams {
        private PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.FrameLayout.LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }

        @SuppressLint("NewApi")
		public LayoutParams(PercentFrameLayout.LayoutParams source) {
            this((android.widget.FrameLayout.LayoutParams)source);
            this.mPercentLayoutInfo = source.mPercentLayoutInfo;
        }

        public PercentLayoutInfo getPercentLayoutInfo() {
            return this.mPercentLayoutInfo;
        }

        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }
    }
}
