package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.view.MatrixImageView;

/**
 * Created by zzu on 2016/4/27.
 */
public class ImageDetailFragment extends  BaseFragment {
    private View mView;
    private String url;
    private MatrixImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.layout_image_detail,null);
        url=getArguments().getString("params");
        initViews();
        return mView;
    }

    @Override
    protected void lazyLoad() {

    }

    private void initViews(){
        mImageView= (MatrixImageView) mView.findViewById(R.id.image);
        ImageLoader.getInstance().displayImage(url,mImageView);
    }
    public static Fragment getInstance(String params) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("params", params);
        fragment.setArguments(bundle);
        return fragment;

    }
}
