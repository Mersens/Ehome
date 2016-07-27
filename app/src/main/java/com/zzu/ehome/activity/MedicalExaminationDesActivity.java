package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MedicalExaminationAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MedicalBean;
import com.zzu.ehome.bean.MedicalDate;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.XCRoundRectImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/18.
 */
public class MedicalExaminationDesActivity extends BaseActivity implements View.OnClickListener{
    private RequestMaker requestMaker;
    private String title,id,userid;
    private Intent mIntent;
    private List<MedicalBean> meList;
    private TextView edt_time,edt_jzdw,tvname;
    private GridAdapter mAdapter;
    private RecyclerView resultRecyclerView;
    private LinearLayout llcheckr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_examination_layout);
        requestMaker=RequestMaker.getInstance();
        userid= SharePreferenceUtil.getInstance(MedicalExaminationDesActivity.this).getUserId();
        mIntent=this.getIntent();
        title=mIntent.getStringExtra("Title");
        id=mIntent.getStringExtra("ID");
        initViews();
        initEnent();
    }

    private void initEnent() {
        startProgressDialog();
        requestMaker.MeidicalReportInquiry(userid,id,new JsonAsyncTask_Info(MedicalExaminationDesActivity.this, true,new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {
                try {
                    stopProgressDialog();

                    String value=result.toString();
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MeidicalReportInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {

                    } else {
                        MedicalDate date = JsonTools.getData(result.toString(), MedicalDate.class);
                        meList = date.getData();
                        String checktime=DateUtils.StringPattern(meList.get(0).getCheckTime(),"yyyy/MM/dd HH:mm:ss","yyyy年MM月dd日");
                        edt_time.setText(checktime);
                        edt_jzdw.setText(meList.get(0).getInstituteName());
                        tvname.setText(meList.get(0).getUserName());
                        String imgstr=meList.get(0).getReportImage();

                        if (imgstr.indexOf(",") >= 0) {
                            llcheckr.setVisibility(View.VISIBLE);
                            String[] strs = imgstr.split("\\,");
                            List<String> images=new ArrayList<String>();
                            for (int m = 0; m < strs.length; m++) {
                                String imgurl = Constants.EhomeURL + strs[m].replace("~", "").replace("\\", "/");
                                images.add(imgurl);
                            }
                            mAdapter=new GridAdapter(images,MedicalExaminationDesActivity.this);
                            resultRecyclerView.setLayoutManager(new GridLayoutManager(MedicalExaminationDesActivity.this, 3));
                            resultRecyclerView.setAdapter(mAdapter);
                        }else{
                            if(!TextUtils.isEmpty(imgstr)){
                                llcheckr.setVisibility(View.VISIBLE);
                                List<String> images2=new ArrayList<String>();
                                String imgurl2 = Constants.EhomeURL + imgstr.replace("~", "").replace("\\", "/");
                                images2.add(imgurl2);
                                mAdapter=new GridAdapter(images2,MedicalExaminationDesActivity.this);
                                resultRecyclerView.setLayoutManager(new GridLayoutManager(MedicalExaminationDesActivity.this, 3));
                                resultRecyclerView.setAdapter(mAdapter);
                            }else{
                                llcheckr.setVisibility(View.GONE);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }

    }
    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left,title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        edt_time=(TextView) findViewById(R.id.edt_time);
        edt_jzdw=(TextView) findViewById(R.id.edt_jzdw);
        tvname=(TextView) findViewById(R.id.tvname);
        llcheckr=(LinearLayout)findViewById(R.id.llcheckr);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);

    }
    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mContext;
        private ImageLoader imageLoader;
        public List<String> getmList() {
            return mList;
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
        }

        private List<String> mList;
        public GridAdapter(List<String> list,Context context){
            super();
            this.mList=list;
            this.mContext=context;
            imageLoader=ImageLoader.getInstance();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_report, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(MedicalExaminationDesActivity.this)
                    .load(mList.get(position))
                    .centerCrop()
                    .into(holder.imageView);
//            imageLoader.displayImage(mList.get(position),holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageAlbumManager.class);
                    intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                    intent.putExtra("position", position);
                    intent.putExtra("type", 1);
                    mContext.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mList==null?0:mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);

            }
        }
    }

}
