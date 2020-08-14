package com.topsci.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class JsonUtils {

	/**
	 * 获取json中String
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static String getString(JSONObject jsonObject,String key) throws JSONException{
		try{
			return jsonObject.get(key).toString();
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}
	/**
	 * 获取json中String
	 * @param jsonObject
	 * @param key
	 * @param defaultStr
	 * @return
	 * @throws JSONException
	 */
	public static String getString(JSONObject jsonObject,String key,String defaultStr) throws JSONException{
		if(!jsonObject.containsKey(key)){
			return defaultStr;
		}

		try{
			return jsonObject.get(key).toString();
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}

	/**
	 * 获取json中array
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getJSONArray(String str) throws JSONException{
		try{
			return JSONArray.fromObject(str);
		}catch(Exception e){
			throw new JSONException("缺少"+str+"参数错误！", e);
		}
	}

	/**
	 * 获取json中String
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static int getInteger(JSONObject jsonObject,String key) throws JSONException{
		try{
			return jsonObject.getInt(key);
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}
	public static int getInteger(JSONObject jsonObject,String key,int defaultNum) throws JSONException{
		if(!jsonObject.containsKey(key)){
			return defaultNum;
		}
		try{
			return jsonObject.getInt(key);
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}

	/**
	 * 获取json中String
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static long getLong(JSONObject jsonObject,String key,long defaultNum) throws JSONException{
		try{
			if(jsonObject.containsKey(key)){
				return jsonObject.getLong(key);
			}
			return defaultNum;
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}
	/**
	 * 获取json中String
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static long getLong(JSONObject jsonObject,String key) throws JSONException{
		try{
			return jsonObject.getLong(key);
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}

	/**
	 * 获取json中double
	 * @param jsonObject
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public static Double getDouble(JSONObject jsonObject,String key) throws JSONException{
		try{
			return jsonObject.getDouble(key);
		}catch(Exception e){
			throw new JSONException("缺少"+key+"参数错误！", e);
		}
	}

	public static JSONObject beanToJson(Object obj) {
		return JSONObject.fromObject(obj);
	}
}
