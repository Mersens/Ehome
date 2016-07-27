package com.zzu.ehome.application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zzu.ehome.main.ehome.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

/** 
 *  
 *  
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。  
 *                           如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框 
 *                           实现该接口并注册为程序中的默认未捕获异常处理  
 *                           这样当未捕获异常发生时，就可以做些异常处理操作 
 *                           例如：收集异常信息，发送错误报告 等。 
 *  
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告. 
 */  
public class CrashHandler implements UncaughtExceptionHandler {  
    /** Debug Log Tag */  
    public static final String TAG = "CrashHandler";  
    /** CrashHandler实例 */  
    private static CrashHandler INSTANCE;  
    /** 程序的Context对象 */  
    private Context mContext;  
    /** 系统默认的UncaughtException处理类 */  
    private UncaughtExceptionHandler mDefaultHandler;
    
    private SendReports sendReports=null;
  //用来存储设备信息和异常信息  
     private Map<String, String> infos = new HashMap<String, String>();  
     //用于格式化日期,作为日志文件名的一部分  
      private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");


    /** 使用Properties来保存设备的信息和错误堆栈信息 */  
    private Properties mDeviceCrashInfo = new Properties();  
    private static final String VERSION_NAME = "versionName";  
    private static final String VERSION_CODE = "versionCode";  
    private static final String STACK_TRACE = "STACK_TRACE";  
    /** 错误报告文件的扩展名 */  
    private static final String CRASH_REPORTER_EXTENSION = ".cr";  
      
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        if (INSTANCE == null)  
            INSTANCE = new CrashHandler();  
        return INSTANCE;  
    }  
      
    /** 
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器 
     *  
     * @param ctx 
     */  
    public void init(Context ctx) {  
        mContext = ctx;  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
      
    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
            // 如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);
            CustomApplcation.getInstance().exit();
        } else {  
            // Sleep一会后结束程序  
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序  
            try {  
                Thread.sleep(4000);  
            } catch (InterruptedException e) {  
            	Log.e(TAG, "Error : ", e);  
            }  
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(10);  
        }  
    }  
  
    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false 
     */  
    private boolean handleException(final Throwable ex) {  
        if (ex == null) {  
            return true;  
        }  
        final String msg = ex.getLocalizedMessage();
        // 收集设备信息
        collectCrashDeviceInfo(mContext);
        // 使用友盟SDK将错误报告保存到文件中，待下次应用程序重启时上传log
        saveCrashInfo2File(ex);
     
        // 使用Toast来显示异常信息  
        new Thread() {  
            @Override  
            public void run() {  
                // Toast 显示需要出现在一个线程的消息队列中  
                Looper.prepare();  
                Toast.makeText(mContext, "程序迷路了,请关闭应用后重试！", Toast.LENGTH_SHORT).show();  
                Looper.loop();  
            }  
        }.start();  
        

//        String crashFileName = saveCrashInfoToFile(ex);  
        // 发送错误报告到服务器
        //已经使用了友盟SDK上传，故此处注掉
        //sendCrashReportsToServer(mContext);  
        restartApplication();
        return true;  
    }
    private void restartApplication() {
        Intent intent = new Intent(mContext,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        CustomApplcation.getInstance().exit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    /** 
     * 收集程序崩溃的设备信息 
     *  
     * @param ctx 
     */  
    public void collectCrashDeviceInfo(Context ctx) {  
        try {  
            // Class for retrieving various kinds of information related to the  
            // application packages that are currently installed on the device.  
            // You can find this class through getPackageManager().  
            PackageManager pm = ctx.getPackageManager();  
            // getPackageInfo(String packageName, int flags)  
            // Retrieve overall information about an application package that is installed on the system.  
            // public static final int GET_ACTIVITIES  
            // Since: API Level 1 PackageInfo flag: return information about activities in the package in activities.  
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                // public String versionName The version name of this package,  
                // as specified by the <manifest> tag's versionName attribute.  
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);  
                // public int versionCode The version number of this package,   
                // as specified by the <manifest> tag's versionCode attribute.  
                mDeviceCrashInfo.put(VERSION_CODE, String.valueOf(pi.versionCode));  
            } 
            //添加渠道号
            mDeviceCrashInfo.put("ctype", "mamaaiwo");
            
        } catch (NameNotFoundException e) {  
        	Log.e(TAG, "Error while collect package info", e);  
        }  
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,  
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息  
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段  
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                // setAccessible(boolean flag)  
                // 将此对象的 accessible 标志设置为指示的布尔值。  
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常  
                field.setAccessible(true);  
                mDeviceCrashInfo.put(field.getName(), String.valueOf(field.get(null)));  
                Log.d(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
            	Log.e(TAG, "Error while collect crash info", e);  
            }  
        }  
    }  
      
    /** 
     * 保存错误信息到文件中 
        *  
     * @param ex 
      * @return  返回文件名称,便于将文件传送到服务器 
        */  
        private String saveCrashInfo2File(Throwable ex) {  
             
          StringBuffer sb = new StringBuffer();  
          for (Map.Entry<String, String> entry : infos.entrySet()) {  
               String key = entry.getKey();  
                String value = entry.getValue();  
               sb.append(key + "=" + value + "\n");  
            }  
            
          Writer writer = new StringWriter();  
          PrintWriter printWriter = new PrintWriter(writer);  
           ex.printStackTrace(printWriter);  
           Throwable cause = ex.getCause();  
          while (cause != null) {  
               cause.printStackTrace(printWriter);  
                cause = cause.getCause();  
          }  
           printWriter.close();  
         String result = writer.toString();
            Log.e("RESULT",result);
          sb.append(result);  
           try {  
               long timestamp = System.currentTimeMillis();  
               String time = formatter.format(new Date());  
             String fileName = "crash-" + time + "-" + timestamp + ".log";  
              if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
                   String path = "/sdcard/ehomecrash/";
                   File dir = new File(path);  
                  if (!dir.exists()) {  
                        dir.mkdirs();  
                   }  
                   FileOutputStream fos = new FileOutputStream(path + fileName);  
                  fos.write(sb.toString().getBytes());  
                    fos.close();  
               }  
                return fileName;  
         } catch (Exception e) {  
             Log.e(TAG, "an error occured while writing file...", e);  
            }  
            return null;  
       }  

    /** 
     * 保存错误信息到文件中 
     *  
     * @param ex 
     * @return 
     */  
    private String saveCrashInfoToFile(Throwable ex) {  
        Writer info = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(info);  
        // printStackTrace(PrintWriter s)  
        // 将此 throwable 及其追踪输出到指定的 PrintWriter  
        ex.printStackTrace(printWriter);  
  
        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
  
        // toString() 以字符串的形式返回该缓冲区的当前值。  
        String result = info.toString();  
        printWriter.close();  
        mDeviceCrashInfo.put(STACK_TRACE, result);  
        Log.e(TAG, result);  
        //友盟是现将错误信息保存在com_umeng__crash.cache文件中，然后在应用程序启动时调用MobclickAgent.onError(context);来启动一线程上传log
        String log=mDeviceCrashInfo.toString();
        //MobclickAgent.reportError(mContext,log);
        return null;  
    }  
      
    /** 
     * 把错误报告发送给服务器,包含新产生的和以前没发送的. 
     *  
     * @param ctx 
     */  
    public void sendCrashReportsToServer(Context ctx) {  
        String[] crFiles = getCrashReportFiles(ctx);  
        if (crFiles != null && crFiles.length > 0) {  
            TreeSet<String> sortedFiles = new TreeSet<String>();  
            sortedFiles.addAll(Arrays.asList(crFiles));  
  
            for (String fileName : sortedFiles) {  
                File cr = new File(ctx.getFilesDir(), fileName);  
                postReport(ctx,cr);  
                cr.delete();// 删除已发送的报告  
            }  
        }  
    }  
  
    /** 
     * 获取错误报告文件名 
     *  
     * @param ctx 
     * @return 
     */  
    private String[] getCrashReportFiles(Context ctx) {  
        File filesDir = ctx.getFilesDir();  
        // 实现FilenameFilter接口的类实例可用于过滤器文件名  
        FilenameFilter filter = new FilenameFilter() {  
            // accept(File dir, String name)  
            // 测试指定文件是否应该包含在某一文件列表中。  
            public boolean accept(File dir, String name) {  
                return name.endsWith(CRASH_REPORTER_EXTENSION);  
            }  
        };  
        // list(FilenameFilter filter)  
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录  
        return filesDir.list(filter);  
    }  
  
    private void postReport(Context ctx,File file) {  
    	try{
	    	FileInputStream fin = new FileInputStream(file);
	    	StringBuffer localStringBuffer = new StringBuffer();
	    	byte [] buffer = new byte[1024];
	    	int i = 0;
	        while ((i = fin.read(buffer)) != -1){
	        	localStringBuffer.append(new String(buffer, 0, i));
	        }
	        fin.close();
	    	//MobclickAgent.reportError(ctx,localStringBuffer.toString());
    	}catch(Exception e){ 
            e.printStackTrace(); 
    	}
    }  
  
    /** 
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告 
     */  
    public void sendPreviousReportsToServer() { 
    	if(null!=sendReports&&!sendReports.isAlive()){
    		sendReports.start();
    	}
    }  
    
    private final class SendReports extends Thread{

    	private Context mContext; 
    	private final Object lock = new Object();
    	
		public SendReports(Context mContext) {
			this.mContext=mContext;
		}
		public void run(){
			try{
				synchronized (this.lock){
					sendCrashReportsToServer(mContext);
				}
			}catch(Exception e){
				Log.e(TAG, "SendReports error.",e);
			}
		}
    	
    }
}  

