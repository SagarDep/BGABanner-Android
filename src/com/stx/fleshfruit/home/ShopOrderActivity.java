package com.stx.fleshfruit.home;

import java.net.HttpURLConnection;
import java.net.URL;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.stx.fleshfruit.LoginActivity;
import com.stx.fleshfruit.R;
import com.stx.fleshfruit.entity.Shopcart;
import com.stx.fleshfruit.util.IPAddress;
import com.stx.fleshfruit.util.StreamParser;

public class ShopOrderActivity extends Activity {
	public List<Map<String, Object>> OrderS;
	private ListView lv;
	private SimpleAdapter sa;
	private Handler handler;
	private String result;
	private Button sureCheckOut;
	public static String gdid;
	public static String sales;
	private ImageView cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_order);
		lv = (ListView) findViewById(R.id.order_listview);
		cancle = (ImageView) findViewById(R.id.cancle);
		sureCheckOut = (Button) findViewById(R.id.sureCheckOut);
		OrderS = new ArrayList<Map<String, Object>>();
		sa = new SimpleAdapter(this, OrderS, R.layout.layout_shopcart,
				new String[] { "image", "gname", "price" }, new int[] {
						R.drawable.photo, R.id.name, R.id.price });
		lv.setAdapter(sa);
		ClickListener cl = new ClickListener();
		cancle.setOnClickListener(cl);
		sureCheckOut.setOnClickListener(cl);
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection con = null;
				// String s = "";
				try {
					URL url = new URL(IPAddress.GETSO + ";jsessionid="
							+ LoginActivity.JSESSIONID);
					String s = null;
					con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(5 * 1000);
					con.connect();
					if (con.getResponseCode() == 200) {
						s = StreamParser.parserStream(con.getInputStream());
						// Log.i("server response", s);

					}

					// send the data to ui thread
					// 1.set the data
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("tc", s);
					msg.setData(data);
					// 2.use handler to send the message
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (con != null)
						con.disconnect();
				}
			}
		}.start();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Shopcart s = new Shopcart();
				super.handleMessage(msg);
				Bundle data = msg.getData();
				result = (String) data.get("tc");
				JSONArray array;
				try {
					array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						JSONObject list = array.getJSONObject(i);
						s.setGdid(list.getString("gdid"));
						s.setSales(list.getString("sales"));
						gdid = s.getGdid();
						sales = s.getSales();
						// Log.i("gdid", gdid);
						// Log.i("sales", sales);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				OrderS.clear();
				OrderS.addAll(JSONParser.parseListJson(result));
				// call the adapter to reflesh the listview
				sa.notifyDataSetChanged();
			}
		};
	}

	// 提交订单
	public void Addorder() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = "";
				try {
					Log.i("gdid", gdid);
					Log.i("sales", sales);
					// 创建连接
					HttpClient httpClient = new DefaultHttpClient();
					// 把接口地址进行封装
					HttpPost post = new HttpPost(IPAddress.ADDORDER
							+ ";jsessionid=" + LoginActivity.JSESSIONID);
					// 设置参数，仿html表单提交
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					BasicNameValuePair param = new BasicNameValuePair("gdid",
							gdid);
					BasicNameValuePair param1 = new BasicNameValuePair("sales",
							sales);
					paramList.add(param);
					paramList.add(param1);
					post.setEntity(new UrlEncodedFormEntity(paramList,
							HTTP.UTF_8));
					// 发送HttpPost请求，并返回HttpResponse对象
					HttpResponse httpResponse = httpClient.execute(post);
					// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
					// if (httpResponse.getStatusLine().getStatusCode() == 200)
					// {
					// // 获取返回结果
					// // result =
					// EntityUtils.toString(httpResponse.getEntity());
					// }
					// send the data to ui thread
					// 1.set the data
					// Message msg = new Message();
					// Bundle data = new Bundle();
					// data.putString("sc", result);
					// msg.setData(data);
					// // 2.use handler to send the message
					// handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	// 事件点击监听器
	private final class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cancle:// 返回购物车
				Intent intent = new Intent(ShopOrderActivity.this,
						ShopcarActivity.class);
				startActivity(intent);
				ShopOrderActivity.this.finish();
				break;
			case R.id.sureCheckOut:// 提交订单
				Addorder();
				Toast.makeText(ShopOrderActivity.this, "提交成功",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	}
}
