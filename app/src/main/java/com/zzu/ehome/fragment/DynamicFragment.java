package com.zzu.ehome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.ECGActivity;
import com.zzu.ehome.activity.ECGDetailsActivity;
import com.zzu.ehome.activity.InternetHospitalActivity;
import com.zzu.ehome.activity.NetHospitalAreaActivity;
import com.zzu.ehome.adapter.ECGAdapter;
import com.zzu.ehome.adapter.ECGReportAdapter;
import com.zzu.ehome.bean.ECGDate;
import com.zzu.ehome.bean.ECGDynamicBean;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserInfoDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_ECGInfo;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.List;

/**
 * Created by dell on 2016/6/20.
 */
public class DynamicFragment extends BaseFragment {
    private View mView;
    private ListView listView;
    private ECGReportAdapter adapter;
    private LinearLayout layout_no_msg;
    private TextView tv_hlwyy;
    private RequestMaker requestMaker;
    private  String userid;
    private LinearLayout heardchat;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_ect, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        initViews();
        initEvent();
        initDatas();
        return mView;
    }

    public void initViews() {
        heardchat= (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_head, null);
        listView = (ListView) mView.findViewById(R.id.listview);
        layout_no_msg = (LinearLayout) mView.findViewById(R.id.layout_no_msg);
        tv_hlwyy = (TextView) mView.findViewById(R.id.tv_hlwyy);
        listView.addHeaderView(heardchat);
    }

    public void initEvent() {
        tv_hlwyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InternetHospitalActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initDatas() {
        String starttime = "";
        String endtime = "";

        startProgressDialog();
//        userid="130521";
        requestMaker.HolterPDFInquiry(userid, starttime, endtime, new JsonAsyncTask_ECGInfo(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                Log.e("TAG", "dynamic result===" + result.toString());
                stopProgressDialog();
                try {
                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("Result");
                    int code=Integer.valueOf(array.getJSONObject(0).getString("MessageCode"));
                    if(code==0){
                        layout_no_msg.setVisibility(View.VISIBLE);
                    }else{
                        layout_no_msg.setVisibility(View.GONE);

                        JSONObject mySORel = array.getJSONObject(0).getJSONObject("MessageContent").getJSONObject("resultDetail");

                        ECGDate date = JsonTools.getData(mySORel.toString(), ECGDate.class);
                        List<ECGDynamicBean> list = date.getData();
                        adapter=new ECGReportAdapter(getActivity(),list);
                        listView.setAdapter(adapter);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }

        }));


    }


    public static Fragment getInstance() {
        return new DynamicFragment();
    }

    @Override
    protected void lazyLoad() {

    }


}
