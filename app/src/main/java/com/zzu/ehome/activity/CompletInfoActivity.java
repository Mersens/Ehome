package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.bean.UserInfoDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.ImageTools;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.CircleImageView;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PicPopupWindows;
import com.zzu.ehome.view.crop.Crop;
import com.zzu.ehome.view.crop.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zzu on 2016/4/8.
 */
public class CompletInfoActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_add_head;
    private EditText edt_setting_name;
    private Button btn_complate;
    private CircleImageView iv_head;

    private RequestMaker requestMaker;
    String userid;
    private PicPopupWindows picPop;
    private EHomeDao dao;
    private Intent mIntetnt;
    private ImageLoader mImageLoader;
    private Boolean isHead=false;
    private static final int SCALE = 5;// 照片缩小比例
    private byte[] mContent;
    private final String mPageName = "CompletInfoActivity";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        setContentView(R.layout.layout_completinfo);
        mImageLoader = ImageLoader.getInstance();
        mIntetnt=this.getIntent();
        userid=SharePreferenceUtil.getInstance(CompletInfoActivity.this).getUserId();
        initViews();
        initEvent();
        if(mIntetnt!=null){
            if(mIntetnt.getStringExtra("username")!=null){
                edt_setting_name.setText(mIntetnt.getStringExtra("username"));
            }
            if(mIntetnt.getStringExtra("imgHead")!=null){
                isHead=true;
                mImageLoader.displayImage(
                        mIntetnt.getStringExtra("imgHead"), iv_head);
            }
        }
    }

    public void initViews(){
        iv_head=(CircleImageView) findViewById(R.id.iv_head);
        tv_add_head=(TextView) findViewById(R.id.tv_add_head);
        edt_setting_name=(EditText) findViewById(R.id.edt_name);
        btn_complate=(Button) findViewById(R.id.btn_complate);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "完善信息", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(CompletInfoActivity.this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(CompletInfoActivity.this);
    }


    public void initEvent(){
        tv_add_head.setOnClickListener(this);
        btn_complate.setOnClickListener(this);

        iv_head.setOnClickListener(this);
        dao = new EHomeDaoImpl(this);

    }

    /**
     *添加头像
     */
    private void  doAddHeadImage(){
        InputMethodManager imm = (InputMethodManager)getSystemService(CompletInfoActivity.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_setting_name.getWindowToken(),0);
        picPop=new PicPopupWindows(CompletInfoActivity.this,iv_head,this);

    }

    /**
     * 完成
     */
    private void doComplate(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_head:
                doAddHeadImage();
                break;
            case R.id.btn_complate:
                if(edt_setting_name.getText().toString().trim().equals("")){
                    ToastUtils.showMessage(CompletInfoActivity.this,"姓名不允许为空");
                    return;
                }else if(!isHead){
                    ToastUtils.showMessage(CompletInfoActivity.this,"请上传头像");
                    return;
                }else if(edt_setting_name.getText().toString().trim().length()>4){
                    ToastUtils.showMessage(CompletInfoActivity.this,"姓名长度过长");
                    return;
                }
                else if(!IOUtils.isName(edt_setting_name.getText().toString().trim())){
                    ToastUtils.showMessage(CompletInfoActivity.this,"姓名需要输入汉字");
                    return;
                }
                else {
                    doInfoChange();
                }
                break;

        }
    }

    private void doInfoChange() {
        startProgressDialog();
       requestMaker.userInfo(userid,edt_setting_name.getText().toString().trim(),new JsonAsyncTask_Info(CompletInfoActivity.this, true, new JsonAsyncTaskOnComplete() {
           @Override
           public void processJsonObject(Object result) {
               String returnvalue = result.toString();
               try {
                   JSONObject mySO = (JSONObject) result;
                   JSONArray array = mySO.getJSONArray("UserInfoChange");

                   if (array.getJSONObject(0).getString("MessageCode")
                           .equals("0")){
                       doInquery();

                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }));
    }

    private void doInquery() {
        stopProgressDialog();
        requestMaker.UserInquiry(userid,new JsonAsyncTask_Info(CompletInfoActivity.this, true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {
                UserInfoDate date = JsonTools.getData(result.toString(), UserInfoDate.class);
                List<User> list = date.getData();

                String imgHead = list.get(0).getImgHead();
                if(imgHead!=null) {
                    if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                        imgHead="";
                    } else {
                        imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");

                    }
                }else{
                    imgHead="";
                }

                User dbUser=dao.findUserInfoById(userid);
                dbUser.setImgHead(imgHead);
                dbUser.setUsername(edt_setting_name.getText().toString().trim());
                dao.updateUserInfo(dbUser,userid);
                CustomApplcation.getInstance().finishSingleActivityByClass(LoginActivity.class);
                CustomApplcation.getInstance().finishSingleActivityByClass(RegisterActivity.class);
                CommonUtils.intentAction(CompletInfoActivity.this, MainActivity.class);

//                {
//                    "UserInquiry": [
//                    {
//                        "UserID": "51064",
//                            "RealName": "bbbbb",
//                            "UserNO": "",
//                            "UserSex": "02",
//                            "NickName": "",
//                            "PictureURL": "~/UploadFile/image/2016041211194951064.jpg",
//                            "City": "",
//                            "ClientID": "null",
//                            "UserAge": "0",
//                            "Point": ""
//                    }
//                    ]
//                }

            }

        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result){
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SHOW_ALL_PICTURE) {
//               beginCrop(result.getData());
                try {
                    ContentResolver resolver = getContentResolver();
                    // 照片的原始资源地址
                    Uri originalUri = result.getData();
                    long mid = ImageUtil.getAutoFileOrFilesSize(ImageUtil.getImageAbsolutePath(CompletInfoActivity.this, originalUri));
                    if (mid > 6 * 1024 * 1024) {
                        Toast.makeText(CompletInfoActivity.this, "图片尺寸过大", 1).show();
                        return;
                    }
                    Bitmap photo  = BitmapFactory.decodeFile(ImageUtil.doPicture(ImageUtil.getImageAbsolutePath(CompletInfoActivity.this, originalUri)));

                    ExifInterface exif = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        exif = new ExifInterface(IOUtils.getPath(CompletInfoActivity.this, originalUri));
                    } else {
                        exif = new ExifInterface(
                                getAbsoluteImagePath(originalUri));
                    }

                    int degree = ImageUtil.getDegree(exif);
                    String path = ImageUtil.doPicture(ImageUtil.getUriString(result.getData(), getContentResolver()));
                    startProgressDialog();
                    if (photo != null) {


                        if (degree != 0) {
                            photo = ImageUtil.rotateImage(photo, degree);
                        }
                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG,
                                100, baos2);
                        mContent = baos2.toByteArray();
                        String uploadBuffer1 = new String(
                                Base64.encode(mContent));
                        UploadPicture(uploadBuffer1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (requestCode == Constants.REQUEST_CODE_CAPTURE_CAMEIA) {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment
                        .getExternalStorageDirectory() + "/image.jpg");
                Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
                        bitmap.getWidth() / SCALE, bitmap.getHeight()
                                / SCALE);
                // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                bitmap.recycle();

                String url = Environment.getExternalStorageDirectory() + "/image.jpg";

                ExifInterface exif2 = null;
                try
                {
                    exif2 = new ExifInterface(url);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                int degree2 = ImageUtil.getDegree(exif2);
                if(degree2!=0)
                {
                    newBitmap = ImageUtil.rotateImage(newBitmap,degree2);
                }

                // 将处理过的图片显示在界面上，并保存到本地
                //iv_image.setImageBitmap(newBitmap);
                ImageTools.savePhotoToSDCard(newBitmap, Environment
                                .getExternalStorageDirectory().getAbsolutePath(),
                        String.valueOf(System.currentTimeMillis()));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG,
                        100, baos);
                mContent = baos.toByteArray();

                String uploadBuffer = new String(
                        Base64.encode(mContent));

                String uploadBuffer2 = new String(
                        Base64.encode(mContent));
                UploadPicture(uploadBuffer2);


            }
            else if (requestCode == Constants.CROP) {

                ContentResolver resolver2 = getContentResolver();
                Uri uri = null;

                if (result != null)
                {
                    uri = result.getData();
                    System.out.println("Data");
                }
                else
                {
                    System.out.println("File");
                    String fileName = getSharedPreferences("temp",
                            Context.MODE_WORLD_WRITEABLE).getString(
                            "tempName", "");
                    uri = Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), fileName));
                }


                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
                {
                    cropImageUriAfterKikat(uri, 500, 500, Constants.CROP_PICTURE);
                }
                else
                {
                    cropImageUri(uri, 500, 500, Constants.CROP_PICTURE);
                }
            }
            else if (requestCode == Constants.CROP_PICTURE) {

                Bitmap photo = null;
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
                {
                    //Uri photoUri = data.getData();
                    //Bundle extra = data.getExtras();

                    photo = decodeUriAsBitmap(Uri.fromFile(new File(Constants.IMGPATH, Constants.TMP_IMAGE_FILE_NAME)));
                    //photo = decodeUriAsBitmap(photoUri);//decode bitmap
                    //photo = data.getParcelableExtra("data");
                    //photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));


                    //photo = (Bitmap) extra.get("data");

                    if (photo != null)
                    {
                        //photoUri = (Uri)extra.get("data");
                        //photo = BitmapFactory.decodeFile(photoUri.getPath());
                        //photo = (Bitmap) extra.get("data");
//							updateUserPortrait(photo);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100,
                                stream);

                        mContent = stream.toByteArray();

                        String uploadBuffer2 = new String(
                                Base64.encode(mContent));

                        UploadPicture(uploadBuffer2);
                        System.gc();
                    }

                }
                else
                {
                    if (result == null) {
                        return;
                    }//剪裁后的图片
                    Bundle extras = result.getExtras();
                    if (extras == null) {
                        return;
                    }
                    photo = extras.getParcelable("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100,
                            stream);
//						updateUserPortrait(photo);
                    mContent = stream.toByteArray();

                    String uploadBuffer2 = new String(
                            Base64.encode(mContent));

                    UploadPicture(uploadBuffer2);
                    System.gc();
                }
            }
        }



    }
    private Bitmap decodeUriAsBitmap(Uri uri)
    {
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    private void cropImageUriAfterKikat(Uri uri, int outputX, int outputY,
                                        int requestCode)
    {

        String url=ImageUtil.getPath(CompletInfoActivity.this,uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false); // 返回数据bitmap

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Constants.IMGPATH, Constants.TMP_IMAGE_FILE_NAME)));

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }
    private void cropImageUri(Uri uri, int outputX, int outputY,
                              int requestCode) {


        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);

    }
    protected String getAbsoluteImagePath(Uri uri)
    {
        // can post image
        String[] proj =
                { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void beginCrop(Uri source) {

        String fileName = "Temp_" + String.valueOf( System.currentTimeMillis());
        File cropFile = new File( picPop.getmTempDir(), fileName);
        Uri outputUri = Uri.fromFile( cropFile);
        //true 圆形裁剪 false 方形裁剪
        new Crop( source).output( outputUri).setCropType(false).start( this);
    }
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            System.out.println(" handleCrop: Crop.getOutput(result) "+Crop.getOutput(result));
            Bitmap newBitmap=BitmapFactory.decodeFile(ImageUtil.doPicture(ImageUtil.getImageAbsolutePath(this,Crop.getOutput(result))));

            String uploadBuffer = ImageUtil.Bitmap2StrByBase64(newBitmap);
            UploadPicture(uploadBuffer);



        } else if (resultCode == Crop.RESULT_ERROR) {

        }
    }

    private Bitmap getCircleBitmap(Uri uri) {
        Bitmap src =  BitmapFactory.decodeFile( uri.getPath());
        Bitmap output = Bitmap.createBitmap( src.getWidth(), src.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas( output);

        Paint paint = new Paint();
        Rect rect = new Rect( 0, 0, src.getWidth(), src.getHeight());

        paint.setAntiAlias( true);
        paint.setFilterBitmap( true);
        paint.setDither( true);
        canvas.drawARGB( 0, 0, 0, 0);
        canvas.drawCircle( src.getWidth() / 2, src.getWidth() / 2, src.getWidth() / 2, paint);
        paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap( src, rect, rect, paint);
        return output;
    }


    /**
     * 用当前时间给取得的图片命名
     *
     */
    private String getPhotoFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss"+userid);
        return dateFormat.format(date) + ".jpg";
    }
    private void UploadPicture(String uploadBuffer){

        requestMaker.uploadPic(uploadBuffer,userid,getPhotoFileName(),new JsonAsyncTask_Info(CompletInfoActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("UploadUserPhoto");


                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")){

                        doInqueryHead();
                    }else{
                        ToastUtils.showMessage(CompletInfoActivity.this,"头像上传失败，请重试！");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }));
    }

    private void doInqueryHead() {
       startProgressDialog();
        requestMaker.UserInquiry(userid, new JsonAsyncTask_Info(CompletInfoActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                UserInfoDate date = JsonTools.getData(result.toString(), UserInfoDate.class);
                List<User> list = date.getData();
                User user=list.get(0);
                String imgHead =user .getImgHead();
                stopProgressDialog();
                if (imgHead != null) {
                    if (imgHead.equals("") || imgHead.contains("vine.gif")) {
                        imgHead = "";
                    } else {
                        imgHead = Constants.JE_BASE_URL3 + imgHead.replace("~", "").replace("\\", "/");
                    }
                } else {
                    imgHead = "";
                }
                User userOld=dao.findUserInfoById(userid);
                userOld.setImgHead(imgHead);
                dao.updateUserInfo(userOld, userid);
                mImageLoader.displayImage(
                        imgHead, iv_head);
                isHead=true;



            }

        }));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
