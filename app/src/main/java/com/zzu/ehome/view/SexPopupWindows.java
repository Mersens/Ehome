package com.zzu.ehome.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.PersonalCenterInfo;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.fragment.DoctorFragment;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/5/21.
 */
public class SexPopupWindows extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private Context context;
    PersonalCenterInfo personalCenterInfo;


    public SexPopupWindows(Context mContext, View parent, Activity activity) {

        super(mContext);
        personalCenterInfo=(PersonalCenterInfo)mContext;
        View view = View
                .inflate(mContext, R.layout.item_sex_pop, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));
        this.activity=activity;
        this.context=mContext;

        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();


        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);

        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_popupwindows_camera:
                personalCenterInfo.eventbus.post(new RefreshEvent(context.getResources().getInteger(R.integer.change_sex1)));

                dismiss();
                break;
            case R.id.item_popupwindows_Photo:
                personalCenterInfo.eventbus.post(new RefreshEvent(context.getResources().getInteger(R.integer.change_sex2)));

                dismiss();
                break;
            case R.id.item_popupwindows_cancel:

                dismiss();
                break;
        }
    }
}
