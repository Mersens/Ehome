package com.zzu.ehome.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/4/7.
 */
public class ScreenUtils {
    public static float getDisplayScale(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }
    /**
     * author guoqiang
     * @param context
     * @return
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    public static int dip2px(Context context,int i) {
        return (int) (0.5D + (double) (getDensity(context) * (float) i));
    }
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
