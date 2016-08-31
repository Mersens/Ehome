package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.DoctorListActivity;
import com.zzu.ehome.activity.OfficeListActivity;
import com.zzu.ehome.bean.DepartmentPart;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class OfficeListAdapter extends BaseAdapter {
    private Context mContext;
    final int VIEW_TYPE = 2;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private List<DepartmentPart> mList;
    private String ss;

    public OfficeListAdapter(Context context, List<DepartmentPart> objects,String sid) {

        this.mList=objects;
        this.mContext=context;
        this.ss=sid;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub

        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return VIEW_TYPE;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 vh1 = null;
        ViewHolder2 vh2 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.item_yuyue_depart, parent, false);
                    vh1 = new ViewHolder1();
                    vh1.tvName=(TextView) convertView
                            .findViewById(R.id.tvname);
                    convertView.setTag(vh1);
                    break;
                case TYPE_2:
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.office_list_item, parent, false);
                    vh2= new ViewHolder2();
                    vh2.tvName=(TextView) convertView
                            .findViewById(R.id.tv_name);
                    convertView.setTag(vh2);
                    break;

            }
        }else{
            switch (type) {
                case TYPE_1:
                    vh1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    vh2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }

        switch (type){
            case TYPE_1:

               vh1.tvName.setText(mList.get(position).getDepartmentName());
                break;
            case TYPE_2:
                vh2.tvName.setText(mList.get(position).getDepartmentName());
                final int p=position;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(mContext,DoctorListActivity.class);
                        i.putExtra("HospitalID",ss);
                        i.putExtra("DepartmentID",mList.get(p).getDepartmentID());
                        i.putExtra("DepartmentName",mList.get(p).getDepartmentName());
                        mContext.startActivity(i);
                    }
                });

                break;
        }

        return convertView;
    }

    class ViewHolder1 {

        TextView tvName;

    }
    class ViewHolder2 {
        TextView tvName;
    }
}
