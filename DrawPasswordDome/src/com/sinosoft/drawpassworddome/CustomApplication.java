package com.sinosoft.drawpassworddome;


import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class CustomApplication extends Application{
	
	public static final String LOCSPWD_SHARED_KEY="main";
	private static CustomApplication app;
	public static CustomApplication getIntans(){
		if(app==null){
			app=new CustomApplication();
		}
		return app;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		sp=getSharedPreferences(LOCSPWD_SHARED_KEY, MODE_PRIVATE);
		backListener();
	}
	SharedPreferences sp;
	public int count = 0;
	private void backListener(){

       registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
//                Log.v("viclee", activity + "onActivityStopped");
                count--;
                if (count == 0) {
//                    Log.v("viclee", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                    Editor e = sp.edit();
                    e.putBoolean("lifecycle", true);
                    e.putBoolean("loginsuccess", false);
                    e.putBoolean("isCloseViewPassword", false);
//                    e.putBoolean("isCloseViewPassword", false);
                    e.commit();
                    if(null!=callBackInterface){
                    callBackInterface.isShow("no");
                    }
                }
                   
            }

            @Override
            public void onActivityStarted(Activity activity) {
//                Log.v("viclee", activity + "onActivityStarted");
                if (count == 0) {
//                    Log.v("viclee", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                	 if(null!=callBackInterface){
                	callBackInterface.isShow("lifecycle");
                	 }
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//                Log.v("viclee", activity + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
//                Log.v("viclee", activity + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                Log.v("viclee", activity + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                Log.v("viclee", activity + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                Log.v("viclee", activity + "onActivityCreated");
            }
        });
	}
	
	
	IsCallBackInterface callBackInterface;
	public interface IsCallBackInterface{
		public boolean isShow(String is);
	}
	public void showCallback(IsCallBackInterface callBackInterface){
		this.callBackInterface=callBackInterface;
	}
}
