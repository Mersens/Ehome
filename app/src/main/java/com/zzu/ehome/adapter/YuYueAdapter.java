package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.OfficeListActivity;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class YuYueAdapter extends BaseListAdapter<HospitalBean> {
    private List<HospitalBean> mList;
    private Context mContext;


    public YuYueAdapter(Context context, List<HospitalBean> objects) {
        super(context, objects);
        this.mList=objects;
        this.mContext=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.item_yygh,null);
        TextView name=(TextView) mView.findViewById(R.id.hosptial_name);
        name.setText(mList.get(position).getHospital_FullName());
        final int p=position;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, OfficeListActivity.class);
                i.putExtra("id",mList.get(p).getHospital_Id());
                mContext.startActivity(i);


            }
        });
        return mView;
    }
}
