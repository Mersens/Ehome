package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.AppointmentActivity;
import com.zzu.ehome.activity.AppointmentDetailActivity;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.bean.ScheduleBean;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.fragment.DoctorFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by zzu on 2016/4/12.
 */
public class AppointmentAdapter extends BaseListAdapter<DoctorBean> {
    private Context mcontext;
    private List<DoctorBean> mList;
    private RequestMaker requestMaker;
    private String PatientId,userid;
    private EHomeDao dao;
    private Set<String> times;

    public AppointmentAdapter(Context context, List<DoctorBean> objects) {
        super(context, objects);
        this.mcontext=context;
        this.mList=objects;
        userid = SharePreferenceUtil.getInstance(context).getUserId();
        dao= new EHomeDaoImpl(context);
        PatientId=dao.findUserInfoById(userid).getPatientId();
    }
    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.listview_doctor_item,null);
            holder.btn_yuyue=(Button) convertView.findViewById(R.id.btn_yuyue);
            holder.user_img=(ImageView) convertView.findViewById(R.id.user_img);
            holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_zc=(TextView) convertView.findViewById(R.id.tv_zc);
            holder.tv_yy=(TextView) convertView.findViewById(R.id.tv_yy);
            holder.tv_ks=(TextView) convertView.findViewById(R.id.tv_ks);
            holder.tv_sc_msg=(TextView) convertView.findViewById(R.id.tv_sc_msg);
            convertView.setTag(holder);
        }else{
          holder=(ViewHolder) convertView.getTag();
        }
       final DoctorBean bean=mList.get(position);
        holder.btn_yuyue.setOnClickListener(new MyListener(position));
        ImageLoader.getInstance().displayImage(bean.getUser_Icon(),holder.user_img);
        holder.tv_name.setText(bean.getUser_FullName());
        holder.tv_zc.setText(bean.getDoctor_Title());
        holder.tv_yy.setText(bean.getHosptial_Name());
        holder.tv_ks.setText(bean.getHosptial_office());
        holder.tv_sc_msg.setText(bean.getDoctor_Specialty());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,AppointmentDetailActivity.class);
                intent.putExtra("department_id",AppointmentActivity.department_id);
                intent.putExtra("doctor_id",bean.getUser_Id());
                mcontext.startActivity(intent);
            }
        });
        return convertView;
    }

    public static  class ViewHolder{
        private Button btn_yuyue;
        private ImageView user_img;
        private TextView tv_name;
        private TextView tv_zc;
        private TextView tv_yy;
        private  TextView tv_ks;
        private TextView tv_sc_msg;

    }


    private class MyListener implements View.OnClickListener{
        private int pos;
        public MyListener(int pos){
            this.pos=pos;
        }
        @Override
        public void onClick(View v) {
            DoctorBean bean=mList.get(pos);
            AppointmentActivity app=(AppointmentActivity)mcontext;
//            app.setIntent(bean.getUser_FullName()+" "+bean.getHosptial_Name()+" "+bean.getHosptial_office(),bean.getUser_Id());
            app.setIntent(bean.getUser_FullName(),bean.getUser_Id());
        }
    }
//    public  void setIntent(final String id){
//        requestMaker.ScheduleInquiry(id,PatientId, new JsonAsyncTask_Info(mcontext, true, new JsonAsyncTaskOnComplete() {
//            @Override
//            public void processJsonObject(Object result) {
//                JSONObject mySO = (JSONObject) result;
//                Log.e("TAG", mySO.toString());
//                Date date = new Date();
//                try {
//                    JSONArray array = mySO.getJSONArray("ScheduleInquiry");
//
//                    for (int i = 0; i < array.length(); i++) {
//                        ScheduleBean bean = new ScheduleBean();
//                        JSONObject jsonObject = (JSONObject) array.get(i);
//                        bean.setStatus(jsonObject.getString("Status"));
//                        bean.setNum(jsonObject.getString("num"));
//                        bean.setDate(jsonObject.getString("Date"));
//
//                        bean.setPerTime(jsonObject.getString("PerTime"));
//                        bean.setWeekDay(jsonObject.getString("WeekDay"));
//                        bean.setPatientCount(jsonObject.getString("PatientCount"));
//                        bean.setTimeSlot(jsonObject.getString("TimeSlot"));
//
//                        bean.setTimeBlock(jsonObject.getString("TimeBlock"));
//                        if(!TextUtils.isEmpty(jsonObject.getString("TimeSlot"))) {
//
//                            times.add(jsonObject.getString("Date"));
//                        }
//                    }
//                    if(times.size()>0) {
//                        Intent intent=new Intent(mcontext,AppointmentDetailActivity.class);
//                        intent.putExtra("department_id",AppointmentActivity.department_id);
//                        intent.putExtra("doctor_id",id);
//                        mcontext.startActivity(intent);
//
//
//                    }else{
//                        ToastUtils.showMessage(mcontext,"该医生暂无坐诊时间!");
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ToastUtils.showMessage(mcontext,"该医生暂无坐诊时间!");
//
//                }
//            }
//        }));
//    }
}
