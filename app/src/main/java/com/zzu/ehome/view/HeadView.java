package com.zzu.ehome.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;

/**
 * Created by zzu on 2016/3/31.
 */
public class  HeadView extends LinearLayout {
    private LinearLayout header_leftview_container;
    private LinearLayout header_layout_rightview_container;
    private ImageView left_button;
    private ImageView right_button;
    private LayoutInflater mInflater;
    private TextView tv_title,right_tv;
    private View header;
    private OnLeftClickListener onleftclicklistener;
    private OnRightClickListener onrightclicklistener;

    public HeadView(Context context) {
        super(context);
        init(context);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @author Mersens
     * @title HeaderStyle
     * @description:枚举类型的标题栏显示类型 DEFAULT--默认显示左侧图标，右侧图标和标题 ONLYTITLE--只显示标题
     * LEFT--只显示左侧图标 LEFTANDTITLE--只显示左侧图标和标题
     *
     */
    public enum HeaderStyle {
        DEFAULT,DEFAULT_TX, ONLYTITLE, LEFT, LEFTANDTITLE, RIGHTANDTITLE,RIGHTANDTITLE_TX;
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        header = mInflater.inflate(R.layout.common_head, null);
        addView(header);
        initViews();
    }

    public int getHeadViewHeight() {
        return header.getHeight();
    }

    public void setHeadViewBackground(int resid) {
        if (resid != 0) {
            header.setBackgroundResource(resid);
        }
    }

    public void setHeadViewTitleColor(int resid) {
        if (resid != 0) {
            tv_title.setTextColor(resid);
        }

    }

    /**
     * @author Mersens 初始化控件
     */
    private void initViews() {
        header_leftview_container = (LinearLayout) header
                .findViewById(R.id.header_leftview_container);
        header_layout_rightview_container = (LinearLayout) header
                .findViewById(R.id.header_layout_rightview_container);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
    }

    /**
     * @param style
     * @author Mersens 根据类型进行初始化,可以通过addView()方法动态添加控件,然后设置其控件的显示
     */
    public void init(HeaderStyle style) {
        switch (style) {
            case DEFAULT:
                removeViews();
                defaultMethod();
                break;
            case ONLYTITLE:
                removeViews();
                tileMethod();
                break;
            case LEFT:
                removeViews();
                leftMethod();
                break;
            case LEFTANDTITLE:
                removeViews();
                backAndTitleMethod();
                break;
            case RIGHTANDTITLE:
                removeViews();
                rightandtitle();
                break;
            case DEFAULT_TX:
                removeViews();
                default_tx();
                break;

        }
    }

    public void default_tx(){
        // 设置title为可见
        tv_title.setVisibility(View.VISIBLE);
        View left_button_view = mInflater.inflate(
                R.layout.common_head_left_img, null);
        left_button = (ImageView) left_button_view.findViewById(R.id.imageView);
        // 初始化左侧按钮，并添加到左侧父布局内
        header_leftview_container.addView(left_button_view);
        View right_button_view = mInflater.inflate(
                R.layout.common_head_right_img, null);
        right_button = (ImageView) right_button_view
                .findViewById(R.id.right_imageView);
        right_button.setVisibility(View.GONE);
        right_tv=(TextView)right_button_view.findViewById(R.id.right_textView);
        right_tv.setVisibility(View.VISIBLE);
        // 初始化右侧按钮，并添加到右侧父布局内
        MarginLayoutParams lp = new MarginLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        lp.rightMargin = 8;
        header_layout_rightview_container.addView(right_button_view, lp);
        left_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接口回调
                if (onleftclicklistener != null) {
                    onleftclicklistener.onClick();
                }
            }
        });

        right_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接口回调
                if (onrightclicklistener != null) {
                    onrightclicklistener.onClick();
                }
            }
        });
    }


    private void rightandtitle() {
        // 设置title为可见
        tv_title.setVisibility(View.VISIBLE);
        View right_button_view = mInflater.inflate(
                R.layout.common_head_right_img, null);
        right_button = (ImageView) right_button_view
                .findViewById(R.id.right_imageView);
        // 初始化右侧按钮，并添加到右侧父布局内
        header_layout_rightview_container.addView(right_button_view);
        right_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接口回调
                if (onrightclicklistener != null) {
                    onrightclicklistener.onClick();
                }
            }
        });
    }

    /**
     * 移除左右控件的子View
     */
    private void removeViews() {
        header_leftview_container.removeAllViews();
        header_layout_rightview_container.removeAllViews();
    }

    @SuppressLint("InflateParams")
    public void defaultMethod() {
        // 设置title为可见
        tv_title.setVisibility(View.VISIBLE);
        View left_button_view = mInflater.inflate(
                R.layout.common_head_left_img, null);
        left_button = (ImageView) left_button_view.findViewById(R.id.imageView);
        // 初始化左侧按钮，并添加到左侧父布局内
        header_leftview_container.addView(left_button_view);
        View right_button_view = mInflater.inflate(
                R.layout.common_head_right_img, null);
        right_button = (ImageView) right_button_view
                .findViewById(R.id.right_imageView);
        // 初始化右侧按钮，并添加到右侧父布局内
        MarginLayoutParams lp = new MarginLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        lp.rightMargin = 8;
        header_layout_rightview_container.addView(right_button_view, lp);
        left_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接口回调
                if (onleftclicklistener != null) {
                    onleftclicklistener.onClick();
                }
            }
        });

        right_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接口回调
                if (onrightclicklistener != null) {
                    onrightclicklistener.onClick();
                }
            }
        });
    }

    public void tileMethod() {
        tv_title.setVisibility(View.VISIBLE);
    }

    public void leftMethod() {
        // 初始化左侧按钮，并添加到左侧父布局内
        View left_button_view = mInflater.inflate(
                R.layout.common_head_left_img, null);
        left_button = (ImageView) left_button_view.findViewById(R.id.imageView);
        header_leftview_container.addView(left_button_view);
        // 添加监听事件
        left_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onleftclicklistener != null) {
                    onleftclicklistener.onClick();
                }
            }
        });
    }

    private void backAndTitleMethod() {
        tv_title.setVisibility(View.VISIBLE);
        View left_button_view = mInflater.inflate(
                R.layout.common_head_left_img, null);
        left_button = (ImageView) left_button_view.findViewById(R.id.imageView);
        header_leftview_container.addView(left_button_view);
        left_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onleftclicklistener != null) {
                    onleftclicklistener.onClick();
                }
            }
        });
    }
    public void setDefaultTXViewMethod(int leftsrcid, String title,
                                       String rightsrcid, OnLeftClickListener onleftclicklistener,
                                     OnRightClickListener onrightclicklistener) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (leftsrcid != 0)
            left_button.setBackgroundResource(leftsrcid);

        if (!TextUtils.isEmpty(rightsrcid))
            right_tv.setText(rightsrcid);

        setOnLeftClickListener(onleftclicklistener);
        setOnRightClickListener(onrightclicklistener);
    }

    public void setDefaultViewMethod(int leftsrcid, String title,
                                     int rightsrcid, OnLeftClickListener onleftclicklistener,
                                     OnRightClickListener onrightclicklistener) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (leftsrcid != 0)
            left_button.setBackgroundResource(leftsrcid);

        if (rightsrcid != 0)
            right_button.setBackgroundResource(rightsrcid);

        setOnLeftClickListener(onleftclicklistener);
        setOnRightClickListener(onrightclicklistener);
    }

    public void setRightAndTitleMethod(String title, int rightsrcid,
                                       OnRightClickListener onrightclicklistener) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (rightsrcid != 0)
            right_button.setBackgroundResource(rightsrcid);

        setOnRightClickListener(onrightclicklistener);

    }

    public void setLeftWithTitleViewMethod(int leftsrcid, String title,
                                           OnLeftClickListener onleftclicklistener) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (leftsrcid != 0)
            left_button.setBackgroundResource(leftsrcid);

        setOnLeftClickListener(onleftclicklistener);
    }

    public void setOnlyTileViewMethod(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
    }

    public void setLeftViewMethod(int leftsrcid,
                                  OnLeftClickListener onleftclicklistener) {
        if (leftsrcid != 0)
            left_button.setBackgroundResource(leftsrcid);

        setOnLeftClickListener(onleftclicklistener);

    }

    public void setOnLeftClickListener(OnLeftClickListener onleftclicklistener) {
        this.onleftclicklistener = onleftclicklistener;
    }

    public void setOnRightClickListener(
            OnRightClickListener onrightclicklistener) {
        this.onrightclicklistener = onrightclicklistener;
    }

    /**
     * @author Mersens 自定义接口回调，用于处理左侧按钮的点击事件
     */
    public interface OnLeftClickListener {
        void onClick();
    }

    /**
     * @author Mersens 自定义接口回调，用于处理右侧侧按钮的点击事件
     */
    public interface OnRightClickListener {
        void onClick();
    }

    public String getRightText(){
        if(right_tv!=null && right_tv.getVisibility()==View.VISIBLE){
            return  right_tv.getText().toString();
        }
        else{
            return null;
        }
    }

    public void setRightText(String title){
        if(right_tv!=null && right_tv.getVisibility()==View.VISIBLE){
            if(!TextUtils.isEmpty(title)){
                right_tv.setText(title);
            }

        }

    }
}
