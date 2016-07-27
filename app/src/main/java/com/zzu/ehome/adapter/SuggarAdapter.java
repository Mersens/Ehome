package com.zzu.ehome.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class SuggarAdapter  extends BaseListAdapter<String>{
    private List<String> list;
    private Context mContext;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    private int current=-1;
    public SuggarAdapter(Context context, List<String> objects) {
        super(context, objects);
        this.list=objects;
        this.mContext=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.listview_select_hospital_item,null);
            holder.hosptial_name=(TextView) convertView.findViewById(R.id.hosptial_name);
            holder.llitem=(LinearLayout) convertView.findViewById(R.id.llitem);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.hosptial_name.setText(list.get(position));
        if(position==current) {
            holder.llitem.setBackgroundResource(R.color.item_color);
        }else{
            holder.llitem.setBackgroundResource(R.color.base_color_text_white);
        }
       setItem(holder,position);


        return convertView;
    }

    private void setItem(final ViewHolder vh,final int p){
        vh.llitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                vh.llitem.setBackgroundResource(R.color.item_color);
                current=p;
                notifyDataSetChanged();
            }
        });

    }

    public static class ViewHolder{
        private TextView hosptial_name;
        private LinearLayout llitem;
    }
}
