package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.SelectPatientActivity;
import com.zzu.ehome.bean.DoctorSchemaByTopmdBean;
import com.zzu.ehome.view.DialogTips;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorTimeAdapter extends BaseListAdapter<DoctorSchemaByTopmdBean> {
    private List<DoctorSchemaByTopmdBean> mList;
    private Context context;

    public DoctorTimeAdapter(Context context, List<DoctorSchemaByTopmdBean> objects) {
        super(context, objects);
        this.mList=objects;
        this.context=context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.doctor_time_item,null);

        TextView tv_gh=(TextView) mView.findViewById(R.id.tv_gh);
        TextView tvcontent=(TextView)mView.findViewById(R.id.tvcontent);
        TextView tvresidue=(TextView)mView.findViewById(R.id.tvresidue);
        tvcontent.setText(mList.get(position).getScheduleTimeContent());
        tvresidue.setText("剩余号源"+mList.get(position).getRegistNum());
        tv_gh.setOnClickListener(new MyClickListener(position));
        return mView;
    }

    class MyClickListener implements View.OnClickListener{
        private int pos;
       public  MyClickListener(int pos){
         this.pos=pos;
        }

        @Override
        public void onClick(View v) {
            if(Integer.parseInt(mList.get(pos).getRegistNum())==0){
                shownDialog();
            }else {
                mContext.startActivity(new Intent(mContext, SelectPatientActivity.class));
            }

        }
    }
    private void shownDialog() {
        DialogTips dialog = new DialogTips(context, "该时段暂无可预约号源",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {


            }
        });

        dialog.show();
        dialog = null;

    }


}
