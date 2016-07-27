package com.zzu.ehome.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
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
import com.zzu.ehome.bean.Image;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.bean.TreatmentInquirywWithPageDate;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
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
import com.zzu.ehome.view.util.FileUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.Manifest;
import de.greenrobot.event.EventBus;


/**
 * Created by zzu on 2016/4/12.
 */
public class CreateillnessActivity extends BaseActivity implements View.OnClickListener {
    private static final int TAKE_PICTURE = 0x000001;
    public static final int ADD_TIME = 0x11;
    public static final int ADD_TIMES = 0x35;

    private TextView edt_time;
    private TextView edt_jzdw;

    private static final String PACKAGE_URL_SCHEME = "package:";

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
    private String userid,jzyy,zdjg,yyjy;
    public static final String EXTRA_IMAGES = "extraImages";
    private RecyclerView resultRecyclerView;
    private ImageView singleImageView;
    private RelativeLayout rlpic;

    private ArrayList<String> images;
    private List<Images> mList=new ArrayList<Images>();
    private final String mPageName = "CreateillnessActivity";
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
        images = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_IMAGES);
        setContentView(R.layout.activity_illness);

        initViews();

        images=new ArrayList<String>();
        mAdapter=new GridAdapter(images,CreateillnessActivity.this);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        resultRecyclerView.setAdapter(mAdapter);
        userid= SharePreferenceUtil.getInstance(CreateillnessActivity.this).getUserId();
        mPermissionsChecker = new PermissionsChecker(CreateillnessActivity.this);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        initEvent();

    }


    public void initViews() {

        edt_time = (TextView) findViewById(R.id.edt_time);
        edt_jzdw = (TextView) findViewById(R.id.edt_jzdw);

        edt_jzjg = (ContainsEmojiEditText) findViewById(R.id.edt_jzjg);


        edt_yyjy = (ContainsEmojiEditText) findViewById(R.id.edt_yyjy);


        btn_save = (Button) findViewById(R.id.btn_save);
        lljzhen=(LinearLayout) findViewById(R.id.lljzhen);
        rlpic=(RelativeLayout)findViewById(R.id.rl_pic);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "新建病例", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
                Intent intentF = new Intent();
                intentF.setAction("action.DateOrFile");
                intentF.putExtra("msgContent", "File");
                sendBroadcast(intentF);
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

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        edt_time.setText(df.format(new Date()));
        lljzhen.setOnClickListener(this);
        edt_jzdw.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        rlpic.setOnClickListener(this);
        requsetHos();

    }

    private void requsetHos() {
        requestMaker.TreatmentInquirywWithPage(userid,1+"","1", new JsonAsyncTask_Info(CreateillnessActivity.this, true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentInquirywWithPage");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode"))
                    {

                    }else {

                        TreatmentInquirywWithPageDate date = JsonTools.getData(result.toString(), TreatmentInquirywWithPageDate.class);
                        List<TreatmentInquirywWithPage> list = date.getData();
                        edt_jzdw.setText(list.get(0).getHosname());

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }));
    }

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mcontext;
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
            this.mcontext=context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(CreateillnessActivity.this)
                    .load(new File(images.get(position)))
                    .centerCrop()
                    .into(holder.imageView);
            holder.imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                    rlpic.setVisibility(View.VISIBLE);
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ImageAlbumManager.class);
                    intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                    intent.putExtra("position", position);
                    mcontext.startActivity(intent);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {

                        showMissingPermissionDialog();
                        return;
                    }
                }

                ImageSelectorActivity.start(CreateillnessActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true,false,false,images.size());

                break;
            case R.id.lljzhen:
                Intent intenttime = new Intent(CreateillnessActivity.this, SelectDateAndTime.class);
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
                yyjy=edt_yyjy.getText().toString();
                checktime=edt_time.getText().toString();
                if(checktime.equals("")){
                    ToastUtils.showMessage(CreateillnessActivity.this,"请选择就诊日期!");
                    return;
                }else if(jzyy.equals("")){
                    ToastUtils.showMessage(CreateillnessActivity.this,"请填写就诊医院!");
                    return;
                }else if(zdjg.equals("")){
                    ToastUtils.showMessage(CreateillnessActivity.this,"请填写就诊结果!");
                    return;
                }else if(yyjy.equals("")){
                    ToastUtils.showMessage(CreateillnessActivity.this,"请填写就用药建议!");
                    return;
                }
//                else if(!IOUtils.stringForEmoji(zdjg)){
//                    ToastUtils.showMessage(CreateillnessActivity.this,"只允许输入中英文!");
//                    return;
//
//                }else if(!IOUtils.stringForEmoji(yyjy)){
//                    ToastUtils.showMessage(CreateillnessActivity.this,"只允许输入中英文!");
//                    return;
//                }
                startProgressDialog();
//                btn_save.setEnabled(false);
                if(images!=null&&images.size()>0){
                    if(images.size()>9){
                        ToastUtils.showMessage(CreateillnessActivity.this,"你最多可以选择9张");
                        return;
                    }

                    for(int i=0;i<images.size();i++) {
                        Images imag=new Images();
                        Bitmap newBitmap = BitmapFactory.decodeFile(ImageUtil.doPicture(images.get(i)));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        newBitmap.compress(Bitmap.CompressFormat.JPEG,
                                100, baos);
                        imag.setCode(ImageUtil.Bitmap2StrByBase64(newBitmap));
                        imag.setFileName(getPhotoFileName(i));
                        mList.add(imag);
                        newBitmap.recycle();
                    }



                }
                zdjg=zdjg.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;");
                yyjy=yyjy.replace("<","&lt;").replace(">","&gt;").replace("&","&amp;");

                requestMaker.OtherTreatmentInsert(userid,checktime,jzyy,zdjg,yyjy,mList,new JsonAsyncTask_Info(CreateillnessActivity.this, true,new JsonAsyncTaskOnComplete(){
                    @Override
                    public void processJsonObject(Object result) {
                        try {
                            stopProgressDialog();
                            String value=result.toString();
                            JSONObject mySO = (JSONObject) result;
                            org.json.JSONArray array = mySO
                                    .getJSONArray("OtherTreatmentInsert");
                            edt_time.setText("");
                            edt_jzdw.setText("");
                            edt_jzjg.setText("");
                            edt_yyjy.setText("");
                            if(mList.size()>0){
                                images.clear();
                                mAdapter.setmList(images);
                                mAdapter.notifyDataSetChanged();
                            }
//                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_manager_file)));


                            Toast.makeText(CreateillnessActivity.this,array.getJSONObject(0).getString("MessageContent").toString(),
                                    Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new RefreshEvent(getResources().getInteger(R.integer.refresh_info)));
                            finishActivity();

                            Intent intentF = new Intent();
                            intentF.setAction("action.DateOrFile");
                            intentF.putExtra("msgContent", "File");
                            sendBroadcast(intentF);

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
                            images.add(images1.get(i));
                        }
                        if(images.size()>9){
                            ToastUtils.showMessage(CreateillnessActivity.this,"你最多可以选择9张");
                        }
                        if(images.size()==9){
                            rlpic.setVisibility(View.GONE);
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
        edt_yyjy.setText("");

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishActivity();
            Intent intentD = new Intent();
            intentD.setAction("action.DateOrFile");
            intentD.putExtra("msgContent", "File");
            sendBroadcast(intentD);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(this, "请点击设置，打开所需存储权限",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startAppSettings();

            }
        });

        dialog.show();
        dialog = null;

    }
    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
