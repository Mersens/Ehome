package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.DateUtils;

import java.util.List;

/**
 * Created by zzu on 2016/4/20.
 */
public class SelectDateAdapter extends BaseListAdapter<String> {
    private Context context;
    private List<String> list;

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    private int curr=-1;

    public SelectDateAdapter(Context context,List<String> list){
        super(context,list);
        this.context=context;
        this.list=list;
    }
    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.listview_select_time_item,null);
            holder.time=(TextView) convertView.findViewById(R.id.time);
            holder.week=(TextView) convertView.findViewById(R.id.week);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(curr!=-1&&position==curr){
            convertView.setBackgroundResource(R.color.item_color);
        }else{
            convertView.setBackgroundResource(R.color.base_color_text_white);
        }
        String time=list.get(position);
        holder.week.setText(DateUtils.dayForWeek(time));
        holder.time.setText(time);
        return convertView;
    }

    public static class ViewHolder{
        private TextView time;
        private TextView week;
    }
}
