package com.mine.date.model;

import com.alibaba.fastjson.JSONObject;

/**
 * 工作日、休息日、节假日
 * @author zoom
 *
 */
public class DateState {
	private int year;
	private int month;
	private int day;
	private int dateState;
	
	private int weekday = WEEK_NO;
	private String remark = REMARK_NO;
	
	private String dateStateStr;
	private String weekdayStr;
	/**
	 * 日期是0工作日、1休息日还是2节假日
	 */
	public static final int DAY_STATE_WORK = 0;
	public static final int DAY_STATE_REST = 1;
	public static final int DAY_STATE_HOLIDAY = 2;
	public static final int DAY_STATE_NOT_EXISTS = -1;
	public static final String DAY_STATE_WORK_STR = "工作日";
	public static final String DAY_STATE_REST_STR = "休息日";
	public static final String DAY_STATE_HOLIDAY_STR = "节假日";
	
	public static final String REMARK_NO = "无";
	
	public static final int WEEK_NO = -1;
	public static final int WEEK_MONDAY = 1;
	public static final int WEEK_TUESDAY = 2;
	public static final int WEEK_WEDNESDAY = 3;
	public static final int WEEK_THURSDAY = 4;
	public static final int WEEK_FRIDAY = 5;
	public static final int WEEK_SATURDAY = 6;
	public static final int WEEK_SUNDAY = 0;
	public static final String[] WEEK_DAYS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	public static final String QUERY_ALL_DATESTATES = "$query_all_dateStaetes$";
	
	public DateState() {
		super();
	}
	public DateState(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getDateState() {
		return dateState;
	}
	public void setDateState(int dateState) {
		this.dateState = dateState;
	}
	public String getDateStr(){
		return year + "" + (month >= 10 ? month : "0" + month) + "" + (day >= 10 ? day : "0" + day);
	}
	public String getDateStateStr() {
		switch(getDateState()){
		case DAY_STATE_WORK:
		case DAY_STATE_NOT_EXISTS:
			dateStateStr = DAY_STATE_WORK_STR;
			break;
		case DAY_STATE_REST:
			dateStateStr = DAY_STATE_REST_STR;
			break;
		case DAY_STATE_HOLIDAY:
			dateStateStr = DAY_STATE_HOLIDAY_STR;
		}
		return dateStateStr;
	}
	public int getWeekday() {
		return weekday;
	}
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	
	public int compareTo(DateState other){
		if(this.getYear() > other.getYear()){
			return 1;
		} else if(this.getYear() == other.getYear()){
			if(this.getMonth() > other.getMonth()){
				return 1;
			} else if(this.getMonth() == other.getMonth()){
				if(this.getDay() > other.getDay()){
					return 1;
				} else if(this.getDay() == other.getDay()){
					return 0;
				} else{
					return -1;
				}
			} else{
				return -1;
			}
		} else{
			return -1;
		}
	}
	
	public JSONObject changeToJson(){
		JSONObject json = new JSONObject();
		json.put("year", year);
		json.put("month", month);
		json.put("day", day);
		json.put("dateState", dateState);
		json.put("weekday", weekday);
		json.put("remark", getRemark());
		json.put("weekdayStr", getWeekdayStr());
		json.put("dateStateStr", getDateStateStr());
		json.put("tip", "dateState：0-工作日、1-休息日、2-节假日；\nweekday：0-星期日、1-星期一、2-星期二、3-星期三、4-星期四、5-星期五、6-星期六");
		return json;
	}
	public String getWeekdayStr() {
		if(weekday >= WEEK_SUNDAY && weekday <= WEEK_SATURDAY){
			weekdayStr = WEEK_DAYS[weekday];
		}
		return weekdayStr;
	}
	public String getRemark() {
		if(remark == null || "".equals(remark)){
			remark = REMARK_NO;
		}
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "DateState [year=" + getYear() + ", month=" + getMonth() + ", day=" + getDay() + ", dateState=" + getDateState()
				+ ", weekday=" + getWeekday() + ", remark=" + getRemark() + ", dateStateStr=" + getDateStateStr()
				+ ", weekdayStr=" + getWeekdayStr() + "]";
	}
	
}
