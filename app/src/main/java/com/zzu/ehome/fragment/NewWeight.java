package com.zzu.ehome.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.TempChatAdapter;
import com.zzu.ehome.adapter.WeightChatAdapter;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TempItemHistory;
import com.zzu.ehome.bean.TemperatureDate;
import com.zzu.ehome.bean.WeightDate;
import com.zzu.ehome.bean.WeightRes;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.TempView;
import com.zzu.ehome.view.WeightView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xclcharts.chart.PointD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/6/12.
 */
public class NewWeight extends BaseFragment implements StickyListHeadersListView.OnHeaderClickListener, StickyListHeadersListView.OnLoadingMoreLinstener {
    private View view;


    private RequestMaker requestMaker;
    String startTime, endTime;
    Date dNow;
    String userid;
    private WeightView mChart;

//    private TextView tvvalue, tvstatus, tvtime;

    private StickyListHeadersListView listview;
    private RelativeLayout moredata;
    private View progressBarView;
    private TextView progressBarTextView;
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private LayoutInflater inflater;
    private WeightChatAdapter mAadpter;
    private int page = 1;
    private boolean isLoading = false;
    private List<HealthDataRes> mList;
    private LinearLayout heardchat, lltmp;
    private RadioGroup group;
    private RadioButton rbday, rbweek, rbmonth;
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<String> weeks=new LinkedList<>();
    private List<String> months=new ArrayList<>();
    private TextView tvnodata;
    private BroadcastReceiver mRefrushBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("action.Weight")){

                setDay();

                page = 1;
                getHistory();
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_temp_chat, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        EventBus.getDefault().register(this);
        mList = new ArrayList<HealthDataRes>();
        initViews();
        initEvents();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.Weight");
        getActivity().registerReceiver(mRefrushBroadcastReceiver, intentFilter);
        rbday.setChecked(true);
        rbday.setTextColor(getResources().getColor(R.color.white));
        rbweek.setChecked(false);
        rbweek.setTextColor(getResources().getColor(R.color.actionbar_color));
        rbmonth.setChecked(false);
        rbmonth.setTextColor(getResources().getColor(R.color.actionbar_color));
        setDay();

        page = 1;
        getHistory();
        return view;
    }

    public void initViews() {
        inflater = LayoutInflater.from(getActivity());

        mAadpter = new WeightChatAdapter(getActivity());
        listview = (StickyListHeadersListView) view.findViewById(R.id.lv_temp);
        heardchat = (LinearLayout) inflater.inflate(R.layout.new_weight_layout, null);

        mChart = (WeightView) heardchat.findViewById(R.id.chart);
        lltmp = (LinearLayout) heardchat.findViewById(R.id.lltmp);
        group = (RadioGroup) view.findViewById(R.id.radioGroup);
        rbday = (RadioButton) view.findViewById(R.id.rb_day);
        rbweek = (RadioButton) view.findViewById(R.id.rb_week);
        rbmonth = (RadioButton) view.findViewById(R.id.rb_month);
        tvnodata=(TextView) heardchat.findViewById(R.id.tvnodate);
//        tvvalue = (TextView) heardchat.findViewById(R.id.tv_tempvalue);
//        tvstatus = (TextView) heardchat.findViewById(R.id.tv_status);
//        tvtime = (TextView) heardchat.findViewById(R.id.tvtime);
        moredata = (RelativeLayout) inflater.inflate(R.layout.moredata, null);
        progressBarView = (View) moredata.findViewById(R.id.loadmore_foot_progressbar);
        progressBarTextView = (TextView) moredata.findViewById(R.id.loadmore_foot_text);
        loadingAnimation = (AnimationDrawable) progressBarView.getBackground();
        listview.addHeaderView(heardchat);
        listview.addFooterView(moredata);

        listview.setOnHeaderClickListener(this);
        listview.setLoadingMoreListener(this);
        listview.setAdapter(mAadpter);
    }


    public void initEvents() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.rb_day:

                        rbday.setChecked(true);
                        rbday.setTextColor(getResources().getColor(R.color.white));
                        rbweek.setChecked(false);
                        rbweek.setTextColor(getResources().getColor(R.color.actionbar_color));
                        rbmonth.setChecked(false);
                        rbmonth.setTextColor(getResources().getColor(R.color.actionbar_color));
                        setDay();
                        break;
                    case R.id.rb_week:
                        rbday.setChecked(false);
                        rbday.setTextColor(getResources().getColor(R.color.actionbar_color));
                        rbweek.setChecked(true);
                        rbweek.setTextColor(getResources().getColor(R.color.white));
                        rbmonth.setChecked(false);
                        rbmonth.setTextColor(getResources().getColor(R.color.actionbar_color));
                        setWeek();

                        break;
                    case R.id.rb_month:
                        rbday.setChecked(false);
                        rbday.setTextColor(getResources().getColor(R.color.actionbar_color));
                        rbweek.setChecked(false);
                        rbweek.setTextColor(getResources().getColor(R.color.actionbar_color));
                        rbmonth.setChecked(true);
                        rbmonth.setTextColor(getResources().getColor(R.color.white));
                        setMonth();

                        break;


                    default:
                        break;
                }
            }


        });


    }

    private void setMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startTime =sdf.format(CommonUtils.changeDate(-29).getTime());
        endTime = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000 * 2);
        months.clear();
        labels.clear();
        months=CommonUtils.getDays(startTime,sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000 ));
        int i=0;
        while(i<30){
            if(i==0){
                labels.add(months.get(i).split("-")[2]);
                i+=4;
            }else{
                labels.add(months.get(i).split("-")[2]);
                i+=5;
            }

        }

        mChart.setX(labels,30);
        requestMaker.WeightInquiryType(userid, startTime, endTime, "D", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {

                try {
                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("WeightInquiry");
                    if (array.getJSONObject(0)
                            .has("MessageCode")) {


//                        tvstatus.setText("");
//                        tvtime.setText("");
//                        tvvalue.setText("");
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);
                    } else {
                        tvnodata.setVisibility(View.GONE);
                        WeightDate date = JsonTools.getData(result.toString(), WeightDate.class);
                        List<WeightRes> list = date.getData();
                        String [] times=list.get(list.size() - 1).getMonitorTime().split("\\ ")[0].split("\\-");
//                        tvtime.setText(times[1]+"-"+times[2]);
//
//                        tvvalue.setText(list.get(list.size()-1).getWeight()+"kg");

                        List<PointD> linePoint = new ArrayList<PointD>();
                        for (WeightRes th : list) {

                            Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],months);
                            Double yd = Double.valueOf(th.getWeight());
                            if(Double.compare(xd,-1d)!=0){
                                linePoint.add(new PointD(xd, yd));
                            }

                        }
                        mChart.refresh(linePoint);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }));

    }

    private void setDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startTime = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000);
        endTime = sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000 * 2);
        labels.clear();
        labels.add("00");
        labels.add("04");
        labels.add("08");
        labels.add("12");
        labels.add("16");
        labels.add("20");
        labels.add("24");
        mChart.setX(labels,24);
        requestMaker.WeightInquiryType(userid, startTime, endTime, "H", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {

                try {
                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("WeightInquiry");
                    if (array.getJSONObject(0)
                            .has("MessageCode")) {


//                        tvstatus.setText("");
//                        tvtime.setText("");
//                        tvvalue.setText("");
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);
                    } else {
                        tvnodata.setVisibility(View.GONE);
                        WeightDate date = JsonTools.getData(result.toString(), WeightDate.class);
                        List<WeightRes> list = date.getData();
//                        tvtime.setText("今天"+list.get(list.size() - 1).getMonitorTime().split("\\ ")[1] + ":00");
//
//                        tvvalue.setText(list.get(list.size()-1).getWeight()+"kg");



                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (WeightRes th : list) {
                            Double xd = Double.valueOf(th.getMonitorTime().split("\\ ")[1]);
                            Double yd = Double.valueOf(th.getWeight());
                            linePoint2.add(new PointD(xd, yd));
                        }


                        mChart.refresh(linePoint2);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }));
    }

    private void setWeek() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        startTime = sdf.format(CommonUtils.changeDate(-6).getTime());
        endTime = sdf.format((new Date()).getTime() + 60 * 60 * 24 * 1000);
        weeks.clear();
        weeks.add(startTime);
        weeks.add(sdf.format(CommonUtils.changeDate(-5).getTime()));
        weeks.add(sdf.format(CommonUtils.changeDate(-4).getTime()));
        weeks.add(sdf.format(CommonUtils.changeDate(-3).getTime()));
        weeks.add(sdf.format(CommonUtils.changeDate(-2).getTime()));
        weeks.add(sdf.format(CommonUtils.changeDate(-1).getTime()));
        weeks.add(sdf.format((new Date()).getTime() ));
        labels.clear();
        for(String day:weeks){
            labels.add(day.split("-")[2]);
        }
        mChart.setX(labels,6);

        requestMaker.WeightInquiryType(userid, startTime, endTime, "D", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {

                try {
                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("WeightInquiry");
                    if (array.getJSONObject(0)
                            .has("MessageCode")) {

//                        tvstatus.setText("");
//                        tvtime.setText("");
//                        tvvalue.setText("");
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);
                    } else {
                        tvnodata.setVisibility(View.GONE);
                        WeightDate date = JsonTools.getData(result.toString(), WeightDate.class);
                        List<WeightRes> list = date.getData();
                        String [] times=list.get(list.size() - 1).getMonitorTime().split("\\ ")[0].split("\\-");

//                        tvtime.setText(times[1]+"-"+times[2]);
//
//                        tvvalue.setText(list.get(list.size()-1).getWeight()+"kg");

                        List<PointD> linePoint2 = new ArrayList<PointD>();

                        for (WeightRes th : list) {

                            Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);
                            Double yd = Double.valueOf(th.getWeight());
                            if(Double.compare(xd,-1d)!=0){
                                linePoint2.add(new PointD(xd, yd));
                            }

                        }


                        mChart.refresh(linePoint2);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }));


    }




    private void getHistory() {
        requestMaker.HealthDataInquirywWithPageType(userid,10+"",page+"","Weight",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue=result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");
                    if (array.getJSONObject(0).has("MessageCode")){
                        if(page==1) {


                        }else
                            loadingFinished();
                    }else{

                        HealteData date = JsonTools.getData(result.toString(), HealteData.class);
                        List<HealthDataRes> list = date.getData();
                        if(list!=null&&list.size()>0) {
                            if(page==1&&mList.size()>0){
                                mList.clear();
                            }
                            for(int i=0;i<list.size();i++){
                                mList.add(list.get(i));
                            }

                            mAadpter.setList(mList);

                            loadingFinished();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loadingFinished();
                }

            }
        }));
    }


    public static Fragment getInstance() {
        return new NewWeight();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header,
                              int itemPosition, long headerId, boolean currentlySticky) {

    }
    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_weight) == event
                .getRefreshWhere()) {
            rbday.setChecked(true);
            rbday.setTextColor(getResources().getColor(R.color.white));
            rbweek.setChecked(false);
            rbweek.setTextColor(getResources().getColor(R.color.actionbar_color));
            rbmonth.setChecked(false);
            rbmonth.setTextColor(getResources().getColor(R.color.actionbar_color));
            setDay();

            page = 1;
            getHistory();

        }

    }

    @Override
    public void OnLoadingMore() {
        progressBarView.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);

        loadingAnimation.start();
        page++;
        if (!isLoading) {
            isLoading = true;
            getHistory();
        }


    }

    public void loadingFinished() {

        if (null != loadingAnimation && loadingAnimation.isRunning()) {
            loadingAnimation.stop();
        }
        progressBarView.setVisibility(View.INVISIBLE);
        progressBarTextView.setVisibility(View.INVISIBLE);
        isLoading = false;

        mAadpter.notifyDataSetChanged();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(mRefrushBroadcastReceiver);

            mRefrushBroadcastReceiver = null;
        } catch (Exception e) {
        }
        EventBus.getDefault().unregister(this);
    }
}
