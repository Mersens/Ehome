package com.zzu.ehome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.MMloveConstants;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/5/16.
 */
public class StaticWebView extends BaseActivity {
    private final String mPageName = "WebViewPage";
    private WebView mWebView;
    private Intent mIntent;
    String title;
    String ID= "";
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
        mIntent = this.getIntent();
        if (mIntent != null && mIntent.getStringExtra("ID") != null) {
            ID= mIntent.getStringExtra("ID");
            title=mIntent.getStringExtra("Title");
        }

        String url = Constants.URLIMAGE+ID;
        if(title.length()>10){
            title=title.substring(0,9)+"...";
        }




        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left,title, new HeadView.OnLeftClickListener() {
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
