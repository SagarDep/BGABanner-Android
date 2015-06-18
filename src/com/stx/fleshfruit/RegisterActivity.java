package com.stx.fleshfruit;

import java.util.ArrayList;
import java.util.List;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stx.fleshfruit.util.IPAddress;

public class RegisterActivity extends Activity {
	private EditText register_username;
	private EditText register_password;
	private EditText reregister_password;
	private Button register_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		register_username = (EditText) findViewById(R.id.register_username);
		register_password = (EditText) findViewById(R.id.register_password);
		reregister_password = (EditText) findViewById(R.id.reregister_password);
		register_btn = (Button) findViewById(R.id.register_btn);
		// 绑定控件
		register_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 判断用户输入框是否为空，两次输入密码是否一致
				if ((register_username.getText().toString().trim().isEmpty())
						&& (register_password.getText().toString().trim()
								.isEmpty())) {
					Toast.makeText(getApplicationContext(), "注册账号或密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (!((register_password.getText().toString().trim())
						.equals(reregister_password.getText().toString().trim()))) {
					Toast.makeText(getApplicationContext(), "两次密码输入不一致！",
							Toast.LENGTH_SHORT).show();
					// 判断用户名及密码长度，限制长度
				} else if (!(register_username.getText().toString().length() >= 4 && register_username
						.getText().toString().length() <= 16)) {
					Toast.makeText(getApplicationContext(),
							"用户名长度必须大于4位小于16位！", Toast.LENGTH_SHORT).show();
				} else if (!(register_password.getText().toString().length() >= 6 && register_password
						.getText().toString().length() <= 20)) {
					Toast.makeText(getApplicationContext(), "密码长度必须大于6位小于20位！",
							Toast.LENGTH_SHORT).show();
				} else {
					new AsyncTask<String, Void, Object>() {
						@Override
						protected Object doInBackground(String... params) {
							String result = "";
							try {
								// 创建连接
								HttpClient httpClient = new DefaultHttpClient();
								HttpPost post = new HttpPost(IPAddress.REGIST);
								// 设置参数，仿html表单提交
								List<NameValuePair> paramList = new ArrayList<NameValuePair>();
								BasicNameValuePair param = new BasicNameValuePair(
										"username", register_username.getText()
												.toString());
								BasicNameValuePair param2 = new BasicNameValuePair(
										"password", register_password.getText()
												.toString());
								paramList.add(param);
								paramList.add(param2);
								post.setEntity(new UrlEncodedFormEntity(
										paramList, HTTP.UTF_8));
								// 发送HttpPost请求，并返回HttpResponse对象
								HttpResponse httpResponse = httpClient
										.execute(post);
								// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
								if (httpResponse.getStatusLine()
										.getStatusCode() == 200) {
									// 获取返回结果
									result = EntityUtils.toString(httpResponse
											.getEntity());
								}
							} catch (Exception e) {
								// 无法连接http组件
							}
							return result;
						}

						// 判断注册用户名是否存在，如果存在，提示用户，如果不存在，进行注册，页面进行跳转
						protected void onPostExecute(Object result) {
							super.onPostExecute(result);
							if (((String) result).startsWith("注册成功")) {
								Intent intent = new Intent(
										RegisterActivity.this,
										HomeActivity.class);
								startActivity(intent);
								Toast.makeText(getApplicationContext(), "注册成功",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(RegisterActivity.this, "注册失败!",
										Toast.LENGTH_SHORT).show();
							}
						}
					}.execute();
				}
			}
		});
	}

//	private class LoadTask extends AsyncTask<String, Void, String> {
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		protected String doInBackground(String... params) {
//			String result = null;
//			String path = params[0];
//			// 设置参数，仿THML提交
//			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//			BasicNameValuePair param1 = new BasicNameValuePair("username",
//					register_username.getText().toString());
//			BasicNameValuePair param2 = new BasicNameValuePair("password",
//					register_password.getText().toString());
//			paramList.add(param1);
//			paramList.add(param2);
//			result = HttpUtil.doPost(path, paramList);
//			return result;
//		}
//
//	}

	// 返回主界面
	public void backOnClick(View v) {
		Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
		startActivity(intent);
		RegisterActivity.this.finish();
	}

}