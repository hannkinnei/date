package com.mine.date.model;

public class YearMonth {
	private int year;
	private int month;
	private String standarStr;
	public YearMonth() {
		super();
	}
	public YearMonth(int year, int month) {
		super();
		this.year = year;
		this.month = month;
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
	public void setStandarStr(String standarStr) {
		this.standarStr = standarStr;
	}
	public String getStandarStr(){
		standarStr = year + "年" + (month < 10 ? ("0" + month) : month) + "月";
		return standarStr;
	}
	
	public YearMonth lastMonthYearMonth(){
		int year = this.year;
		int month = this.month;
		if(month == 1){
			month = 12;
			year--;
		} else{
			month--;
		}
		return new YearMonth(year, month);
	}
	public int compareTo(YearMonth other){
		if(this.year > other.getYear()){
			return 1;
		} else if(this.year == other.getYear()){
			if(this.month > other.getMonth()){
				return 1;
			} else if(this.month == other.getMonth()){
				return 0;
			} else{
				return -1;
			}
		} else{
			return -1;
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(obj instanceof YearMonth){
			YearMonth objYm = (YearMonth)obj;
			if(objYm.year == year && objYm.month == month){
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return "YearMonth [year=" + year + ", month=" + month + ", standarStr=" + standarStr + "]";
	}
}
