package com.zzu.ehome.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class MyViewPageAdapter extends PagerAdapter {
	private List<View> views;
	public MyViewPageAdapter(List<View> views) {
		this.views = views;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	  View view;
      view=views.get(position);
      container.addView(view,0);
      return views.get(position);

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
