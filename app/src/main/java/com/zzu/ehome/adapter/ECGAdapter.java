package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/16.
 */
public class ECGAdapter extends BaseListAdapter<Map<String, Object>> {
    private List<Map<String, Object>> mList;
    public ECGAdapter(Context context, List<Map<String, Object>> objects) {
        super(context, objects);
        this.mList=objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.ecg_item,null);
            holder.name=(TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
//        String name=mList.get(position);
       holder.name.setText(mList.get(position).get("Name").toString());
        return convertView;
    }

    public static  class ViewHolder{
        private TextView name;
    }

}
