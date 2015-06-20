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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.stx.fleshfruit.HomeActivity;
import com.stx.fleshfruit.R;
import com.stx.fleshfruit.entity.Shopcart;
import com.stx.fleshfruit.util.IPAddress;
import com.stx.fleshfruit.util.StreamParser;

public class SingleActivity extends Activity implements OnItemClickListener {
	public List<Map<String, Object>> Single;
	private SimpleAdapter sa;
	private ListView lv;
	private Handler handler;
	private String result;
	public static String gdid;
	private ImageView addshopcar, cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single);
		lv = (ListView) findViewById(R.id.listView2);
//		addshopcar = (ImageView) findViewById(R.id.addshopcar);
		cancle = (ImageView) findViewById(R.id.cancle);
		Single = new ArrayList<Map<String, Object>>();
		sa = new SimpleAdapter(this, Single, R.layout.layout_taocan,
				new String[] { "image", "gname", "price", "sales" }, new int[] {
						R.drawable.photo, R.id.name, R.id.price,
						R.id.salesvolume });
		lv.setAdapter(sa);
		// 设置listview的点击事件
		lv.setOnItemClickListener(this);
		// 设置点击事件
		ClickListener cl = new ClickListener();
		cancle.setOnClickListener(cl);
		// addshopcar.setOnClickListener(cl);
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection con = null;
				// String s = "";
				try {
					URL url = new URL(IPAddress.DANDIAN);
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
					data.putString("sg", s);
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
				result = (String) data.get("sg");
				JSONArray array;
				try {
					array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						JSONObject list = array.getJSONObject(i);
						s.setGdid(list.getString("gdid"));
						gdid = s.getGdid();
						// Log.i("gdid", gdid);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Single.clear();
				Single.addAll(JSONParser.parseSingleJson(result));
				// call the adapter to reflesh the listview
				sa.notifyDataSetChanged();
			}
		};
	}

	// 事件点击监听器
	private final class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cancle:// 返回主界面
				Intent intent = new Intent(SingleActivity.this,
						HomeActivity.class);
				startActivity(intent);
				SingleActivity.this.finish();
				break;
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.setClass(SingleActivity.this, ShopcarActivity.class);
		Map<String, String> map = (Map<String, String>) lv
				.getItemAtPosition(arg2);
		intent.putExtra("gdid", map.get("gdid"));
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "添加购物车成功", Toast.LENGTH_SHORT)
				.show();
	}
}
