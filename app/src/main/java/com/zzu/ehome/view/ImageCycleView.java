package com.zzu.ehome.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


import com.zzu.ehome.R;

import java.util.ArrayList;
import java.util.Map;


public class ImageCycleView extends LinearLayout {
	private Context mContext;
	private ViewPager mAdvPager = null;
	private ImageCycleAdapter mAdvAdapter;


	private ViewGroup mGroup;


	private ImageView mImageView = null;


	private ImageView[] mImageViews = null;


	private int mImageIndex = 0;


	private float mScale;

	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		mAdvPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						startImageTimerTask();
						break;
					default:
						stopImageTimerTask();
						break;
				}
				return false;
			}
		});
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
	}

	/**
	 * 装填图片数据
	 *
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<String> imageUrlList,
								  ArrayList<Map<String, Object>> mObject,ImageCycleViewListener imageCycleViewListener) {
		mGroup.removeAllViews();
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			int imageParams = (int) (mScale * 10+ 0.5f);
			int imagePadding = (int) (mScale * 5 + 0.5f);
			mImageView.setLayoutParams(new LayoutParams(imageParams, imageParams));
//			mImageView.setPadding(0, imagePadding, 0, imagePadding);
			mImageViews[i] = mImageView;
			mImageView.setScaleType(ScaleType.FIT_XY);
			if (i == 0) {
				mImageViews[i].setBackgroundResource(R.drawable.dot_focus);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.dot_blur);
			}
			LayoutParams sizeParams = new LayoutParams (imageParams,
					imageParams);
//			  sizeParams .leftMargin=2;
			mGroup.addView(mImageViews[i],sizeParams);
		}
		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList,mObject, imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		startImageTimerTask();
	}

	/**
	 * 开始轮播(手动控制自动轮播与否，便于资源控制)
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播——用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}
	/**
	 * 更新资源
	 */
	public void destroyImageCycle() {

		mAdvAdapter.notifyDataSetChanged();
	}

	/**
	 * 开始图片滚动任务
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {

		@Override
		public void run() {
			if (mImageViews != null) {
				if ((++mImageIndex) == mImageViews.length) {
					mImageIndex = 0;
				}
				mAdvPager.setCurrentItem(mImageIndex);
			}
		}
	};

	/**
	 * 轮播图片状态监听器
	 *
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			mImageIndex = index;
			mImageViews[index].setBackgroundResource(R.drawable.dot_focus);
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(R.drawable.dot_blur);
				}
			}

		}

	}

	private class ImageCycleAdapter extends PagerAdapter {
		private int mChildCount = 0;

		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<ImageView> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		private ArrayList<String> mAdList = new ArrayList<String>();
		private ArrayList<Map<String, Object>> mObject= new ArrayList<Map<String, Object>>();

		/**
		 * 广告图片点击监听器
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;

		public ImageCycleAdapter(Context context, ArrayList<String> adList,
								 ArrayList<Map<String, Object>> mObject,ImageCycleViewListener imageCycleViewListener) {
			mContext = context;
			this.mAdList = adList;
			this.mObject=mObject;

			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<ImageView>();
		}

		@Override
		public int getCount() {
			return mAdList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			String imageUrl = mAdList.get(position);
			ImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
				imageView.setScaleType(ScaleType.FIT_XY);

			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mImageCycleViewListener.onImageClick(position, v,mObject);
				}
			});

			imageView.setTag(imageUrl);
			container.addView(imageView);
			try {
				mImageCycleViewListener.displayImage(imageUrl, imageView);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block

			}
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView view = (ImageView) object;
			container.removeView(view);
			mImageViewCacheList.add(view);
		}

		@Override
		public int getItemPosition(Object object)   {
			if ( mChildCount > 0) {
				mChildCount --;
				return POSITION_NONE;

			}
			return super.getItemPosition(object);

		}
		@Override

		public void notifyDataSetChanged() {

			mChildCount = getCount();

			super.notifyDataSetChanged();

		}

	}

	/**
	 * 轮播控件的监听事件
	 *
	 * @author guoqiang
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 加载图片资源
		 *
		 * @param imageURL
		 * @param imageView
		 */
		public void displayImage(String imageURL, ImageView imageView);

		/**
		 * 单击图片事件
		 *
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView, ArrayList<Map<String, Object>> mObject);


	}

}
