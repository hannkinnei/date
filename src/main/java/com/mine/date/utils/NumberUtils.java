package com.mine.date.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {
	private static final String PATTERN_EMAIL_STR = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
	private static Pattern patternEmail = Pattern.compile(PATTERN_EMAIL_STR);
	private static Pattern patternInt = Pattern.compile("[0-9]+");
	private static Pattern patternDouble = Pattern.compile("[0-9]+\\.[0-9]+");
	
	/**
	 * 判断一个字符串是否为合法的email
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null){
			return false;
		}
		return Pattern.matches(PATTERN_EMAIL_STR, email);
	}
	
	/**
	 * 判断一个字符串是否为纯数字
	 * @param str
	 * @return
	 */
	public static boolean isAllNumeric(String str){
		if(checkStrIsNull(str)){
			return false;
		}
	    return patternInt.matcher(str).matches(); 
	}
	/**
	 * 将字符串变成int数字，若失败则返回指定值
	 * @param str
	 * @param defaultInt
	 * @return
	 */
	public static int changeStrToInt(String str, int defaultInt){
		if(isAllNumeric(str) == false){
			return defaultInt;
		} else{
			return Integer.parseInt(str);
		}
	}
	/**
	 * 判断一个字符串是否为数字，如1、 3.1等
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		if(checkStrIsNull(str)){
			return false;
		}
	    return patternInt.matcher(str).matches() || patternDouble.matcher(str).matches(); 
	}
	/**
	 * 将一个字符串输出double型的数字
	 * 如：12.3+
	 * 输出为12.3
	 * @param str
	 * @return
	 */
	public static double getNumFromString(String str){
		if(checkStrIsNull(str)){
			return 0;
		}
		Matcher matcherDouble = patternDouble.matcher(str);
		double num = 0;
		while(matcherDouble.find()){
			num = Double.parseDouble(matcherDouble.group());
		}
		if(num > 0){
			return num;
		}
		Matcher matcherInt = patternInt.matcher(str);
		while(matcherInt.find()){
			num = Integer.parseInt(matcherInt.group());
		}
		return num;
	}
	/**
	 * 判断一个字符串第一个字符是否为数字或者字母
	 * @param str
	 * @return
	 */
	public static boolean isNumericOrLetter(String str){
		if(checkStrIsNull(str)){
			return false;
		}
		String regex = "[\\d|[a-z]|[A-Z]].*";
		return Pattern.matches(regex, str);
	}
	/**
	 * 获取一个字符串的最后一个数字
	 * 获得病假
	 * @param str
	 * @return
	 */
	public static double getLastNumberFromStr(String str){
		if(checkStrIsNull(str)){
			return 0;
		}
		String[] strs = str.split(" ");
		String s = strs[strs.length - 1];
		return isNumeric(s) ? Double.parseDouble(s) : (getNumFromString(s) / 8);
	}
	
	public static boolean checkStrIsNull(String str){
		return str == null;
	}
	
}
