package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.view.crop.Crop;

import java.io.File;

/**
 * Created by Administrator on 2016/4/19.
 */
public class UploadActivity extends PopupWindow implements View.OnClickListener {
    private Activity activity;
    private String mCurrentPhotoPath;
    public File getmTempDir() {
        return mTempDir;
    }

    public void setmTempDir(File mTempDir) {
        this.mTempDir = mTempDir;
    }
    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    private File mTempDir;
    public UploadActivity(Context mContext, View parent, Activity activity) {

        super(mContext);
        this.activity=activity;
        View view = View
                .inflate(mContext, R.layout.item_popubwindows, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));

        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
        mTempDir = new File( Environment.getExternalStorageDirectory(),"Temp");
        if(!mTempDir.exists()){
            mTempDir.mkdirs();
        }

        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);

        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.item_popupwindows_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = "Temp_camera" + String.valueOf( System.currentTimeMillis());
                File cropFile = new File( mTempDir, fileName);
                Uri fileUri = Uri.fromFile(cropFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                mCurrentPhotoPath = fileUri.getPath();
                activity.startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_CAMEIA);
                dismiss();
                break;
            case R.id.item_popupwindows_Photo:

                Crop.pickImage(activity);
                dismiss();
                break;
            case R.id.item_popupwindows_cancel:
                dismiss();
                break;

        }

    }
}
