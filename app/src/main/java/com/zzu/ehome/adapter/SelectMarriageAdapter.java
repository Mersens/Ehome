package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectMarriageAdapter extends BaseListAdapter<String> {
    private List<String> list;
    public SelectMarriageAdapter(Context context, List<String> objects) {
        super(context, objects);
        this.list=objects;
    }
    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.listview_select_hospital_item,null);
            holder.hosptial_name=(TextView) convertView.findViewById(R.id.hosptial_name);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.hosptial_name.setText(list.get(position));
        return convertView;
    }

    public static class ViewHolder{
        private TextView hosptial_name;
    }
}
