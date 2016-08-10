package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class YuYueAdapter extends BaseListAdapter<HospitalBean> {
    private List<HospitalBean> mList;


    public YuYueAdapter(Context context, List<HospitalBean> objects) {
        super(context, objects);
        this.mList=objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.item_yygh,null);
        return mView;
    }
}
