# Android-NetworkManager
a library for check network available on Android, which uses ping or request url to get the real network status.


How to use
---
1. Download this repo and import eclipse as library project. Make your project depend on this project.
2. Optional——Config the library in your application onCreate method or activity onCreate method. Use like below
    config ping host and request url(optional, default is "http://www.baidu.com"):   
	` NetStatusManager.getInstance().config("http://www.baidu.com", "http://www.baidu.com");`
    config use ping or request url(optional, default is request url)
	`NetStatusManager.getInstance().setCheckType(false);`	
    config request timeout ( optional, default connect time out is 2000ms and read time out is 2000ms)
        `NetStatusManager.getInstance().configTimeOut(2000, 2000)`
3. In your activity onResume method, add 
   `NetStatusManager.getInstance().refreshStatus();`
4. When you want to access network state, use like below
	`NetStatusManager.getInstance().getNetStatus()`  	which return the network status.
