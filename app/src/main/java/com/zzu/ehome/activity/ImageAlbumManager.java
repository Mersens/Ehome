package com.zzu.ehome.activity;

import android.content.Intent;
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

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * Created by Administrator on 2016/6/24.
 */
public class ImageAlbumManager extends BaseActivity implements PhotoViewAttacher.OnViewTapListener {
    private PhotoViewPager viewPager;
    private PhotoViewAdapter mAdapter;


    private TextView tv_num;
    private String[] strs=null;
    private int index=0;
    private PhotoView mImageView;
    private PhotoViewAttacher mAttacher;
    ArrayList<String> listUrls;
    private int size;
    private int type;
    private TextView tv_count;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_image_detail);

        Intent intent =this. getIntent();
        listUrls = intent.getStringArrayListExtra("imgs");
        index = intent.getIntExtra("position", 0);
        type=intent.getIntExtra("type", 0);
        size=listUrls.size();

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
        tv_count=(TextView) findViewById(R.id.tv_count);
        tv_num=(TextView) findViewById(R.id.tv_num);
        viewPager = (PhotoViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(mListener);

    }

    public void initDatas() {

        mAdapter = new PhotoViewAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);
        tv_count.setText(size+"");
        tv_num.setText(index+1+"");

    }
    @Override
    public void onViewTap(View view, float x, float y) {
//        finish();
    }

    class PhotoViewAdapter extends PagerAdapter  {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = container.inflate(ImageAlbumManager.this,
                    R.layout.layout_image_detail,null);
            mImageView = (PhotoView) view.findViewById(R.id.image);
            //使用ImageLoader加载图片

            //给图片增加点击事件
            mAttacher= new PhotoViewAttacher(mImageView);
            mAttacher.setOnViewTapListener(ImageAlbumManager.this);
            if(type==1) {
                ImageLoader.getInstance().displayImage(listUrls.get(position), mImageView);
            }else{
                ImageLoader.getInstance().displayImage("file://"+listUrls.get(position), mImageView);
            }
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
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            tv_num.setText((position+1)+"");

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
