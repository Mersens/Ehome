package com.zzu.ehome.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zzu.ehome.reciver.NetReceiver;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CustomApplcation extends Application {
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "ehome" + File.separator + "images" + File.separator;

    public static List<Activity> mList = new LinkedList<Activity>();
    public static Map<String, Integer> myMap = new LinkedHashMap();

    public static CustomApplcation mInstance;
    private NetReceiver mReceiver;
    private Intent intent;


    @Override
    public void onCreate() {
        super.onCreate();

//        CrashHandler.getInstance().init(getApplicationContext());
        SDKInitializer.initialize(this);

        initImageLoader(getApplicationContext());
        mReceiver = new NetReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(mReceiver);
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiskCache(new File(IMAGES_FOLDER)))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                .memoryCacheSize(4 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
    }

    public static CustomApplcation getInstance() {
        if (mInstance == null) {
            synchronized (CustomApplcation.class) {
                if (mInstance == null) {
                    mInstance = new CustomApplcation();
                }
            }
        }
        return mInstance;
    }


    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (mList.contains(activity)) {
                mList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class cls) {
        Activity tempActivity = null;
        for (Activity activity : mList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
