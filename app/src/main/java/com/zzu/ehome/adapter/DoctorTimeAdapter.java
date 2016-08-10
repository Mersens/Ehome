package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.SelectPatientActivity;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorTimeAdapter extends BaseListAdapter<String> {
    private List<String> mList;

    public DoctorTimeAdapter(Context context, List<String> objects) {
        super(context, objects);
        this.mList=objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.doctor_time_item,null);
        TextView tv_gh=(TextView) mView.findViewById(R.id.tv_gh);
        tv_gh.setOnClickListener(new MyClickListener(position));
        return mView;
    }

    class MyClickListener implements View.OnClickListener{
        private int pos;
       public  MyClickListener(int pos){
         this.pos=pos;
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(mContext, SelectPatientActivity.class));

        }
    }


}
