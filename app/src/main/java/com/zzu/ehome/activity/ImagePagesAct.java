package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;


import java.util.ArrayList;
import java.util.List;

public class ImagePagesAct extends BaseActivity {
	private ImageLoader imageLoader = ImageLoader.getInstance();
	String[] imageUrls;
	private ViewPager pager;
	private TextView tvFlag;
	int size;
	ArrayList<String> listUrls; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_page);
		Intent intent =this. getIntent();
		iniView();
		if(intent.getStringArrayExtra("imgs") == null && intent.getStringArrayListExtra("imgs") == null) {
			Toast.makeText(this, "未找到图片", 1).show();
			return;
		}
		if (isNetworkAvailable()) {
			listUrls = intent.getStringArrayListExtra("imgs");
			int pagerPosition = intent.getIntExtra("position", 0);
			size=listUrls.size();
			pager.setAdapter(new ImagePagerAdapter(listUrls));
			tvFlag.setText((pagerPosition+1)+"/"+size);
			pager.setCurrentItem(pagerPosition);
			pager.setOnPageChangeListener(mListener);
		

		} else {
			Toast.makeText(ImagePagesAct.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		
		}
		

	protected void iniView() {
		// TODO Auto-generated method stub
		pager = (ViewPager) findViewById(R.id.pager);
		tvFlag=(TextView) findViewById(R.id.tvFlag);
	}

	public class ImagePagerAdapter extends PagerAdapter {
		private LayoutInflater inflater;
		private List<String> list;
		ImagePagerAdapter(List<String> listdata) {
			this.list = listdata;
			inflater = getLayoutInflater();
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
				return listUrls.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_image_pager, view, false);
			ImageView  imageView = (ImageView) imageLayout.findViewById(R.id.image);
			imageLoader.displayImage(listUrls.get(position), imageView);
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			
			return view.equals(object);
		}
		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	
	}
private OnPageChangeListener mListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			tvFlag.setText((position+1)+"/"+size);
			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
}
