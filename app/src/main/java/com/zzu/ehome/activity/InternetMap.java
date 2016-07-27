package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.zzu.ehome.R;
import com.zzu.ehome.fragment.DynamicFragment;
import com.zzu.ehome.fragment.StaticFragment;
import com.zzu.ehome.view.HeadView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by Administrator on 2016/7/25.
 */
public class InternetMap extends BaseActivity implements OnGetGeoCoderResultListener {

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;
    private Intent mIntent;
    private String search,base,title;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_geocoder);
        mIntent=this.getIntent();
        base=mIntent.getStringExtra("Base");
        search=mIntent.getStringExtra("Name");
        title=search;
        if(search.contains("苗圃卫生")) {
            search = "郑州大学第五附属医院苗圃卫生所";
        }
        initViews();
//        initEvent();


    }

    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20.0f);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(msu);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.geocode(new GeoCodeOption().city(base).address(search));


    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(InternetMap.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
//        Toast.makeText(InternetMap.this, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }


}
