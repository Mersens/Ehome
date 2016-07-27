package com.zzu.ehome.utils;

import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class MessageItem {
	public static Map<String,LinearLayout> map=new HashMap<String, LinearLayout>();
	public static void putMsg(String id,LinearLayout layout){
		map.put(id, layout);
	}

	public static LinearLayout getMsg(String key){
		return map.get(key);
	}
	
	public static boolean isContainsKey(String key){
		return map.containsKey(key);
	}
	
	public static void clearMsg(){
		map.clear();
	}
	
	public static void removeItem(String key){
		map.remove(key);
	}
}
