package com.sinosoft.drawpassworddome;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.drawpassworddome.CustomApplication.IsCallBackInterface;
import com.sinosoft.drawpassworddome.component.LocusPassWordView;
import com.sinosoft.drawpassworddome.component.LocusPassWordView.OnCompleteListener;

public class BaseActivity extends Activity implements IsCallBackInterface{
	
	private LocusPassWordView locusPwV;
	private TextView topTv;//, settingTv, clearPwdTv;
	private Button forgetBn, unUserPwdBn;
	private View lv;
	private SharedPreferences sp;
	private String newPassword="";
	private int flagNewPwd=0;
	String username = "";
	String password = "";
	static List<Activity> allActivity=new ArrayList<Activity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allActivity.add(this);
		
	}
	protected void finishAllActivity(){
		for (Activity activity:allActivity) {
			activity.finish();
		}
	}
	private void onListener() {

		locusPwV.setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void onComplete(String password) {
				if(sp.getBoolean("isForget", true)||sp.getString("passwordView","").equals("")){
					System.out.println("setPassword");
					setPassword(password,locusPwV);
				}else{
					if(isTrueOrFalse(password, locusPwV, BaseActivity.this)){
						//TODO 
						setGoneLoceView();
					}
				}
			}
		});
		
		forgetBn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Editor e = sp.edit();
				e.putString("passwordView", "");
				e.putBoolean("isForget", true);
				e.commit();
				Intent intent=new Intent(BaseActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		unUserPwdBn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lv.setVisibility(View.GONE);
				Editor e = sp.edit();
				e.putBoolean("startLoc", false);
				e.putString("isfirst", "1");
				e.putBoolean("isForget",false);
				e.commit();
			}
		});
	}
	
	@Override
	public void setContentView(int layoutResID) {
		View content = LayoutInflater.from(BaseActivity.this).inflate(layoutResID, null);
		setContentView(content);
	}
	@Override
	public void setContentView(View view) {
		FrameLayout layout=new FrameLayout(this);
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.FILL_PARENT);
		view.setLayoutParams(params);
		layout.addView(view);
		
		lv=LayoutInflater.from(this).inflate(R.layout.base_layout, null);
		lv.bringToFront();
		layout.addView(lv, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		super.setContentView(layout);
		initView();
		
	}
	
	private boolean setPassword(String password,LocusPassWordView locusPwV){
		if(flagNewPwd==0){
			newPassword=password;
			flagNewPwd+=1;
			locusPwV.clearPassword();
			System.out.println("第一次密码-"+newPassword+"--"+password);
		}else{
			System.out.println("第二次密码-"+newPassword+"--"+password);
			if(newPassword.equals(password)){
				Editor e = sp.edit();
				e.putString("passwordView", newPassword);
				e.commit();
				Toast.makeText(BaseActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
				locusPwV.clearPassword();
				setGoneLoceView();
				return true;
			}else{
				Toast.makeText(BaseActivity.this, "两次密码不同！请重新次输入", Toast.LENGTH_SHORT).show();
				locusPwV.markError();
				return false;
			}
		}
		return false;
	}
	
	private void initView() {
		locusPwV=(LocusPassWordView) findViewById(R.id.drawpassword_locusview);
		topTv=(TextView) findViewById(R.id.main_top);
		forgetBn=(Button) findViewById(R.id.main_forget);
		unUserPwdBn=(Button) findViewById(R.id.main_no);
	}
	protected void setGoneLoceView(){
		lv.startAnimation(setAnimation(lv));
		Editor e = sp.edit();
		e.putString("isfirst", "1");
		e.putBoolean("lifecycle", false);
		e.commit();
	}
	@Override
	protected void onResume() {
		super.onResume();
		onListener();
		
		sp = getSharedPreferences(CustomApplication.LOCSPWD_SHARED_KEY, MODE_PRIVATE);
		username = sp.getString("username", "");
		password = sp.getString("password", "");
		if ((username.equals("") && password.equals(""))
				|| sp.getString("isfirst", "").equals("")&&!sp.getBoolean("loginsuccess", false)) {
			// 首次登录 调到登录页面
			Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}else{
				if(!sp.getBoolean("startLoc", true)){
					lv.setVisibility(View.GONE);
					if(sp.getBoolean("isCloseViewPassword", false)){
						Editor e=sp.edit();
						
						e.commit();
						return;
					}
					if(!sp.getBoolean("loginsuccess", false)){
						Intent intent=new Intent(BaseActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
						}else{
						}
				}else{
					
					if(sp.getBoolean("isCloseViewPassword", false)){
						lv.setVisibility(View.GONE);
						Editor e=sp.edit();
						
						e.commit();
						return;
					}
					if(sp.getBoolean("isForget", true)||sp.getString("passwordView", "").equals("")){//创建手势密码
						Toast.makeText(BaseActivity.this, "请创建您的手势密码", Toast.LENGTH_SHORT).show();
						System.out.println("请创建您的手势密码");
						Editor e = sp.edit();
						e.putBoolean("isForget", false);
						e.commit();
						forgetBn.setVisibility(View.GONE);
						lv.setVisibility(View.VISIBLE);
						unUserPwdBn.setVisibility(View.VISIBLE);
						setTopInfo("请创建您的手势密码",false);
					}else{
						System.out.println(sp.getBoolean("loginsuccess", false));
						setTopInfo("请绘制手势密码",true);
						GoneView();
				}
			}//
			
		}
	}

	protected Animation setAnimation(final FrameLayout frameLayout) {
		// 初始化渐变动画
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.main_lacu_alpha);
		// 设置动画监听器
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 当监听到动画结束时，开始跳转到MainActivity中去
				frameLayout.setVisibility(View.GONE);
			}
		});
		return animation;
	}
	protected Animation setAnimation(final View frameLayout) {
		// 初始化渐变动画
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.main_lacu_alpha);
		// 设置动画监听器
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 当监听到动画结束时，开始跳转到MainActivity中去
				frameLayout.setVisibility(View.GONE);
			}
		});
		return animation;
	}
	private void setTopInfo(String info,boolean is){
		if(is){
			topTv.setTextColor(getResources().getColor(R.color.holo_blue_light));
		}else{
			topTv.setTextColor(getResources().getColor(R.color.black));
		}
		topTv.setText(info);
	}
	protected boolean isTrueOrFalse(String password,LocusPassWordView locusPassWordView,Context context){
		if(!sp.getString("passwordView", "").equals("")
				&&password.equals(sp.getString("passwordView", ""))){
			locusPassWordView.clearPassword();
		return true;
	}else{
		Toast.makeText(context, "手势密码错误", Toast.LENGTH_SHORT).show();
		topTv.setText("密码错误");
		topTv.setTextColor(getResources().getColor(R.color.red));
		locusPassWordView.markError();
		return false;
		}
	}
	
	private void GoneView(){
		if(sp.getBoolean("lifecycle", true)){
			lv.setVisibility(View.VISIBLE);
		}else{
			lv.setVisibility(View.GONE);
		}
		CustomApplication.getIntans().showCallback(this);
		System.out.println("ssssssssssss=="+s);
	}
	String s;
	@Override
	public boolean isShow(String is) {
		if (null != is) {
			s=is;
			if (is.equals("no")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(lv.getVisibility()==View.VISIBLE){
			for (Activity activity:allActivity) {
				activity.finish();
			}
		}
	}
	
	
}
