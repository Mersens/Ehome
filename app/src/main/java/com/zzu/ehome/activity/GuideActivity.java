package com.zzu.ehome.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.fragment.GuideFragment;
import com.zzu.ehome.utils.GuidedUtil;

import java.util.Timer;
import java.util.TimerTask;


public class GuideActivity extends BaseSimpleActivity{
	private final String mPageName = "GuideActivity";

	private RadioGroup dotLayout;
	private ViewPager viewPager;
	private PageFragmentAdapter adapter;
	public static Activity guidAct;
	private TextView tv_register,tv_login;
	private Timer mTimer;
	int index=-1;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mTimer = new Timer();
		setContentView(R.layout.activity_guide);
		initViews();
		initEvent();

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}



	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

				case 2:

					viewPager.setCurrentItem(index);
					if(index==3){index=-1;}
						break;
			}
			super.handleMessage(msg);
		}
	};
	private void setTimerTask() {
		// TODO Auto-generated method stub
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 2;
				mHandler.sendMessage(message);
				index++;

			}
		}, 1000, 2000);
	}


	private void initViews() {

		dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_login = (TextView) findViewById(R.id.tv_login);
	}
	
	private void initEvent() {
		adapter = new PageFragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyPagerChangeListener());
		guidAct=this;
		tv_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				intentAction(GuideActivity.this, RegisterActivity.class);


			}
		});
		tv_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				intentAction(GuideActivity.this, LoginActivity.class);

			}
		});
		
	}
	public class MyPagerChangeListener implements OnPageChangeListener {

		public void onPageSelected(int position) {


		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
		}
	}
	
	class PageFragmentAdapter extends FragmentPagerAdapter {
		public PageFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int idx) {
			return GuideFragment.getInstance(idx);
		}

		@Override
		public int getCount() {
			return GuideFragment.imags.length;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTimer.cancel();
	}
}
