package com.zzu.ehome.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/11.
 */
public class FreeConsultationActivity extends BaseActivity {
    private RelativeLayout layout_name;
    private PopupWindow pop;
    private LinearLayout layout_all;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_free_consultation);
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "免费咨询", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        layout_name=(RelativeLayout) findViewById(R.id.layout_name);
        layout_all=(LinearLayout)findViewById(R.id.layout_all);
    }
    public void initEvent(){
        layout_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();

            }
        });

    }
    public void initDatas(){}

    public void showPop(){
        View mView= LayoutInflater.from(FreeConsultationActivity.this).inflate(R.layout.pop_add_tips,null);
        TextView tv_cancel=(TextView)mView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pop!=null && pop.isShowing()){
                    pop.dismiss();
                    pop=null;
                }
            }
        });
        ImageView icon_add=(ImageView) mView.findViewById(R.id.icon_add);
        icon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeConsultationActivity.this,AddUserInfoActivity.class));
            }
        });
        pop=new PopupWindow(mView);
        pop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        pop.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 动画效果 从底部弹起
        pop.setAnimationStyle(R.style.Animations_GrowFromBottom);
        pop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);

    }

}
