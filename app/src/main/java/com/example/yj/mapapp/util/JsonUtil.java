package com.example.yj.mapapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {
	public static String info = "info";
//	public static String data = "data";
	public static String data = "d";
	public static String status = "status";

	public static boolean isJson(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取视酷json里面的“resultCode”数据串
	 *
	 * @param content
	 * @return
	 */
	public static String getShiKuCode(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull("resultCode")) {
				String resp_code = jsonObject.getString("resultCode");
				return resp_code;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取视酷json里面的“userId”数据串
	 *
	 * @param content
	 * @return
	 */
	public static String getShiKuUserId(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull("data")) {
				String resp_code = jsonObject.getString("data");
				JSONObject jsonObject1 = new JSONObject(resp_code);
				if(!jsonObject1.isNull("userId")){
					String resp_code1=jsonObject1.getString("userId");
					return resp_code1;
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取json里面的“status”数据串
	 * 
	 * @param content
	 * @return
	 */
	public static String getResCode(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull(status)) {
				String resp_code = jsonObject.getString(status);
				return resp_code;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取json里面的“data”数据串
	 * 
	 * @param content
	 * @return
	 */
	public static String getData(String content) {
		String resp_data = null;
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull(data)) {
				resp_data = jsonObject.getString(data);
				return resp_data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取json里面的“message”数据串
	 * @param content
	 * @return
	 */
	public static String getMessage(String content) {
		String resp_data = null;
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull("m")) {
				resp_data = jsonObject.getString("m");
				return resp_data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取json里面的“s”数据串
	 * @param content
	 * @return
	 */
	public static String getS(String content) {
		String resp_data = null;
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull("s")) {
				resp_data = jsonObject.getString("s");
				return resp_data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取json里面的“info”数据串
	 * 
	 * @param content
	 * @return
	 */
	public static String getInfo(String content) {
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull(info)) {
				String resp_code = jsonObject.getString(info);
				return resp_code;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Json转对对象
	 */
	public static Object jsonToObject(String json, Class<?> classOfT) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(json, classOfT);
	}

	/**
	 * Json转对象
	 */
	public static Object jsonToObject(String json, Type type) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(json, type);
	}

	/**
	 * json解析回ArrayList,参数为new TypeToken<ArrayList<T>>() {},必须加泛型
	 */
	public static List<?> jsonToList(String json, TypeToken<?> token) {
		return (List<?>) jsonToObject(json, token.getType());
	}

	/**
	 * 对象转Json
	 */
	public static String objetcToJson(Object object) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(object);
	}

	public static boolean onSuccess(String content) {
		String resCode = "";
		try {
			JSONObject jsonObject = new JSONObject(content);
			resCode = jsonObject.getString("resCode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != resCode && !"".equals("resCode") && resCode.equals("1")) {
			return true;
		}
		return false;

	}

	public static boolean busTicketonSuccess(String content) {
		String resCode = "";
		try {
			JSONObject jsonObject = new JSONObject(content);
			resCode = jsonObject.getString("desc");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != resCode && !"".equals(resCode) && resCode.equals("成功")) {
			return true;
		}
		return false;

	}

	public static boolean busTicketDingDanonSuccess(String content) {
		String resCode = "";
		try {
			JSONObject jsonObject = new JSONObject(content);
			resCode = jsonObject.getString("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (null != resCode && !"".equals(resCode) && resCode.equals("success")) {
			return true;
		}
		return false;

	}

	public static String resCode(String content) {
		String resCode = "";
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull("resCode")) {
				resCode = jsonObject.getString("resCode");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resCode;
	}

	public static String resContent(String content) {
		String rescontent = "";
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull("rescontent")) {
				rescontent = String.valueOf(jsonObject.get("rescontent"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rescontent;
	}

	public static String value(String content, String key) {
		String value = "";
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull(key)) {
				value = jsonObject.getString(key);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static int getIntValue(String content, String key) {
		int value = 0;
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (!jsonObject.isNull(key)) {
				value = jsonObject.getInt(key);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

}
