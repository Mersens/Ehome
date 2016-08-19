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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.FreeConsultationActivity;
import com.zzu.ehome.activity.MyHome;
import com.zzu.ehome.activity.NearPharmacyActivity;
import com.zzu.ehome.activity.PMDActivity;
import com.zzu.ehome.activity.StaticWebView;
import com.zzu.ehome.activity.YuYueGuaHaoActivity;
import com.zzu.ehome.adapter.HomeNewsAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.News;
import com.zzu.ehome.bean.NewsDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.ImageCycleView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
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
public class HomeFragment1 extends BaseFragment implements View.OnClickListener {
    private static final String PACKAGE_URL_SCHEME = "package:";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };
    private View mView;
    private ImageCycleView mViewPager;
    private ListView mListView;
    private PullToRefreshLayout pulltorefreshlayout;
    private RequestMaker requestMaker;
    private int page=1;

    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
    }

    // private LinearLayout layout_health_files;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private BDLocation location;
    private TextView tvcity, tvweather, tvpm, tvcurrent;
    private String city = "郑州";
    private ImageView ivweather;
    //广告轮播 图片链接
    ArrayList<String> mImageUrl = new ArrayList<String>();
    //广告通用对象
    ArrayList<Map<String, Object>> mObject = new ArrayList<Map<String, Object>>();
    private LinearLayout llrecord;
    private LinearLayout layout_yygh, layout_free_consultation,
            layout_add, layout_fjyd, layout_srys, layout_jcbg,
            layout_tjbg, layout_xdbg;
    private LinearLayout layout_gxy, layout_xxg, layout_tnb, layout_others;
    private HomeNewsAdapter mAadapter;
    private  List<News> mList = new ArrayList<News>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home1, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        requestMaker = RequestMaker.getInstance();
        mPermissionsChecker = new PermissionsChecker(getActivity());
        initViews();
        mAadapter=new HomeNewsAdapter(getActivity());
        mListView.setAdapter(mAadapter);
        location();
        initEvent();
        initDatas();

    }

    public void initViews() {

        //layout_health_files=(LinearLayout) mView.findViewById(R.id.layout_health_files);
        mViewPager = (ImageCycleView) mView.findViewById(R.id.viewPager);
        mListView = (ListView) mView.findViewById(R.id.listView);
        tvcity = (TextView) mView.findViewById(R.id.tvcity);
        tvweather = (TextView) mView.findViewById(R.id.tvweather);
        pulltorefreshlayout = (PullToRefreshLayout) mView.findViewById(R.id.refresh_view);
        tvpm = (TextView) mView.findViewById(R.id.tvpm);
        ivweather = (ImageView) mView.findViewById(R.id.ivweather);
        tvcurrent = (TextView) mView.findViewById(R.id.tvcurrent);
        llrecord = (LinearLayout) mView.findViewById(R.id.llrecord);
        layout_yygh = (LinearLayout) mView.findViewById(R.id.layout_yygh);
        layout_free_consultation = (LinearLayout) mView.findViewById(R.id.layout_free_consultation);
        layout_add = (LinearLayout) mView.findViewById(R.id.layout_add);
        layout_fjyd = (LinearLayout) mView.findViewById(R.id.layout_fjyd);
        layout_srys = (LinearLayout) mView.findViewById(R.id.layout_srys);
        layout_jcbg = (LinearLayout) mView.findViewById(R.id.layout_jcbg);
        layout_tjbg = (LinearLayout) mView.findViewById(R.id.layout_tjbg);
        layout_xdbg = (LinearLayout) mView.findViewById(R.id.layout_xdbg);
        layout_gxy = (LinearLayout) mView.findViewById(R.id.layout_gxy);
        layout_xxg = (LinearLayout) mView.findViewById(R.id.layout_xxg);
        layout_tnb = (LinearLayout) mView.findViewById(R.id.layout_tnb);
        layout_others = (LinearLayout) mView.findViewById(R.id.layout_others);

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

    public void initEvent() {
        // layout_health_files.setOnClickListener(this);
        llrecord.setOnClickListener(this);
        layout_yygh.setOnClickListener(this);
        layout_add.setOnClickListener(this);
        layout_fjyd.setOnClickListener(this);
        layout_srys.setOnClickListener(this);
        layout_jcbg.setOnClickListener(this);
        layout_tjbg.setOnClickListener(this);
        layout_xdbg.setOnClickListener(this);
        layout_gxy.setOnClickListener(this);
        layout_xxg.setOnClickListener(this);
        layout_tnb.setOnClickListener(this);
        layout_others.setOnClickListener(this);
        layout_free_consultation.setOnClickListener(this);
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page=1;
                newsInqury();
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                newsInqury();

            }
        });

    }


    public void initDatas() {
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

//        final List<Integer> mList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            mList.add(i);
//        }
//        mListView.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return mList.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return mList.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View mItemView = LayoutInflater.from(getActivity()).inflate(R.layout.home_item, null);
//                return mItemView;
//            }
//        });
    }

    @Override
    protected void lazyLoad() {

    }

    public Fragment getInstance() {
        return new HomeFragment1();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*            case R.id.layout_health_files:
                startIntent(getActivity(), HealthFilesActivity1.class);
                break;*/
            case R.id.llrecord:
                ToastUtils.showMessage(getActivity(), "记录数据");
                break;
            case R.id.layout_yygh:
                startIntent(getActivity(), YuYueGuaHaoActivity.class);
                break;
            case R.id.layout_free_consultation:
                startIntent(getActivity(), FreeConsultationActivity.class);
                break;
            case R.id.layout_add:
                startIntent(getActivity(), MyHome.class);
                break;
            case R.id.layout_fjyd:
                startIntent(getActivity(), NearPharmacyActivity.class);

                break;
            case R.id.layout_srys:
                ToastUtils.showMessage(getActivity(), "私人医生");
                startIntent(getActivity(), PMDActivity.class);
                break;
            case R.id.layout_jcbg:
                ToastUtils.showMessage(getActivity(), "检查报告");
                break;
            case R.id.layout_tjbg:
                ToastUtils.showMessage(getActivity(), "体检报告");
                break;
            case R.id.layout_xdbg:
                ToastUtils.showMessage(getActivity(), "心电报告");
                break;
            case R.id.layout_gxy:
                ToastUtils.showMessage(getActivity(), "高血压");
                break;
            case R.id.layout_xxg:
                ToastUtils.showMessage(getActivity(), "心血管");
                break;
            case R.id.layout_tnb:
                ToastUtils.showMessage(getActivity(), "糖尿病");
                break;
            case R.id.layout_others:
                ToastUtils.showMessage(getActivity(), "其他慢病");
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
            if (location == null) {
                city = "郑州市";

            } else {
                city = location.getCity();
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

                        String weather = array.getJSONObject(0).getString("weather");
                        tvweather.setText(weather);
                        tvpm.setText(array.getJSONObject(0).getString("pm"));
                        tvcurrent.setText(array.getJSONObject(0).getString("currenttemperature"));
                        if (weather.contains("转")) {

                            set(ivweather, weather.substring(0, weather.indexOf("转")));
                        } else {
                            set(ivweather, weather);
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

    private void set(ImageView iv, String str) {
        if (str.contains("雨")) {
            if (str.contains("小雨") || str.contains("阵雨")) {
                iv.setBackgroundResource(R.mipmap.icon_rain_s);
            } else if (str.contains("中雨")) {
                iv.setBackgroundResource(R.mipmap.icon_rain_m);
            } else {
                iv.setBackgroundResource(R.mipmap.icon_rain_b);
            }
        } else if (str.contains("雪")) {
            iv.setBackgroundResource(R.mipmap.icon_snow);
        } else if (str.contains("云")) {
            iv.setBackgroundResource(R.mipmap.icon_cloudy);
        } else if (str.contains("阴")) {
            iv.setBackgroundResource(R.mipmap.icon_overcast);
        } else {
            iv.setBackgroundResource(R.mipmap.icon_sunshine);
        }

    }
    private  void newsInqury(){
        requestMaker.NewsInquiry(10+"",page+"",new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("NewsInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        NewsDate date = JsonTools.getData(result.toString(), NewsDate.class);
                        List<News> list = date.getData();
                        for(News n:list){
                            mList.add(n);
                        }
                        mAadapter.setList(mList);
                        mAadapter.notifyDataSetChanged();

                    }
                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }.sendEmptyMessageDelayed(0, 3000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));
    }


}
