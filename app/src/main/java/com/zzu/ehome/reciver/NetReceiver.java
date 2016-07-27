package com.zzu.ehome.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.zzu.ehome.bean.NetEvent;
import com.zzu.ehome.utils.NetUtils;

import de.greenrobot.event.EventBus;

/**
 * @Description:网络状态的Receive
 * @author http://blog.csdn.net/finddreams
 */ 
public class NetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetUtils.isNetworkConnected(context);
			if (isConnected) {
				EventBus.getDefault().post(new NetEvent(true));
			} else {
				EventBus.getDefault().post(new NetEvent(false));
			}
		}
	}

}
