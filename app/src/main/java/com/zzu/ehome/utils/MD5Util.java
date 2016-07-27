package com.zzu.ehome.utils;

import java.security.MessageDigest;
/**
 *
 * @title MD5Util
 * @description:MD5加密密码
 * @author Mersens
 * @time 2016年4月6日
 */
public class MD5Util {
	public static String MD5(String str){
		byte[] bt=str.getBytes();
		StringBuffer sbf=null;
		MessageDigest md=null;
		try {
			md=MessageDigest.getInstance("MD5");
			byte[] bt1=md.digest(bt);
			sbf=new StringBuffer();
	        for (int i = 0; i < bt1.length; i++){  
	            int val = ((int) bt1[i]) & 0xff;  
	            if (val < 16)  
	            	sbf.append("0");  
	            sbf.append(Integer.toHexString(val));  
	        } 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbf.toString();
	}
}
