package com.stx.fleshfruit.home;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stx.fleshfruit.HomeActivity;
import com.stx.fleshfruit.R;
import com.stx.fleshfruit.entity.Shopcart;
import com.stx.fleshfruit.util.IPAddress;
import com.stx.fleshfruit.util.StreamParser;

public class TaocanActivity extends Activity implements OnItemClickListener {
	public List<Map<String, Object>> Taocan;
	private SimpleAdapter sa;
	private ListView lv;
	private Handler handler;
	private String result;
	public static String gdid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taocan);
		lv = (ListView) findViewById(R.id.listView1);
		Taocan = new ArrayList<Map<String, Object>>();
		sa = new SimpleAdapter(this, Taocan, R.layout.layout_taocan,
				new String[] { "image", "gname", "price", "sales" }, new int[] {
						R.drawable.photo, R.id.name, R.id.price,
						R.id.salesvolume });
		lv.setAdapter(sa);
		// 设置listview的点击事件
		lv.setOnItemClickListener(this);
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection con = null;
				// String s = "";
				try {
					URL url = new URL(IPAddress.TAOCAN);
					String s = null;
					con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(5 * 1000);
					con.connect();
					if (con.getResponseCode() == 200) {
						s = StreamParser.parserStream(con.getInputStream());
						Log.i("server response", s);
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
						gdid = s.getGdid();
//						Log.i("gdid", gdid);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Taocan.clear();
				Taocan.addAll(JSONParser.parseListJson(result));
				// call the adapter to reflesh the listview
				sa.notifyDataSetChanged();
			}
		};
	}

	// 返回到主界面
	public void cancle(View v) {
		Intent intent = new Intent(TaocanActivity.this, HomeActivity.class);
		startActivity(intent);
		TaocanActivity.this.finish();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.setClass(TaocanActivity.this, ShopcarActivity.class);
		Map<String, String> map = (Map<String, String>) lv
				.getItemAtPosition(arg2);
		intent.putExtra("gdid", map.get("gdid"));
		startActivity(intent);
		// String gdidt = map.get("gdid");
		// Toast.makeText(getApplicationContext(), gdidt, Toast.LENGTH_SHORT)
		// .show();
	}
}
