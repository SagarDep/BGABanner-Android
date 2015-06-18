package com.stx.fleshfruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.stx.fleshfruit.home.SingleActivity;
import com.stx.fleshfruit.home.TaocanActivity;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	// 跳转到套餐界面
	public void taocan(View v) {
		Intent intent = new Intent(HomeActivity.this, TaocanActivity.class);
		startActivity(intent);
	}

	// 跳转到水果单点界面
	public void single(View v) {
		Intent intent = new Intent(HomeActivity.this, SingleActivity.class);
		startActivity(intent);
	}
}
