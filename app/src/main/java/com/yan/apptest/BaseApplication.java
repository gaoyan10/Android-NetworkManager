package com.yan.apptest;

import android.app.Application;

import com.yan.netmanager.NetStatusManager;

/**
 * Created by Yan on 2015/7/9.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetStatusManager.getInstance().config("http://www.baidu.com", "http://121.42.54.50:8001/ping");
        NetStatusManager.getInstance().setCheckType(false);
    }
}
