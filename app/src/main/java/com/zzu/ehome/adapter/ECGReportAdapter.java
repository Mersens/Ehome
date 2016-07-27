package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.ECGDetailsActivity;
import com.zzu.ehome.bean.ECGDynamicBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class ECGReportAdapter extends BaseListAdapter<ECGDynamicBean> {
    private List<ECGDynamicBean> mList;
    private Context mContext;

    public ECGReportAdapter(Context context, List<ECGDynamicBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.ecg_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String time = mList.get(position).getUpdatedDate().split(" ")[0];
        String[] yeartime = time.split("/");


        final String title=yeartime[0] + "年" + yeartime[1] + "月" + yeartime[2] + "日" + "心电报告";
       final ECGDynamicBean item=getItem(position);
        holder.name.setText(title);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, ECGDetailsActivity.class);
                i.putExtra("Title",title);
                i.putExtra("Result",item.getReportResult());
                i.putExtra("Download",item.getFilePathRelative());
                i.putExtra("FileMD5",item.getFileMD5());
                i.putExtra("ReportType",item.getReportType());

                mContext.startActivity(i);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private TextView name;
    }
}