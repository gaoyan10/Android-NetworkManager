# Android-NetworkManager
a library for check network available on Android, which uses ping or request url to get the real network status.


How to use
---
1. Download this repo and import eclipse as library project. Make your project depend on this project.
2. In your AndroidManifest.xml, add the code below
    `<receiver   
			android:name="me.yan.network.NetBroadcastReceiver"
            android:label="NetworkConnection" >
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
            </intent-filter>
    </receiver>`
  add uses-permission below
	`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />`  
3. In your activity onResume method, add 
   `NetStatusManager.getInstance().refreshStatus();`
4. When you want to access network state, use like below
	`NetStatusManager.getInstance().getNetStatus()`  	which return the network status.
5. Config your ping host
   `NetStatusManager.getInstance().config(String hostname)`  
	the default host is 'www.baidu.com'. Other config, read the source code.
