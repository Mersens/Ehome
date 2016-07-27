package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.DepartmentBean;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectOfficeActivity extends NetBaseActivity{
    private FlowLayout flowLayout, flowLayout1;
    private ViewGroup.MarginLayoutParams lp;
    private List<TextView> tvlists = new ArrayList<TextView>();
    private List<DepartmentBean> mList;
    private TextView tv_cancel;
    private TextView tv_ok;
    private int firstIndex=0;
    private String hospital_id;

    //请求单例
    private RequestMaker requestMaker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_office);
        requestMaker = RequestMaker.getInstance();
        hospital_id=getIntent().getStringExtra("hospital_id");
        initViews();
        initDates();
        initEvent();

    }

    public void initViews() {
        lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lp.bottomMargin = 8;
        lp.leftMargin = 8;
        lp.rightMargin = 8;
        lp.topMargin = 8;
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        flowLayout1 = (FlowLayout) findViewById(R.id.flowLayout1);
        tv_cancel=(TextView) findViewById(R.id.tv_cancel);
        tv_ok=(TextView) findViewById(R.id.tv_ok);

    }

    public void initDates() {
        startProgressDialog();
        requestMaker.DepartmentInquiry(hospital_id,new JsonAsyncTask_Info(SelectOfficeActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("DepartmentInquiry");
                    mList=new ArrayList<DepartmentBean>();
                    stopProgressDialog();
                    for(int i=0;i<array.length();i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        mList.add(JsonTools.getData(jsonObject.toString(),DepartmentBean.class));
                    }
                    creatLable();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void creatLable(){
        for (int i = 0; i < mList.size(); i++) {
            TextView tv = new TextView(SelectOfficeActivity.this);
            tv.setText(mList.get(i).getDepartment_FullName());
            tv.setId(i);
            tv.setTextSize(16);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setOnClickListener(mListener);
            tv.setBackground(getResources().getDrawable(R.drawable.textunselector));
            flowLayout.addView(tv, lp);
            tvlists.add(tv);
        }

    }

    public void initEvent() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("department",mList.get(firstIndex).getDepartment_FullName());
                intent.putExtra("department_id",mList.get(firstIndex).getDepartment_Id());
                setResult(DoctorFragment.ADD_OFFICE,intent);
                finish();
            }
        });
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            resetColor();
            int index = v.getId();
            firstIndex=index;
            tvlists.get(index).setBackground(getResources().getDrawable(R.drawable.textselector));
            tvlists.get(index).setTextColor(getResources().getColor(R.color.base_color_text_white));

        }
    };

    public void resetColor() {
        for (TextView t : tvlists) {
            t.setBackground(getResources().getDrawable(R.drawable.textunselector));
            t.setTextColor(getResources().getColor(R.color.text_color));
        }

    }

}
