package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.BaseFilesActivity;
import com.zzu.ehome.activity.CreateillnessActivity;
import com.zzu.ehome.activity.DataChatActivity;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.utils.CommonUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/3/31.
 */
public class HealthFragment extends BaseFragment implements View.OnClickListener {
    private View viewline;
    private LinearLayout layout_data;
    private LinearLayout layout_files;
    private LinearLayout llchat;
    private TextView tv_data;
    private TextView tv_files;
    private RelativeLayout layout_add,layout_add_chat;
    private LinearLayout lv_new;
    private TextView tv_name;
    private int tag=-1;
    private View view;
    private TextView tvchatcontent;
    private ImageView ivnewbace;
    private Boolean isFile;
    private boolean isPrepared;
    private View vTop;
    private BroadcastReceiver mDateOrFileBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("action.File")){
                String content=intent.getStringExtra("msgContent");
                if(content!=null&&content.equals("File")){
                    setColors(Style.FILES);
                    addFragment(Style.FILES);
                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_file)));
                }
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_health, null);

        EventBus.getDefault().register(this);
        initViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int h= CommonUtils.getStatusHeight(getActivity());
            ViewGroup.LayoutParams params=vTop.getLayoutParams();
            params.height=h;
            params.width= ViewGroup.LayoutParams.FILL_PARENT;
            vTop.setLayoutParams(params);
        }else{
            vTop.setVisibility(View.GONE);
        }
        initEvent();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.File");
        getActivity().registerReceiver(mDateOrFileBroadcastReceiver, intentFilter);
        if(tag==1){
            setColors(Style.FILES);
        }isPrepared = true;
        lazyLoad();

        return view;
    }

    public void initViews() {
        layout_data = (LinearLayout) view.findViewById(R.id.layout_data);
        layout_files = (LinearLayout) view.findViewById(R.id.layout_files);
        tv_data = (TextView) view.findViewById(R.id.tv_data);
        tv_files = (TextView) view.findViewById(R.id.tv_files);
        layout_add = (RelativeLayout) view.findViewById(R.id.layout_add_new);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        layout_add_chat=(RelativeLayout) view.findViewById(R.id.layout_add_chat);
        viewline=(View)view.findViewById(R.id.viewline);
        llchat=(LinearLayout)view.findViewById(R.id.llchat);
        tvchatcontent=(TextView) view.findViewById(R.id.tv_name_chat);
        ivnewbace=(ImageView) view.findViewById(R.id.ivnewbace);


        lv_new=(LinearLayout)view.findViewById(R.id.lv_new);
        if(tag==-1)
        addFragment(Style.DATA);
        vTop=(View) view.findViewById(R.id.v_top);

    }


    public void initEvent() {
        layout_data.setOnClickListener(this);
        layout_files.setOnClickListener(this);
        layout_add.setOnClickListener(this);
        llchat.setOnClickListener(this);
//        layout_add_chat.setOnClickListener(this);
    }

    public void addFragment(Style style) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.container);
        ft.addToBackStack(null);
        ft.replace(R.id.container, getFragment(style)).commitAllowingStateLoss();
    }
    public void addBrodFragment(Style style,int tag) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.container);
        ft.addToBackStack(null);
        ft.replace(R.id.container,getFragment(style)).commitAllowingStateLoss();
        this.tag=tag;
    }

    public Fragment getFragment(Style style) {
        Fragment fragment = null;
        switch (style) {
            case DATA:
                fragment = HealthDataFragment.getInstance();
                break;
            case FILES:
                fragment = HealthFilesFragment.getInstance();

                break;

        }
        return fragment;
    }

    public static Fragment getInstance() {

        return new HealthFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_data:

                setColors(Style.DATA);
                addFragment(Style.DATA);
                lv_new.setVisibility(View.GONE);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                    }
                },500);


                break;
            case R.id.layout_files:
                setColors(Style.FILES);
                addFragment(Style.FILES);
                lv_new.setVisibility(View.VISIBLE);
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_file)));
                    }
                },500);



                break;
            case R.id.layout_add_new:
                add();
                break;
            case R.id.llchat:
                if(tvchatcontent.getText().toString().equals("健康走势")) {
                    chat();
                }else{
                    Intent intent=new Intent(getActivity(), BaseFilesActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }
    public void onEventMainThread(RefreshEvent event) {

        if(getResources().getInteger(R.integer.change_date) == event
                .getRefreshWhere()) {
            setColors(Style.DATA);
            lv_new.setVisibility(View.GONE);

        }
        if(getResources().getInteger(R.integer.change_file) == event
                .getRefreshWhere()) {
            setColors(Style.FILES);
            lv_new.setVisibility(View.VISIBLE);
        }
        if(getResources().getInteger(R.integer.refresh_manager_file) == event
                .getRefreshWhere()) {
            setColors(Style.FILES);
            lv_new.setVisibility(View.VISIBLE);
        }
        if(getResources().getInteger(R.integer.refresh_manager_data) == event
                .getRefreshWhere()) {
            setColors(Style.DATA);
            lv_new.setVisibility(View.GONE);

        }


    }
    public void reload(){
        setColors(Style.DATA);
        addFragment(Style.DATA);
    }
    private void chat() {
        startActivity(new Intent(getActivity(),DataChatActivity.class));
    }

    public void setColors(Style style) {
        resetColor();
        switch (style) {
            case DATA:
               layout_data.setBackgroundResource(R.mipmap.switch_left_normal);

                tv_data.setTextColor(getResources().getColor(R.color.actionbar_color));
              tv_name.setText("新建数据");
                ivnewbace.setBackgroundResource(R.mipmap.icon_new_chart);
                tvchatcontent.setText("健康走势");

                break;
            case FILES:
                layout_files.setBackgroundResource(R.mipmap.switch_right_normal);
                tv_files.setTextColor(getResources().getColor(R.color.actionbar_color));
                tv_name.setText("新建病例");
                ivnewbace.setBackgroundResource(R.mipmap.icon_basic);
                tvchatcontent.setText("基础档案");

                break;
        }
    }

    public void resetColor() {
        layout_data.setBackgroundResource(R.mipmap.switch_left_pressed);
        tv_data.setTextColor(getResources().getColor(R.color.base_color_text_white));
        layout_files.setBackgroundResource(R.mipmap.switch_right_pressed);
        tv_files.setTextColor(getResources().getColor(R.color.base_color_text_white));
    }

    public enum Style {
        DATA, FILES;
    }

    public void add(){
        String name=tv_name.getText().toString();
        if(name.equals("新建数据")){
//            intentAction(getActivity(), CreateDataActivity.class);
        }else{
            intentAction(getActivity(), CreateillnessActivity.class);
        }
    }

    public <T> void intentAction(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

            try {
                getActivity().unregisterReceiver(mDateOrFileBroadcastReceiver);

                mDateOrFileBroadcastReceiver = null;
            } catch (Exception e) {
            }
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible ) {
            return;
        }
    }


}
