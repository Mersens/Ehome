package com.zzu.ehome.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.CreateillnessActivity;
import com.zzu.ehome.activity.ECGActivity;

import com.zzu.ehome.activity.MedicalExaminationActivity;

import com.zzu.ehome.activity.SettingActivity;
import com.zzu.ehome.activity.StaticWebView;
import com.zzu.ehome.adapter.MyChartAdapter;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.CustomApplcation;
import com.zzu.ehome.bean.HealteData;
import com.zzu.ehome.bean.HealthData;
import com.zzu.ehome.bean.HealthDataRes;
import com.zzu.ehome.bean.HealthDes;
import com.zzu.ehome.bean.RefreshEvent;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.bean.StepCounterDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.main.ehome.WelcomeActivity;
import com.zzu.ehome.service.DownloadServiceForAPK;
import com.zzu.ehome.service.StepDetector;
import com.zzu.ehome.service.StepService;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DialogUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.NetUtils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.CircleImageView;
import com.zzu.ehome.view.DialogEnsureCancelView;
import com.zzu.ehome.view.ImageCycleView;
import com.zzu.ehome.view.MyViewPageAdapter;
import com.zzu.ehome.view.MyViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/3/31.
 */
public class HomeFragment extends BaseFragment implements OnClickListener {
    private View view;
    private int oldCode;
    String city;
//    private SwipeRefreshLayout swipeRefreshLayout;

    //请求单例
    private RequestMaker requestMaker;
    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 3;// 默认展示最大行数3行
    private static final int SHOW_CONTENT_NONE_STATE = 0;// 扩充
    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态
    private static int mStateMed = SHRINK_UP_STATE;//默认收起状态

    private TextView mContentText, mMedicine;// 展示文本内容
    private RelativeLayout mShowMore, more;// 展示更多
    private ImageView mImageSpread;// 展开
    private ImageView mImageShrinkUp;// 收起
    private ImageView mImageMedSpread;// 展开
    private ImageView mImageMedShrinkUp;// 收起
    private int newCode;

    private ImageCycleView mAdView;
    private LayoutInflater inflaterView;
    //血压，血糖
    private MyViewPager mBloodsugar;
    private ArrayList<View> views = new ArrayList<View>();
    private MyViewPageAdapter vpAdapter;
    String userid, ClientID;
    private LinearLayout lluserinfo;
    private Intent intent;

    //广告轮播 图片链接
    ArrayList<String> mImageUrl = new ArrayList<String>();
    //广告通用对象
    ArrayList<Map<String, Object>> mObject = new ArrayList<Map<String, Object>>();
    private EHomeDao dao;
    //头像
    private CircleImageView ivhead;
    private ImageLoader mImageLoader;
    private TextView tvdaytime, tvname, tvcreatecase, tvcreatehistory;
    private LinearLayout llrecorddate, llrecordfile, llrecordguahao;
    private RelativeLayout rlnodate, ll_check_report, ll_ECG_report;
    private TextView tvweightnum, tvxueya, tvxuetang, tvtw;
    private LinearLayout llysjy, llyyjy;
    private String versionlog;
    private RadioGroup dotLayout;
    private boolean isPrepared;
    private User user;
    private float height;
    private float weight;
    private double calories;
    private int step_length=55;
    private int minute_distance=80;
    private String timeCount;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private BDLocation location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home, null);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao = new EHomeDaoImpl(getActivity());
        mImageLoader = ImageLoader.getInstance();
        EventBus.getDefault().register(this);
        initViews();
        initEvent();

        versioninquiry();
//        getCity();
        location();
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }
    private void versioninquiry() {
        requestMaker.updaateApk(new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                Map<String, Object> map;
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("VersionInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            newCode = Integer.valueOf(array
                                    .getJSONObject(i).getString(
                                            "VersionID"));
                            int VersionFlag = Integer.valueOf(array
                                    .getJSONObject(i).getString(
                                            "VersionFlag"));
                            PackageManager manager = getActivity()
                                    .getPackageManager();
                            PackageInfo info = manager.getPackageInfo(
                                    getActivity().getPackageName(), 0);
                            String appVersion = info.versionName; // 版本名
                            oldCode = info.versionCode; // 版本号
                            if (oldCode < newCode) {
                                versionlog = array.getJSONObject(i)
                                        .getString("VersionLog")
                                        .replace("@", "\n");
                                if (VersionFlag == 1) {
                                    showForceUpdate();
                                } else {
                                    showUpdateDialog();
                                }

                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));

    }
    private void getJIbu(){
        /**
         * 查询历史数据
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        requestMaker.StepCounterInquiry(userid, sdf.format(CommonUtils.changeDate(-1).getTime() + 60 * 60 * 24 * 1000),new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInquiry");
                    if (array.getJSONObject(0).has("MessageCode"))
                    {
                        StepDetector.CURRENT_SETP=0;
                        StepBean step=new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(0);
                        step.setUserid("");
                        step.setUploadState(0);
                        dao.updateStep(step);

                    }else {

                        StepCounterDate date = JsonTools.getData(result.toString(), StepCounterDate.class);
                        List<StepCounterBean> list = date.getData();

                        StepDetector.CURRENT_SETP=Integer.valueOf( list.get(0).getTotalStep());
                        StepBean step=new StepBean();
                        step.setEndTime("");
                        step.setStartTime("");
                        step.setNum(StepDetector.CURRENT_SETP);
                        step.setUserid(userid);
                        step.setUploadState(0);
                        dao.updateStep(step);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }));
    }

    private void showForceUpdate() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_default_ensure, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setCancelable(false);
        builder.create().show();
        TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
        TextView tvtitel = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
        tvtitel.setText("检测到新版本");
        TextView tvcontent = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
        tvcontent.setText(versionlog);
        tvok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final Intent it = new Intent(getActivity(),
                        DownloadServiceForAPK.class);
                getActivity().startService(it);

            }
        });
    }


    public void initViews() {
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.actionbar_color);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                showADs();
//                showUserInfo(userid);
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                },4000);
//            }
//        });

        ll_check_report = (RelativeLayout) view.findViewById(R.id.ll_check_report);
        ll_ECG_report = (RelativeLayout) view.findViewById(R.id.ll_ECG_report);

        mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
        mContentText = (TextView) view.findViewById(R.id.text_advice);
        mShowMore = (RelativeLayout) view.findViewById(R.id.rladvicdmore);
        mImageSpread = (ImageView) mShowMore.findViewById(R.id.spread);
        mImageShrinkUp = (ImageView) mShowMore.findViewById(R.id.shrink_up);
        ivhead = (CircleImageView) view.findViewById(R.id.iv_head);
        mShowMore.setOnClickListener(this);
        ll_check_report.setOnClickListener(this);
        more = (RelativeLayout) view.findViewById(R.id.more);
        mMedicine = (TextView) view.findViewById(R.id.tv_medicine);
        mImageMedSpread = (ImageView) more.findViewById(R.id.spread);
        mImageMedShrinkUp = (ImageView) more.findViewById(R.id.shrink_up);
        mBloodsugar = (MyViewPager) view.findViewById(R.id.myView_blood);
        tvdaytime = (TextView) view.findViewById(R.id.day_time);
        tvname = (TextView) view.findViewById(R.id.user_name);
        lluserinfo = (LinearLayout) view.findViewById(R.id.ll_UserInfo);
        llrecorddate = (LinearLayout) view.findViewById(R.id.ll_record_date);
        llrecorddate.setOnClickListener(this);
        llrecordfile = (LinearLayout) view.findViewById(R.id.ll_record_file);
        llrecordfile.setOnClickListener(this);
        llrecordguahao = (LinearLayout) view.findViewById(R.id.ll_record_guahao);
        llrecordguahao.setOnClickListener(this);
        more.setOnClickListener(this);
        tvcreatecase = (TextView) view.findViewById(R.id.create_case);
        tvcreatecase.setOnClickListener(this);
        tvcreatehistory = (TextView) view.findViewById(R.id.create_history);
        rlnodate = (RelativeLayout) view.findViewById(R.id.rl_nodate);
        tvcreatehistory.setOnClickListener(this);
        tvweightnum = (TextView) view.findViewById(R.id.tv_weight_num);

        tvxueya = (TextView) view.findViewById(R.id.tv_jcz1);
        tvxuetang = (TextView) view.findViewById(R.id.tv_jcz2);
        tvtw = (TextView) view.findViewById(R.id.tvcankao);
        llysjy = (LinearLayout) view.findViewById(R.id.llysjy);
        llyyjy = (LinearLayout) view.findViewById(R.id.llyyjy);
        dotLayout = (RadioGroup) view.findViewById(R.id.advertise_point_group);

    }

    private void initBlood() {
        List<Fragment> mFragments = new ArrayList<Fragment>();
        mFragments.add(new TiwenChartFragment());
//       mFragments.add(new TizhongChartFragment());
        mFragments.add(new XueyaChartFragment());
        mFragments.add(new XuetangChartFragment());
        inflaterView = LayoutInflater.from(getActivity());
/*        for (int i = 0; i < 2; i++) {
        View view = inflaterView.inflate(R.layout.bloodsugaritem,
                null);
        views.add(view);
    }*/
//            vpAdapter = new MyViewPageAdapter(vie);

        mBloodsugar.setAdapter(new MyChartAdapter(getChildFragmentManager(), mFragments));
        mBloodsugar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBloodsugar.setCurrentItem(0);

    }



    public void initEvent() {
        setHead();
        initBlood();
        showADs();
        showUserInfo(userid);


            getWeight();

        ll_check_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MedicalExaminationActivity.class));

            }
        });
        ll_ECG_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ECGActivity.class);
                startActivity(intent);

            }
        });


    }


    public void onEventMainThread(RefreshEvent event) {

        if (getResources().getInteger(R.integer.refresh_info) == event
                .getRefreshWhere()) {
            userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
            initEvent();

        }

    }

    private void setHead() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String[] str = df.format(new Date()).split("\\-");
        tvdaytime.setText(str[0] + "年" + str[1] + "月" + str[2] + "日");

        User user = dao.findUserInfoById(userid);
        mImageLoader.displayImage(
                user.getImgHead(), ivhead);
        tvname.setText(user.getUsername());

    }

    private void showUserInfo(String userid) {
        startProgressDialog();
        requestMaker.healthInfo(userid, new JsonAsyncTask_Info(
                getActivity(), true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                JSONObject mySO = (JSONObject) result;
                stopProgressDialog();

                try {
                    JSONArray array = mySO.getJSONArray("HealthDataSearch");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        rlnodate.setVisibility(View.VISIBLE);
                        lluserinfo.setVisibility(view.GONE);
                    } else {
                        rlnodate.setVisibility(View.GONE);
                        lluserinfo.setVisibility(View.VISIBLE);
                        HealthData date = JsonTools.getData(returnvalue, HealthData.class);
                        List<HealthDes> list = date.getDate();
                        for (HealthDes des : list) {


                            CustomApplcation.getInstance().myMap.put("precount", Integer.valueOf(des.getPrecount()));
                            CustomApplcation.getInstance().myMap.put("weightcount", Integer.valueOf(des.getWeightcount()));
                            CustomApplcation.getInstance().myMap.put("tempcount", Integer.valueOf(des.getTempcount()));
                            CustomApplcation.getInstance().myMap.put("sugcount", Integer.valueOf(des.getSugcount()));
                            CustomApplcation.getInstance().myMap = CommonUtils.sortMap(CustomApplcation.getInstance().myMap);

                            if (TextUtils.isEmpty(des.getWeight())) {
                                tvweightnum.setText("暂无数据");
                            } else {

                                tvweightnum.setText(des.getWeight() + "kg");
                            }
//                            tvtwnum.setText(des.getTempvalue()+"℃");
                            if (!des.getTempvalue().equals("")) {
                                float tw = Float.parseFloat(des.getTempvalue());

                                if (Float.compare(tw, 37.1F) >= 0 && Float.compare(tw, 38F) <= 0) {
                                    tvtw.setText("低热");
                                    tvtw.setTextColor(Color.parseColor("#f9a116"));
//                                    tvtwnum.setTextColor(Color.parseColor("#f9a116"));

                                } else if (Float.compare(tw, 38.1F) >= 0 && Float.compare(tw, 39F) <= 0) {
                                    tvtw.setText("中等度热");
                                    tvtw.setTextColor(Color.parseColor("#fb7701"));
//                                    tvtwnum.setTextColor(Color.parseColor("#fb7701"));
                                } else if (Float.compare(tw, 39.1F) >= 0 && Float.compare(tw, 41) <= 0) {
                                    tvtw.setText("高热");
                                    tvtw.setTextColor(Color.parseColor("#fa3b00"));
//                                    tvtwnum.setTextColor(Color.parseColor("#fa3b00"));
                                } else if (Float.compare(tw, 41F) > 0) {

                                    tvtw.setText("超高热");
                                    tvtw.setTextColor(Color.parseColor("#ea0b35"));
//                                    tvtwnum.setTextColor(Color.parseColor("#ea0b35"));
                                } else {
                                    tvtw.setText("体温正常");
//                                    tvtwnum.setTextColor(Color.parseColor("#53bbb3"));
                                    tvtw.setTextColor(Color.parseColor("#53bbb3"));
                                }
                            } else {
//                                tvtwnum.setText("暂无数据");

                                tvtw.setText("暂无数据");
                            }
                            if (!des.getPrevalue().equals("")) {
                                String[] strs = des.getPrevalue().split("\\,");
                                changeImgeView(tvxueya, Integer.valueOf(strs[0]), Integer.valueOf(strs[1]));
                            } else {
                                tvxueya.setText("暂无数据");
                            }
                            if (TextUtils.isEmpty(des.getSugvalue())) {
                                tvxuetang.setText("暂无数据");

                            } else {
                                String[] xuetang = des.getSugvalue().split("\\,");
                                int type = Integer.valueOf(xuetang[2]);
                                float xf = Float.parseFloat(xuetang[0]);
                                if (type == 2) {
                                    xf = xf / 18;
                                }
                                if (xuetang[1].contains("餐后") || xuetang[1].contains("睡前")) {
                                    if (Float.compare(xf, 6.7F) >= 0 && Float.compare(xf, 11.1F) <= 0) {

                                        tvxuetang.setText("血糖正常");
                                        tvxuetang.setTextColor(Color.parseColor("#53bbb3"));
                                    } else if (Float.compare(xf, 6.7F) < 0) {
                                        tvxuetang.setText("低血糖");
                                        tvxuetang.setTextColor(Color.parseColor("#fb7701"));
                                    } else {
                                        tvxuetang.setText("高血糖");
                                        tvxuetang.setTextColor(Color.parseColor("#ea0b35"));
                                    }
                                } else {
                                    if (Float.compare(xf, 3.9F) < 0) {
                                        tvxuetang.setText("低血糖");
                                        tvxuetang.setTextColor(Color.parseColor("#fb7701"));
                                    } else if (Float.compare(xf, 3.9F) >= 0 && Float.compare(xf, 6.1F) <= 0) {
                                        tvxuetang.setText("血糖正常");
                                        tvxuetang.setTextColor(Color.parseColor("#53bbb3"));
                                    } else {
                                        tvxuetang.setText("高血糖");
                                        tvxuetang.setTextColor(Color.parseColor("#ea0b35"));
                                    }
                                }
                            }
                            if (TextUtils.isEmpty(des.getDiagnosis())) {
                                llysjy.setVisibility(View.GONE);
                            } else {
                                llysjy.setVisibility(View.VISIBLE);
                            }
                            if (TextUtils.isEmpty(des.getOpinion())) {
                                llyyjy.setVisibility(View.GONE);

                            } else {
                                llyyjy.setVisibility(View.VISIBLE);

                            }
                            String zdjg = des.getDiagnosis().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
                            String yyjy = des.getOpinion().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");


                            mContentText.setText(zdjg);
                            mContentText.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mContentText.getLineCount() >= 3) {

                                        mShowMore.setVisibility(View.VISIBLE);
                                    } else {

                                        mShowMore.setVisibility(View.GONE);
                                    }
                                }
                            });
                            mMedicine.setText(yyjy);
                            mMedicine.post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mMedicine.getLineCount() >= 3) {

                                                more.setVisibility(View.VISIBLE);
                                            } else {

                                                more.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                            );
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    private void changeImgeView(TextView tv, int ssz, int szy) {
        int lvssz = CommonUtils.computeSsz(ssz);
        int lvszy = CommonUtils.computeSzy(szy);
        int lv = CommonUtils.MaxInt(lvssz, lvszy);
        switch (lv) {
            case 1:

                tv.setText("血压正常");
                tv.setTextColor(Color.parseColor("#53bbb3"));

                break;
            case 2:

                tv.setText("高血压一期");
                tv.setTextColor(Color.parseColor("#fb7701"));
                break;
            case 3:

                tv.setText("高血压二期");
                tv.setTextColor(Color.parseColor("#fa3b00"));
                break;
            case 4:

                tv.setText("高血压三期");
                tv.setTextColor(Color.parseColor("#ea0b35"));
                break;

        }

    }


    public static Fragment getInstance() {

        return new HomeFragment();
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
                        mAdView.setVisibility(View.GONE);
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
                            mAdView.setVisibility(View.VISIBLE);
                            mAdView.setImageResources(mImageUrl, mObject, new ImageCycleView.ImageCycleViewListener() {
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

    private void showUpdateDialog() {
        DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
                getActivity()).setDialogMsg("检测到新版本", versionlog, "下载")
                .setOnClickListenerEnsure(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Intent it = new Intent(getActivity(),
                                DownloadServiceForAPK.class);
                        getActivity().startService(it);

                    }
                });
        DialogUtils.showSelfDialog(getActivity(), dialogEnsureCancelView);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rladvicdmore: {
                if (mState == SPREAD_STATE) {
                    mContentText.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mContentText.requestLayout();
                    mImageShrinkUp.setVisibility(View.GONE);
                    mImageSpread.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mContentText.setMaxLines(Integer.MAX_VALUE);
                    mContentText.requestLayout();
                    mImageShrinkUp.setVisibility(View.VISIBLE);
                    mImageSpread.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;

            }
            case R.id.more: {
                if (mStateMed == SPREAD_STATE) {
                    mMedicine.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mMedicine.requestLayout();
                    mImageMedShrinkUp.setVisibility(View.GONE);
                    mImageMedSpread.setVisibility(View.VISIBLE);
                    mStateMed = SHRINK_UP_STATE;
                } else if (mStateMed == SHRINK_UP_STATE) {
                    mMedicine.setMaxLines(Integer.MAX_VALUE);
                    mMedicine.requestLayout();
                    mImageMedShrinkUp.setVisibility(View.VISIBLE);
                    mImageMedSpread.setVisibility(View.GONE);
                    mStateMed = SPREAD_STATE;
                }
                break;

            }
            case R.id.ll_record_date:
                if (CommonUtils.isFastClick())
                    return;

                Intent intentD = new Intent();
                intentD.setAction("action.DateOrFile");
                intentD.putExtra("msgContent", "Date");
                getActivity().sendBroadcast(intentD);

                break;
            case R.id.ll_record_file:
                if (CommonUtils.isFastClick())
                    return;
//                getActivity().startActivity(new Intent(getActivity(), CreateillnessActivity.class));
                Intent intentF = new Intent();
                intentF.setAction("action.DateOrFile");
                intentF.putExtra("msgContent", "File");
                getActivity().sendBroadcast(intentF);
                break;
            case R.id.ll_record_guahao:
                Intent intentGuaHao = new Intent();
                intentGuaHao.setAction("action.DateOrFile");
                intentGuaHao.putExtra("msgGua", "Gua");
                getActivity().sendBroadcast(intentGuaHao);
                break;
            case R.id.create_case:
//                getActivity().startActivity(new Intent(getActivity(), CreateDataActivity.class));
                Intent intentD2 = new Intent();
                intentD2.setAction("action.DateOrFile");
                intentD2.putExtra("msgContent", "Date");
                getActivity().sendBroadcast(intentD2);

                break;
            case R.id.create_history:
                getActivity().startActivity(new Intent(getActivity(), CreateillnessActivity.class));
                break;

        }
    }

    private void getWeight(){
        requestMaker.HealthDataInquirywWithPageType(userid,"1","1","Weight",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete(){
            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    String resultValue=result.toString();
                    JSONArray array = mySO
                            .getJSONArray("HealthDataInquiryWithPage");
                    if (array.getJSONObject(0).has("MessageCode")){
                        SharePreferenceUtil.getInstance(getActivity()).setWeight("");
                    }else{
                        HealteData date = JsonTools.getData(result.toString(), HealteData.class);
                        List<HealthDataRes> list = date.getData();
                        SharePreferenceUtil.getInstance(getActivity()).setWeight(list.get(0).getValue1());

                    }
                    intent=new Intent(getActivity(), StepService.class);
                    getActivity().startService(intent);
                    if(TextUtils.isEmpty(SharePreferenceUtil.getInstance(getActivity()).getWeight())){
                        weight=50.0f;
                    }else {
                        weight = Float.parseFloat(SharePreferenceUtil.getInstance(getActivity()).getWeight());
                    }
                    if(StepDetector.CURRENT_SETP==0){
                        getJIbu();
                    }else {
                        upload();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }));
    }


    public void upload(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        calories = (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01)/1000;
        double d = step_length * StepDetector.CURRENT_SETP;
        timeCount = String.format("%.2f", d / 100000);
        int m = StepDetector.CURRENT_SETP / minute_distance;
        String h1 = String.valueOf(m / 60);
        String h2 = String.valueOf(m % 60);
        requestMaker.StepCounterInsert(userid, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, String.format("%.2f",calories),sdf.format(new Date()), new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("StepCounterInsert");
                    getJIbu();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 获取昨天日期
     * @return
     */
    public String getYesterday(){
        Date as = new Date(new Date().getTime()-24*60*60*1000);
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        return matter1.format(as);
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
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为3000ms
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Toast.makeText(getActivity(),location.getCity()+"vvvvvvvvvvvvvvv" ,Toast.LENGTH_SHORT).show();




        }
    }


}
