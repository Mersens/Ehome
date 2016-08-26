package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.StaticECGDetailActivity;
import com.zzu.ehome.activity.StaticECGDetial;
import com.zzu.ehome.bean.StaticBean;
import com.zzu.ehome.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class ECGStaticAadapter extends BaseListAdapter<StaticBean> {
    private List<StaticBean> mList;
    private Context mContext;

    public ECGStaticAadapter(Context context, List<StaticBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.dynamic_item, null);
            holder.tvtitle=(TextView)convertView.findViewById(R.id.tv_title);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvtitle.setText("静态心电报告");
        final StaticBean item=getItem(position);
        holder.time.setText(item.getCollectTime());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, StaticECGDetailActivity.class);
                i.putExtra("imurl",item.getImgPath());
                i.putExtra("Diagnosis",item.getDiagnosis());
                i.putExtra("PatientName",item.getPatientName());
                i.putExtra("CollectTime",item.getCollectTime());
                mContext.startActivity(i);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private TextView tvtitle;
        private TextView time;
    }
}
