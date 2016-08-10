package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class SelectPatientAdapter extends BaseListAdapter<String> {
    private List<String> mList;
    public SelectPatientAdapter(Context context, List<String> objects) {
        super(context, objects);
        mList=objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.doctor_item,null);
        TextView tv_name=(TextView)mView.findViewById(R.id.tv_name);
        tv_name.setText(mList.get(position));
        return mView;
    }

}
