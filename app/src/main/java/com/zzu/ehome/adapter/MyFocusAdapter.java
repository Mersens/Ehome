package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.DoctorBean;
import com.zzu.ehome.utils.ImageOptions;

import java.util.List;

/**
 * Created by zzu on 2016/4/15.
 */
public class MyFocusAdapter extends BaseAdapter {
    private Context mcontext;
    private List<DoctorBean> mList;
    private LayoutInflater mInflater;

  public MyFocusAdapter(Context mcontext,List<DoctorBean> mList){
      this.mcontext=mcontext;
      this.mList=mList;
      mInflater=LayoutInflater.from(mcontext);
  }





    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_doctor_item, null);
            holder.btn_yuyue = (Button) convertView.findViewById(R.id.btn_yuyue);
            holder.user_img = (ImageView) convertView.findViewById(R.id.user_img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_zc = (TextView) convertView.findViewById(R.id.tv_zc);
            holder.tv_yy = (TextView) convertView.findViewById(R.id.tv_yy);
            holder.tv_ks = (TextView) convertView.findViewById(R.id.tv_ks);
            holder.tv_sc_msg = (TextView) convertView.findViewById(R.id.tv_sc_msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btn_yuyue.setVisibility(View.GONE);
        final DoctorBean bean = mList.get(position);
        ImageLoader.getInstance().displayImage(bean.getUser_Icon(), holder.user_img, ImageOptions.getHeaderOptions());
        holder.tv_name.setText(bean.getUser_FullName());
        holder.tv_zc.setText(bean.getDoctor_Title());
        holder.tv_yy.setText(bean.getHosptial_Name());
        holder.tv_ks.setText(bean.getHosptial_office());
        holder.tv_sc_msg.setText(bean.getDoctor_Specialty());
        return convertView;
    }

    public static class ViewHolder {
        private Button btn_yuyue;
        private ImageView user_img;
        private TextView tv_name;
        private TextView tv_zc;
        private TextView tv_yy;
        private TextView tv_ks;
        private TextView tv_sc_msg;

    }
}
