package com.zzu.ehome.view;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.ShareAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.ShareModel;
import com.zzu.ehome.utils.ToastUtils;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SharePopupWindow extends PopupWindow {

    private Context mcontext;

    private ShareParams shareParams;

//    Button btn_cancel;

    public SharePopupWindow(Context cx) {
        this.mcontext = cx;

    }





    public void showShareWindow() {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.share_layout,null);
        GridView gridView = (GridView) view.findViewById(R.id.share_gridview);
        ShareAdapter adapter = new ShareAdapter(mcontext);
        gridView.setAdapter(adapter);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明

        // 设置SelectPicPopupWindow弹出窗体的背景ffb0c9
        ColorDrawable dw = new ColorDrawable(0xffeeeeee);

        this.setBackgroundDrawable(dw);


        gridView.setOnItemClickListener(new ShareItemClickListener(this));

    }

    private class ShareItemClickListener implements OnItemClickListener {
        private PopupWindow pop;

        public ShareItemClickListener(PopupWindow pop) {
            this.pop = pop;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            share(position);
            pop.dismiss();

        }
    }
    public  void showShare(final Context context,
                                 String platform, boolean isDialog) {
        final OnekeyShare oks = new OnekeyShare();
        if (platform != null) {
            oks.setPlatform(platform);
        }

        if (isDialog) {
            // 令编辑页面显示为Dialog模式
            oks.setDialogMode();
        }


        // 关闭sso授权

        oks.disableSSOWhenAuthorize();
        if(platform.equals(Wechat.NAME)){

            oks.setTitle(shareParams.getTitle());
        }else if(platform.equals(WechatMoments.NAME)){
            oks.setTitle(shareParams.getText());
        }
        else {

            oks.setTitle(shareParams.getTitle());  //最多30个字符
        }
        // text是分享文本：所有平台都需要这个字段
        oks.setText(shareParams.getText());
        oks.setImagePath(shareParams.getImagePath());//网络图片rul
        oks.setUrl(shareParams.getUrl());   //网友点进链接后，可以看到分享的详情
        oks.setSite(shareParams.getTitle());
        oks.setSiteUrl(shareParams.getUrl());
        oks.setComment(shareParams.getText());

        // Url：仅在QQ空间使用
        oks.setTitleUrl(shareParams.getUrl());  //网友点进链接后，可以看到分享的详情



        // 启动分享GUI
        oks.show(context);

    }

    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {


        switch (position){
            case 0:
                showShare(mcontext, Wechat.NAME,true);


                break;
            case 1:
                showShare(mcontext, WechatMoments.NAME,true);


                break;
            case 2:
                if(isQQClientAvailable(mcontext)){
                    showShare(mcontext, QQ.NAME,true);}
                else{
                    ToastUtils.showMessage(mcontext,"请安装qq");
                }
                break;
            case 3:
                if(isQQClientAvailable(mcontext)){
                    showShare(mcontext,QZone.NAME,true);
                }
                else{

                    ToastUtils.showMessage(mcontext,"请安装qq");
                }

                break;





        }
    }



    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

//    /**
//     * sina 分享
//     */
//    private void showSina() {
//        mcontext.startActivity(new Intent(mcontext,FakeSinaActivity.class));
//
//    }

    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            ShareParams sp = new ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setTitle(shareModel.getTitle());
            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImagePath(shareModel.getImgPath());
            shareParams = sp;
        }
    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = QQ.NAME;

                break;
            case 1:
                platform = QZone.NAME;
                break;
            case 2:
                platform = Wechat.NAME;
                break;
            case 3:
                platform = WechatMoments.NAME;
                break;

        }
        return platform;
    }




}
