package com.zzu.ehome.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.JibuAdapter;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.service.StepService;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.CircleBar;

import org.json.JSONObject;

/**
 * Created by Mersens on 2016/7/6.
 */
public class JibuFragment extends BaseFragment {
    private View mView;
    private LinearLayout headerView;
    private StickyListHeadersListView listview;
    private LayoutInflater mInflater;
    private CircleBar circleBar;
    private JibuAdapter adapter;
    private Thread thread;
    private boolean isThreadStart = false;
    private EHomeDao dao;
    private String userid;
    RequestMaker requestmaker;
    private TextView tv_jl,tv_time,tv_rl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_jibu, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        dao = new EHomeDaoImpl(getActivity());
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestmaker = RequestMaker.getInstance();
        initViews();
        initEvent();
        initDatas();
        mThread();

    }

    public void initViews() {
        adapter = new JibuAdapter(getActivity());
        mInflater = LayoutInflater.from(getActivity());
        headerView = (LinearLayout) mInflater.inflate(R.layout.layout_jibu_header, null);
        listview = (StickyListHeadersListView) mView.findViewById(R.id.lv_jibu);
        circleBar = (CircleBar) headerView.findViewById(R.id.progress_pedometer);
        circleBar.setMax(10000);
        circleBar.setProgress(StepDetector.CURRENT_SETP, 1);
        circleBar.startCustomAnimation();
        tv_jl=(TextView) headerView.findViewById(R.id.tv_jl);
        tv_time=(TextView) headerView.findViewById(R.id.tv_time);
        tv_rl=(TextView) headerView.findViewById(R.id.tv_rl);
    }

    public void initEvent() {

    }

    public void initDatas() {
        String time = DateUtils.getTodayTime();
        StepBean step = dao.loadSteps(userid, time.substring(0, 10));
        if (step == null) {
            step = new StepBean();
            step.setNum(0);
            step.setUserid(userid);
            step.setStartTime(time);
            step.setEndTime("");
            dao.saveStep(step);
        } else {
            StepDetector.CURRENT_SETP = step.getNum();
            step.setStartTime(time);
            dao.updateStep(step);
        }
        Intent intent = new Intent(getActivity(), StepService.class);
        getActivity().startService(intent);

        listview.addHeaderView(headerView);
        listview.setAdapter(adapter);

    }

    private void mThread() {
        isThreadStart = true;
        if (thread == null) {
            thread = new Thread(new Runnable() {
                public void run() {
                    while (isThreadStart) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (StepService.flag) {
                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }
                    }
                }
            });
            thread.start();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            circleBar.setProgress(StepDetector.CURRENT_SETP, 1);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        onSave();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadStart = false;
        if (thread != null) {
            thread = null;
        }
        onSave();
    }


    /**
     * 保存计步数据
     */
    public void onSave() {
        String time = DateUtils.getTodayTime();
        StepBean step = dao.loadSteps(userid, time.substring(0, 10));
        if (step == null) {
            step = new StepBean();
            step.setNum(0);
            step.setUserid(userid);
            step.setStartTime(time);
            step.setEndTime("");
            dao.saveStep(step);
        } else {
            step.setNum(StepDetector.CURRENT_SETP);
            step.setStartTime(time);
            dao.updateStep(step);
        }
    }

    @Override
    protected void lazyLoad() {

    }
}
