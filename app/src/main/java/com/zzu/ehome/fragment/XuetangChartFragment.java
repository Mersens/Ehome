package com.zzu.ehome.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zzu.ehome.R;
import com.zzu.ehome.activity.DataChatActivity;
import com.zzu.ehome.activity.TiwenDataActivity;
import com.zzu.ehome.activity.XuetangDataActivity;
import com.zzu.ehome.bean.BloodPreessDate;
import com.zzu.ehome.bean.BloodPressRes;
import com.zzu.ehome.bean.BloodSuggarDate;
import com.zzu.ehome.bean.BloodSuggarRes;
import com.zzu.ehome.bean.BusEvent;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HomeSuggarView;

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
 * Created by Administrator on 2016/5/16.
 */
public class XuetangChartFragment extends  BaseFragment {
    private View view;
    private HomeSuggarView mChart;
    String startTime,endTime;
    private RequestMaker requestMaker;
    String userid;
    private ImageView ivxuetang;
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<String> weeks=new LinkedList<>();
    private List<String> months=new ArrayList<>();
    private TextView tvnodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_xuetangchart,null);
        requestMaker=RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();

        initViews();
        initEvents();
        return view;
    }
    public void initViews() {

        ivxuetang=(ImageView) view.findViewById(R.id.iv_blood);
        mChart=(HomeSuggarView)view.findViewById(R.id.chart);
        tvnodata=(TextView) view.findViewById(R.id.tvnodate);
    }
    public void onEventMainThread(RefreshEvent event) {

        if(getResources().getInteger(R.integer.refresh_info) == event
                .getRefreshWhere()) {
            userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
            setWeek();
        }
        if(getResources().getInteger(R.integer.refresh_manager_data) == event
                .getRefreshWhere()) {
            setWeek();

        }
        if(getResources().getInteger(R.integer.refresh_suggar) == event
                .getRefreshWhere()) {
            userid=SharePreferenceUtil.getInstance(getActivity()).getUserId();
            setWeek();
        }
    }

    public void initEvents() {
        setWeek();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), XuetangDataActivity.class);
                intent.putExtra("position",3);
                intent.putExtra("time","");
                startActivity(intent);

            }
        });

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

                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        mChart.refresh(linePoint1,linePoint2);
                        tvnodata.setVisibility(View.VISIBLE);
                    }else{
                        tvnodata.setVisibility(View.GONE);
                        BloodSuggarDate date = JsonTools.getData(result.toString(), BloodSuggarDate.class);
                        List<BloodSuggarRes> list = date.getData();

                        List<PointD> linePoint1 = new ArrayList<PointD>();
                        List<PointD> linePoint2 = new ArrayList<PointD>();
                        for (BloodSuggarRes th : list) {
                            if(th.getMonitorPoint().contains("餐后")){
                                Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);
                                Double ydH= Double.valueOf(th.getBloodSugarValue());
                                linePoint1.add(new PointD(xd, ydH));
                            }else {
                                Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);
                                Double ydl = Double.valueOf(th.getBloodSugarValue());

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
    protected void lazyLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
