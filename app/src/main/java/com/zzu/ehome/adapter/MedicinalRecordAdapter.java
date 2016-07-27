package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.GridView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.nostra13.universalimageloader.utils.L;
import com.zzu.ehome.R;

import com.zzu.ehome.activity.ImageAlbumManager;
import com.zzu.ehome.activity.ImageDetailActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.MedicalBean;

import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.view.crop.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 用药记录
 * Created by Administrator on 2016/6/30.
 */
public class MedicinalRecordAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private LayoutInflater mInflater;
    private Context context;

    public List<MedicationRecord> getList() {
        return list;
    }

    public void setList(List<MedicationRecord> list) {
        this.list = list;
    }

    private List<MedicationRecord> list;
    public MedicinalRecordAdapter(Context mcontext){
        this.context=mcontext;

        mInflater=LayoutInflater.from(mcontext);
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
        MedicationRecord item=list.get(position);
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_yyjy, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.gridView=(GridView)convertView.findViewById(R.id.gridView);
            holder.remark=(TextView) convertView.findViewById(R.id.tv);
            holder.llche=(LinearLayout)convertView.findViewById(R.id.llche);
            holder.llremark=(LinearLayout)convertView.findViewById(R.id.llremark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position>0){
            if(item.getMedicationTime().equals(list.get(position-1).getMedicationTime())){
                holder.tv_time.setVisibility(View.INVISIBLE);
            }else{
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(DateUtils.StringPattern(item.getMedicationTime(),"yyyy/MM/dd HH:mm:ss","dd日"));
            }
        }
        if(position==0){
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(DateUtils.StringPattern(item.getMedicationTime(),"yyyy/MM/dd HH:mm:ss","dd日"));
        }

        String mc=item.getDrugName()+":"+item.getNumber()+"片/次";
        int bstart = mc.indexOf(":");
//        int bend = bstart + ("℃").length();
        SpannableString style = new SpannableString(mc);
        style.setSpan(
                new TextAppearanceSpan(context, R.style.styleNormalColor),bstart, mc.length()-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_name.setText(style);

        if(!TextUtils.isEmpty(item.getRemarks())) {
            holder.llremark.setVisibility(View.VISIBLE);
            holder.remark.setVisibility(View.VISIBLE);
            holder.remark.setText("备注:" + item.getRemarks());
        }
        else{
            holder.llremark.setVisibility(View.GONE);
            holder.remark.setVisibility(View.GONE);
        }
        String urls=item.getDrugImage();
        List<String> mList = new ArrayList<String>();
        if(urls.indexOf(",") >= 0){
            String[] strs = urls.split("\\,");
            for (int m = 0; m < strs.length; m++) {
                String imgurl = Constants.EhomeURL + strs[m].replace("~", "").replace("\\", "/");

                mList.add(imgurl);
            }
            holder.gridView.setVisibility(View.VISIBLE);
        }else{
            if(TextUtils.isEmpty(urls)){
                holder.gridView.setVisibility(View.GONE);

            }else{

                String imgurl2 = Constants.EhomeURL + urls.replace("~", "").replace("\\", "/");
                mList.add(imgurl2);
                holder.gridView.setVisibility(View.VISIBLE);
            }

        }
        if(mList.size()>0) {
            holder.llche.setVisibility(View.VISIBLE);
           if (mList.size() == 4) {
                holder.gridView.setNumColumns(2);
            } else {
                holder.gridView.setNumColumns(3);
            }
        }else{
            holder.llche.setVisibility(View.GONE);
        }
        holder.gridView.setAdapter(new GridViewImgAdapter(context, mList));
        final List<String> mImgs=mList;
        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ImageAlbumManager.class);
                intent.putStringArrayListExtra("imgs", (ArrayList<String>)mImgs);
                intent.putExtra("position", position);
                intent.putExtra("type", 1);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public static class ViewHolder {

        private TextView tv_time;
        private TextView tv_name;
        private GridView gridView;
        private TextView remark;
        private LinearLayout llche;
        private LinearLayout llremark;

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        MedicationRecord res= (MedicationRecord) getItem(position);
        View headView = mInflater.inflate(R.layout.item_chat_header,
                parent, false);
        TextView tvheader = (TextView) headView.findViewById(R.id.tv_time);
        String date=DateUtils.StringPattern(res.getMedicationTime(),"yyyy/MM/dd HH:mm:ss","M月yyyy年");
        int bstart = date.indexOf("月");
        SpannableString style = new SpannableString(date);
        style.setSpan(
                new TextAppearanceSpan(context, R.style.styleItemColor), 0, bstart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvheader.setText(style);
        return headView;
    }

    /**
     * 决定header出现的时机，如果当前的headerid和前一个headerid不同时，就会显示
     */
    @Override
    public long getHeaderId(int position) {
        MedicationRecord res= (MedicationRecord) getItem(position);
        return CommonUtils.returnLongNew(DateUtils.StringPattern(res.getMedicationTime(),"yyyy/MM/dd HH:mm:ss","yyyy-MM"));

    }
}
