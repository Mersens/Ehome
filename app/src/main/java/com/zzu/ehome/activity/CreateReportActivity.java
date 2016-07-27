package com.zzu.ehome.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.bean.TreatmentInquirywWithPageDate;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.ContainsEmojiEditText;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.GridViewForScrollView;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.XCRoundRectImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by zzu on 2016/4/12.
 */
public class CreateReportActivity extends BaseActivity implements View.OnClickListener {
    private static final int TAKE_PICTURE = 0x000001;
    public static final int ADD_TIME = 0x11;
    public static final int ADD_TIMES = 0x35;

    private TextView edt_time;
    private TextView edt_jzdw;



    private ContainsEmojiEditText edt_jzjg;

    private ContainsEmojiEditText edt_yyjy;
    private String checktime="";
    private GridAdapter mAdapter;

    private Button btn_save;
    private GridViewForScrollView noScrollgridview;

    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap ;
    private LinearLayout lljzhen;
    private RequestMaker requestMaker;
    private String userid,jzyy,zdjg;
    public static final String EXTRA_IMAGES = "extraImages";
    private RecyclerView resultRecyclerView;
    private ImageView singleImageView;


    private ArrayList<String> images;
    private List<Images> mList=new ArrayList<Images>();
    private final String mPageName = "CreateReportActivity";
    private String path;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker=RequestMaker.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.layout_create_report);

        initViews();

        images=new ArrayList<String>();
        path=ImageUtil.saveResTolocal(CreateReportActivity.this.getResources(),R.mipmap.icon_add_xd,"add_xd");
        images.add(path);
        mAdapter=new GridAdapter(images,CreateReportActivity.this);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        resultRecyclerView.setAdapter(mAdapter);
        userid= SharePreferenceUtil.getInstance(CreateReportActivity.this).getUserId();

        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        initEvent();

    }


    public void initViews() {
        edt_time = (TextView) findViewById(R.id.edt_time);
        edt_jzdw = (TextView) findViewById(R.id.edt_jzdw);

        edt_jzjg = (ContainsEmojiEditText) findViewById(R.id.edt_jzjg);





        btn_save = (Button) findViewById(R.id.btn_save);
        lljzhen=(LinearLayout) findViewById(R.id.lljzhen);

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "拍报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });

        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    public void initEvent() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        edt_time.setText(df.format(new Date()));
        lljzhen.setOnClickListener(this);
        edt_jzdw.setOnClickListener(this);
        btn_save.setOnClickListener(this);

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
        public GridAdapter(List<String> list, Context context){
            super();
            this.mList=list;
            this.mContext=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(CreateReportActivity.this)
                    .load(new File(images.get(position)))
                    .centerCrop()
                    .into(holder.imageView);


            if(mList.get(position).equals(path)){
                holder.imageClose.setVisibility(View.GONE);
            }else{
                holder.imageClose.setVisibility(View.VISIBLE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(position).equals(path)) {

                        ImageSelectorActivity.start((Activity) mContext, 9, ImageSelectorActivity.MODE_MULTIPLE, true, false, false, images.size() - 1);
                    }else{
                        Intent intent = new Intent(mContext, ImageAlbumManager.class);
                        if(mList.size()==9) {
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                        }else{
                            List<String> imgs=new ArrayList<String>(mList);
                            //imgs=mList;
                            imgs.remove(mList.size()-1);
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
                    if(position==8){
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
            return mList==null?0:mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
          ImageView imageView;
            ImageView imageClose;


            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageClose=(ImageView) itemView.findViewById(R.id.ivclose);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_pic:
                //相机图片

                ImageSelectorActivity.start(CreateReportActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true,false,false,images.size());

                break;
            case R.id.lljzhen:
                Intent intenttime = new Intent(CreateReportActivity.this, SelectDateAct.class);
                startActivityForResult(intenttime, Constants.ADDTTIME);
                break;
            case R.id.edt_jzdw:
                Intent intenttimes = new Intent(this, HosListActivity.class);
                startActivityForResult(intenttimes, ADD_TIMES);
                break;
            case R.id.btn_save:
                //保存按钮
                if(CommonUtils.isFastClick())
                    return;

                jzyy=edt_jzdw.getText().toString();
                zdjg=edt_jzjg.getText().toString();

                checktime=edt_time.getText().toString();
                if(checktime.equals("")){
                    ToastUtils.showMessage(CreateReportActivity.this,"请选择体检日期!");
                    return;
                }else if(jzyy.equals("")){
                    ToastUtils.showMessage(CreateReportActivity.this,"请选择体检机构!");
                    return;
                }else if(zdjg.equals("")){
                    ToastUtils.showMessage(CreateReportActivity.this,"请填写体检人姓名!");
                    return;
                }else if(zdjg.length()>5){
                    ToastUtils.showMessage(CreateReportActivity.this,"体检人姓名过长!");
                    return;
                }

                startProgressDialog();

                if(images!=null&&images.size()>0){
                    if(images.size()>9){
                        ToastUtils.showMessage(CreateReportActivity.this,"你最多可以选择9张");
                        return;
                    }
                    List<String> imgs=new ArrayList<String>(images);

                    if(images.size()<9){
                        imgs.remove(imgs.size()-1);

                    }

                    for(int i=0;i<imgs.size();i++) {
                        Images imag=new Images();
                        Bitmap newBitmap = BitmapFactory.decodeFile(ImageUtil.doPicture(images.get(i)));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        newBitmap.compress(Bitmap.CompressFormat.JPEG,
                                100, baos);
                        imag.setCode(ImageUtil.Bitmap2StrByBase64(newBitmap));
                        imag.setFileName(getPhotoFileName(i));
                        mList.add(imag);
                    }
                }
                zdjg=zdjg.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;");
                requestMaker.MedicalReportInsert(userid,zdjg,checktime,jzyy,mList,new JsonAsyncTask_Info(CreateReportActivity.this, true,new JsonAsyncTaskOnComplete(){
                    @Override
                    public void processJsonObject(Object result) {
                        try {
                            stopProgressDialog();
                            String value=result.toString();
                            JSONObject mySO = (JSONObject) result;
                            org.json.JSONArray array = mySO
                                    .getJSONArray("MedicalReportInsert");
                            edt_time.setText("");
                            edt_jzdw.setText("");
                            edt_jzjg.setText("");
                            if(mList.size()>0){
                                images.clear();
                                mAdapter.setmList(images);
                                mAdapter.notifyDataSetChanged();
                            }
                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_report)));
                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                            Toast.makeText(CreateReportActivity.this,array.getJSONObject(0).getString("MessageContent").toString(),
                                    Toast.LENGTH_SHORT).show();
                            finishActivity();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }));



                break;

        }
    }
    private String getPhotoFileName(int i)
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        return dateFormat.format(date) +i+"_"+ ".jpg";
    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageSelectorActivity.REQUEST_IMAGE:
                if(data!=null) {
                    ArrayList<String> images1 = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    if (images1 != null) {
                        for (int i = 0; i < images1.size(); i++) {
                            images.add(0,images1.get(i));
                        }
                        if(images.size()>10){

                            ToastUtils.showMessage(CreateReportActivity.this,"你最多可以选择9张");
                        }
                        if(images.size()==10){
                            images.remove(9);
                        }
                    }

                    mAdapter.setmList(images);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case Constants.ADDTTIME:
                if(data != null){
                    String time = data.getStringExtra("time");
                    if (!TextUtils.isEmpty(time)) {
                        edt_time.setText(time);
                        checktime = time;
                    }
                }
                break;
            case ADD_TIMES:
                if ( data != null) {
                    String times = data.getStringExtra("times");
                    if (!TextUtils.isEmpty(times)) {
                        edt_jzdw.setText(times);

                    }

                }

                break;

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList=null;
        edt_time.setText("");
        edt_jzdw.setText("");
        edt_jzjg.setText("");


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishActivity();


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
