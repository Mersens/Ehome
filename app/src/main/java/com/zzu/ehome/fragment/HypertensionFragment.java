package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ToastUtils;

/**
 * Created by Mersens on 2016/8/29.
 */
public class HypertensionFragment extends BaseFragment {
    private View mView;
    private boolean isPrepared;
    public static final String PATH="path";
    private String path=null;
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_hypertension,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        path=getArguments().getString(PATH);
        isPrepared=true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        ToastUtils.showMessage(getActivity(),"hhhhh");
        initViews();
        initEvent();
        initDatas();
        //如果有异步请求，需在在异步请求结束设置isPrepared=false
       // isPrepared=false;
    }

    public void initViews(){

        webView=(WebView)mView.findViewById(R.id.webView);

    }

    public void initEvent(){
        WebSettings webSettings=webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    isPrepared=false;
                }
            }
        });
    }

    public void initDatas(){
        webView.loadUrl(path);
    }
    public  static Fragment getInstance(String path){
        Bundle b=new Bundle();
        b.putString(PATH,path);
        HypertensionFragment af= new HypertensionFragment();
        af.setArguments(b);
        return af;
    }
}
