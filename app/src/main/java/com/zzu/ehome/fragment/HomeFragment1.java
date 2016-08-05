package com.zzu.ehome.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.HealthFilesActivity1;
import com.zzu.ehome.activity.StaticWebView;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.ImageCycleView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mersens on 2016/7/26.
 */
public class HomeFragment1 extends BaseFragment implements View.OnClickListener{
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
    };
    private View mView;
    private ImageCycleView mViewPager;
    private ListView mListView;
    private PullToRefreshLayout pulltorefreshlayout;
    private RequestMaker requestMaker;
    private LinearLayout layout_health_files;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private BDLocation location;
    private TextView tvcity,tvweather,tvpm,tvcurrent;
    private String city="郑州";
    private ImageView ivweather;
    //广告轮播 图片链接
    ArrayList<String> mImageUrl = new ArrayList<String>();
    //广告通用对象
    ArrayList<Map<String, Object>> mObject = new ArrayList<Map<String, Object>>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home1,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        requestMaker=RequestMaker.getInstance();
        mPermissionsChecker = new PermissionsChecker(getActivity());
        initViews();
        location();
        initEvent();
        initDatas();



    }

    public void initViews(){

        layout_health_files=(LinearLayout) mView.findViewById(R.id.layout_health_files);
        mViewPager=(ImageCycleView) mView.findViewById(R.id.viewPager);
        mListView=(ListView) mView.findViewById(R.id.listView);
        tvcity=(TextView)mView.findViewById(R.id.tvcity);
        tvweather=(TextView)mView.findViewById(R.id.tvweather);
        pulltorefreshlayout=(PullToRefreshLayout)mView.findViewById(R.id.refresh_view);
        tvpm=(TextView)mView.findViewById(R.id.tvpm);
        ivweather=(ImageView)mView.findViewById(R.id.ivweather);
        tvcurrent=(TextView)mView.findViewById(R.id.tvcurrent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位

    }
    private void showADs() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());// new Date()为获取当前系统时间


        requestMaker.searchAds(date, new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                JSONObject mySO = (JSONObject) result;
                Map<String, Object> map;
                try {
                    JSONArray array = mySO.getJSONArray("ADInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        // Toast.makeText(Index.this, "数据为空", 1).show();
                        mViewPager.setVisibility(View.GONE);
                    } else {
                        if (mImageUrl != null && mImageUrl.size() > 0) {
                            mImageUrl.clear();
                        }
                        if (mObject != null && mObject.size() > 0) {
                            mObject.clear();
                        }
                        for (int i = 0; i < array.length(); i++) {

                            map = new HashMap<String, Object>();

                            map.put("ImageUr",
                                    (Constants.EhomeURL + array
                                            .getJSONObject(i).getString(
                                                    "ImageUrl")).toString()
                                            .replace("~", "")
                                            .replace("\\", "/"));
                            map.put("ID",
                                    array.getJSONObject(i).getString("ID"));
                            map.put("Title",
                                    array.getJSONObject(i).getString("Title"));

                            mImageUrl
                                    .add((Constants.EhomeURL + array
                                            .getJSONObject(i).getString(
                                                    "ImageUrl")).toString()
                                            .replace("~", "")
                                            .replace("\\", "/"));
                            mObject.add(map);
                            mViewPager.setVisibility(View.VISIBLE);
                            mViewPager.setImageResources(mImageUrl, mObject, new ImageCycleView.ImageCycleViewListener() {
                                @Override
                                public void displayImage(String imageURL,
                                                         ImageView imageView) {
                                    try {
                                        ImageLoader.getInstance().displayImage(imageURL,
                                                imageView);
                                        ViewGroup.LayoutParams para;
                                        para = imageView.getLayoutParams();
                                        para.width = ScreenUtils.getScreenWidth(getActivity());
                                        para.height = para.width * 2 / 5;
                                        imageView.setLayoutParams(para);
                                    } catch (OutOfMemoryError e) {
                                        // TODO Auto-generated catch block

                                    }

                                }

                                @Override
                                public void onImageClick(int position, View imageView,
                                                         ArrayList<Map<String, Object>> mObject) {
                                    String ID = mObject.get(position).get("ID")
                                            .toString();
                                    String title = mObject.get(position).get("Title")
                                            .toString();
                                    Intent i = new Intent(getActivity(),
                                            StaticWebView.class);
                                    i.putExtra("ID", ID);
                                    i.putExtra("Title", title);
                                    startActivity(i);

                                }

                            });

                        }

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                showMissingPermissionDialog();
                return;
            }
        }
    }

    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(getActivity(), "请点击设置，打开所需位置权限",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startAppSettings();

            }
        });

        dialog.show();
        dialog = null;

    }
    public void initEvent(){
        layout_health_files.setOnClickListener(this);

        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });

    }



    public void initDatas(){
//        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return ImageFragment.getInstance();
//            }
//
//            @Override
//            public int getCount() {
//                return 3;
//            }
//        });
        showADs();
        final List<Integer> mList=new ArrayList<>();
        for(int i=0;i<10;i++){
            mList.add(i);
        }
        mListView.setAdapter(new BaseAdapter() {
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
                View mItemView=LayoutInflater.from(getActivity()).inflate(R.layout.home_item,null);
                return mItemView;
            }
        });
    }
    @Override
    protected void lazyLoad() {

    }

    public Fragment getInstance(){
        return new HomeFragment1();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_health_files:
                startIntent(getActivity(),HealthFilesActivity1.class);
                break;
        }

    }

    public <T> void startIntent(Activity context, Class<T> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }
    private void location() {
        mLocationClient = new LocationClient(getActivity());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    // 初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(2000);// 设置发起定位请求的间隔时间为3000ms
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置


//            ToastUtils.showMessage(getActivity(),location.getCity()+"mmmmm");
            if(location==null){
                city="郑州市";

            }else{
                city=location.getCity();
            }
            tvcity.setText(city);
            requestMaker.WeatherInquiry(city, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
                @Override
                public void processJsonObject(Object result) {
                    try {

                        JSONObject mySO = (JSONObject) result;
//                        ToastUtils.showMessage(getActivity(),result.toString());
                        org.json.JSONArray array = mySO
                                .getJSONArray("WeatherInquiry");

                        String weather=array.getJSONObject(0).getString("weather");
                        tvweather.setText(weather);
                        tvpm.setText(array.getJSONObject(0).getString("pm"));
                        tvcurrent.setText(array.getJSONObject(0).getString("currenttemperature"));
                        if(weather.contains("转")){

                            set(ivweather,weather.substring(0,weather.indexOf("转")));
                        }else{
                            set(ivweather,weather);
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));


        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getActivity().getPackageName()));
        startActivity(intent);
    }
    private void set(ImageView iv,String str){
        if(str.contains("雨")) {
            if (str.contains("小雨")||str.contains("阵雨")) {
                iv.setBackgroundResource(R.mipmap.icon_rain_s);
            } else if (str.contains("中雨")) {
                iv.setBackgroundResource(R.mipmap.icon_rain_m);
            } else {
                iv.setBackgroundResource(R.mipmap.icon_rain_b);
            }
        }else if(str.contains("雪")){
            iv.setBackgroundResource(R.mipmap.icon_snow);
        }else if(str.contains("云")){
            iv.setBackgroundResource(R.mipmap.icon_cloudy);
        }else if(str.contains("阴")){
            iv.setBackgroundResource(R.mipmap.icon_overcast);
        }else{
            iv.setBackgroundResource(R.mipmap.icon_sunshine);
        }

    }




}
