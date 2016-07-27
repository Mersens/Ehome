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
import com.zzu.ehome.bean.TempItemHistory;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.RequestMaker;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class TempChatAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public List<HealthDataRes> getList() {
        return list;
    }

    public void setList(List<HealthDataRes> list) {
        this.list = list;
    }

    List<HealthDataRes> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public TempChatAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.item_health_temperature,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_tw = (TextView) convertView
                    .findViewById(R.id.tv_tw);
            holder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_twjg_msg);
            holder.rl_status=(RelativeLayout)convertView.findViewById(R.id.rl_status);
            holder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String tw="体温:"+res.getValue1()+"℃";
        int bstart = tw.indexOf("℃");
        int bend = tw.indexOf("体温:");
        SpannableString style = new SpannableString(tw);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),bend+3, bstart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_tw.setText(style);

        float temp=Float.valueOf(res.getValue1());
        if(Float.compare(temp,37.1f)>=0&&Float.compare(temp,38F)<=0){
            holder.tv_status.setText("低热");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue);

        }else if(Float.compare(temp,38.1F)>=0&&Float.compare(temp,39F)<=0){
            holder.tv_status.setText("中等度热");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_1);

        }else if(Float.compare(temp,39.1F)>=0&&Float.compare(temp,41F)<=0){
            holder.tv_status.setText("高热");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_2);

        }else if(Float.compare(temp,41F)>0){
            holder.tv_status.setText("超高热");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_3);

        }
        else{
            holder.tv_status.setText("正常");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_4);


        }
        holder.tv_time.setText(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","dd日 HH:mm"));
        return convertView;

    }
    public static class ViewHolder {

        public TextView tv_tw;
        private TextView tv_status;
        public RelativeLayout rl_status;
        public TextView tv_time;

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HealthDataRes res= (HealthDataRes) getItem(position);
        View headView = mInflater.inflate(R.layout.item_chat_header,
                parent, false);
        TextView tvheader = (TextView) headView.findViewById(R.id.tv_time);
        String date=DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","M月yyyy年");
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
