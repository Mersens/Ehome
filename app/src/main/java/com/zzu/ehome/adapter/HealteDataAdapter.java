package com.zzu.ehome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.utils.CommonUtils;

import java.util.List;

/**
 * Created by zzu on 2016/4/9.
 */
public class HealteDataAdapter extends BaseAdapter implements StickyListHeadersAdapter {



    private List<HealthDataRes> list;
    private Context mContext;
    final int VIEW_TYPE = 4;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    final int TYPE_4 = 3;
    private LayoutInflater mInflater;


    public HealteDataAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

    }
    public void setList(List<HealthDataRes> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return VIEW_TYPE;
    }
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
       String  type=list.get(position).getType();
        if (type.equals("Temperature")) {
            return TYPE_1;

        } else if(type.equals("BloodPressure")){
            return TYPE_2;
        }else if(type.equals("BloodSugar")){
            return  TYPE_3;

        }else{
            return  TYPE_4;
        }

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
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        ViewHolder4 holder4 = null;
        int type = getItemViewType(position);
        HealthDataRes res= (HealthDataRes) getItem(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = mInflater.inflate(R.layout.item_data_temperature,
                            parent, false);
                    holder1 = new ViewHolder1();
                    holder1.tv_tw = (TextView) convertView
                            .findViewById(R.id.tv_tw);
                    holder1.tv_status = (TextView) convertView
                            .findViewById(R.id.tv_twjg_msg);
                    holder1.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = mInflater.inflate(R.layout.item_data_bloodpress,
                            parent, false);
                    holder2 = new ViewHolder2();
                    holder2.tv_ssy = (TextView) convertView
                            .findViewById(R.id.tv_ssy_msg);
                    holder2.tv_szy=(TextView) convertView.findViewById(R.id.tv_szy_msg);
                    holder2.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
                    holder2.tvmb=(TextView)convertView.findViewById(R.id.tv_mb_msg);
                    holder2.tv_status = (TextView) convertView
                            .findViewById(R.id.tv_xy_msg);

                    convertView.setTag(holder2);

                    break;
                case TYPE_3:
                    convertView = mInflater.inflate(R.layout.item_data_bloodsuggar,
                            parent, false);
                    holder3 = new ViewHolder3();
                    holder3.tv_value = (TextView) convertView
                            .findViewById(R.id.tv_xt_num);
                    holder3.tv_suggartime=(TextView) convertView.findViewById(R.id.tv_xt_time);
                    holder3.tvtime=(TextView) convertView.findViewById(R.id.tv_time);
                    holder3.tv_status=(TextView)convertView.findViewById(R.id.tv_xt_res);
                    convertView.setTag(holder3);
                    break;
                case TYPE_4:
                    convertView = mInflater.inflate(R.layout.item_data_weirht,
                            parent, false);
                    holder4 = new ViewHolder4();
                    holder4.tv_value = (TextView) convertView
                            .findViewById(R.id.tv_tz_num);
                    holder4.tv_status=(TextView) convertView.findViewById(R.id.tv_tz_msg);
                    holder4.tv_time=(TextView) convertView.findViewById(R.id.tv_time);



                    convertView.setTag(holder4);
                    break;


            }
        }else{
            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();

                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
                case TYPE_3:
                    holder3 = (ViewHolder3) convertView.getTag();
                    break;

                case TYPE_4:
                    holder4 = (ViewHolder4) convertView.getTag();
                    break;
                default:
                    break;
            }

        }
        switch (type) {
            case TYPE_1:
                holder1.tv_tw.setText(res.getValue1()+"℃");

                float temp=Float.valueOf(res.getValue1());

                if(Float.compare(temp,37.1F)>=0&&Float.compare(temp,38F)<=0){
                    holder1.tv_status.setText("低热");
                    holder1.tv_status.setTextColor(Color.parseColor("#f9a116"));
                }else if(Float.compare(temp,38.1F)>=0&&Float.compare(temp,39F)<=0){
                    holder1.tv_status.setText("中等度热");
                    holder1.tv_status.setTextColor(Color.parseColor("#fb7701"));
                }else if(Float.compare(temp,39.1F)>=0&&Float.compare(temp,41F)<=0){
                    holder1.tv_status.setText("高热");
                    holder1.tv_status.setTextColor(Color.parseColor("#fa3b00"));
                }else if(Float.compare(temp,41F)>0){
                    holder1.tv_status.setText("超高热");
                    holder1.tv_status.setTextColor(Color.parseColor("#ea0b35"));
                }
                else{
                    holder1.tv_status.setText("正常");
                    holder1.tv_status.setTextColor(Color.parseColor("#53bbb3"));

                }
                holder1.tv_time.setText(CommonUtils.returnTime(res.getMonitorTime(),2));


                break;
            case TYPE_2:
                holder2.tv_ssy.setText(res.getValue1());
                holder2.tv_szy.setText(res.getValue2());
                holder2.tvmb.setText(res.getValue3());

                int ssz=CommonUtils.computeSsz(Integer.valueOf(res.getValue1()));
                int szy=CommonUtils.computeSzy(Integer.valueOf(res.getValue2()));
                int lv=CommonUtils.MaxInt(ssz,szy);
                switch (lv){
                    case 1:
                        holder2.tv_status.setText("血压正常");
                        holder2.tv_status.setTextColor(Color.parseColor("#53bbb3"));

                        break;
                    case 2:
                        holder2.tv_status.setText("高血压一期");
                        holder2.tv_status.setTextColor(Color.parseColor("#fb7701"));
                        break;
                    case 3:
                        holder2.tv_status.setText("高血压二期");
                        holder2.tv_status.setTextColor(Color.parseColor("#fa3b00"));
                        break;
                    case 4:
                        holder2.tv_status.setText("高血压三期");
                        holder2.tv_status.setTextColor(Color.parseColor("#ea0b35"));
                        break;
                }
                holder2.tv_time.setText(CommonUtils.returnTime(res.getMonitorTime(),2));

                break;
            case TYPE_3:
                holder3 = (ViewHolder3) convertView.getTag();
                int typevalue=Integer.valueOf(res.getValue3());
                switch (typevalue){
                    case 1:
                        holder3.tv_value.setText(res.getValue1()+"mmol/l");
                        checkSuager(Float.valueOf(res.getValue1()),holder3.tv_status,res.getValue2());
                        break;
                    case 2:
                        holder3.tv_value.setText(res.getValue1()+"mg/dl");
                        checkSuager(Float.valueOf(res.getValue1())/18,holder3.tv_status,res.getValue2());
                        break;

                }
                holder3.tv_suggartime.setText(res.getValue2());
                holder3.tvtime.setText(CommonUtils.returnTime(res.getMonitorTime(),2));

                break;

            case TYPE_4:
                holder4 = (ViewHolder4) convertView.getTag();
                holder4.tv_value.setText(res.getValue1()+"kg");
                holder4.tv_time.setText(CommonUtils.returnTime(res.getMonitorTime(),2));
                break;
            default:
                break;

        }

        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HealthDataRes res= (HealthDataRes) getItem(position);
        View headView = mInflater.inflate(R.layout.item_health_header,
                parent, false);


            TextView tvheader = (TextView) headView.findViewById(R.id.tv_time);
            tvheader.setText(CommonUtils.returnTime3(res.getMonitorTime(), 1));

        return headView;
    }


    /**
     * 决定header出现的时机，如果当前的headerid和前一个headerid不同时，就会显示
     */
    @Override
    public long getHeaderId(int position) {



            HealthDataRes res= (HealthDataRes) getItem(position);

           return CommonUtils.returnLongTime(res.getMonitorTime());

    }

    private void checkSuager(float value,TextView tv,String time) {
        if(time.contains("餐后")||time.contains("睡前")){
            if(Float.compare(value,6.1F)>=0&&Float.compare(value,11.1F)<=0){
                tv.setText("血糖正常");
                tv.setTextColor(Color.parseColor("#53bbb3"));
            }else if(Float.compare(value,11.1F)>0){
                tv.setText("血糖偏高");
                tv.setTextColor(Color.parseColor("#ff6616"));
            }else{
                tv.setText("血糖偏低");
                tv.setTextColor(Color.parseColor("#fac833"));
            }

        }else{
            if(Float.compare(value,3.9F)<0){
                tv.setText("血糖偏低");
                tv.setTextColor(Color.parseColor("#fac833"));
            }else if(Float.compare(value,3.9F)>=0&&Float.compare(value,6.1F)<=0){
                tv.setText("血糖正常");
                tv.setTextColor(Color.parseColor("#53bbb3"));
            }else{
                tv.setText("血糖偏高");
                tv.setTextColor(Color.parseColor("#ff6616"));
            }

        }
    }
    public static class ViewHolder1 {

        public TextView tv_tw;
        public TextView tv_status;
        public TextView tv_time;

    }

    public static class ViewHolder2 {
        public TextView tv_ssy;
        public TextView tv_szy;
        public TextView tvmb;
        public TextView tv_time;
        public TextView tv_status;

    }
    public static class ViewHolder3 {
        public TextView tv_value;
        public TextView tv_suggartime;
        public TextView tvtime;
        public TextView tv_status;

    }
    public static class ViewHolder4 {
        public TextView tv_value;
        public TextView tv_status;
        public TextView tv_time;


    }

}
