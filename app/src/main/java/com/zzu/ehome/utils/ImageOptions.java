package com.zzu.ehome.utils;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zzu.ehome.R;

import android.graphics.Bitmap;
public class ImageOptions {

	public static DisplayImageOptions getDefaultOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
	public static DisplayImageOptions getHeadOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.pic_my_touxiang_x)
				.showImageForEmptyUri(R.mipmap.pic_my_touxiang_x)
				.showImageOnFail(R.mipmap.pic_my_touxiang_x).cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true)
				.build();
		return options;
	}
	public static DisplayImageOptions getHeaderOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.icon_tx_doc)
				.showImageForEmptyUri(R.mipmap.icon_tx_doc)
				.showImageOnFail(R.mipmap.icon_tx_doc).cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

}
