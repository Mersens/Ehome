package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.AboutEhomeActivity;
import com.zzu.ehome.activity.AdviceActivity;
import com.zzu.ehome.activity.MyAppointmentActivity;
import com.zzu.ehome.activity.MyFocusActivity;
import com.zzu.ehome.activity.MyInfoActivity;
import com.zzu.ehome.activity.MyRemindActivity;
import com.zzu.ehome.activity.PersonalCenterInfo;
import com.zzu.ehome.activity.SettingActivity;
import com.zzu.ehome.application.MMloveConstants;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.ShareModel;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.SharePopupWindow;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/3/31.
 */
public class MyInfoFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private RelativeLayout layout_add_friends;
    private RelativeLayout layout_remind;
    private RelativeLayout layout_focus;
    private RelativeLayout layout_yuyue;
    private RelativeLayout layout_advice, layout_set;
    private RelativeLayout layout_invite_friends;
    private ImageView ivmask;
    private RelativeLayout layout_device;
    private RelativeLayout layout_about;
    private EHomeDao dao;
    private ImageLoader mImageLoader;
    private String userid;
    private TextView tv_name;
    User user;
    private ImageView ivsetting;
    private SharePopupWindow share;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_myinfo, null);
        dao = new EHomeDaoImpl(getActivity());
        mImageLoader = ImageLoader.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        EventBus.getDefault().register(this);
        initViews();
        initEvent();
        return view;
    }

    public void initViews() {
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        layout_add_friends = (RelativeLayout) view.findViewById(R.id.layout_add_friends);
        layout_remind = (RelativeLayout) view.findViewById(R.id.layout_remind);
        layout_focus = (RelativeLayout) view.findViewById(R.id.layout_focus);
        layout_yuyue = (RelativeLayout) view.findViewById(R.id.layout_yuyue);
        ivmask = (ImageView) view.findViewById(R.id.iv_head);
        layout_set = (RelativeLayout) view.findViewById(R.id.layout_set);
        layout_invite_friends = (RelativeLayout) view.findViewById(R.id.layout_invite_friends);
        layout_advice = (RelativeLayout) view.findViewById(R.id.layout_advice);
        layout_device = (RelativeLayout) view.findViewById(R.id.layout_device);
        ivsetting = (ImageView) view.findViewById(R.id.ivseting);
        layout_about = (RelativeLayout) view.findViewById(R.id.layout_about);
/*        setDefaultViewMethod(view, R.mipmap.icon_message, "", R.mipmap.icon_setting, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                intentAction(getActivity(), MyInfoActivity.class);
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                intentAction(getActivity(), SettingActivity.class);
            }
        });*/
    }

    public void initEvent() {
        layout_add_friends.setOnClickListener(this);
        layout_remind.setOnClickListener(this);
        layout_focus.setOnClickListener(this);
        layout_yuyue.setOnClickListener(this);
        ivmask.setOnClickListener(this);
        layout_advice.setOnClickListener(this);
        layout_invite_friends.setOnClickListener(this);
        layout_device.setOnClickListener(this);
        layout_set.setOnClickListener(this);
        setHead();
        tv_name.setText(user.getUsername());
        ivsetting.setOnClickListener(this);
        layout_about.setOnClickListener(this);
    }

    private void setHead() {
        user = dao.findUserInfoById(userid);
        mImageLoader.displayImage(
                user.getImgHead(), ivmask);
        tv_name.setText(user.getUsername());
    }


    public void onEventMainThread(RefreshEvent event) {

        if (getResources().getInteger(R.integer.refresh_info) == event
                .getRefreshWhere()) {
            userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
            setHead();

        }
    }


    public static Fragment getInstance() {
        return new MyInfoFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add_friends:
                ToastUtils.showMessage(getActivity(), "添加朋友");
                break;
            case R.id.layout_remind:
                intentAction(getActivity(), MyRemindActivity.class);
                break;
            case R.id.layout_focus:
                intentAction(getActivity(), MyFocusActivity.class);
                break;
            case R.id.layout_yuyue:
                intentAction(getActivity(), MyAppointmentActivity.class);
                break;
            case R.id.iv_head:
                intentAction(getActivity(), PersonalCenterInfo.class);
                break;
            case R.id.layout_advice:
                intentAction(getActivity(), AdviceActivity.class);
                break;
            case R.id.layout_invite_friends:
                share = new SharePopupWindow(getActivity());
                ShareModel model = new ShareModel();
                model.setImgPath(ImageUtil.saveResTolocal(getActivity().getResources(),R.drawable.share,"home_logo"));
                model.setText("跟我一起关注个人和家人健康吧，还可以预约网络视频问诊哦!");
                model.setTitle("个人健康数据管理专家");
                model.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.zzu.ehome.main.ehome");
                share.initShareParams(model);
                share.showShareWindow();
                share.showAtLocation(getActivity().findViewById(R.id.set),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            case R.id.layout_device:
//                ToastUtils.showMessage(getActivity(), "智能设备");
                break;
            case R.id.ivseting:
                // intentAction(getActivity(), SettingActivity.class);
                break;
            case R.id.layout_about:
                intentAction(getActivity(), AboutEhomeActivity.class);
                break;
            case R.id.layout_set:
                intentAction(getActivity(), SettingActivity.class);
                break;

        }

    }

    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyLoad() {

    }
}
