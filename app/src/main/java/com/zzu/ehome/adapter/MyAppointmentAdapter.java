package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.AppointmentDetailActivity;
import com.zzu.ehome.activity.MyAppointmentActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.TreatmentSearch;
import com.zzu.ehome.utils.ImageOptions;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.MessageItem;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.SlideView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zzu on 2016/4/15.
 */
public class MyAppointmentAdapter extends BaseAdapter {

    private Context mcontext;
    private LayoutInflater mInflater;
    public List<TreatmentSearch> getList() {
        return list;
    }

    public void setList(List<TreatmentSearch> list) {
        this.list = list;
    }

    private List<TreatmentSearch> list;


    public MyAppointmentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext=context;

    }



    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        TreatmentSearch item = list.get(position);
        if(item.isOverdue()){
            return 1;
        }else{
            return 0;
        }


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TreatmentSearch bean = list.get(position);
//        SlideView slideView = (SlideView) convertView;
        ViewHolder holder=null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_doctor_item_01, null);
            holder.user_img = (ImageView) convertView.findViewById(R.id.user_img);
            holder.iv_status=(ImageView) convertView.findViewById(R.id.iv_status);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_zc = (TextView) convertView.findViewById(R.id.tv_zc);
            holder.tv_yy = (TextView) convertView.findViewById(R.id.tv_yy);
            holder.tv_ks = (TextView) convertView.findViewById(R.id.tv_ks);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.lldes=(LinearLayout) convertView.findViewById(R.id.lldes);
//            slideView = new SlideView(mcontext);
//            RelativeLayout layout = (RelativeLayout) slideView.findViewById(R.id.holder);
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteRecent(position);
//                }
//            });
//            slideView.setContentView(itemView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(bean.getDoctorIcon())) {
            String url = bean.getDoctorIcon().replace("~", "").replace("\\", "/");
            ImageLoader.getInstance().displayImage(Constants.ICON + url, holder.user_img, ImageOptions.getHeaderOptions());
        }
//        boolean isOpen = bean.isOpen();

        if(bean.isOverdue()){
            holder.iv_status.setVisibility(View.VISIBLE);
//            slideView.setOnClickListener(null);
//            slideView.shrink();
        }else{
//            holder.lldes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i=new Intent(mcontext,AppointmentDetailActivity.class);
//                    i.putExtra("yuyue","yuyue");
//                    mcontext.startActivity(i);
//                }
//            });
            holder.iv_status.setVisibility(View.GONE);
//            slideView.setOnSlideListener(new SlideView.OnSlideListener() {
//
//                @Override
//                public void onSlide(View view, int status) {
//                    if (status == SLIDE_STATUS_ON) {
//                        list.get(position).setOpen(true);
//                    }
//                    if (status == SLIDE_STATUS_OFF) {
//                        list.get(position).setOpen(false);
////                        if (!isHasOpen()) {
////                            MyAppointmentActivity activity = (MyAppointmentActivity) mcontext;
////                            activity.setRightText("管理");
////                        }
//                    }
//                }
//            });
        }
        holder.tv_name.setText(bean.getName());
        holder.tv_zc.setText(bean.getJobName());
        holder.tv_yy.setText(bean.getHospital());
        holder.tv_ks.setText(bean.getDepartment());
        holder.tv_time.setText(bean.getTime());
//        if (isOpen) {
//            slideView.open();
//        } else {
//            slideView.shrink();
//        }

//        MessageItem.putMsg(bean.getName(), slideView);
        return convertView;

    }

//    public void deleteRecent(final int position) {
//        TreatmentSearch bean = list.get(position);
//        String id = bean.getReservationId();
//        requestMaker.TreatmentCancel(id, new JsonAsyncTask_Info(mcontext, true, new JsonAsyncTaskOnComplete() {
//            @Override
//            public void processJsonObject(Object result) {
//                try {
//                    Log.e("Log", result.toString());
//                    JSONObject mySO = (JSONObject) result;
//                    org.json.JSONArray array = mySO
//                            .getJSONArray("TreatmentCancel");
//                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(mcontext, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
//                        remove(position);
//                        notifyDataSetChanged();
//                        if(list.size()==0){
//                            MyAppointmentActivity activity=(MyAppointmentActivity)mcontext;
//                            activity.rl_yuyue.setVisibility(View.VISIBLE);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }));

}

class ViewHolder {
    LinearLayout lldes;
    ImageView user_img;
    TextView tv_name;
    TextView tv_zc;
    TextView tv_yy;
    TextView tv_ks;
    TextView tv_time;
    ImageView iv_status;

}

//    //关闭全部
//    public void shrinkAll() {
//        setState(false);
//        notifyDataSetChanged();
//    }
//
//    //打开全部
//    public void openAll() {
//        setState(true);
//        notifyDataSetChanged();
//
//    }

//    public void remove(int position) {
//        this.list.remove(position);
//    }
//
//
//    public List<TreatmentSearch> getDatas() {
//        return this.list;
//    }



