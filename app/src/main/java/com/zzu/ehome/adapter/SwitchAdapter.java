package com.zzu.ehome.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.DiseaseBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/1.
 */
public class SwitchAdapter extends BaseListAdapter<DiseaseBean> {
    private List<DiseaseBean> mList;
    private Context context;
    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();

    public SwitchAdapter(Context context, List<DiseaseBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.context = context;
    }

    @Override
    public View getGqView(final int position, View convertView, ViewGroup parent) {

        View view = getInflater().inflate(R.layout.layout_switch, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        SwitchButton sbtn = (SwitchButton) view.findViewById(R.id.sbutn_md);
        final DiseaseBean bean = mList.get(position);
        name.setText(bean.getName());

        sbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                map.put(position, isChecked);
                bean.setOpen(isChecked);

            }
        });
        sbtn.setChecked(bean.isOpen());
        return view;
    }

/*
    public static class ViewHolder {
        private TextView name;
        private SwitchButton sbtn;

    }
*/

    public Map<Integer, Boolean> getDataMap() {
        for(Map.Entry<Integer,Boolean> entry:map.entrySet()){
           Log.e("TAG",entry.getKey()+"-------------"+entry.getValue());
        }
        return this.map;
    }



}
