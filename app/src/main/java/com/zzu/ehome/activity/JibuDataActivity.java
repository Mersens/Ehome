package com.zzu.ehome.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.JibuAdapter;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.MedicationDate;
import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.bean.StepCounterDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.JibuFragment;
import com.zzu.ehome.fragment.NewSuggarFrament;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.service.StepService;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.NetUtils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.CircleBar;
import com.zzu.ehome.view.HeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;


/**
 * Created by Mersens on 2016/6/27.
 */
public class JibuDataActivity extends BaseActivity implements StickyListHeadersListView.OnHeaderClickListener, StickyListHeadersListView.OnLoadingMoreLinstener {
    private LinearLayout headerView;
    private StickyListHeadersListView listview;
    private LayoutInflater mInflater;
    private CircleBar circleBar;
    private JibuAdapter adapter;
    private Thread thread;
    private boolean isThreadStart = false;
    private EHomeDao dao;
    private String userid;
    private RequestMaker requestmaker;
    private float weight;
    private TextView tv_jl, tv_time, tv_rl;
    private double calories ;
    private int step_length = 55;
    private int minute_distance = 80;
    private static int REFRESHCOUNT = 0x11;
    private static int REFRESHDATA = 0x01;
    private Timer timer = new Timer();
    private boolean isLoading = false;
    int page = 1;
    private RelativeLayout moredata;
    private View progressBarView;
    private TextView progressBarTextView;
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private LayoutInflater inflater;
    private List<StepCounterBean> mList;
    private String timeCount;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_jibu);
        dao = new EHomeDaoImpl(this);
        userid = SharePreferenceUtil.getInstance(this).getUserId();
        requestmaker = RequestMaker.getInstance();
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(this).getWeight())) {
            weight = 50.0f;
        } else {
            weight = Float.parseFloat(SharePreferenceUtil.getInstance(this).getWeight());
        }
        mList = new ArrayList<StepCounterBean>();
        initViews();
        initEvent();
        initDatas();
        mThread();
    }

    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "计步", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        adapter = new JibuAdapter(this);
        mInflater = LayoutInflater.from(this);
        headerView = (LinearLayout) mInflater.inflate(R.layout.layout_jibu_header, null);
        listview = (StickyListHeadersListView) findViewById(R.id.lv_jibu);
        circleBar = (CircleBar) headerView.findViewById(R.id.progress_pedometer);
        circleBar.setMax(10000);
        circleBar.setProgress(StepDetector.CURRENT_SETP, 1);
        circleBar.startCustomAnimation();
        moredata = (RelativeLayout) mInflater.inflate(R.layout.moredata, null);
        progressBarView = (View) moredata.findViewById(R.id.loadmore_foot_progressbar);
        progressBarTextView = (TextView) moredata.findViewById(R.id.loadmore_foot_text);
        loadingAnimation = (AnimationDrawable) progressBarView.getBackground();
        tv_jl = (TextView) headerView.findViewById(R.id.tv_jl);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        tv_rl = (TextView) headerView.findViewById(R.id.tv_rl);
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
            step.setUploadState(0);
            dao.saveStep(step);
        } else {
            //StepDetector.CURRENT_SETP = step.getNum();
            step.setStartTime(time);
            dao.updateStep(step);
        }
/*
        Intent intent = new Intent(this, StepService.class);
        startService(intent);*/
        listview.addHeaderView(headerView);
        listview.setAdapter(adapter);
        listview.addFooterView(moredata);
        Date as = new Date(new Date().getTime());
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        setData();
        getDate();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.arg1 = REFRESHDATA;
                handler.sendMessage(msg);
            }
        }, 1000, 1000 * 30);
    }

    private void getDate() {
        /**
         * 查询历史数据
         */
        requestmaker.StepCounterInquiry(userid, "", 10 + "", page + "", new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if (page == 1) {

                        } else
                            loadingFinished();

                    } else {

                        StepCounterDate date = JsonTools.getData(result.toString(), StepCounterDate.class);
                        List<StepCounterBean> list = date.getData();
                        if (list != null && list.size() > 0) {
                            if (page == 1 && mList.size() > 0) {
                                mList.clear();
                            }
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i));
                            }
                            adapter.setList(mList);
                            loadingFinished();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }));
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
                            msg.arg1 = REFRESHCOUNT;
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
            if (msg.arg1 == REFRESHCOUNT) {
                circleBar.setProgress(StepDetector.CURRENT_SETP, 1);
            } else if (msg.arg1 == REFRESHDATA) {
                setData();
            }
        }
    };

    public void setData() {

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01)/1000;
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        double d = step_length * StepDetector.CURRENT_SETP;
        String count = String.format("%.2f", d / 100000);
        tv_jl.setText(count + "公里");
        tv_time.setText(h1 + "." + h2 + "小时");
        tv_rl.setText(String.format("%.2f",calories) + "千卡");
    }


    @Override
    protected void onStop() {
        super.onStop();
//        upload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadStart = false;
        if (thread != null) {
            thread = null;
        }

        timer.cancel();
        timer = null;
        upload();
    }

    public void upload() {
        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01)/1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        if (NetUtils.isNetworkConnected(this)) {
            requestmaker.StepCounterInsert(userid, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f",calories) , getTime(), new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
                @Override
                public void processJsonObject(Object result) {
                    try {
                        JSONObject mySO = (JSONObject) result;
                        org.json.JSONArray array = mySO
                                .getJSONArray("StepCounterInsert");
                        Log.e("TAG", mySO.toString());
                        EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取昨天日期
     *
     * @return
     */

    public String getYesterday() {
        Date as = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        return matter1.format(as);
    }

    @Override
    public void OnLoadingMore() {
        progressBarView.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);

        loadingAnimation.start();
        page++;
        if (!isLoading) {
            isLoading = true;
            getDate();

        }


    }

    public void loadingFinished() {

        if (null != loadingAnimation && loadingAnimation.isRunning()) {
            loadingAnimation.stop();
        }
        progressBarView.setVisibility(View.INVISIBLE);
        progressBarTextView.setVisibility(View.INVISIBLE);
        isLoading = false;

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header,
                              int itemPosition, long headerId, boolean currentlySticky) {

    }
}
