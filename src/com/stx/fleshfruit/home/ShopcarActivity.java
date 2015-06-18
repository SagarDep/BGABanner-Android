package com.stx.fleshfruit.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stx.fleshfruit.HomeActivity;
import com.stx.fleshfruit.LoginActivity;
import com.stx.fleshfruit.R;
import com.stx.fleshfruit.util.IPAddress;

public class ShopcarActivity extends Activity {
	public List<Map<String, Object>> Shopcart;
	private ListView lv;
	private SimpleAdapter sa;
	private Handler handler;
	private String result;

	// private TextView prices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopcar);
		lv = (ListView) findViewById(R.id.shopcar_listView);
		Shopcart = new ArrayList<Map<String, Object>>();
		sa = new SimpleAdapter(this, Shopcart, R.layout.layout_shopcart,
				new String[] { "image", "gname", "price" }, new int[] {
						R.drawable.photo, R.id.name, R.id.price });
		lv.setAdapter(sa);
		Intent intent = getIntent();
		final String gdid = intent.getStringExtra("gdid");
		Log.i("kankan", gdid);
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = "";
				try {
					// 创建连接
					HttpClient httpClient = new DefaultHttpClient();
					// 把接口地址进行封装
					HttpPost post = new HttpPost(IPAddress.SHOPCART
							+ ";jsessionid=" + LoginActivity.JSESSIONID);
					// 设置参数，仿html表单提交
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					BasicNameValuePair param = new BasicNameValuePair("gdid",
							gdid);
					paramList.add(param);
					post.setEntity(new UrlEncodedFormEntity(paramList,
							HTTP.UTF_8));
					// 发送HttpPost请求，并返回HttpResponse对象
					HttpResponse httpResponse = httpClient.execute(post);
					// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 获取返回结果
						result = EntityUtils.toString(httpResponse.getEntity());
					}
					// send the data to ui thread
					// 1.set the data
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("sc", result);
					msg.setData(data);
					// 2.use handler to send the message
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}).start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				result = (String) data.get("sc");
				System.out.println(result);
				Shopcart.clear();
				Shopcart.addAll(JSONParser.parseShopcartJson(result));
				// call the adapter to reflesh the listview
				sa.notifyDataSetChanged();
			}
		};
	}

	public void cancle(View v) {
		Intent intent = new Intent(ShopcarActivity.this, HomeActivity.class);
		startActivity(intent);
		ShopcarActivity.this.finish();

	}
}
