package com.stx.fleshfruit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamParser {
	public static String parserStream(InputStream is) {
		BufferedReader br = null;
		String s = "";
		try {
			br = new BufferedReader(
					new InputStreamReader(is, "utf-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				s += tmp;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return s;
	}
}
