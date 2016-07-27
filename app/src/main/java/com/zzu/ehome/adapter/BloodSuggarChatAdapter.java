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

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BloodSuggarChatAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public List<HealthDataRes> getList() {
        return list;
    }

    public void setList(List<HealthDataRes> list) {
        this.list = list;
    }

    List<HealthDataRes> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public BloodSuggarChatAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.item_bloodsuggar,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_value = (TextView) convertView
                    .findViewById(R.id.tv_xt_num);
            holder.tv_suggartime=(TextView) convertView.findViewById(R.id.tv_xt_time);
            holder.tvtime=(TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_status=(TextView)convertView.findViewById(R.id.tv_xt_res);
            holder.rl_status=(RelativeLayout)convertView.findViewById(R.id.rl_status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        int typevalue=Integer.valueOf(res.getValue3());

        switch (typevalue){
            case 1:
                String xt="血糖:"+res.getValue1()+"mmol/l";

                SpannableString style = new SpannableString(xt);
                style.setSpan(
                        new TextAppearanceSpan(mContext, R.style.styleNormalColor),3, xt.length()-6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.tv_value.setText(style);
                checkSuager(Float.valueOf(res.getValue1()),holder.tv_status,res.getValue2(),holder.rl_status);
                break;
            case 2:
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String xt2="血糖:"+decimalFormat.format(Float.valueOf(res.getValue1())/18)+"mmol/l";
                int bstart2 = xt2.indexOf("mmol/l");

                SpannableString style2 = new SpannableString(xt2);
                style2.setSpan(
                        new TextAppearanceSpan(mContext, R.style.styleNormalColor),3, xt2.length()-6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.tv_value.setText(style2);
                checkSuager(Float.valueOf(res.getValue1())/18,holder.tv_status,res.getValue2(),holder.rl_status);
                break;

        }
        if(res.getValue2().contains("餐后")||res.getValue2().contains("睡前")){
            holder.tv_suggartime.setText("餐后");
        }else{
            holder.tv_suggartime.setText("空腹");
        }

        holder.tvtime.setText(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","dd日 HH:mm"));


        return convertView;

    }
    public static class ViewHolder {

        public TextView tv_value;
        public TextView tv_suggartime;
        public TextView tvtime;
        public TextView tv_status;
        public RelativeLayout rl_status;

    }
    private void checkSuager(float value,TextView tv,String time,RelativeLayout rl) {
        if(time.contains("餐后")||time.contains("睡前")){
            if(Float.compare(value,6.7F)>=0&&Float.compare(value,11.1F)<=0){
                tv.setText("正常");
               rl.setBackgroundResource(R.drawable.btn_yuyue_4);

            }else if(Float.compare(value,11.1F)>0){
                tv.setText("偏高");
                rl.setBackgroundResource(R.drawable.btn_yuyue_6);

            }else{
                tv.setText("偏低");
                rl.setBackgroundResource(R.drawable.btn_yuyue_5);

            }

        }else{
            if(Float.compare(value,3.9F)<0){
                tv.setText("偏低");
                rl.setBackgroundResource(R.drawable.btn_yuyue_5);
            }else if(Float.compare(value,3.9F)>=0&&Float.compare(value,6.1F)<=0){
                tv.setText("正常");
                rl.setBackgroundResource(R.drawable.btn_yuyue_4);
            }else{
                tv.setText("偏高");
                rl.setBackgroundResource(R.drawable.btn_yuyue_6);
            }

        }
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
