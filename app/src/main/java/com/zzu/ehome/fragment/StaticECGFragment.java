package com.zzu.ehome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.StaticECGDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/22.
 */
public class StaticECGFragment extends BaseFragment {

    private View mView;
    private LinearLayout layout_none;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dynamic,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        layout_none=(LinearLayout)mView.findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);
        listView=(ListView)mView.findViewById(R.id.listView);


    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), StaticECGDetailActivity.class));

            }
        });

    }

    public void initDatas(){
       final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<5;i++){
            mList.add(i);
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=LayoutInflater.from(getActivity()).inflate(R.layout.dynamic_item,null);
                TextView tv_title=(TextView) v.findViewById(R.id.tv_title);
                tv_title.setText("静态心电报告");
                return v;
            }
        });

    }


    public static Fragment getInstance(){
        return new StaticECGFragment();
    }

    @Override
    protected void lazyLoad() {

    }
}
