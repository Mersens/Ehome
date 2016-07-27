package com.zzu.ehome.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;


import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.application.MMloveConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadServiceForAPK extends Service
{
	private static final int NOTIFY_ID = 0;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;
	// 返回的安装包url
	private String apkUrl = Constants.DOWNLOADURL;
	// private String apkUrl = MyApp.downloadApkUrl;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/ehome/";

	private static final String saveFileName = savePath + "ehome.apk";
	// private MyApp app;
	private boolean serviceIsDestroy = false;

	private Context mContext = this;
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what)
			{
				case 0:
					// app.setDownload(false);
					// 下载完毕
					// 取消通知
					mNotificationManager.cancel(NOTIFY_ID);
					installApk();
					break;
				case 2:
					//app.setDownload(false);
					// 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
					// 取消通知
					mNotificationManager.cancel(NOTIFY_ID);
					break;
				case 1:
					int rate = msg.arg1;
					//app.setDownload(true);
					if (rate < 100) {
						RemoteViews contentview = no.contentView;

						contentview.setTextViewText(R.id.tv_progress, rate + "%");
						contentview.setProgressBar(R.id.progressbar, 100, rate, false);
						contentview.setViewVisibility(R.id.progressbar, View.VISIBLE);
						mNotification.setContent(contentview);


					} else {
						System.out.println("下载完毕!!!!!!!!!!!");
						// 下载完毕后变换通知形式
						no.flags = Notification.FLAG_AUTO_CANCEL;

						mNotification.setContentTitle("下载完成").setContentText("文件已下载完毕");
						mNotification.build();

						serviceIsDestroy = true;
						

						
						stopSelf();// 停掉服务自身
					}
					mNotificationManager.notify(NOTIFY_ID, no);
					break;
			}
		}
	};



	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		System.out.println("是否执行了 onBind");
		return null;
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("downloadservice ondestroy");

	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		// TODO Auto-generated method stub
		System.out.println("downloadservice onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent)
	{
		// TODO Auto-generated method stub

		super.onRebind(intent);
		System.out.println("downloadservice onRebind");
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// setForeground(true);// 这个不确定是否有作用
		// app = (MyApp) getApplication();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		// CreateInform();
		setUpNotification();
		startDownload();
		return super.onStartCommand(intent, flags, startId);
	}

	private void startDownload()
	{
		// TODO Auto-generated method stub
		canceled = false;
		downloadApk();
	}

	//
	Notification.Builder mNotification;
	Notification no;

	// 通知栏
	/**
	 * 创建通知
	 */
	private void setUpNotification()
	{
		int icon =R.mipmap.push;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
//		mNotification = new Notification(icon, tickerText, when);
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.download_notification_layout);
		contentView.setImageViewResource(R.id.image, R.mipmap.push);


		contentView.setTextViewText(R.id.name, " 健康E家 正在下载...");


		mNotification = new Notification.Builder(mContext);
		mNotification.setContent(contentView).setOngoing(true).setContentText(tickerText).setOngoing(false)
				.setSmallIcon(icon).setContentTitle(tickerText).setWhen(when).setAutoCancel(true).setPriority(Notification.PRIORITY_DEFAULT);
//		// 放置在"正在运行"栏目中

//
//
//
//		// 指定个性化视图
//		mNotification.contentView = contentView;
		no=mNotification.build();
		no.flags = Notification.FLAG_ONGOING_EVENT;
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);

		// Intent intent = new Intent(this, NotificationUpdateActivity.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// intent,PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		// mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, no);

	}

	//
	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private Thread downLoadThread;

	private void downloadApk()
	{
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk()
	{
		File apkfile = new File(saveFileName);
		if (!apkfile.exists())
		{
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	/** Notification构造器 */




	private int lastRate = 0;
	private Runnable mdownApkRunnable = new Runnable()
	{
		public void run()
		{
			try
			{
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists())
				{
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do
				{
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1)
					{
						mHandler.sendMessage(msg);
						lastRate = progress;
					}
					if (numread <= 0)
					{
						// 下载完成通知安装
						mHandler.sendEmptyMessage(0);
						// 下载完了，cancelled也要设置
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				}
				while (!canceled);// 点击取消就停止下载.

				fos.close();
				is.close();
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	};

}
