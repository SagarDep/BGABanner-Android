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
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stx.fleshfruit.util.IPAddress;

public class LoginActivity extends Activity {
	private EditText login_username;
	private EditText login_password;
	private Button login_Button;
	public static String JSESSIONID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login_username = (EditText) findViewById(R.id.login_username);
		login_password = (EditText) findViewById(R.id.login_password);
		login_Button = (Button) findViewById(R.id.login_button);
		login_Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 判断用户输入的帐号和密码是否为空
				if ("".equals(login_username.getText().toString())
						|| "".equals(login_password.getText().toString())) // 判断
																			// 帐号和密码
				{
					new AlertDialog.Builder(LoginActivity.this)
							.setIcon(
									getResources().getDrawable(
											R.drawable.login_error_icon))
							.setTitle("登录错误")
							.setMessage("帐号或者密码不能为空，\n请输入后再登录！").create()
							.show();
				} else {
					new AsyncTask<String, Void, Object>() {
						@Override
						protected Object doInBackground(String... params) {
							String result = "";
							try {
								// 创建连接
								HttpClient httpClient = new DefaultHttpClient();
								// 把接口地址进行封装
								HttpPost post = new HttpPost(IPAddress.LOGIN);
								// 设置参数，仿html表单提交
								List<NameValuePair> paramList = new ArrayList<NameValuePair>();
								BasicNameValuePair param = new BasicNameValuePair(
										"LoginName", login_username.getText()
												.toString());
								BasicNameValuePair param2 = new BasicNameValuePair(
										"LoginPassword", login_password
												.getText().toString());
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
								e.printStackTrace();
							}
							return result;
						}

						// 判断用户登录是否正确并进行页面跳转
						protected void onPostExecute(Object result) {
							super.onPostExecute(result);
							// 判断 帐号和密码
							if (((String) result).startsWith("登陆失败")) {
								new AlertDialog.Builder(LoginActivity.this)
										.setIcon(
												getResources()
														.getDrawable(
																R.drawable.login_error_icon))
										.setTitle("登录失败")
										.setMessage("帐号或者密码不正确，\n请检查后重新输入！")
										.create().show();

							} else {
								JSESSIONID=result.toString();
								Log.i("JSESSIONID", JSESSIONID);
								Intent intent = new Intent(LoginActivity.this,
										HomeActivity.class);
								startActivity(intent);
								LoginActivity.this.finish();
								Toast.makeText(getApplicationContext(), "登录成功",
										Toast.LENGTH_SHORT).show();
							}
						}
					}.execute();
				}
			}
		});
	}

	// 点击注册，跳转到注册界面
	public void register(View view) {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	// 点击返回主界面
	public void cancle(View v) {
		Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
	}
}
