package com.zzu.ehome.fragment;

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

import com.zzu.ehome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/27.
 */
public class MyReportFragment extends BaseFragment {
    private View mView;
    private ListView listView;
    public static final String PATH="path";
    private String path=null;
    private LinearLayout layout_none;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment,null);
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
        listView=(ListView)mView.findViewById(R.id.listView);
        layout_none=(LinearLayout)mView.findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);

    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void initDatas(){
        path=getArguments().getString(PATH);
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
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.examination_report_item,null);
                return view;
            }
        });
    }

    public  static Fragment getInstance(String path){
        Bundle b=new Bundle();
        b.putString(PATH,path);
        MyReportFragment af= new MyReportFragment();
        af.setArguments(b);
        return af;
    }
    @Override
    protected void lazyLoad() {

    }
}
