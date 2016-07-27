package com.zzu.ehome.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.xclcharts.event.click.ChartArcListener;


import com.zzu.ehome.R;
import com.zzu.ehome.activity.DataChatActivity;
import com.zzu.ehome.activity.TiwenDataActivity;
import com.zzu.ehome.adapter.TempChatAdapter;
import com.zzu.ehome.bean.BusEvent;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TempItemHistory;
import com.zzu.ehome.bean.TemperatureDate;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HomeTempView;
import com.zzu.ehome.view.MyLinearLayout;
import com.zzu.ehome.view.TempView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xclcharts.chart.PointD;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.event.click.ChartArcListener;

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
public class TiwenChartFragment extends  BaseFragment {
    private View view;
    private HomeTempView mChart;
    String startTime,endTime;
    private RequestMaker requestMaker;
    String userid;
    private LinearLayout llchat;
    private ImageView ivtiewen;
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<String> weeks=new LinkedList<>();
    private List<String> months=new ArrayList<>();
    private TextView tvnodata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_tiwenchart,null);
        requestMaker=RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();

        initViews();
        initEvents();
        return view;
    }
    public void initViews() {

//        llchat=(LinearLayout)view.findViewById(R.id.llchat);

        mChart=(HomeTempView)view.findViewById(R.id.chart);
        ivtiewen=(ImageView) view.findViewById(R.id.iv_blood);
        tvnodata=(TextView) view.findViewById(R.id.tvnodate);
    }

    public void initEvents() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getActivity(), DataChatActivity.class);
//                i.putExtra("position",0);
//                getActivity().startActivity(i);
                Intent intent = new Intent(getActivity(), TiwenDataActivity.class);
                intent.putExtra("position",0);
                intent.putExtra("time","");
                startActivity(intent);

            }
        });


        setWeek();

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
        if(getResources().getInteger(R.integer.refresh_temp) == event
                .getRefreshWhere()) {
            setWeek();

        }
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

        requestMaker.TemperatureInquiry(userid, startTime, endTime, "D", new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {

                try {
                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("TemperatureInquiry");
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
                        TemperatureDate date = JsonTools.getData(result.toString(), TemperatureDate.class);
                        List<TempItemHistory> list = date.getData();
//                        float temp = Float.valueOf(list.get(list.size() - 1).getValue());
//
//                        String [] times=list.get(list.size() - 1).getMonitorTime().split("\\ ")[0].split("\\-");
//
//                        tvtime.setText(times[1]+"-"+times[2]);
//                        tvtime.setText(CommonUtils.returnTime3(list.get(list.size() - 1).getMonitorTime(), 1) + "  " + CommonUtils.returnTime(list.get(list.size() - 1).getMonitorTime(), 2));

//                        setTemp(temp);
                        List<PointD> linePoint2 = new ArrayList<PointD>();
//                        linePoint2.add(new PointD(1d, 33d));
//                        linePoint2.add(new PointD(5d, 33.9d));
//                        linePoint2.add(new PointD(6d, 39.9d));
                        for (TempItemHistory th : list) {

                            Double xd = CommonUtils.position(th.getMonitorTime().split("\\ ")[0],weeks);
                            Double yd = Double.valueOf(th.getValue());
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


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
