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
public class OfficeListAdapter extends BaseListAdapter<String> {
    private List<String> mList;
    public OfficeListAdapter(Context context, List<String> objects) {
        super(context, objects);
        this.mList=objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.office_list_item,null);
        TextView tv_name=(TextView) mView.findViewById(R.id.tv_name);
        tv_name.setText(mList.get(position));
        return mView;
    }
}
