package com.zzu.ehome.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Time;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class IOUtils
{
	
	public static Date YunStart(Date d)
	{
		long time = 280l * 24 * 60 * 60 * 1000;
		Date dStart = new Date(d.getTime() - time);

		return dStart;
	}
	/**
	 * 时间戳转换为特定格式的时间 时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param string
	 * @return
	 */
	
	public static String getTime(String string) {
		String re_str_time = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long lcc_time = Long.parseLong(Long.valueOf(string) * 1000 + "");
			re_str_time = sdf.format(new Date(lcc_time));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return re_str_time;
	}
	/**
	 * 获取当前时间
	 * 返回形式 YYYY-MM-DD hh:mm:ss
	 * @return
	 */
	public static String getCurrentTime() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		String time = new StringBuilder()
				.append(t.year)
				.append("-")
				.append((t.month + 1) < 10 ? "0" + (t.month + 1)
						: (t.month + 1)).append("-")
				.append((t.monthDay < 10) ? "0" + t.monthDay : t.monthDay).append(" ")
				.append((t.hour < 10) ? "0" + t.hour : t.hour).append(":")
				.append((t.minute < 10) ? "0" + t.minute : t.minute).append(":")
				.append((t.second < 10) ? "0" + t.second : t.second).toString();
//		System.out.println("当前时间："+time);
		return time;
	}
	/**
	 * 字符串转化为data
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */
	public static Date string2Date(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	// 返回string类型时间
	public static String stringToDate2(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatter.format(date);
	}
	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return date;
 	}
	public static String getMistimingTimes(String strtime) {
		String mistimingTime = "";
		try {
	
		String timeCurrent = getCurrentTime();
		String year_current = timeCurrent.substring(0, 4);
		String month_current = timeCurrent.substring(5, 7);
		String day_current = timeCurrent.substring(8, 10);
		String hour_current = timeCurrent.substring(11, 13);
		String minute_current = timeCurrent.substring(14, 16);
//		String sconde_current = timeCurrent.substring(17, 19);
		String year_time = strtime.substring(0, 4);
		String month_time = strtime.substring(5, 7);
		String day_time = strtime.substring(8, 10);
		String hour_time = strtime.substring(11, 13);
		String minute_time = strtime.substring(14, 16);
//		String sconde_time = time.substring(17, 19);
		if(year_current.equals(year_time)) {
			if(month_current.equals(month_time)) {
				if(day_current.equals(day_time)) {
					if(hour_current.equals(hour_time)) {
						if(minute_current.equals(minute_time)) {
							mistimingTime = "刚刚";
						}
						else {
							mistimingTime = (Integer.valueOf(minute_current) - Integer.valueOf(minute_time)) + "分钟前";
						}
					}
					else {
						int s=Integer.valueOf(minute_time);
						String ss=s<10?"0"+s:s+"";
						mistimingTime = "今天:"+(Integer.valueOf(hour_time))+":"+ss;
					}
				}
				else {
					int c = Integer.valueOf(day_current);
					int t = Integer.valueOf(day_time);
					if(Integer.valueOf((Integer.valueOf(c) - Integer.valueOf(t)))==1){
						int s=Integer.valueOf(minute_time);
						String ss=s<10?"0"+s:s+"";
						mistimingTime ="昨天:"+Integer.valueOf(hour_time)+":"+ss;
					}else if(Integer.valueOf((Integer.valueOf(c) - Integer.valueOf(t)))==2){
						int s=Integer.valueOf(minute_time);
						String ss=s<10?"0"+s:s+"";
						mistimingTime ="前天"+Integer.valueOf(hour_time)+":"+ss;
					}else {
						mistimingTime =Integer.valueOf(month_time)+"月"+ Integer.valueOf(day_time)+"日";
					}
					
				}
			}
			else {
				int month = Integer.valueOf(month_time);
				int day = Integer.valueOf(day_time);
				mistimingTime = month + "月" + day + "日";
			}
		}
		else {
			int month = Integer.valueOf(month_time);
			int year = Integer.valueOf(year_time);
			mistimingTime = year + "年" + month + "月"+ Integer.valueOf(day_time)+"日";
		}
		}
		catch(Exception e) {
			mistimingTime = "未知";
		}
//		System.out.println(mistimingTime);
		return mistimingTime;
	}
	public static int DaysCount(Date d)
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date dNow = new Date();
		try
		{
			dNow = fmt.parse(fmt.format(dNow));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long intermilli = d.getTime() - dNow.getTime();
		int days = (int) (intermilli / (24 * 60 * 60 * 1000));
		
//		if(days < 0)
//		{
//			days = 0;
//		}
		return days;
	}
	/**
	 * 传入天数距离现在
	 * @param d
	 * @return
	 */
	public static int DaysCount2(Date d)
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date dNow = new Date();
		try
		{
			dNow = fmt.parse(fmt.format(dNow));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long intermilli = dNow.getTime()-d.getTime();
		int days = (int) (intermilli / (24 * 60 * 60 * 1000));
		
//		if(days < 0)
//		{
//			days = 0;
//		}
		return days;
	}
	public static String calculatTime(long millSecondTime){
		long hour=millSecondTime/(60*60*1000);
		long minute=(millSecondTime-hour*60*60*1000)/(60*1000);
		long sencond=(millSecondTime-hour*60*60*1000-minute*60*1000)/1000;
		if(sencond>=60){
			sencond=sencond%60;
			minute+=sencond/60;
		}
		if(minute>=60){
			minute=minute%60;
			hour+=minute/60;
		}
		String sh=String.valueOf(hour);
		String sm=String.valueOf(minute);
	
		return sh+":"+sm;
	}
	public static String ComputeYuchan(String date){

		
		String[] time=date.split("\\-");
		String year=time[0];
		String month=time[1];
		String day=time[2];
		int yeartmp=Integer.valueOf(year);
		int mothtmp=Integer.valueOf(month);
		int tmpday=Integer.valueOf(day);
		if(mothtmp<=3){
			mothtmp=mothtmp+9;
		}else{
			yeartmp=yeartmp+1;
			mothtmp=mothtmp-3;
		}
		tmpday=tmpday+7;
		if(checkYear(yeartmp)&&mothtmp==2){
			if(tmpday>29){
				tmpday=tmpday-29;
			     mothtmp=mothtmp+1;
			}
		}else if(mothtmp==2){
			if(tmpday>28){
				tmpday=tmpday-28;
			    mothtmp=mothtmp+1;
			}
		}
		if(tmpday>30){
			tmpday=tmpday-30;
			mothtmp=mothtmp+1;
		}
		if(mothtmp>12){
			mothtmp=mothtmp-12;
			yeartmp=yeartmp+1;
		}
		return yeartmp+"-"+mothtmp+"-"+tmpday;
		

	}
	public static boolean checkYear(int year){
	if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){    
		return true;
				}else{  
					
					return false;
					}
	}
	
	public static String convert(long mill){
		 Date date=new Date(mill);
		 String strs="";
		 try {
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 strs=sdf.format(date);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
		 return strs;
		 } 
	
	public static Boolean IsBeyondTimeSet(Date d)
	{
		Boolean isBeyond = false;
		Date dNow = new Date();
		long time = 280l * 24 * 60 * 60 * 1000;
		//long time2 = 1l*24*60*60*1000;
		Date dEnd = new Date(dNow.getTime() + time);
		
		//Date dStart = new Date(dNow.getTime() + time2);
		Date dStart = new Date(dNow.getTime());
		
		if(d.compareTo(dStart)<=0||0<d.compareTo(dEnd))
		{
			isBeyond = true;
		}

		return isBeyond;
	}
	public static Boolean IsYuejingTimeSet(Date d)
	{
		Boolean isYuejing = false;
		Date dNow = new Date();
		
		Date dEnd = new Date(d.getTime()+280L* 24 * 60 * 60 * 1000);
		
		if(d.compareTo(dNow)>=0)
		{
			isYuejing = false;
		}else{
			isYuejing = true;
		}

		return isYuejing;
	}
	
	public static Boolean IsBeyondTimeSet2(Date d,Date dStart,Date dEnd)
	{
		Boolean isBeyond = false;
		Date dNow = new Date();
//		long time = 280l * 24 * 60 * 60 * 1000;
//		long time2 = 1l*24*60*60*1000;
//		Date dEnd = new Date(dNow.getTime() + time);
//		
//		Date dStart = new Date(dNow.getTime() + time2);
		
		if(d.compareTo(dStart)<0||0<d.compareTo(dEnd))
		{
			isBeyond = true;
		}

		return isBeyond;
	}
	
	//计算还有几天进行下一次产检
	public static int DaysCount2(String time)
	{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		time = time.split(" ")[0].replace("/", "-");
		Date d = null;
		Date dNow = new Date();
		try
		{
			d = fmt.parse(time);
			dNow = fmt.parse(fmt.format(dNow));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long intermilli = d.getTime() - dNow.getTime();
		int days = (int) (intermilli / (24 * 60 * 60 * 1000));
		return days;
	}


	public static Date getNextDay(Date date, int next)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, next);
		date = calendar.getTime();
		return date;
	}

	public static String getWeekOfDate(Date date)
	{
		String[] weekOfDays =
		{ "日", "一", "二", "三", "四", "五", "六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null)
		{
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
		{
			w = 0;
		}
		return weekOfDays[w];
	}

	public static String getDayOfDate(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		return String.valueOf(day);
	}
	 /** 
	    * 获取当前时间的前一天时间 
	    * @param cl 
	    * @return 
	    */  
	 private static Calendar getBeforeDay(Calendar cl){  
	        //使用roll方法进行向前回滚  
	        //cl.roll(Calendar.DATE, -1);  
	       //使用set方法直接进行设置  
      int day = cl.get(Calendar.DATE);  
	      cl.set(Calendar.DATE, day-1);  
	        return cl;  
	   }  

	public static String formatStrDate(String time)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd");
		time = time.split(" ")[0].replace("/", "-");

		try
		{
			time = formatter.format(formatter
					.parse(time));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	public static Boolean isOutoftwo(Date date)
	{
		Boolean isout = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (14 <= hour)
		{
			isout = true;
		}
		return isout;
	}

	public static Bitmap getHttpBitmap(String url)
	{
		URL myFileURL;
		Bitmap bitmap = null;
		try
		{
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return bitmap;

	}



	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles)
	{
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 * (^1(3[0-9]|4[57]|5[0-35-9]|8[0-9])\\d{8}$)|(^170[059]\\d{7}$)
		 */
		/**
		 * 手机号码:
		 * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0, 5, 9]
		 * 移动号段: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
		 * 联通号段: 130,131,132,155,156,185,186,145,176,1709
		 * 电信号段: 133,153,180,181,189,177,1700
		 */
		String mobile="(^1(3[0-9]|4[57]|5[0-35-9]|8[0-9])\\d{8}$)|(^170[059]\\d{7}$)";
		/**
		 * 中国移动：China Mobile
		 * 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
		 */
		String cm="(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
		/**
		 * 中国联通：China Unicom
		 * 130,131,132,155,156,185,186,145,176,1709
		 */
		String cu="(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
		/**
		 * 中国电信：China Telecom
		 * 133,153,180,181,189,177,1700
		 */
         String ct="(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";
//		String telRegex = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9])\\\\d{8}$)|(^170[059]\\\\d{7}$";/
		if (TextUtils.isEmpty(mobiles))
			return false;
		else if(mobiles.matches(mobile)||mobiles.matches(cm)||mobiles.matches(cu)||mobiles.matches(ct)){
			return true;
		}else{
			return false;
		}

	}
	/**
	 * 必须是字母数字组合
	 */
	public static boolean isPass(String pwd)
	{
	
		String Regex = "^\\w+$";
		if (TextUtils.isEmpty(pwd))
			return false;
		else
			return pwd.matches(Regex);
	}
	
	/**
	 * 验证身份证格式
	 */
	public static boolean isUserNO(String NO)
	{
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "\\d{15}|\\d{18}|\\d{17}([0-9]|X|x)";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(NO))
			return false;
		else
			return NO.matches(telRegex);
	}
	
	/**
	 * 年龄的范围判断
	 */
	public static boolean isBeyondAge(int age)
	{
		if(age>0&&age<101)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 判断是否有网络连接,没有返回false
	 */
	public static boolean hasInternetConnected(Context mContext) {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}
	
	public static String stringFilter(String str)throws PatternSyntaxException
	{     
	      // 只允许字母、数字和汉字      
	      String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";                     
	      Pattern   p   =   Pattern.compile(regEx);     
	      Matcher   m   =   p.matcher(str);     
	      return   m.replaceAll("").trim();     
	}
	public static Boolean isChar(String str)throws PatternSyntaxException
	{
		// 只允许字母、数字和汉字
		String regEx = "^[^!@#$%^&*()-=+]+$";


		if (TextUtils.isEmpty(str))
		{
			return false;
		}
		else
		{
			if((str.matches(regEx)&&str.length()>1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public static Boolean isName(String str)throws PatternSyntaxException
	{     
		// 只允许字母、数字和汉字
		String regEx = "^[A-Za-z]+$";
		String regEx3 = "^[\u4e00-\u9fa5]{0,}$";
		String regEx2="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

		if (TextUtils.isEmpty(str))
		{
			return false;
		}
		else
		{
			if((str.matches(regEx3)&&str.length()>1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public static boolean stringForEmoji(String str)throws PatternSyntaxException
	{     
	      // 只允许字母、数字和汉字      
	      String   regEx  =  "[^0-9a-zA-Z\\u4e00-\\u9fa5.，,。？“”]+\n";
//	      Pattern   p   =   Pattern.compile(regEx);     
//	      Matcher   m   =   p.matcher(str);     
//	      return   m.replaceAll("").trim();    
	      
	      if (TextUtils.isEmpty(str))
				return false;
			else
				return str.matches(regEx);
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
}
