package com.zzu.ehome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodPressChatAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public List<HealthDataRes> getList() {
        return list;
    }

    public void setList(List<HealthDataRes> list) {
        this.list = list;
    }

    List<HealthDataRes> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public BloodPressChatAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= null;
        HealthDataRes res= (HealthDataRes) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_health_bloodpress,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_ssy = (TextView) convertView
                    .findViewById(R.id.tv_ssy_msg);

            holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
            holder.tvmb=(TextView)convertView.findViewById(R.id.tv_mb_msg);
            holder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_xy_msg);
            holder.rl_status=(RelativeLayout)convertView.findViewById(R.id.rl_status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String press="血压:"+res.getValue1()+"/"+res.getValue2()+"mmHg";
        SpannableString style = new SpannableString(press);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),3, press.length()-4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_ssy.setText(style);
        String mb="脉搏:"+res.getValue3();
        SpannableString style1 = new SpannableString(mb);
        style1.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),3, mb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvmb.setText(style1);
        int ssz=CommonUtils.computeSsz(Integer.valueOf(res.getValue1()));
        int szy=CommonUtils.computeSzy(Integer.valueOf(res.getValue2()));
        int lv=CommonUtils.MaxInt(ssz,szy);
        switch (lv){
            case 1:
                holder.tv_status.setText("正常");
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_4);

                break;
            case 2:
                holder.tv_status.setText("一期");
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_1);

                break;
            case 3:
                holder.tv_status.setText("二期");
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_5);

                break;
            case 4:
                holder.tv_status.setText("三期");
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_6);
                break;
        }
        holder.tv_time.setText(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","dd日 HH:mm"));


        return convertView;

    }
    public static class ViewHolder {

        public TextView tv_ssy;
        public RelativeLayout rl_status;
        public TextView tvmb;
        public TextView tv_time;
        public TextView tv_status;

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HealthDataRes res= (HealthDataRes) getItem(position);
        View headView = mInflater.inflate(R.layout.item_chat_header,
                parent, false);
        TextView tvheader = (TextView) headView.findViewById(R.id.tv_time);
        String date= DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","M月yyyy年");
        int bstart = date.indexOf("月");
        SpannableString style = new SpannableString(date);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleItemColor), 0, bstart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvheader.setText(style);
        return headView;
    }

    /**
     * 决定header出现的时机，如果当前的headerid和前一个headerid不同时，就会显示
     */
    @Override
    public long getHeaderId(int position) {

        HealthDataRes res= (HealthDataRes) getItem(position);
        return CommonUtils.returnLongNew(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","yyyy-MM"));

    }
}
