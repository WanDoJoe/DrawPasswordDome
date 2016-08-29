package com.sinosoft.drawpassworddome;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
	Context mContext;
	TextView settingTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		onListener();
	}
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	private void onListener() {
		settingTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent=new Intent(MainActivity.this,BActiviyt.class);
				startActivity(intent);
			}
		});
	}
	private void initView() {
		mContext=this;
		settingTv=(TextView) findViewById(R.id.main_setting);
		
	}
	
	
}
