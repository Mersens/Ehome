package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.CustomProgressDialog;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/6/17.
 */
public abstract class BaseLazyFragmet extends Fragment {
    private HeadView mHeadView;
    private CustomProgressDialog progressDialog = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTranslucentStatus();
    }
//暂时不启用
//    private void setTranslucentStatus() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = getActivity().getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            winParams.flags |= bits;
//            win.setAttributes(winParams);
//        }
//        SystemStatusManager tintManager = new SystemStatusManager(getActivity());
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.drawable.bg_top);// 状态栏无背景
//    }

    /**
     * @author Mersens
     * setDefaultViewMethod--默认显示左侧按钮，标题和右侧按钮
     * @param leftsrcid
     * @param title
     * @param rightsrcid
     * @param onleftclicklistener
     * @param onrightclicklistener
     */
    public void setDefaultViewMethod(View v, int leftsrcid, String title, int rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT);
        mHeadView.setDefaultViewMethod(leftsrcid,title,rightsrcid,onleftclicklistener,onrightclicklistener);
    }
    public void setDefaultTXViewMethod(View v,int leftsrcid, String title, String rightsrcid, HeadView.OnLeftClickListener onleftclicklistener, HeadView.OnRightClickListener onrightclicklistener) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.DEFAULT_TX);
        mHeadView.setDefaultTXViewMethod(leftsrcid,title,rightsrcid,onleftclicklistener,onrightclicklistener);
    }
    /**
     * @author Mersens
     * setRightAndTitleMethod--显示右侧按钮和标题
     * @param title
     * @param rightsrcid
     * @param onRightClickListener
     */
    public void setRightAndTitleMethod(View v,String title,int rightsrcid, HeadView.OnRightClickListener onRightClickListener){
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.RIGHTANDTITLE);
        mHeadView.setRightAndTitleMethod(title, rightsrcid, onRightClickListener);
    }


    /**
     * @author Mersens
     * setLeftWithTitleViewMethod--显示左侧按钮和标题
     * @param leftsrcid
     * @param title
     * @param onleftclicklistener
     */
    public void setLeftWithTitleViewMethod(View v,int leftsrcid,String title, HeadView.OnLeftClickListener onleftclicklistener){
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFTANDTITLE);
        mHeadView.setLeftWithTitleViewMethod( leftsrcid, title,  onleftclicklistener);
    }

    /**
     * @author Mersens
     * setOnlyTileViewMethod--只显示标题
     * @param title
     */
    public void setOnlyTileViewMethod(View v,String title) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.ONLYTITLE);
        mHeadView.setOnlyTileViewMethod(title);
    }

    /**
     * @author Mersens
     * setLeftViewMethod--只显示左侧按钮
     * @param leftsrcid
     * @param onleftclicklistener
     */
    public void setLeftViewMethod(View v,int leftsrcid,HeadView.OnLeftClickListener onleftclicklistener) {
        mHeadView=(HeadView) v.findViewById(R.id.common_actionbar);
        mHeadView.init(HeadView.HeaderStyle.LEFT);
        mHeadView.setLeftViewMethod( leftsrcid, onleftclicklistener);
    }


    public  void setHeadViewBackground(int resid){
        if(resid!=0 && mHeadView!=null){
            mHeadView.setHeadViewBackground(resid);
        }
    }

    /**
     *
     * @Title: setHeadViewTitleColor
     * @Description:设置HeadView的标题颜色
     * @author Mersens
     * @param resid
     * @throws
     */
    public void setTitleColor(int resid){
        if(resid!=0 && mHeadView!=null){
            mHeadView.setHeadViewTitleColor(resid);
        }

    }
    public void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(getActivity());
            //progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
    }

    public void stopProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {//frahment从不可见到完全可见的时候，会调用该方法
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
            onInvisible();
        }
    }

    protected abstract void lazyLoad();//懒加载的方法,在这个方法里面我们为Fragment的各个组件去添加数据

    protected void onVisible(){
        lazyLoad();
        ToastUtils.showMessage(getActivity(),"vvvvvvvvvvvvvvvvbbbbbbbb");
    }

    protected void onInvisible(){

    }

}
