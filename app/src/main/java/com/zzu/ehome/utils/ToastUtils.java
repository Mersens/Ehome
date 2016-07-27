package com.zzu.ehome.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Toast toast = null;

	public static void showMessage(final Context act, final String msg) {
		if(act!=null){
			showMessage(act, msg, Toast.LENGTH_LONG);
		}
		
	}

	public static void showMessage(final Context act, final int msg) {
		if(act!=null){
			showMessage(act, msg, Toast.LENGTH_LONG);
		}
		
	}

	public static void showMessage(final Context act, final int msg,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (toast != null) {
					toast.setText(msg);
					toast.setDuration(len);
				} else {
					toast = Toast.makeText(act, msg, len);
				}
				toast.show();
			}
		});
	}

	public static void showMessage(final Context act, final String msg,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (toast != null) {
					toast.setText(msg);
					toast.setDuration(len);
				} else {
					toast = Toast.makeText(act, msg, len);
				}
				toast.show();
			}
		});
	}  
}
