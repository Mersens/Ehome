package com.zzu.ehome.fragment;

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
import com.zzu.ehome.adapter.BloodSuggarChatAdapter;
import com.zzu.ehome.bean.BloodPressRes;
import com.zzu.ehome.bean.BloodSuggarDate;
import com.zzu.ehome.bean.BloodSuggarRes;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.SuggarView;

import org.json.JSONArray;
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
 * Created by Administrator on 2016/4/26.
 */
public class NewSuggarFrament extends BaseFragment implements StickyListHeadersListView.OnHeaderClickListener,StickyListHeadersListView.OnLoadingMoreLinstener{
    private View view;
    private LayoutInflater inflater;
    private SuggarView mChart;
    private LinearLayout heardchat,lltmp;
    private RadioGroup group;
    private RadioButton rbday, rbweek,rbmonth;
//    private TextView tvvalue,tvstatus,tvtime;
    private String startTime,endTime;
    private RequestMaker requestMaker;
    private String userid;
    private int page=1;
    private StickyListHeadersListView listview;
    private BloodSuggarChatAdapter mAadpter;
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
        rbday.setChecked(true);
        rbday.setTextColor(getResources().getColor(R.color.white));
        rbweek.setChecked(false);
        rbweek.setTextColor(getResources().getColor(R.color.actionbar_color));
        rbmonth.setChecked(false);
        rbmonth.setTextColor(getResources().getColor(R.color.actionbar_color));
        setDay();

        page=1;
        getHistory();
        return view;
    }

    public void initViews() {
        inflater = LayoutInflater.from(getActivity());
        mAadpter=new BloodSuggarChatAdapter(getActivity());
        listview=(StickyListHeadersListView)view.findViewById(R.id.lv_temp);
        heardchat=(LinearLayout) inflater.inflate(R.layout.new_suggar,null);
        mChart=(SuggarView)heardchat.findViewById(R.id.chart);
        group = (RadioGroup) view.findViewById(R.id.radioGroup);
        rbday = (RadioButton) view.findViewById(R.id.rb_day);
        rbweek = (RadioButton) view.findViewById(R.id.rb_week);
        rbmonth=(RadioButton) view.findViewById(R.id.rb_month);
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
    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_suggar) == event
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
//        ToastUtils.showMessage(getActivity(),"vvvhhhhBolloess");
        requestMaker.HealthDataInquirywWithPageType(userid,10+"",page+"","BloodSugar",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue=result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");
                    if (array.getJSONObject(0).has("MessageCode")){
                        if(page==1) {

//                           listview.setVisibility(View.GONE);
                        }else
                            loadingFinished();
                    }else{
                        listview.setVisibility(View.VISIBLE);
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
        requestMaker.BloodSugarInquiryType(userid,startTime,endTime,"H",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                try {
                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BloodSugarInquiry");
                    if(array.getJSONObject(0)
                            .has("MessageCode")){


                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);

                    }else{
                        tvnodata.setVisibility(View.GONE);
                        BloodSuggarDate date = JsonTools.getData(result.toString(), BloodSuggarDate.class);
                        List<BloodSuggarRes> list = date.getData();

//                        setBloodSuggar(list);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodSuggarRes th : list) {
                            if(th.getMonitorPoint().contains("餐后")){
                                Double xd = Double.valueOf(th.getMonitorTime().split("\\ ")[1]);
                                Double ydH;

                                    ydH = Double.valueOf(th.getBloodSugarValue());
                                linePoint1.add(new PointD(xd, ydH));
                            }else {
                                Double xd = Double.valueOf(th.getMonitorTime().split("\\ ")[1]);
                                Double ydl;

                                    ydl = Double.valueOf(th.getBloodSugarValue());



                                linePoint2.add(new PointD(xd, ydl));
                            }

                        }
                        mChart.refresh(linePoint1,linePoint2);




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
        requestMaker.BloodSugarInquiryType(userid,startTime,endTime,"D",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                try {
                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BloodSugarInquiry");
                    if(array.getJSONObject(0)
                            .has("MessageCode")){
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);



                    }else{
                        tvnodata.setVisibility(View.GONE);
                        BloodSuggarDate date = JsonTools.getData(result.toString(), BloodSuggarDate.class);
                        List<BloodSuggarRes> list = date.getData();
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodSuggarRes th : list) {
                            if(th.getMonitorPoint().contains("餐后")){
                                Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],months);
                                Double ydH;

                                    ydH = Double.valueOf(th.getBloodSugarValue());



                                linePoint1.add(new PointD(xd, ydH));
                            }else {
                                Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],months);
                                Double ydl;

                                    ydl = Double.valueOf(th.getBloodSugarValue());



                                linePoint2.add(new PointD(xd, ydl));
                            }




                        }
                        mChart.refresh(linePoint1,linePoint2);




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
        requestMaker.BloodSugarInquiryType(userid,startTime,endTime,"D",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                try {
                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BloodSugarInquiry");
                    if(array.getJSONObject(0)
                            .has("MessageCode")){
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);
                    }else{
                        tvnodata.setVisibility(View.GONE);
                        BloodSuggarDate date = JsonTools.getData(result.toString(), BloodSuggarDate.class);
                        List<BloodSuggarRes> list = date.getData();
                        heardchat.setVisibility(View.VISIBLE);
                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodSuggarRes th : list) {
                            if(th.getMonitorPoint().contains("餐后")){
                                Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);
                                Double ydH;

                                    ydH = Double.valueOf(th.getBloodSugarValue());



                                linePoint1.add(new PointD(xd, ydH));
                            }else {
                                Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);
                                Double ydl;

                                    ydl = Double.valueOf(th.getBloodSugarValue());



                                linePoint2.add(new PointD(xd, ydl));
                            }




                        }
                        mChart.refresh(linePoint1,linePoint2);





                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

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



    public static Fragment getInstance() {
        return new NewSuggarFrament();
    }

    @Override
    protected void lazyLoad() {

    }
}
