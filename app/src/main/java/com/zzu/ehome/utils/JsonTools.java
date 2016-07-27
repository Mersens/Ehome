package com.zzu.ehome.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonTools {
	
	public static <T> T getData(String responseString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(responseString, cls);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return t;
	}
	
	public static <T> List<T> getList(String responseString, Class<T> cls) {
		List<T> list = new ArrayList<T>(); 
		
		try {
			Gson gson = new Gson();
			list = gson.fromJson(responseString, new TypeToken<List<T>>(){}.getType());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
//		list = new ArrayList<T>(list);
		return list;
	}
}
