package com.zzu.ehome.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.activity.LoginActivity;
import com.zzu.ehome.activity.RegisterActivity;
import com.zzu.ehome.R;
import com.zzu.ehome.utils.GuidedUtil;

public class GuideFragment extends BaseFragment {
	private View view;
	private ImageView imageView;
	public static int[] imags = {R.mipmap.bg_yidao1, R.mipmap.bg_yidao2,R.mipmap.bg_yidao3,R.mipmap.bg_yidao4, };
	private int index = 0;
	private TextView tv_register,tv_login;
	private LinearLayout layout_tips;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_guide, null);
		index = getArguments().getInt("params");
		initViews();
		initEvent();
		return view;
	}

	private void initViews() {
		layout_tips=(LinearLayout) view.findViewById(R.id.layout_tips);
		imageView = (ImageView) view.findViewById(R.id.imageView);

		imageView.setImageResource(imags[index]);

	}

public void initEvent(){

}
	public static Fragment getInstance(int params) {
		GuideFragment fragment = new GuideFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("params", params);
		fragment.setArguments(bundle);
		return fragment;

	}
	public <T> void intentAction(Activity context, Class<T> cls) {
		Intent intent = new Intent(context, cls);
		startActivity(intent);

	}
	@Override
	protected void lazyLoad() {

	}
}
