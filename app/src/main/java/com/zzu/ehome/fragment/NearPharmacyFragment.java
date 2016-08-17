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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.NearPharmacyMapActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/17.
 * NearPharmacyFragment
 */
public class NearPharmacyFragment extends BaseFragment {
    private ListView mListView;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near_pharmacy,null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        initViews();
        initEvent();
        initDatas();
    }

    public void  initViews(){
        mListView=(ListView)mView.findViewById(R.id.lilstView);
    }


    public void initEvent(){

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), NearPharmacyMapActivity.class));
            }
        });
    }


    public void initDatas(){
        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<10;i++){
            mList.add(i);

        }

        mListView.setAdapter(new BaseAdapter() {
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
                View v=LayoutInflater.from(getActivity()).inflate(R.layout.near_pharmacy_item,null);
                RelativeLayout layout_b=(RelativeLayout) v.findViewById(R.id.layout_b);
                layout_b.setVisibility(View.GONE);
                RelativeLayout layout_h=(RelativeLayout) v.findViewById(R.id.layout_h);
                layout_h.setVisibility(View.GONE);
                return v;
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }


    public static Fragment getInstance(){

        return new NearPharmacyFragment();
    }

}
