package com.jan;

/**
 * @author jan
 * @date 2017年11月19日 下午7:49:46
 * @version V1.0
 */
public class RandomString {

	private static final String src_number = "0123456789";
	private static final String src_lower = "abcdef";
	private static final String src_upper = src_lower.toUpperCase();
	private static final String src_hex_lower = "0123456789abcdef";
	private static final String src_hex_upper = src_hex_lower.toUpperCase();
	private static final String esc_char = "?";

	public static String get(int size) {
		StringBuffer r = new StringBuffer(size);
		String src = src_number + src_upper;
		for (int i = 0; i < size; i++) {
			r.append(getRandomChar(src));
		}
		return r.toString();
	}

	public static String get(String format) {
		StringBuffer r = new StringBuffer(format.length());
		String src = src_number + src_upper;
		for (int i = 0; i < format.length(); i++) {
			String curr = String.valueOf(format.charAt(i));
			if (curr.equalsIgnoreCase(esc_char)) {
				r.append(getRandomChar(src));
			} else {
				r.append(curr);
			}
		}
		return r.toString();
	}

	public static String get(String format, char esc) {
		StringBuffer r = new StringBuffer(format.length());
		String src = src_number + src_upper;
		for (int i = 0; i < format.length(); i++) {
			String curr = String.valueOf(format.charAt(i));
			if (curr.equalsIgnoreCase(String.valueOf(esc))) {
				r.append(getRandomChar(src));
			} else {
				r.append(curr);
			}
		}
		return r.toString();
	}

	public static String getNum(int size) {
		StringBuffer r = new StringBuffer(size);
		String src = src_number;
		for (int i = 0; i < size; i++) {
			r.append(getRandomChar(src));
		}
		return r.toString();
	}

	public static String getNum(String format) {
		StringBuffer r = new StringBuffer(format.length());
		String src = src_number;
		for (int i = 0; i < format.length(); i++) {
			String curr = String.valueOf(format.charAt(i));
			if (curr.equalsIgnoreCase(esc_char)) {
				r.append(getRandomChar(src));
			} else {
				r.append(curr);
			}
		}
		return r.toString();
	}

	// Java中文网:http://www.javaweb.cc
	public static String getNum(String format, char esc) {
		StringBuffer r = new StringBuffer(format.length());
		String src = src_number;
		for (int i = 0; i < format.length(); i++) {
			String curr = String.valueOf(format.charAt(i));
			if (curr.equalsIgnoreCase(String.valueOf(esc))) {
				r.append(getRandomChar(src));
			} else {
				r.append(curr);
			}
		}
		return r.toString();
	}

	public static String getHex(int size) {
		StringBuffer r = new StringBuffer(size);
		String src = src_hex_upper;
		for (int i = 0; i < size; i++) {
			r.append(getRandomChar(src));
		}
		return r.toString();
	}

	public static String getHex(String format) {
		StringBuffer r = new StringBuffer(format.length());
		String src = src_hex_upper;
		for (int i = 0; i < format.length(); i++) {
			String curr = String.valueOf(format.charAt(i));
			if (curr.equalsIgnoreCase(esc_char)) {
				r.append(getRandomChar(src));
			} else {
				r.append(curr);
			}
		}
		return r.toString();
	}

	public static String getHex(String format, char esc) {
		StringBuffer r = new StringBuffer(format.length());
		String src = src_hex_upper;
		for (int i = 0; i < format.length(); i++) {
			String curr = String.valueOf(format.charAt(i));
			if (curr.equalsIgnoreCase(String.valueOf(esc))) {
				r.append(getRandomChar(src));
			} else {
				r.append(curr);
			}
		}
		return r.toString();
	}

	// Java教程:http://www.javaweb.cc
	private static final String getRandomChar(String src) {
		if (null == src || "".equals(src)) {
			return "";
		}
		return String.valueOf((src.charAt((int) (Math.random() * src.length()))));
	}
}
