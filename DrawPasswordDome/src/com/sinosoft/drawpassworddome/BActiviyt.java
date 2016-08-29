package com.sinosoft.drawpassworddome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.drawpassworddome.component.LocusPassWordView;
import com.sinosoft.drawpassworddome.component.LocusPassWordView.OnCompleteListener;
import com.sinosoft.drawpassworddome.widget.ToggleButton;
import com.sinosoft.drawpassworddome.widget.ToggleButton.OnToggleChanged;

public class BActiviyt extends BaseActivity {
	private ToggleButton tb;
	private Button modificationBn;
	private LocusPassWordView locusPassWordView;
	private FrameLayout pwd_fl;
	private TextView topInfoTv;
	private LinearLayout topLayout;

	private SharedPreferences sp;
	
	private String showFramelayout="show";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b_layout);
		sp=getSharedPreferences(CustomApplication.LOCSPWD_SHARED_KEY, MODE_PRIVATE);
		initView();
		onListen();
		setViews();
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		if (sp.getString("passwordView", "").equals("")&&sp.getBoolean("startLoc", true)) {
			topLayout.setVisibility(View.GONE);
			pwd_fl.setVisibility(View.VISIBLE);
			modificationBn.setVisibility(View.GONE);
			topInfoTv.setText("创建新手势密码");
			showFramelayout="show";
		}else{
			topLayout.setVisibility(View.VISIBLE);
			pwd_fl.setVisibility(View.GONE);
			modificationBn.setVisibility(View.VISIBLE);
			topInfoTv.setText("旧手势密码");
			showFramelayout="gone";
		}
		if(sp.getBoolean("startLoc", true)){
			tb.setToggleOn();
			modificationBn.setVisibility(View.VISIBLE);
		}else{
			tb.setToggleOff();
			modificationBn.setVisibility(View.GONE);
		}
	}
	private void setViews() {
		pwd_fl.setVisibility(View.GONE);	
		showFramelayout="gone";
	}

	private void initView() {
		tb = (ToggleButton) findViewById(R.id.setting_locatPwd_tb);
		modificationBn = (Button) findViewById(R.id.setting_modifi_bn);
		locusPassWordView = (LocusPassWordView) findViewById(R.id.setting_locusview);
		pwd_fl = (FrameLayout) findViewById(R.id.setting_pwdView_fl);
		topInfoTv=(TextView) findViewById(R.id.setting_top_info);
		topLayout=(LinearLayout) findViewById(R.id.setting_top_ll);
	}
	
	private void onListen() {
		
		tb.setOnToggleChanged(new OnToggleChanged(){

			@Override
			public void onToggle(boolean is) {
				if(is){
					Editor e=sp.edit();
					e.putBoolean("startLoc", true);
					e.putBoolean("isCloseViewPassword", true);
					e.commit();
					if(sp.getString("passwordView", "").equals("")){
						topLayout.setVisibility(View.GONE);
						pwd_fl.setVisibility(View.VISIBLE);
						topInfoTv.setText("创建新手势密码");
						showFramelayout="show";
					}else{
						topInfoTv.setText("旧手势密码");
						pwd_fl.setVisibility(View.VISIBLE);
						showFramelayout="show";
					}
					
					modificationBn.setVisibility(View.VISIBLE);
				}else{
					Editor e=sp.edit();
					e.putBoolean("startLoc", false);
					e.putBoolean("loginsuccess", false);
					e.putBoolean("isCloseViewPassword", true);
					e.commit();
					modificationBn.setVisibility(View.GONE);
					pwd_fl.setVisibility(View.GONE);
					
					showFramelayout="gone";
				}				
			}});
		
		modificationBn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				System.out.println(showFramelayout);
				if(showFramelayout.equals("show")){
					showFramelayout="gone";
					pwd_fl.setVisibility(View.GONE);
				}else{
					pwd_fl.setVisibility(View.VISIBLE);
					showFramelayout="show";
					if(!sp.getString("passwordView", "").equals("")){
//						//修改密码
						topInfoTv.setText("旧手势密码");
					}
				}
			}
		});
		locusPassWordView.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String password) {
				if(sp.getString("passwordView", "").equals("")){
					//新手势密码
					modificationPassword( password,true);
				}else{
					
					if(isTrueOrFalse(password,locusPassWordView,BActiviyt.this)){
						topInfoTv.setText("设置新手势密码");
						Editor e = sp.edit();
						e.putString("passwordView", "");
						e.commit();
						flagNewPwd=0;
						}
					}
			}
		});
	}
	String newPassword="";
	int flagNewPwd=0;
	private boolean modificationPassword(String password,boolean newPwd){
		if(flagNewPwd==0){
			newPassword=password;
			flagNewPwd+=1;
			topInfoTv.setText("重复上次手势密码");
			locusPassWordView.clearPassword();
		}else{
		   if(newPassword.equals(password)){
				Editor e = sp.edit();
				e.putString("passwordView", newPassword);
				e.commit();
				Toast.makeText(BActiviyt.this, "密码设置成功", Toast.LENGTH_SHORT).show();
				locusPassWordView.clearPassword();
				if(newPwd){
					topLayout.setVisibility(View.VISIBLE);
				}
				pwd_fl.setVisibility(View.GONE);
				showFramelayout="gone";
				flagNewPwd=0;
				newPassword="";
				return true;
			}else{
				Toast.makeText(BActiviyt.this, "两次密码不同！请重新次输入", Toast.LENGTH_SHORT).show();
				locusPassWordView.markError();
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		finish();
	}
}
