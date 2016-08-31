package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/27.
 */
public class MessageFragment extends BaseFragment {
    private View mView;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_message,null);
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
        setLeftWithTitleViewMethod(mView, R.mipmap.icon_arrow_left, "消息", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                getActivity().finish();
            }
        });
        listView=(ListView)mView.findViewById(R.id.listView);
    }

    public void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void initDatas(){
        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<6;i++){
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
                View v=LayoutInflater.from(getActivity()).inflate(R.layout.message_item,null);
                return v;
            }
        });

    }

    public static Fragment getInstance(){
        return new MessageFragment();
    }

    @Override
    protected void lazyLoad() {

    }
}
