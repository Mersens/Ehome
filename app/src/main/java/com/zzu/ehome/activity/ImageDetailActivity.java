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
 * Created by zzu on 2016/4/27.
 */
public class ImageDetailActivity extends BaseActivity implements OnViewTapListener{
    private PhotoViewPager viewPager;
    private PhotoViewAdapter mAdapter;
    private String imgstr = null;
    private TextView tv_count;
    private TextView tv_num;
    private String[] strs=null;
    private int index=0;
    private PhotoView mImageView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_image_detail);
        imgstr = getIntent().getStringExtra("imgstr");
        index=getIntent().getIntExtra("index",0);
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
        if (imgstr.indexOf(",") >= 0) {
            String[] strold = imgstr.split("\\,");
            strs=new String[strold.length];
            for(int i=0;i<strold.length;i++){
                strs[i]= Constants.EhomeURL + strold[i].replace("~", "").replace("\\", "/");
            }
        }else{
            strs=new String[1];
            strs[0]=Constants.EhomeURL+imgstr.replace("~", "").replace("\\", "/");
        }
        if(strs!=null){
            tv_count.setText(strs.length+"");
            tv_num.setText(index+1+"");
        }
        mAdapter = new PhotoViewAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);


    }
    @Override
    public void onViewTap(View view, float x, float y) {
//        finish();
    }

    class PhotoViewAdapter extends PagerAdapter  {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = container.inflate(ImageDetailActivity.this,
                    R.layout.layout_image_detail,null);
            mImageView = (PhotoView) view.findViewById(R.id.image);
            //使用ImageLoader加载图片

            //给图片增加点击事件
            mAttacher= new PhotoViewAttacher(mImageView);
            mAttacher.setOnViewTapListener(ImageDetailActivity.this);
            ImageLoader.getInstance().displayImage(strs[position],mImageView);
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
            return strs.length;
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

//    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
//
//        public void onPageSelected(int position) {
//
//        }
//
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//
//        public void onPageScrolled(int position, float positionOffset,
//                                   int positionOffsetPixels) {
//            tv_num.setText((position+1)+"");
//
//        }
//    }

//    class PageFragmentAdapter extends FragmentPagerAdapter {
//        public PageFragmentAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int idx) {
//            return ImageDetailFragment.getInstance(strs[idx]);
//        }
//
//        @Override
//        public int getCount() {
//            return strs==null?0:strs.length;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//    }
}
