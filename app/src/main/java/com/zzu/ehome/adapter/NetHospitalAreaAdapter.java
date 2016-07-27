package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/17.
 */
public class NetHospitalAreaAdapter extends BaseListAdapter <Map<String, Object>> {
    private List <Map<String, Object>> mList;
    private Context context;
    public NetHospitalAreaAdapter(Context context, List <Map<String, Object>> objects) {
        super(context, objects);
        this.mList=objects;
        this.context=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.ecg_item,null);
            holder.name=(TextView) convertView.findViewById(R.id.tv_name);
            holder.icon_right=(ImageView) convertView.findViewById(R.id.icon_right);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

       holder.name.setText(mList.get(position).get("Name").toString());
        holder.name.setTextColor(context.getResources().getColor(R.color.actionbar_color));
        holder.icon_right.setVisibility(View.GONE);
        return convertView;
    }

    public static  class ViewHolder{
        private TextView name;
        private ImageView icon_right;
    }
}
