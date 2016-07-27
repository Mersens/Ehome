package com.zzu.ehome.activity;

import android.os.Bundle;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;

import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PhotoViewPager;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * Created by Administrator on 2016/6/23.
 */
public class ImageECGDetail extends BaseActivity implements PhotoViewAttacher.OnViewTapListener {
    private PhotoViewPager viewPager;
    private PhotoViewAdapter mAdapter;
    private String imgstr = null;
    private PhotoView mImageView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_image_detal_des);
        imgstr = getIntent().getStringExtra("imurl");
        initViews();
        initDatas();
    }

    public void initViews() {
        setLeftViewMethod(R.mipmap.icon_arrow_left, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });

        viewPager = (PhotoViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(mListener);
    }

    public void initDatas() {

        mAdapter = new PhotoViewAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);


    }
    @Override
    public void onViewTap(View view, float x, float y) {
//        finish();
    }

    class PhotoViewAdapter extends PagerAdapter  {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = container.inflate(ImageECGDetail.this,
                    R.layout.layout_image_detail,null);
            mImageView = (PhotoView) view.findViewById(R.id.image);
            mAttacher= new PhotoViewAttacher(mImageView);
            mAttacher.setOnViewTapListener(ImageECGDetail.this);
            ImageLoader.getInstance().displayImage(imgstr,mImageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mAttacher = null;
            container.removeView((View)object);

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


}
