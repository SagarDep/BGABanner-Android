package com.stx.fleshfruit.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
	/**
	 * 主界面展示
	 */
	public static List<Map<String, Object>> parseListJson(String TaocanJson) {
		List<Map<String, Object>> Taocan = new ArrayList<Map<String, Object>>();
		JSONArray array;
		try {
			array = new JSONArray(TaocanJson);
			for (int i = 0; i < array.length(); i++) {
				JSONObject taocan = array.getJSONObject(i);
				Map<String, Object> ss = new HashMap<String, Object>();
				ss.put("gdid", taocan.getString("gdid"));
				ss.put("gname", taocan.getString("gname"));
				ss.put("price", taocan.getString("price"));
				ss.put("sales", taocan.getString("sales"));
				Taocan.add(ss);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Taocan;
	}

	/**
	 * 购物车
	 */
	public static List<Map<String, Object>> parseShopcartJson(
			String ShopcartJson) {
		List<Map<String, Object>> Shopcart = new ArrayList<Map<String, Object>>();
		JSONArray array;
		try {
			// JSONObject jsonObject = new JSONObject(ShopcartJson);
			// array = jsonObject.getJSONArray("Goods");
			array = new JSONArray(ShopcartJson);
			for (int i = 0; i < array.length(); i++) {
				JSONObject shopcart = array.getJSONObject(i);
				Map<String, Object> ss = new HashMap<String, Object>();
				ss.put("gname", shopcart.getString("gname"));
				ss.put("price", shopcart.getString("price"));
				Shopcart.add(ss);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Shopcart;
	}

	/**
	 * 单点
	 */

	public static List<Map<String, Object>> parseSingleJson(String SingleJson) {
		List<Map<String, Object>> Single = new ArrayList<Map<String, Object>>();
		JSONArray array;
		try {
			array = new JSONArray(SingleJson);
			for (int i = 0; i < array.length(); i++) {
				JSONObject single = array.getJSONObject(i);
				Map<String, Object> ss = new HashMap<String, Object>();
				ss.put("gdid", single.getString("gdid"));
				ss.put("gname", single.getString("gname"));
				ss.put("price", single.getString("price"));
				ss.put("sales", single.getString("sales"));
				Single.add(ss);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Single;
	}
}
