package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.DoctorBeanDes;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorListAdapter extends BaseListAdapter<DoctorBeanDes> {
    private List<DoctorBeanDes> mList;
    private ImageLoader mImageLoader;
    public DoctorListAdapter(Context context, List<DoctorBeanDes> objects) {
        super(context, objects);
        mList=objects;
        mImageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.doctor_item,null);
        TextView tv_name=(TextView)mView.findViewById(R.id.tv_name);
        ImageView ivhead=(ImageView)mView.findViewById(R.id.icon_hosptial);
        mImageLoader.displayImage(mList.get(position).getPictureURL().replace("~", "")
                .replace("\\", "/"),ivhead);
        tv_name.setText(mList.get(position).getDoctorName());
        return mView;
    }
}
