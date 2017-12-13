package com.mine.date.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mine.date.model.DateState;
import com.mine.date.model.YearMonth;

public class DateUtils {
	private static final String PATTERN_YEAR_STR = "\\d{4}";
	private static Pattern patternYear = Pattern.compile(PATTERN_YEAR_STR);
	private static final String PATTERN_STR = "[\\D]{1,}(\\d{1,2})(\\.)(\\d{1,2})";
	private static Pattern pattern = Pattern.compile(PATTERN_STR);
	private static final String PATTERN_HOR_MINUTE_STR = "([0-9]{1,}):([0-9]{1,})";
	private static Pattern patternHourMinute = Pattern.compile(PATTERN_HOR_MINUTE_STR);
	private static final String PATTERN_DATE_STR = "([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})";
	private static Pattern patternDateStr = Pattern.compile(PATTERN_DATE_STR);
	private static final String PATTERN_YEAR_MONTH = "(\\d{4})[\\.](\\d{1,2})";
	private static Pattern patternYearMonth = Pattern.compile(PATTERN_YEAR_MONTH);
	
	/**
	 * 初始年假
	 */
	public static final double INITIAL_VACATION_DAY = 7.0;
	/**
	 * 最大年假
	 */
	public static final double MAX_VACATION_DAY 	= 15.0;
	/**
	 * 年假为初始年假的年份数
	 */
	public static final int	   SAME_VACATION_DAY	= 2;
	/**
	 * 假期最小单位
	 */
	public static final double MIN_VACATION_DAY 	= 0.5;
	
	/**
	 * 获得当前年份
	 * @return
	 */
	public static int getCurrentYear(){
    	Calendar now = Calendar.getInstance();
    	return now.get(Calendar.YEAR); 
    }
	
	/**
	 * 输入一个yyyy-MM-dd入职日期，返回当年可用的初始年假
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static double getVacationDays(String dateStr) {
		if(dateStr == null){
			return 0;
		}
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			int thisYear = Calendar.getInstance().get(Calendar.YEAR);
			int dateStrDay = Integer.parseInt(dateStr.substring(0, 4));;
			int subYear = thisYear - dateStrDay;
			int dateMonth = Integer.parseInt(dateStr.substring(5,7));
			int workingYear = (dateMonth <= 6) ? subYear + 1 : subYear;
			if (subYear > 0) {
				if(workingYear == 1 || workingYear == 2){
					return INITIAL_VACATION_DAY;
				} else{
					double days = INITIAL_VACATION_DAY + workingYear - SAME_VACATION_DAY;
					return days > MAX_VACATION_DAY ? MAX_VACATION_DAY : days;				
				}
				
			} else if(subYear == 0) {
				double days = INITIAL_VACATION_DAY * (12 - dateMonth)  / 12.0;
				return ((days - (int) days) >= MIN_VACATION_DAY) ? ((int) days + MIN_VACATION_DAY) : (int) days;
			}
			return 0;
			
		}  catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 将一个距离1900-01-01的天数转变为日期字符串
	 * @param days
	 * @return
	 */
	public static String initialDay(double days){
		Calendar cal = Calendar.getInstance();  
	    cal.set(1900, 0, 1); 
	    cal.add(Calendar.DAY_OF_YEAR, (int)days);  
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(cal.getTime());
	}
	
	/**
	 * 将距离1900/01/01的天数转变为Date
	 * @param days
	 * @return
	 */
	public static Date changeToDate(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(-2209017600000L));
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

	/**
	 * 将一个时:分转换成以天为单位的double类型
	 * @param time
	 * @return
	 * @throws NumberFormatException
	 */
	public static double changeHourAndMinuteTimeToDays(String time) throws NumberFormatException {
		if(isHourAndMinute(time) == false){
			return 0;
		}
		String[] nums = time.split(":");
		if(nums.length < 2){
			return 0;
		}
		int hour = 0;
		int minute = 0;
		hour = Integer.valueOf(nums[0]);
		minute = Integer.valueOf(nums[1]);
		double days = (minute / 60.0 + hour) / 24.0;
		return days;
	}
	
	/**
	 * 将一个时:分转换成以分钟为单位的double类型
	 * @param time
	 * @return
	 * @throws NumberFormatException
	 */
	public static double changeHourAndMinuteTimeToMinutes(String time) throws NumberFormatException {
		if(isHourAndMinute(time) == false){
			return 0;
		}
		Matcher matcherHourMinutes = patternHourMinute.matcher(time);
    	
		String[] nums = new String[2];
		if(matcherHourMinutes.find()){
			nums[0] = matcherHourMinutes.group(1);
			nums[1] = matcherHourMinutes.group(2);
		}
		int hour = 0;
		int minute = 0;
		hour = Integer.valueOf(nums[0]);
		minute = Integer.valueOf(nums[1]);
		double minutes = hour * 60 + minute;
		return minutes;
	}
	
	/**
	 * 将一个时:分转换成以小时为单位的double类型
	 * @param time
	 * @return
	 * @throws NumberFormatException
	 */
	public static double changeHourAndMinuteTimeToHours(String time) throws NumberFormatException {
		if(isHourAndMinute(time) == false){
			return 0;
		}
		Matcher matcherHourMinutes = patternHourMinute.matcher(time);
    	
		String[] nums = new String[2];
		if(matcherHourMinutes.find()){
			nums[0] = matcherHourMinutes.group(1);
			nums[1] = matcherHourMinutes.group(2);
		}
		int hour = 0;
		int minute = 0;
		hour = Integer.valueOf(nums[0]);
		minute = Integer.valueOf(nums[1]);
		double hours = hour + minute / 60.0;
		return hours;
	}
	
	/**
	 * 判断一个字符串是否为时:分形式
	 * @param str
	 * @return
	 */
	public static boolean isHourAndMinute(String str){
		if(str == null){
			return false;
		}
		return Pattern.matches(PATTERN_HOR_MINUTE_STR, str);
	}
	
	/**
	 * 将分钟转变为hour:minute形式
	 * @param minute
	 * @return
	 */
	public static String changeDoubleMinutesToHourMinutes(double m){
		int hour = (int)(m / 60);
		int minute = (int)(m - hour * 60);
		return hour + ":" + minute;
	}
	
	
	/**
	 * 将小时转变为hour:minute形式
	 * @param hour
	 * @return
	 */
	public static String changeDoubleHoursToHourMinutes(double hour){
		return (int)hour + ":" + (hour - (int)hour) * 60;
	}
	
	/**
	 * 将timestamp转换成String yyyy-MM-dd HH:mm:ss格式
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestamp(Timestamp timestamp){
		if(timestamp == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(timestamp);
	}
	
	/**
	 * 将类似2017/2/3格式的日期转变为Timestamp
	 * @param str
	 * @return
	 */
	public static Timestamp changeDateStrToTimestamp(String str){
		if(str == null){
			return null;
		}
		if(Pattern.matches(PATTERN_DATE_STR, str) == false){
			return null;
		}
		Matcher matcher = patternDateStr.matcher(str);
		if(matcher.find()){
			int year = Integer.valueOf(matcher.group(1));
			int month = Integer.valueOf(matcher.group(2));
			int day = Integer.valueOf(matcher.group(3));
			String dateStr = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day) + " 00:00:01";
			Timestamp time = Timestamp.valueOf(dateStr);
			return time;
		}
		return null;
	}
	
	/**
	 * yearOrMonthOrDay 可取：year month day
	 * @param time
	 * @param yearOrMonthOrDay
	 * @return
	 */
	public static int getYearOrMonthOrDayFromTimestamp(Timestamp time, String yearOrMonthOrDay){
		if(time == null){
			return -1;
		}
		String timeStr = formatTimestamp(time);
		if("year".equals(yearOrMonthOrDay)){
			return Integer.parseInt(timeStr.substring(0, 4));
		}
		if("month".equals(yearOrMonthOrDay)){
			return Integer.parseInt(timeStr.substring(5, 7));
		}
		if("day".equals(yearOrMonthOrDay)){
			return Integer.parseInt(timeStr.substring(8, 10));
		}
		return -1;
	}
	
	/**
	 * 获得一个指定时间几天后的日期
	 * @param timestamp
	 * @param day
	 * @return
	 */
	public static Timestamp addDayInTimestamp(Timestamp timestamp, int day){
		if(timestamp == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(timestamp);
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = format.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, day);
        Timestamp time = new Timestamp(calendar.getTimeInMillis());
        return time;
	}
	/**
	 * timestamp星期几
	 * @param date
	 * @return
	 */
	public static int getWeekDayFromTimestamp(Timestamp date){
		if(date == null){
			return -1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if(w < 0){
			w = 0;
		}
		return w;
	}
	/**
	 * 年月日星期几
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getWeekDayFromYearMonthDay(int year, int month, int day){
		Timestamp date = changeDateStrToTimestamp(year + "/" + month + "/" + day);
		return getWeekDayFromTimestamp(date);
	}
	
	public static List<DateState> getDateStatesFromStartToEnd(Timestamp start, Timestamp end){
		if(isAfterTimestamp(start, end) != AFTER_TIMESTAMP){
			return new ArrayList<DateState>();
		}
		List<DateState> dates = new ArrayList<DateState>();
		for(int i = 0;i <= getDaysOfTimestamps(start, end); i++){
			Timestamp temp = addDayInTimestamp(start, i);
			int year = getYearOrMonthOrDayFromTimestamp(temp, "year");
			int month = getYearOrMonthOrDayFromTimestamp(temp, "month");
			int day = getYearOrMonthOrDayFromTimestamp(temp, "day");
			int w = getWeekDayFromTimestamp(temp);
			DateState date = new DateState();
			date.setYear(year);
			date.setMonth(month);
			date.setDay(day);
			date.setWeekday(w);
			if(date.getWeekday() == DateState.WEEK_SATURDAY || date.getWeekday() == DateState.WEEK_SUNDAY){
				date.setDateState(DateState.DAY_STATE_REST);
			} else{
				date.setDateState(DateState.DAY_STATE_WORK);
			}
			dates.add(date);
		}
		
		return dates;
	}
	
	 /**
	  * 计算两个日期之间相差的天数 
	  * @param start
	  * @param end
	  * @return
	  */
    public static int getDaysOfTimestamps(Timestamp start,Timestamp end){  
    	if(start == null || end == null){
    		return 0;
    	}
        Calendar startCalendar = Calendar.getInstance();  
        Calendar endCalendar = Calendar.getInstance();  
        startCalendar.setTime(start);  
        endCalendar.setTime(end);  
        int days = 0;  
        while(endCalendar.before(startCalendar)){  
            days++;  
            endCalendar.add(Calendar.DAY_OF_YEAR, 1);  
        }  
          
        if(days == 0){  
            endCalendar.setTime(start);  
            startCalendar.setTime(end);  
            while(endCalendar.before(startCalendar)){  
                days++;  
                endCalendar.add(Calendar.DAY_OF_YEAR, 1);  
            }  
        }  
        return --days;  
    }  
    
    public static int AFTER_TIMESTAMP = 1;
    public static int BEFORE_TIMESTAMP = -1;
    public static int EQUAL_TIMESTAMP = 0;
    
    /**
     * 判断end是否在start之后
     * 如 2017-10-30 2017-10-31 返回1
     * @param start
     * @param end
     * @return
     */
    public static int isAfterTimestamp(Timestamp start, Timestamp end){
    	if(start == null || end == null){
    		return 0;
    	}
    	Calendar startCalendar = Calendar.getInstance();  
        Calendar endCalendar = Calendar.getInstance();  
        startCalendar.setTime(start);  
        endCalendar.setTime(end);  
        if(endCalendar.after(startCalendar)){
        	return AFTER_TIMESTAMP;
        } else if(endCalendar.before(startCalendar)){
        	return BEFORE_TIMESTAMP;
        } else{
        	return EQUAL_TIMESTAMP;
        }
    }
    
    
    public static final int DATE_TYPE_NONE = 0;
    public static final int DATE_TYPE_YEAR = 1;
    public static final int DATE_TYPE_YEAR_MONTH = 2;
    public static final int DATE_TYPE_YEAR_MONTH_DAY = 3;
    public static int getDateType(int year, int month, int day){
    	if(year == 0){
    		return DATE_TYPE_NONE;
    	}
    	if(year != 0 && month == 0){
    		return DATE_TYPE_YEAR;
    	}
    	if(year != 0 && month != 0 && day == 0){
    		return DATE_TYPE_YEAR_MONTH;
    	}
    	if(year != 0 && month != 0 && day != 0){
    		return DATE_TYPE_YEAR_MONTH_DAY;
    	}
    	return DATE_TYPE_NONE;
    }
    public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static int getYearOrMonthFromStrOfYearPointMonth(String str, String yearOrMonth){
		patternYearMonth.matcher(str);
		if(Pattern.matches(PATTERN_YEAR_MONTH, str)){
			Matcher matcher = patternYearMonth.matcher(str);
			if(matcher.find()){
				if(YEAR.equals(yearOrMonth)){
					return Integer.parseInt(matcher.group(1));
				}
				if(MONTH.equals(yearOrMonth)){
					return Integer.parseInt(matcher.group(2));
				}
			}
			return 0;
		} else{
			return 0;
		}
	}
	
	public static YearMonth changeStandarStrToYearMonth(String str){
		try{
			int year = Integer.parseInt(str.substring(0, 4));
			int month = Integer.parseInt(str.substring(5, 7));
			return new YearMonth(year, month);
		} catch (Exception e) {
			return null;
		}
	}
}
