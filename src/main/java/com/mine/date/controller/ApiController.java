package com.mine.date.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mine.date.exception.ErrorException;
import com.mine.date.model.DateState;
import com.mine.date.service.DateStateService;
import com.mine.date.utils.DateUtils;

@Controller
public class ApiController {
	@Autowired
	private DateStateService dateStateService;
	/**
	 * 查找
	 * @param request
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@RequestMapping(value = "/date/dayState/{year}/{month}/{day}")
	@ResponseBody
	public String getDayStateByYearMonthDay(HttpServletRequest request, @PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day){
		JSONObject result = new JSONObject();
		try{
			DateState dateState = dateStateService.findDateState(year, month, day);
			if(dateState == null){
				throw new ErrorException("not found the day");
			}
			result.put("result", "success");
			result.put("year", dateState.getYear());
			result.put("month", dateState.getMonth());
			result.put("day", dateState.getDay());
			result.put("dayState", dateState.getDateState());
			result.put("dayStateStr", dateState.getDateStateStr());
		} catch (ErrorException e){
			result.put("result", "fail");
			result.put("message", e.getMsg());
		} catch (Exception e) {
			result.put("result", "fail");
			result.put("message", e.getMessage());
		}
		return result.toJSONString();
	}
	@RequestMapping(value = "/date/dateState/{year}/{month}/{day}")
	@ResponseBody
	public JSONObject getDateStateByYearMonthDay(HttpServletRequest request, @PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day){
		JSONObject result = new JSONObject();
		try{
			DateState dateState = dateStateService.findDateState(year, month, day);
			if(dateState == null){
				throw new ErrorException("not found the day");
			}
			result.put("result", "success");
			result.put("data", dateState.changeToJson());
		} catch (ErrorException e){
			result.put("result", "fail");
			result.put("message", e.getMsg());
		} catch (Exception e) {
			result.put("result", "fail");
			result.put("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 添加或修改
	 * @param request
	 * @param year
	 * @param month
	 * @param day
	 * @param dayState
	 * @return
	 */
	@RequestMapping(value = "/admin/date/dayState/{year}/{month}/{day}/{dayState}")
	@ResponseBody
	public String getDayStateByYearMonthDay(HttpServletRequest request
			, @PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day
			, @PathVariable("dayState") int dayState){
		JSONObject result = new JSONObject();
		try{
			DateState dateState = dateStateService.findDateState(year, month, day);
			if(dateState == null){
				dateState = new DateState();
				dateState.setYear(year);
				dateState.setMonth(month);
				dateState.setDay(day);
			}
			dateState.setWeekday(DateUtils.getWeekDayFromYearMonthDay(year, month, day));
			dateState.setDateState(dayState);
			dateState.setRemark(DateState.REMARK_NO);
			dateStateService.addOrUpdateDateState(dateState);
			result.put("result", "success");
			result.put("year", dateState.getYear());
			result.put("month", dateState.getMonth());
			result.put("day", dateState.getDay());
			result.put("dayState", dateState.getDateState());
			result.put("dayStateStr", dateState.getDateStateStr());
			result.put("weekday", dateState.getWeekdayStr());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "fail");
			result.put("message", e.getMessage());
		}
		return result.toJSONString();
	}
	
}
