package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.fragment.BloodPressureFragment;
import com.zzu.ehome.fragment.YYJLFragment;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Mersens on 2016/6/27.
 */
public class YYJLActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout layout_fysj;
    private LinearLayout layout_ypmc;
    private EditText edit_num, editText_bz, editText_ypmc_name;
    private ImageView imageView_add;
    private Button btn_save;
    private TextView tv_fysj_time;
    private RecyclerView resultRecyclerView;
    private ArrayList<String> images;
    private List<Images> mList = new ArrayList<Images>();
    public static final String EXTRA_IMAGES = "extraImages";
    private GridAdapter mAdapter;
    private String path;
    private String userid;
    private RequestMaker requestMaker;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_yyjl);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(YYJLActivity.this).getUserId();
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "用药记录", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });


        layout_fysj = (RelativeLayout) findViewById(R.id.layout_fysj);
        layout_ypmc = (LinearLayout) findViewById(R.id.layout_ypmc);
        edit_num = (EditText) findViewById(R.id.editText);
        editText_bz = (EditText) findViewById(R.id.editText_bz);
        imageView_add = (ImageView) findViewById(R.id.imageView_add);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_fysj_time = (TextView) findViewById(R.id.tv_fysj_time);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        editText_ypmc_name = (EditText) findViewById(R.id.editText_ypmc_name);

    }

    public void initEvent() {
        layout_fysj.setOnClickListener(this);
        imageView_add.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }

    public void initDatas() {
        images = new ArrayList<String>();
        path = ImageUtil.saveResTolocal(YYJLActivity.this.getResources(), R.mipmap.icon_add_xd, "photo_yao");
        images.add(path);
        mAdapter = new GridAdapter(images, YYJLActivity.this);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(YYJLActivity.this, 3));
        resultRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_fysj:
                Intent intenttime = new Intent(YYJLActivity.this, SelectDateAct.class);
                startActivityForResult(intenttime, Constants.ADDTTIME);
                break;
            case R.id.imageView_add:
                ImageSelectorActivity.start(YYJLActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true, false, false, images.size());

                break;
            case R.id.btn_save:
                doSave();
                break;
        }

    }


    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mContext;

        public List<String> getmList() {
            return mList;
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
        }

        private List<String> mList;

        public GridAdapter(List<String> list, Context context) {
            super();
            this.mList = list;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(YYJLActivity.this)
                    .load(new File(images.get(position)))
                    .centerCrop()
                    .into(holder.imageView);

            if (mList.get(position).equals(path)) {
                holder.imageClose.setVisibility(View.GONE);
            } else {
                holder.imageClose.setVisibility(View.VISIBLE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).equals(path)) {
                        ImageSelectorActivity.start((Activity) mContext, 9, ImageSelectorActivity.MODE_MULTIPLE, true, false, false, images.size() - 1);
                    } else {
                        Intent intent = new Intent(mContext, ImageAlbumManager.class);
                        if (mList.size() == 9) {
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                        } else {
                            List<String> imgs = new ArrayList<String>(mList);
                            //imgs=mList;
                            imgs.remove(mList.size() - 1);
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) imgs);
                        }
                        intent.putExtra("position", position);
                        intent.putExtra("type", 2);
                        mContext.startActivity(intent);
                    }
                }
            });
            holder.imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mList.remove(position);
//                        images.remove(position);
                    if (position == 8) {
//                        images.remove(8);
                        images.add(path);
                        setmList(images);
                    }
                    notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView imageClose;


            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageClose = (ImageView) itemView.findViewById(R.id.ivclose);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADDTTIME:
                if (data != null) {
                    String time = data.getStringExtra("time");
                    if (!TextUtils.isEmpty(time)) {
                        tv_fysj_time.setText(time);
                    }
                }
                break;
            case ImageSelectorActivity.REQUEST_IMAGE:
                if (data != null) {
                    ArrayList<String> images1 = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    if (images1 != null) {
                        for (int i = 0; i < images1.size(); i++) {
                            images.add(0, images1.get(i));
                        }
                        if (images.size() > 10) {

                            ToastUtils.showMessage(YYJLActivity.this, "你最多可以选择9张");
                        }
                        if (images.size() == 10) {
                            images.remove(9);
                        }
                    }

                    mAdapter.setmList(images);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }

    }

    //保存
    public void doSave() {
        if (CommonUtils.isFastClick())
            return;
         String time = tv_fysj_time.getText().toString().trim();
         String ypmc = editText_ypmc_name.getText().toString().trim();
         String num = edit_num.getText().toString().trim();
         String bz = editText_bz.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            ToastUtils.showMessage(YYJLActivity.this, "请选择服药时间！");
            return;
        }
        if (TextUtils.isEmpty(ypmc)) {
            ToastUtils.showMessage(YYJLActivity.this, "请输入药品名称！");
            return;

        }
        if (TextUtils.isEmpty(num)) {
            ToastUtils.showMessage(YYJLActivity.this, "请输入服药剂量！");
            return;
        }
        if(ypmc.length()>20){
            ToastUtils.showMessage(YYJLActivity.this, "药品名称最长输入20个字符！");
            return;
        }
        if(bz.length()>50){
            ToastUtils.showMessage(YYJLActivity.this, "备注最长输入50个字符！");
            return;
        }
      float numF= Float.valueOf(num);
        if(Float.compare(numF,100f)>=0){
            ToastUtils.showMessage(YYJLActivity.this, "服药剂量过大！");
            return;
        }
        startProgressDialog();
        if (images != null && images.size() > 0) {
            if (images.size() > 9) {
                ToastUtils.showMessage(YYJLActivity.this, "你最多可以选择9张");
                return;
            }
            List<String> imgs = new ArrayList<String>(images);

            if (images.size() < 9) {
                imgs.remove(imgs.size() - 1);

            }

            for (int i = 0; i < imgs.size(); i++) {
                Images imag = new Images();
                Bitmap newBitmap = BitmapFactory.decodeFile(ImageUtil.doPicture(images.get(i)));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG,
                        100, baos);
                imag.setCode(ImageUtil.Bitmap2StrByBase64(newBitmap));
                imag.setFileName(getPhotoFileName(i));
                mList.add(imag);
            }
        }
        //执行上传
        //doUpload()
        requestMaker.MedicationRecordInsert(userid, time, ypmc, num, bz, mList, new JsonAsyncTask_Info(YYJLActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    stopProgressDialog();
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("MedicationRecordInsert");
                    tv_fysj_time.setText("");
                    editText_ypmc_name.setText("");
                    edit_num.setText("");
                    editText_bz.setText("");
                    if(mList.size()>0){
                        images.clear();
                        mAdapter.setmList(images);
                        mAdapter.notifyDataSetChanged();
                    }
                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_data)));
                    EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                    ToastUtils.showMessage(YYJLActivity.this, array.getJSONObject(0).getString("MessageContent").toString());
                    finishActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }));

    }

    private String getPhotoFileName(int i) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        return dateFormat.format(date) + i + "_" + ".jpg";
    }
}
