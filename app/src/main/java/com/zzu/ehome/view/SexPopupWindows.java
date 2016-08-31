package com.zzu.ehome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zzu.ehome.R;

/**
 * Created by Administrator on 2016/5/21.
 */
public class SexPopupWindows extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private Context context;

    public OnGetData getmOnGetData() {
        return mOnGetData;
    }

    public void setmOnGetData(OnGetData mOnGetData) {
        this.mOnGetData = mOnGetData;
    }

    private OnGetData mOnGetData;

    public interface OnGetData {
         void onDataCallBack(String sex);
    }



    public SexPopupWindows(Context mContext, View parent, Activity activity) {

        super(mContext);

        View view = View
                .inflate(mContext, R.layout.popview, null);
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
//                Intent intent=new Intent();
//                intent.putExtra("sex","01");
//                activity.setResult(Constants.REQUEST_CODE_SEX,intent);
                mOnGetData.onDataCallBack("01");
                dismiss();
                break;
            case R.id.item_popupwindows_Photo:
//                Intent i2=new Intent();
//                i2.putExtra("sex","02");
//                activity.setResult(Constants.REQUEST_CODE_SEX,i2);
                mOnGetData.onDataCallBack("02");
                dismiss();
                break;
            case R.id.item_popupwindows_cancel:

                dismiss();
                break;
        }
    }
}
