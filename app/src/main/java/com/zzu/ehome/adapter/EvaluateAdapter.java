package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.ehome.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public class EvaluateAdapter extends BaseListAdapter<String>{
    

    public EvaluateAdapter(Context context, List<String> objects) {
        super(context, objects);
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.evaluate_item_layout,null);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }


    public static class ViewHolder{


    }
}
