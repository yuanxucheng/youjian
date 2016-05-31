package com.example.yj.mapapp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String getMD5(String val) throws NoSuchAlgorithmException {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(val.getBytes());
		byte[] m = algorithm.digest();
		return getString(m);
	}

	private static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			int n = b[i];
			if (n < 0)
				n += 256;

			if (n < 16)
				sb.append("0");

			sb.append(Integer.toHexString(n));
		}

		return sb.toString();
	}

	public static String myMD5(String plainText, int n) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("UTF-8"));
			byte b[] = md.digest();

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				if (Integer.toHexString(0xFF & b[offset]).length() == 1)
					buf.append("0"); 

				buf.append(Integer.toHexString(0xFF & b[offset]));
			}

			if (n == 16)
				return buf.toString().substring(8, 24);
			else
				return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "failed";
		}
	}
}
