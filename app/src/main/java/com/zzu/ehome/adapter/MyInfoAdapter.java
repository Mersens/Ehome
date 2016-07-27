package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/15.
 */
public class MyInfoAdapter extends BaseListAdapter<String>{
    public MyInfoAdapter(Context context, List<String> objects) {
        super(context, objects);
    }
    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.myinfo_item_layout,null);
            holder.tv_content=(TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_num=(TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }


    public static class ViewHolder{
        private TextView tv_num;
        private TextView tv_content;
        private TextView tv_time;
    }
}
