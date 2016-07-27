package com.zzu.ehome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WeightChatAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private String userid;
    public List<HealthDataRes> getList() {
        return list;
    }
    private EHomeDao dao;
    User dbUser;
    public void setList(List<HealthDataRes> list) {
        this.list = list;
    }

    List<HealthDataRes> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public WeightChatAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        dao=new EHomeDaoImpl(context);
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
        userid = SharePreferenceUtil.getInstance(mContext).getUserId();

        dbUser=dao.findUserInfoById(userid);
        ViewHolder holder= null;
        HealthDataRes res= (HealthDataRes) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_weight_layout,
                    parent, false);
            holder = new ViewHolder();

            holder.tv_value = (TextView) convertView
                    .findViewById(R.id.tv_tz_num);
//            holder.tv_status=(TextView) convertView.findViewById(R.id.tv_tz_msg);
            holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_bmi=(TextView)convertView.findViewById(R.id.tv_tz_bmi);
            holder.rl_status=(RelativeLayout)convertView.findViewById(R.id.rl_status);
            holder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_twjg_msg);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String s="体重:"+res.getValue1()+"kg";

        SpannableString style = new SpannableString(s);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor),3, s.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_value.setText(style);
        if(!TextUtils.isEmpty(dbUser.getUserHeight())) {
            holder.rl_status.setVisibility(View.VISIBLE);
            float w = Float.valueOf(res.getValue1());
            float h = Float.valueOf(dbUser.getUserHeight()) / 100;

            float bmi = w / (h * h);
            BigDecimal b  =   new BigDecimal(bmi);

            String showBmi=CommonUtils.showBMIDetail(b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            holder.tv_status.setText(showBmi);
            if(showBmi.equals(CommonUtils.TINY)){
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_7);
            }else if(showBmi.equals(CommonUtils.NORMAL)){
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_4);
            }else if(showBmi.equals(CommonUtils.OVERLOAD)){
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_1);
            }else if(showBmi.equals(CommonUtils.SAMLL)){
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_5);
            }else if(showBmi.equals(CommonUtils.MIDDLE)){
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_6);
            }else if(showBmi.equals(CommonUtils.BIG)){
                holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_8);
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            String ss = "BMI:" + decimalFormat.format(bmi);

            SpannableString style2 = new SpannableString(ss);
            style2.setSpan(
                    new TextAppearanceSpan(mContext, R.style.styleNormalColor), 4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tv_bmi.setText(style2);
        }else{
            String ss = "BMI:0";

            SpannableString style2 = new SpannableString(ss);
            style2.setSpan(
                    new TextAppearanceSpan(mContext, R.style.styleNormalColor), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.tv_status.setText("");
            holder.rl_status.setVisibility(View.INVISIBLE);
            holder.tv_bmi.setText(style2);
        }
        holder.tv_time.setText(DateUtils.StringPattern(res.getMonitorTime(),"yyyy/MM/dd HH:mm:ss","dd日 HH:mm"));

        return convertView;

    }
    public static class ViewHolder {

        public TextView tv_value;
//        public TextView tv_status;
        public TextView tv_time;
        public TextView tv_bmi;
        public RelativeLayout rl_status;
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
