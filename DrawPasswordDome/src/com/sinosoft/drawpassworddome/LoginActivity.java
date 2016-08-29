package com.sinosoft.drawpassworddome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText usernameEd,passwordEd;
	Button clearUsernamBn,clearPwdBn,loginBn;
	CheckBox saveCb;
	SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		sp=getSharedPreferences(CustomApplication.LOCSPWD_SHARED_KEY, MODE_PRIVATE);
		initView();
		onListener();
		setViewValue();
	}

	private void setViewValue() {
		if(!sp.getString("username", "").equals("")){
			usernameEd.setText(Base64Util.getFromBase64(sp.getString("username", "")));
		}
	}

	private void onListener() {
		loginBn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(usernameEd.getText().toString().trim().equals("")||
						usernameEd.getText().toString().trim().equals(null)){
					Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(passwordEd.getText().toString().equals("")||
						passwordEd.getText().toString().trim().equals(null)){
					Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				Editor e=sp.edit();
				e.putString("username", Base64Util.Base64Code(usernameEd.getText().toString()));
				e.putString("password", Base64Util.Base64Code(passwordEd.getText().toString()));
				e.putBoolean("loginsuccess", true);
				e.commit();
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initView() {
		usernameEd=(EditText) findViewById(R.id.login_name);
		passwordEd=(EditText) findViewById(R.id.login_password);
		clearUsernamBn=(Button) findViewById(R.id.login_clearname);
		clearPwdBn=(Button) findViewById(R.id.login_clearpassword);
		loginBn=(Button) findViewById(R.id.login_login);
		saveCb=(CheckBox) findViewById(R.id.login_savepwd);
	}
}
