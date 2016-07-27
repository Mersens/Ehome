package com.zzu.ehome.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MedicinalRecordAdapter;
import com.zzu.ehome.bean.MedicationDate;
import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Mersens on 2016/6/27.
 */
public class YYJLDataActivity extends BaseActivity implements StickyListHeadersListView.OnHeaderClickListener,AdapterView.OnItemClickListener,StickyListHeadersListView.OnLoadingMoreLinstener{
    private RequestMaker requestMaker;
    private StickyListHeadersListView listView;
    private String userid;
    private int page=1;
    private MedicinalRecordAdapter mAdapter;

    private LinearLayout rlup;
    private RelativeLayout moredata;
    private LayoutInflater inflater;
    private View progressBarView;
    private TextView progressBarTextView;
    private boolean isLoading = false;
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private List<MedicationRecord> mlist=new ArrayList<MedicationRecord>();
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker= RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(YYJLDataActivity.this).getUserId();
        setContentView(R.layout.layout_yyjl_data);
        initViews();
        EventBus.getDefault().register(this);
        initDate();


    }

public void initViews() {
    inflater = LayoutInflater.from(YYJLDataActivity.this);
    rlup=(LinearLayout)findViewById(R.id.ll_up);

    listView=(StickyListHeadersListView) findViewById(R.id.listView);
    mAdapter=new MedicinalRecordAdapter(YYJLDataActivity.this);


    listView.setAdapter(mAdapter);
    moredata = (RelativeLayout)inflater.inflate(R.layout.moredata_date, null);
    progressBarView = (View) moredata.findViewById(R.id.loadmore_foot_progressbar);
    progressBarTextView = (TextView) moredata.findViewById(R.id.loadmore_foot_text);
    loadingAnimation = (AnimationDrawable) progressBarView.getBackground();
    listView.addFooterView(moredata);
    listView.setOnItemClickListener(this);
    listView.setOnHeaderClickListener(this);
    listView.setLoadingMoreListener(this);


    setDefaultViewMethod(R.mipmap.icon_arrow_left, "用药记录", R.mipmap.icon_add_zoushi
            , new HeadView.OnLeftClickListener() {
                @Override
                public void onClick() {
                    finishActivity();
                }
            }, new HeadView.OnRightClickListener() {
                @Override
                public void onClick() {
//                        doCheck();
                    startActivity(new Intent(YYJLDataActivity.this,YYJLActivity.class));

                }
            });
}
    private void initDate() {
        startProgressDialog();
        requestMaker.MedicationRecordInquiry(userid,10+"",page+"", new JsonAsyncTask_Info(YYJLDataActivity.this, true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MedicationRecordInquiry");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode"))
                    {
                        if(page==1) {
                            rlup.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }else
                            loadingFinished();

                    }else {

                        if(page==1){
                            rlup.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                        MedicationDate date = JsonTools.getData(result.toString(), MedicationDate.class);
                        List<MedicationRecord> list = date.getData();
                        if(page==1){
                            if(mlist.size()>0) mlist.clear();
                        }
                        if(list.size()>0) {
                            for (int i = 0; i < list.size(); i++) {
                                mlist.add(list.get(i));
                            }
                            mAdapter.setList(mlist);

                        }
                        loadingFinished();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingFinished();
                }
            }
        }));

    }
    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    public void onEventMainThread(RefreshEvent event) {
        if (getResources().getInteger(R.integer.refresh_manager_data) == event
                .getRefreshWhere()) {
            page=1;
            initDate();
        }

    }



    public void loadingFinished() {

        if (null != loadingAnimation && loadingAnimation.isRunning()) {
            loadingAnimation.stop();
        }
        progressBarView.setVisibility(View.INVISIBLE);
        progressBarTextView.setVisibility(View.INVISIBLE);
        isLoading = false;

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnLoadingMore() {
        progressBarView.setVisibility(View.VISIBLE);
        progressBarTextView.setVisibility(View.VISIBLE);

        loadingAnimation.start();
        page++;
        if(!isLoading) {
            isLoading = true;
            initDate();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

