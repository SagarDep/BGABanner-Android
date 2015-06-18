package com.stx.fleshfruit.home;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.stx.fleshfruit.R;
import com.stx.fleshfruit.util.IPAddress;
import com.stx.fleshfruit.util.StreamParser;

public class SingleActivity extends Activity {
	public List<Map<String, Object>> Single;
	private SimpleAdapter sa;
	private ListView lv;
	private Handler handler;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single);
		lv = (ListView) findViewById(R.id.listView2);
		Single = new ArrayList<Map<String, Object>>();
		sa = new SimpleAdapter(this, Single, R.layout.layout_taocan,
				new String[] { "image", "gname", "price", "sales" }, new int[] {
						R.drawable.photo, R.id.name, R.id.price,
						R.id.salesvolume });
		lv.setAdapter(sa);
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
				super.handleMessage(msg);
				Bundle data = msg.getData();
				result = (String) data.get("sg");
				Single.clear();
				Single.addAll(JSONParser.parseSingleJson(result));
				// call the adapter to reflesh the listview
				sa.notifyDataSetChanged();
			}
		};
	}

	public void cancle(View v) {
		Intent intent = new Intent(SingleActivity.this, HomeActivity.class);
		startActivity(intent);
		SingleActivity.this.finish();
	}
}
