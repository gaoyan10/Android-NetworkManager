package com.yan.netmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetBroadcastReceiver extends BroadcastReceiver {
	public NetBroadcastReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean hasActiveNetwork = NetUtils.isNetworkConnected(context);
		if (hasActiveNetwork) {
			NetStatusManager.getInstance().changeStatus(NetStatus.WAIT);
			NetStatusManager.getInstance().refreshStatus();
		}else {
			NetStatusManager.getInstance().changeStatus(NetStatus.BREAK);
		}
	}

}
