package com.topsci.util;

import java.util.UUID;

/**
 * UUID 生成工具类
 * 
 * @author quietywind
 * 
 */
public class UUIDGenerator {
	/**
	 * 获得一个 UUID 值
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获得一个不带 '-' 分割的 UUID 值
	 * 
	 * @return String No Cross UUID
	 */
	public static String getNoCrossUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 获得指定数量的 UUID 值
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

	/**
	 * 获得指定数量的不带 '-' 分割的 UUID 值
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] No Cross UUID数组
	 */
	public static String[] getNoCrossUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getNoCrossUUID();
		}
		return ss;
	}
}