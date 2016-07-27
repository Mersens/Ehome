package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.utils.ImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class InstitutionAdapter extends BaseAdapter {
    private Context mcontext;
    private List<DoctorBean> mList;
    private LayoutInflater mInflater;

    public InstitutionAdapter(Context mcontext,List<DoctorBean> mList){
        this.mcontext=mcontext;
        this.mList=mList;
        mInflater=LayoutInflater.from(mcontext);
    }





    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_city_item, null);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    public static class ViewHolder {


    }
}
