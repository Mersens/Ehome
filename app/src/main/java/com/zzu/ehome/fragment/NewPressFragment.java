package com.zzu.ehome.fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.zzu.ehome.adapter.BloodPressChatAdapter;
import com.zzu.ehome.adapter.WeightChatAdapter;
import com.zzu.ehome.bean.BloodPreessDate;
import com.zzu.ehome.bean.BloodPressRes;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TempItemHistory;
import com.zzu.ehome.bean.WeightDate;
import com.zzu.ehome.bean.WeightRes;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.PressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xclcharts.chart.PointD;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/6/15.
 */
public class NewPressFragment  extends BaseFragment implements StickyListHeadersListView.OnHeaderClickListener,StickyListHeadersListView.OnLoadingMoreLinstener{
    private View view;
    private LayoutInflater inflater;
    private PressView mChart;
    private LinearLayout heardchat,lltmp;
    private RadioGroup group;
    private RadioButton rbday, rbweek,rbmonth;
//    private TextView tvvalue,tvstatus,tvtime;
    private String startTime,endTime;
    private RequestMaker requestMaker;
    private String userid;
    private int page=1;
    private StickyListHeadersListView listview;
    private BloodPressChatAdapter mAadpter;
    private RelativeLayout moredata;
    private View progressBarView;
    private TextView progressBarTextView;
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private boolean isLoading = false;
    private List<HealthDataRes> mList;
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<String> weeks=new LinkedList<>();
    private List<String> months=new ArrayList<>();
    private TextView tvnodata;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_temp_chat, null);
        requestMaker=RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();
        mList = new ArrayList<HealthDataRes>();
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
        setDay();
        setDay();
        rbday.setChecked(true);
        rbday.setTextColor(getResources().getColor(R.color.white));
        rbweek.setChecked(false);
        rbweek.setTextColor(getResources().getColor(R.color.actionbar_color));
        rbmonth.setChecked(false);
        rbmonth.setTextColor(getResources().getColor(R.color.actionbar_color));
        page=1;
        getHistory();
        return view;
    }

    public void initViews() {
        inflater = LayoutInflater.from(getActivity());
        mAadpter=new BloodPressChatAdapter(getActivity());
        listview=(StickyListHeadersListView)view.findViewById(R.id.lv_temp);
        heardchat=(LinearLayout) inflater.inflate(R.layout.new_press_layout,null);
        mChart=(PressView)heardchat.findViewById(R.id.chart);
        group = (RadioGroup) view.findViewById(R.id.radioGroup);
        rbday = (RadioButton) view.findViewById(R.id.rb_day);
        rbweek = (RadioButton) view.findViewById(R.id.rb_week);
        rbmonth=(RadioButton) view.findViewById(R.id.rb_month);

//        tvvalue=(TextView)heardchat.findViewById(R.id.tv_tempvalue);
//        tvstatus=(TextView)heardchat.findViewById(R.id.tv_status);
//        tvtime=(TextView)heardchat.findViewById(R.id.tvtime);
        tvnodata=(TextView) heardchat.findViewById(R.id.tvnodate);
        moredata = (RelativeLayout)inflater.inflate(R.layout.moredata, null);
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
    private void getHistory() {
        requestMaker.HealthDataInquirywWithPageType(userid,10+"",page+"","BloodPressure",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete(){
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
                        if(page==1&&mList.size()>0){
                            mList.clear();
                        }
                        if(list!=null&&list.size()>0) {
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

        requestMaker.BloodPressureInquiryType(userid,startTime,endTime,"H",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                try {
                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BloodPressureInquiry");
                    if(array.getJSONObject(0)
                            .has("MessageCode")){
                        heardchat.setVisibility(View.VISIBLE);


//                        tvstatus.setText("");
//                        tvtime.setText("");
//                        tvvalue.setText("");
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);
                    }else{
                        tvnodata.setVisibility(View.GONE);
                        BloodPreessDate date = JsonTools.getData(result.toString(), BloodPreessDate.class);
                        List<BloodPressRes> list = date.getData();
                        heardchat.setVisibility(View.VISIBLE);
//                        tvtime.setText("今天"+CommonUtils.returnTime(list.get(list.size()-1).getMonitorTime(),2));



//                        int ssz=CommonUtils.computeSsz(Integer.valueOf(list.get(list.size()-1).getHigh()));
//                        int szy=CommonUtils.computeSzy(Integer.valueOf(list.get(list.size()-1).getLow()));
//                        int lv=CommonUtils.MaxInt(ssz,szy);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodPressRes th : list) {

                            Double xd = Double.valueOf(th.getMonitorTime().split("\\ ")[1]);
                            Double ydH = Double.valueOf(th.getHigh());
                            Double ydL = Double.valueOf(th.getLow());
                            linePoint1.add(new PointD(xd, ydH));
                            linePoint2.add(new PointD(xd, ydL));
                        }
                        mChart.refresh(linePoint1,linePoint2);

//                        switch (lv) {
//                            case 1:
//                                tvstatus.setText("血压正常");
//                                tvstatus.setTextColor(Color.parseColor("#53bbb3"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 2:
//                                tvstatus.setText("高血压一期");
//                                tvstatus.setTextColor(Color.parseColor("#fb7701"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 3:
//                                tvstatus.setText("高血压二期");
//                                tvstatus.setTextColor(Color.parseColor("#fac833"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 4:
//                                tvstatus.setText("高血压三期");
//                                tvstatus.setTextColor(Color.parseColor("#ff6616"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

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
        requestMaker.BloodPressureInquiryType(userid,startTime,endTime,"D",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                try {
                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BloodPressureInquiry");
                    if(array.getJSONObject(0)
                            .has("MessageCode")){
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);


//                        tvstatus.setText("");
//                        tvtime.setText("");
//                        tvvalue.setText("");
                    }else{
                        tvnodata.setVisibility(View.GONE);
                        BloodPreessDate date = JsonTools.getData(result.toString(), BloodPreessDate.class);
                        List<BloodPressRes> list = date.getData();
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodPressRes th : list) {
                            Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],months);
                            Double ydH = Double.valueOf(th.getHigh());
                            Double ydL = Double.valueOf(th.getLow());
                            linePoint1.add(new PointD(xd, ydH));
                            linePoint2.add(new PointD(xd, ydL));
                        }
                        mChart.refresh(linePoint1,linePoint2);
//                        tvtime.setText(CommonUtils.returnTime3(list.get(list.size()-1).getMonitorTime(),1)+" "+CommonUtils.returnTime(list.get(list.size()-1).getMonitorTime(),2));


//                        int ssz=CommonUtils.computeSsz(Integer.valueOf(list.get(list.size()-1).getHigh()));
//                        int szy=CommonUtils.computeSzy(Integer.valueOf(list.get(list.size()-1).getLow()));
//                        int lv=CommonUtils.MaxInt(ssz,szy);
//                        switch (lv) {
//                            case 1:
//                                tvstatus.setText("血压正常");
//                                tvstatus.setTextColor(Color.parseColor("#53bbb3"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 2:
//                                tvstatus.setText("高血压一期");
//                                tvstatus.setTextColor(Color.parseColor("#fb7701"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 3:
//                                tvstatus.setText("高血压二期");
//                                tvstatus.setTextColor(Color.parseColor("#fac833"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 4:
//                                tvstatus.setText("高血压三期");
//                                tvstatus.setTextColor(Color.parseColor("#ff6616"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                        }


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
        requestMaker.BloodPressureInquiryType(userid,startTime,endTime,"D",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                try {
                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BloodPressureInquiry");
                    if(array.getJSONObject(0)
                            .has("MessageCode")){
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);

//                        tvstatus.setText("");
//                        tvtime.setText("");
//                        tvvalue.setText("");
                    }else{
                        BloodPreessDate date = JsonTools.getData(result.toString(), BloodPreessDate.class);
                        List<BloodPressRes> list = date.getData();
                        tvnodata.setVisibility(View.GONE);
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodPressRes th : list) {
                            Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);

                            Double ydH = Double.valueOf(th.getHigh());
                            Double ydL = Double.valueOf(th.getLow());
                            linePoint1.add(new PointD(xd, ydH));
                            linePoint2.add(new PointD(xd, ydL));
                        }
                        mChart.refresh(linePoint1,linePoint2);
//                        tvtime.setText(CommonUtils.returnTime3(list.get(list.size()-1).getMonitorTime(),1)+" "+CommonUtils.returnTime(list.get(list.size()-1).getMonitorTime(),2));


//                        int ssz=CommonUtils.computeSsz(Integer.valueOf(list.get(list.size()-1).getHigh()));
//                        int szy=CommonUtils.computeSzy(Integer.valueOf(list.get(list.size()-1).getLow()));
//                        int lv=CommonUtils.MaxInt(ssz,szy);
//                        switch (lv) {
//                            case 1:
//                                tvstatus.setText("血压正常");
//                                tvstatus.setTextColor(Color.parseColor("#53bbb3"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 2:
//                                tvstatus.setText("高血压一期");
//                                tvstatus.setTextColor(Color.parseColor("#fb7701"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 3:
//                                tvstatus.setText("高血压二期");
//                                tvstatus.setTextColor(Color.parseColor("#fac833"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                            case 4:
//                                tvstatus.setText("高血压三期");
//                                tvstatus.setTextColor(Color.parseColor("#ff6616"));
//                                tvvalue.setText("收缩压"+list.get(list.size()-1).getHigh()+"mmhg; "+"舒张压"+list.get(list.size()-1).getLow()+"mmhg");
//                                break;
//                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_press) == event
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







    public static Fragment getInstance() {
        return new NewPressFragment();
    }
    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header,
                              int itemPosition, long headerId, boolean currentlySticky) {

    }
    @Override
    public void OnLoadingMore() {
        progressBarView.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);

        loadingAnimation.start();
        page++;
        if(!isLoading) {
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
        EventBus.getDefault().unregister(this);
    }
}
