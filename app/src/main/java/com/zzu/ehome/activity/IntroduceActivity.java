package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/6/7.
 */
public class IntroduceActivity extends BaseActivity{
    private final String mPageName = "IntroduceActivity";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.testwebview_layout);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setWebViewClient(new MyWebViewClient());
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        String url = Constants.EhomeURL+"/help/about.html";

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left,"中心简介", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });

        mWebView.loadUrl(url);


    }
    class MyWebViewClient extends WebViewClient {
        // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            view.loadUrl(url);

            return true;

        }

    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}
