package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.DoctorListActivity;
import com.zzu.ehome.activity.DoctorTimeActivity;
import com.zzu.ehome.bean.DoctorBeanDes;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorListAdapter extends BaseListAdapter<DoctorBeanDes> {
    private List<DoctorBeanDes> mList;
    private ImageLoader mImageLoader;
    private Context context;
    private String hosid,depid,DepartmentName;
    public DoctorListAdapter(Context context, List<DoctorBeanDes> objects,String hosid,String depid,String DepartmentName) {
        super(context, objects);
        mList=objects;
        this.context=context;
        this.hosid=hosid;
        this.depid=depid;
        this.DepartmentName=DepartmentName;
        mImageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.doctor_item,null);
        TextView tv_name=(TextView)mView.findViewById(R.id.tv_name);
        ImageView ivhead=(ImageView)mView.findViewById(R.id.icon_hosptial);
        final String picUrl=mList.get(position).getPictureURL().replace("~", "")
                .replace("\\", "/");
        mImageLoader.displayImage(picUrl,ivhead);

        tv_name.setText(mList.get(position).getDoctorName());
        final DoctorBeanDes item=getItem(position);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,DoctorTimeActivity.class);
                i.putExtra("HospitalID",hosid);
                i.putExtra("DepartmentID",depid);
                i.putExtra("DoctorId",item.getDoctorID());
                i.putExtra("DepartName",DepartmentName);
                i.putExtra("picUrl",picUrl);
                i.putExtra("doctorName",item.getDoctorName());

                context.startActivity(i);
            }
        });
        return mView;
    }
}
