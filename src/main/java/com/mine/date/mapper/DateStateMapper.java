package com.mine.date.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mine.date.model.DateState;

public interface DateStateMapper {
	/**
	 * 
	 * @param d
	 */
	@Insert("insert into DateState("
			+ "year,"
			+ "month,"
			+ "day,"
			+ "dateState,"
			+ "weekday,"
			+ "remark)"
			+ " values("
			+ "#{year},"
			+ "#{month},"
			+ "#{day},"
			+ "#{dateState},"
			+ "#{weekday},"
			+ "#{remark})")
	public int addDateState(DateState d);
	
	@Update("update DateState set "
			+ "year=${year},"
			+ "month=#{month},"
			+ "day=#{day},"
			+ "dateState=#{dateState},"
			+ "weekday=#{weekday},"
			+ "remark=#{remark} "
			+ "where year=#{year} and month=#{month} and day=#{day}")
	public int updateDateState(DateState d);
	
	@Delete("delete from DateState where year=#{arg0} and month=#{arg1} and day=#{arg2}")
	public int deleteDateState(int year, int month, int day);
	
	@Select("select * from DateState where year=#{arg0} and month=#{arg1} and day=#{arg2}")
	@Results({
		@Result(property = "year",  column = "year"),
		@Result(property = "month", column = "month"),
		@Result(property = "day", column = "day"),
		@Result(property = "dateState", column = "dateState"),
		@Result(property = "weekday", column = "weekday"),
		@Result(property = "remark", column = "remark")
	})
	public DateState findDateStateByDate(int year, int month, int day);
	
	@Select("select * from DateState")
	@Results({
		@Result(property = "year",  column = "year"),
		@Result(property = "month", column = "month"),
		@Result(property = "day", column = "day"),
		@Result(property = "dateState", column = "dateState"),
		@Result(property = "weekday", column = "weekday"),
		@Result(property = "remark", column = "remark")
	})
	public List<DateState> findAllDateStates();
	
}
