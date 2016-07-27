package com.zzu.ehome.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.StepCounterBean;
import com.zzu.ehome.bean.StepCounterDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.NetUtils;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AutoSaveService extends Service {
	private EHomeDao dao;
	private String userid;
	private RequestMaker requestmaker;
	private User user;
	private float height;
	private float weight;
	private int calories;
	private int step_length=55;
	private int minute_distance=80;
	private String timeCount;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		init();
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressLint("SimpleDateFormat")
	private void init() {
		requestmaker=RequestMaker.getInstance();
		dao = new EHomeDaoImpl(this);
		userid = SharePreferenceUtil.getInstance(this).getUserId();
		weight=Float.parseFloat(SharePreferenceUtil.getInstance(this).getWeight());
		user=dao.findUserInfoById(userid);
		height=Float.parseFloat(user.getUserHeight());
		String time = DateUtils.getTodayTime();
		StepBean step = new StepBean();
		step.setNum(0);
		step.setUserid(userid);
		step.setStartTime("");
		step.setEndTime("");
		step.setUploadState(0);
		dao.saveStep(step);
		upload();
	}

	public void upload(){
		    String time =getYesterday();
			calories = (int) (weight * StepDetector.CURRENT_SETP * 50 * 0.01 * 0.01);
			double d = step_length * StepDetector.CURRENT_SETP;
			timeCount = String.format("%.2f", d / 100000);
			int m = StepDetector.CURRENT_SETP / minute_distance;
			String h1 = String.valueOf(m / 60);
			String h2 = String.valueOf(m % 60);
			if (NetUtils.isNetworkConnected(this)) {
				requestmaker.StepCounterInsert(userid, StepDetector.CURRENT_SETP + "", timeCount, h1 + "." + h2, calories + "", getYesterday(), new JsonAsyncTask_Info(this, true, new JsonAsyncTaskOnComplete() {
					@Override
					public void processJsonObject(Object result) {
						try {
							JSONObject mySO = (JSONObject) result;
							org.json.JSONArray array = mySO
									.getJSONArray("StepCounterInsert");
							if (array.getJSONObject(0).has("MessageCode")) {
								StepDetector.CURRENT_SETP=0;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}));
			}
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

}
