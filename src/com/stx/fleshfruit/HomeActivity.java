package com.stx.fleshfruit;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.stx.fleshfruit.home.ShopcarActivity;
import com.stx.fleshfruit.home.SingleActivity;
import com.stx.fleshfruit.home.TaocanActivity;
import com.stx.fleshfruit.scroll.ScrollImage;

public class HomeActivity extends Activity {
	private ScrollImage scrollImage;
	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		scrollImage = (ScrollImage) findViewById(R.id.myscrollimg);
		List<Bitmap> list = new ArrayList<Bitmap>();

		// 添加轮播图片
		list.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.pinpan1));
		list.add(BitmapFactory.decodeResource(getResources(),
				R.drawable.pinpan2));
		list.add(BitmapFactory
				.decodeResource(getResources(), R.drawable.caomei));
		list.add(BitmapFactory.decodeResource(getResources(), R.drawable.xueba));
		list.add(BitmapFactory.decodeResource(getResources(), R.drawable.guozo));
		list.add(BitmapFactory
				.decodeResource(getResources(), R.drawable.yintao));
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int x = (int) (0.7 * width * list.get(0).getHeight() / list.get(0)
				.getWidth());
		Log.i("MainActivity", x + "");
		scrollImage.setHeight((int) (x));// 图片的高度
		scrollImage.setBitmapList(list);
		scrollImage.start(3000);
	}

	// 跳转到套餐界面
	public void taocan(View v) {
		Intent intent = new Intent(HomeActivity.this, TaocanActivity.class);
		startActivity(intent);
		HomeActivity.this.finish();
	}

	// 跳转到水果单点界面
	public void single(View v) {
		Intent intent = new Intent(HomeActivity.this, SingleActivity.class);
		startActivity(intent);
		HomeActivity.this.finish();
	}

	// 按两次退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
