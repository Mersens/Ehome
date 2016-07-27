package com.zzu.ehome.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.CreateillnessActivity;
import com.zzu.ehome.adapter.HealteFilesAdapter;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.bean.TreatmentInquirywWithPageDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by zzu on 2016/4/9.
 */
public class HealthFilesFragment extends BaseFragment implements StickyListHeadersListView.OnHeaderClickListener,AdapterView.OnItemClickListener,StickyListHeadersListView.OnLoadingMoreLinstener {
    private View view;
    private RequestMaker requestMaker;
    private StickyListHeadersListView listView;
    private String userid;
    private int page=1;
    private HealteFilesAdapter mAdapter;

    private RelativeLayout rlup;
    private ImageView ivupload;
    private RelativeLayout moredata;
    private LayoutInflater inflater;
    private View progressBarView;
    private TextView progressBarTextView;
    private boolean isLoading = false;
    private AnimationDrawable loadingAnimation; //加载更多，动画
    private List<TreatmentInquirywWithPage> mlist=new ArrayList<TreatmentInquirywWithPage>();
private TextView tvcopyright;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        view = inflater.inflate(R.layout.layout_health_files, null);
        requestMaker=RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(getActivity()).getUserId();
        EventBus.getDefault().register(this);
        initView();

        setListener();
//        initDate();
        return view;
    }
    private void setListener(){
        rlup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateillnessActivity.class));
            }
        });
        ivupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateillnessActivity.class));
            }
        });
    }

    private void initDate() {
        startProgressDialog();
        requestMaker.TreatmentInquirywWithPage(userid,5+"",page+"", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentInquirywWithPage");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode"))
                    {
                        if(page==1) {
                            rlup.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }else
                        loadingFinished();
//                        tvcopyright.setVisibility(View.VISIBLE);
                    }else {
//                        tvcopyright.setVisibility(View.VISIBLE);
                        if(page==1){
                            rlup.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                        TreatmentInquirywWithPageDate date = JsonTools.getData(result.toString(), TreatmentInquirywWithPageDate.class);
                        List<TreatmentInquirywWithPage> list = date.getData();
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

    public void initView(){
        inflater = LayoutInflater.from(getActivity());
        rlup=(RelativeLayout)view.findViewById(R.id.rl_up);

        listView=(StickyListHeadersListView) view.findViewById(R.id.listView);
        mAdapter=new HealteFilesAdapter(getActivity());
        ivupload=(ImageView)view.findViewById(R.id.iv_upload);

        listView.setAdapter(mAdapter);
        moredata = (RelativeLayout)inflater.inflate(R.layout.moredata_date, null);
        progressBarView = (View) moredata.findViewById(R.id.loadmore_foot_progressbar);
        progressBarTextView = (TextView) moredata.findViewById(R.id.loadmore_foot_text);
        loadingAnimation = (AnimationDrawable) progressBarView.getBackground();
        listView.addFooterView(moredata);
        listView.setOnItemClickListener(this);
        listView.setOnHeaderClickListener(this);
        listView.setLoadingMoreListener(this);

//        tvcopyright= (TextView) moredata.findViewById(R.id.tvcopyright);
    }


    public static Fragment getInstance() {
        return new HealthFilesFragment();
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
    public void onEventMainThread(RefreshEvent event) {
        if(getResources().getInteger(R.integer.refresh_manager_file) == event
                .getRefreshWhere()) {
            page=1;
            initDate();

        }
        if(getResources().getInteger(R.integer.refresh_manager) == event
                .getRefreshWhere()) {
            page=1;
            initDate();

        }
//        if(getResources().getInteger(R.integer.refresh_manager) == event
//                .getRefreshWhere()) {
//            page=1;
//            initDate();
//
//        }
//        if(getResources().getInteger(R.integer.change_file) == event
//                .getRefreshWhere()) {
//            page=1;
//            initDate();
//
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void lazyLoad() {

    }
}
