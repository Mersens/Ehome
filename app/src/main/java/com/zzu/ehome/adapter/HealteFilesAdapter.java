package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.ImageDetailActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzu on 2016/4/9.
 */
public class HealteFilesAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private LayoutInflater mInflater;

    private List<TreatmentInquirywWithPage> list;
    private Context context;

    public HealteFilesAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    public List<TreatmentInquirywWithPage> getList() {
        return list;
    }

    public void setList(List<TreatmentInquirywWithPage> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TreatmentInquirywWithPage bean = list.get(position);
        ViewHolder2 holder2 = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_health_file,
                    parent, false);
            holder2 = new ViewHolder2();
            holder2.tv_jg = (TextView) convertView.findViewById(R.id.tv_zdjg);
            holder2.tv_jy = (TextView) convertView.findViewById(R.id.tv_yyjy);
            holder2.gridView = (GridView) convertView.findViewById(R.id.gridView);
            holder2.tv_fileitem=(TextView) convertView.findViewById(R.id.file_item);
            convertView.setTag(holder2);

        } else {
            holder2 = (ViewHolder2) convertView.getTag();
        }
        TreatmentInquirywWithPage mItem = list.get(position);

        String zdjg=mItem.getZhenduan().replace("&lt;","<").replace("&gt;",">").replace("&amp;","&");
        String yyjy=mItem.getOpinion().replace("&lt;","<").replace("&gt;",">").replace("&amp;","&");
        holder2.tv_jg.setText(zdjg);
        holder2.tv_jy.setText(yyjy);
        int type = getItemViewType(position);
        final String imgstr = mItem.getImages();



            List<String> mList = new ArrayList<String>();
        if (imgstr.indexOf(",") >= 0) {
            String[] strs = imgstr.split("\\,");
            for (int m = 0; m < strs.length; m++) {
                String imgurl = Constants.EhomeURL + strs[m].replace("~", "").replace("\\", "/");
                mList.add(imgurl);
            }
            holder2.tv_fileitem.setVisibility(View.VISIBLE);
        }else{
            if(TextUtils.isEmpty(imgstr)){
                holder2.gridView.setVisibility(View.GONE);
                holder2.tv_fileitem.setVisibility(View.INVISIBLE);
            }else{
                holder2.tv_fileitem.setVisibility(View.VISIBLE);
                String imgurl2 = Constants.EhomeURL + imgstr.replace("~", "").replace("\\", "/");
                mList.add(imgurl2);
            }

        }

            if(mList.size()==1){
                holder2.gridView.setNumColumns(1);
            }else if(mList.size()==4){
                holder2.gridView.setNumColumns(2);
            }else{
                holder2.gridView.setNumColumns(3);
            }
            holder2.gridView.setAdapter(new GridViewImgAdapter(context, mList));
            holder2.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, ImageDetailActivity.class);
                    intent.putExtra("imgstr", imgstr);
                    intent.putExtra("index", position);
                    context.startActivity(intent);
                }
            });



        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        TreatmentInquirywWithPage mItem = list.get(position);
        View headView = mInflater.inflate(R.layout.item_health_file_header,
                parent, false);


            TextView tvheader = (TextView) headView.findViewById(R.id.tv_time);
            tvheader.setText(CommonUtils.returnTime2(mItem.getTime(), 1));
            TextView tv_hosname = (TextView) headView.findViewById(R.id.tv_hosname);

            tv_hosname.setText(mItem.getHosname());

        return headView;
    }

    @Override
    public long getHeaderId(int position) {


            TreatmentInquirywWithPage res= (TreatmentInquirywWithPage) getItem(position);

            return CommonUtils.returnLongTime(res.getTime());

    }


    /**
     * 有图片Item
     */
    public static class ViewHolder2 {
        private TextView tv_time;
        private TextView tv_name;
        private TextView tv_jg;
        private TextView tv_jy;
        private TextView tv_hosname;
        private TextView tv_fileitem;
        private GridView gridView;

    }


}




