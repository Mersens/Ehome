package com.zzu.ehome.adapter;

import android.content.Context;
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
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class JibuAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public List<StepCounterBean> getList() {
        return list;
    }

    public void setList(List<StepCounterBean> list) {
        this.list = list;
    }

    List<StepCounterBean> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public JibuAdapter(Context context) {
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
        StepCounterBean res= (StepCounterBean) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_jibu_layout,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_day = (TextView) convertView
                    .findViewById(R.id.tv_day);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_times);
            holder.tv_foot = (TextView) convertView
                    .findViewById(R.id.tv_foot);
            holder.tv_distance = (TextView) convertView
                    .findViewById(R.id.tv_distance);
            holder.tv_kj=(TextView)convertView.findViewById(R.id.tv_kj);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_day.setText(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","dd日"));
        String foots="总步数:"+res.getTotalStep()+"步";
        SpannableString style = new SpannableString(foots);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),4, foots.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_foot.setText(style);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String times="总时长:"+decimalFormat.format(Float.valueOf(res.getTotalTime()))+"Hr";
        SpannableString style2 = new SpannableString(times);
        style2.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),4, times.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_time.setText(style2);
        String distances="总距离:"+res.getTotalDistance()+"Km";
        SpannableString style3 = new SpannableString(distances);
        style3.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),4, distances.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_distance.setText(style3);


        String kj="消耗热量:"+String.format("%.2f",Float.valueOf(res.getTotalHeat()))+"Kcal";
        SpannableString style4 = new SpannableString(kj);
        style4.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),5, kj.length()-4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_kj.setText(style4);

        return convertView;

    }
    public static class ViewHolder {

        public TextView tv_day;
        public TextView tv_time;
        public TextView tv_foot;
        public TextView tv_distance;

        public TextView tv_kj;

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        StepCounterBean res= (StepCounterBean) getItem(position);
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
        StepCounterBean res= (StepCounterBean) getItem(position);
        return CommonUtils.returnLongNew(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","yyyy-MM"));

    }
}
