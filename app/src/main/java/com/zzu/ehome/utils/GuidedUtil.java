package com.zzu.ehome.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GuidedUtil {
	private static GuidedUtil gu;
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor editor;
	public static final String PREFERENCE_NAME = "_guidedinfo";
	public static final String SHARED_KEY_ISFIRST = "shared_key_isfirst";
	private GuidedUtil(){
		
	}
	
	public static synchronized GuidedUtil getInstance(Context context){
		if(gu==null){
		gu=new GuidedUtil();
		mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
		}
		return gu;
	}

	public boolean isFirst(){
		return mSharedPreferences.getBoolean(SHARED_KEY_ISFIRST, true);
	}
	
	public  void setIsFirst(boolean isFist){
		editor.putBoolean(SHARED_KEY_ISFIRST, isFist);
		editor.commit();
	}
	
	
}
