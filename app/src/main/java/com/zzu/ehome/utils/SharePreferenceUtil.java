package com.zzu.ehome.utils;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * @title SharePreferenceUtil
 * @description:SharePreference工具类，数据存储
 * @author Mersens
 * @time 2016年4月6日
 */
public class SharePreferenceUtil {
	private static SharePreferenceUtil sp;
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor editor;
	private static final String PREFERENCE_NAME = "_sharedinfo";
	private static final String USER_ID="user_id";
	private static final String IS_REMEBER="is_remeber";
	private static final String IS_FIRST="is_first";
	private static final String WEIGHT="WEIGHT";
	private static final String PARENT_ID="parent_id";


	public static Boolean getIsRemeber() {
		return mSharedPreferences.getBoolean(IS_REMEBER, false);
	}

	public static void setIsRemeber(Boolean isRemeber) {
		editor.putBoolean(IS_REMEBER,isRemeber);
		editor.commit();

	}
	public static Boolean getIsFirst() {
		return mSharedPreferences.getBoolean(IS_FIRST, false);
	}

	public static void setIsFirst(Boolean isIsFirst) {
		editor.putBoolean(IS_FIRST,isIsFirst);
		editor.commit();

	}


	private SharePreferenceUtil(){
		
	}
	public static synchronized SharePreferenceUtil getInstance(Context context){
		if(sp==null){
		sp=new SharePreferenceUtil();
		mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
		}
		return sp;
	}

	//用户亲友id
	public String getPARENTID(){
		return mSharedPreferences.getString(PARENT_ID, "");
	}
	public void setPARENTID(String parentid){
		editor.putString(PARENT_ID, parentid);
		editor.commit();
	}
	//用户id
	public String getUserId(){
		return mSharedPreferences.getString(USER_ID, "");
	}
	public void setUserId(String userid){
		editor.putString(USER_ID, userid);
		editor.commit();
	}

	//清除数据
	public void clearAllData(){
		editor.clear().commit();
	}
	public  void setWeight(String W) {
		editor.putString(WEIGHT,W);
		editor.commit();

	}
	public  String getWeight() {
		return mSharedPreferences.getString(WEIGHT,"");
	}

}
