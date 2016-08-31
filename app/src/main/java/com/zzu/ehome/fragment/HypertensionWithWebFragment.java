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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zzu.ehome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/29.
 */
public class HypertensionWithWebFragment extends BaseFragment {
    private View mView;
    private boolean isPrepared;
    public static final String PATH="path";
    private String path=null;
    private WebView webView;
    private ListView listView;
    private LinearLayout layout_tj;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_hypertension_web,null);
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
        initViews();
        initEvent();
        initDatas();
        //如果有异步请求，需在在异步请求结束设置isPrepared=false
       // isPrepared=false;
    }

    public void initViews(){
        webView=(WebView)mView.findViewById(R.id.webView);
        listView=(ListView)mView.findViewById(R.id.listView);
        layout_tj=(LinearLayout)mView.findViewById(R.id.layout_tj);

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
                    initListView();
                }
            }
        });
    }

    public void initDatas(){
        webView.loadUrl(path);
    }

    public void initListView(){
        layout_tj.setVisibility(View.VISIBLE);
        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<6;i++){
            mList.add(i);
        }

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_doctor,null);
                return view;
            }
        });
    }

    public  static Fragment getInstance(String path){
        Bundle b=new Bundle();
        b.putString(PATH,path);
        HypertensionWithWebFragment af= new HypertensionWithWebFragment();
        af.setArguments(b);
        return af;
    }
}
