package com.zzu.ehome.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;

import static android.graphics.Bitmap.CompressFormat.JPEG;

public class ImageUtil {

	public static int getDegree(ExifInterface exif)
	{
		int degree = 0;

		if (exif != null)
		{

			// 读取图片中相机方向信息

			int ori = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,

					ExifInterface.ORIENTATION_UNDEFINED);

			// 计算旋转角度

			switch (ori)
			{

				case ExifInterface.ORIENTATION_ROTATE_90:

					degree = 90;

					break;

				case ExifInterface.ORIENTATION_ROTATE_180:

					degree = 180;

					break;

				case ExifInterface.ORIENTATION_ROTATE_270:

					degree = 270;

					break;

				default:

					degree = 0;

					break;

			}
		}
		return degree;
	}
	public static Bitmap rotateImage(Bitmap bmp, int degrees)
	{
		if (degrees != 0)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(degrees);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrix, true);
		}
		return bmp;
	}


	public static String Bitmap2StrByBase64(Bitmap bit){
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
		byte[] bytes=bos.toByteArray();
		return new String(org.kobjects.base64.Base64.encode(bytes));
		}

	/**
	 * 压缩图片返回压缩路径
	 * @param path
	 * @return
	 * @author guoqiang
	 */
	public static String doPicture(String path) {

		// 压缩图片的路径
		String new_path = path;
		File file = new File(path);
		Bitmap bm = null;
		if(file.length() / 1024 < 150) {
			bm = BitmapFactory.decodeFile(new_path);
			
		}
		else {
			bm = getSmallBitmap(path, 320, 480);
		}
		if(bm == null) {
			return new_path;
		}
		int degree = readPictureDegree(path);
		bm = rotaingImageView(degree, bm);
		FileOutputStream fos;
		String pathDir=Environment
		.getExternalStorageDirectory().getPath() + "/ehome/";
		try {
			if(file.length() / 1024 < 150) {
				fos = new FileOutputStream(new File(
						pathDir, file.getName()));
				bm.compress(JPEG, 100, fos);
				new_path = pathDir+ file.getName();
			}
			else {
				fos = new FileOutputStream(new File(
						pathDir, file.getName()));
				bm.compress(JPEG, 60, fos);
				new_path = pathDir+ file.getName();
			}
			bm.recycle();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new_path;
	}
	@SuppressLint("NewApi")
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

	 * 转换文件大小

	 * @param fileS

	 * @return

	 */

	private static String FormetFileSize(long fileS)

	{

	DecimalFormat df = new DecimalFormat("#.00");

	String fileSizeString = "";

	String wrongSize="0B";

	if(fileS==0){

	return wrongSize;

	}

	if (fileS < 1024){

	fileSizeString = df.format((double) fileS) + "B";

	 }

	else if (fileS < 1048576){

	fileSizeString = df.format((double) fileS / 1024) + "KB";

	}

	else if (fileS < 1073741824){

	    fileSizeString = df.format((double) fileS / 1048576) + "MB";

	  }

	else{

	    fileSizeString = df.format((double) fileS / 1073741824) + "GB";

	  }

	return fileSizeString;

	}
	/**

	* 获取指定文件大小

	* @param f

	* @return

	* @throws Exception

	*/

	private static long getFileSize(File file) throws Exception

	{

	long size = 0;

	 if (file.exists()){

	 FileInputStream fis = null;

	 fis = new FileInputStream(file);

	 size = fis.available();
	 }

	 else{
	 file.createNewFile();

	 }
	 return size;

	}
	
	/**
     * 根据Uri获取文件的路径
     * @Title: getUriString
     * @param uri
     * @return
     * @return String
     * @date 2012-11-28 下午1:19:31
     */
    public static String getUriString(Uri uri,ContentResolver cr){
    	String imgPath = null;
    	if (uri != null) {
    		String uriString = uri.toString();
    		//小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
    		//以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
    		//所以就不能通过query来查询，否则获取的cursor会为null。
    		if(uriString.startsWith("file")){ 
    			//uri的格式为file:///mnt....,将前七个过滤掉获取路径
    			imgPath = uriString.substring(7, uriString.length());
    			return imgPath;
    		}
    		Cursor cursor = cr.query(uri, null, null,null, null);
    		cursor.moveToFirst();
    		imgPath = cursor.getString(1); // 图片文件路径
    		
    	}
    	return imgPath;
    }
    /**

    * 调用此方法自动计算指定文件或指定文件夹的大小

    * @param filePath 文件路径

    * @return 计算好的带B、KB、MB、GB的字符串

    */

    public static long getAutoFileOrFilesSize(String filePath){

    File file=new File(filePath);

    long blockSize=0;

    try {

    if(file.isDirectory()){

    blockSize = getFileSizes(file);

    }else{

    blockSize = getFileSize(file);

    }

    } catch (Exception e) {

    e.printStackTrace();


    }

    return blockSize;

    }
    /**

    * 获取指定文件夹

    * @param f

    * @return

    * @throws Exception

    */

    private static long getFileSizes(File f) throws Exception

    {

    long size = 0;

    File flist[] = f.listFiles();

    for (int i = 0; i < flist.length; i++){

    if (flist[i].isDirectory()){

    size = size + getFileSizes(flist[i]);

    }

    else{

    size =size + getFileSize(flist[i]);

    }

    }

    return size;

    }
    /** 
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换 
     * @param activity 
     * @param imageUri 
     * @author guoqaing
     * @date 2014-10-12 
     */  
    @TargetApi(19)  
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {  
        if (context == null || imageUri == null)  
            return null;  
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {  
            if (isExternalStorageDocument(imageUri)) {  
                String docId = DocumentsContract.getDocumentId(imageUri);  
                String[] split = docId.split(":");  
                String type = split[0];  
                if ("primary".equalsIgnoreCase(type)) {  
                    return Environment.getExternalStorageDirectory() + "/" + split[1];  
                }  
            } else if (isDownloadsDocument(imageUri)) {  
                String id = DocumentsContract.getDocumentId(imageUri);  
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
                return getDataColumn(context, contentUri, null, null);  
            } else if (isMediaDocument(imageUri)) {  
                String docId = DocumentsContract.getDocumentId(imageUri);  
                String[] split = docId.split(":");  
                String type = split[0];  
                Uri contentUri = null;  
                if ("image".equals(type)) {  
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                } else if ("video".equals(type)) {  
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
                } else if ("audio".equals(type)) {  
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
                }  
                String selection = MediaStore.Images.Media._ID + "=?";  
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);  
            }  
        } // MediaStore (and general)  
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {  
            // Return the remote address  
            if (isGooglePhotosUri(imageUri))  
                return imageUri.getLastPathSegment();  
            return getDataColumn(context, imageUri, null, null);  
        }  
        // File  
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {  
            return imageUri.getPath();  
        }  
        return null;  
    }
    
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {  
        Cursor cursor = null;  
        String column = MediaStore.Images.Media.DATA;  
        String[] projection = { column };  
        try {  
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);  
            if (cursor != null && cursor.moveToFirst()) {  
                int index = cursor.getColumnIndexOrThrow(column);  
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

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}


	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	/**
	 * 获取图片储存路径
	 * @return
	 */
	private static String getDirPath() {
		String dirPath = null;
		// 判断sd卡是否存在
		if(Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED)) {
			dirPath = Environment.getExternalStorageDirectory().toString() + File.separator + "mmlove" + File.separator + "images"+File.separator;//获取跟目录
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}


		return dirPath;
	}
	/**
	 * 保存资源文件到本地（新浪微博）
	 *
	 */
	public static String saveResTolocal(Resources res, int resId, String name) {
		String path = null;
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(
					getDirPath(), name+".jpg"));
			bitmap.compress(JPEG, 100, fos);
			path = getDirPath()+name + ".jpg";
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
	}

}
