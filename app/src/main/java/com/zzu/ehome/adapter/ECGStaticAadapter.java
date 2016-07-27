package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
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
            convertView = getInflater().inflate(R.layout.ecg_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        String time = mList.get(position).getUpdatedDate().split(" ")[0];
//        String[] yeartime = time.split("/");
//
//
//        final String title=yeartime[0] + "年" + yeartime[1] + "月" + yeartime[2] + "日" + "心电报告";
        //    2016/4/28 14:55:11

        final StaticBean item=getItem(position);
        holder.name.setText(DateUtils.StringPattern(item.getCollectTime(),"yyyy/MM/dd HH:mm:ss","yyyy年M月dd日")+"心电报告");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, StaticECGDetial.class);
                i.putExtra("imurl",item.getImgPath());
                mContext.startActivity(i);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private TextView name;
    }
}
