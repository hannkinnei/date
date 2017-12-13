package com.mine.date.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mine.date.model.DateState;
import com.mine.date.service.DateStateService;
import com.mine.date.utils.DateUtils;
import com.mine.date.utils.SortUtils;
import com.mine.date.utils.UrlOpeUtils;

@Controller
public class DateStateController {
	@Autowired
	private DateStateService dateStateService;
	
	@RequestMapping(value = "/admin/dateState", method = RequestMethod.GET)
	public String toDateState(HttpServletRequest request) {
		return "datestate";
	}
	
	@RequestMapping(value = "/admin/date/upload", method = RequestMethod.POST)
	public String checkMonthUploadEmployee(@RequestParam("file_date") MultipartFile uploadedFileDate, RedirectAttributes redirectAttributes, HttpServletRequest request){
		if(uploadedFileDate.isEmpty()){
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return UrlOpeUtils.redirectURLByProt(request, "admin/uploadStatus");
		}
		try {
			Map<String, DateState> dates = dateStateService.extractEmployeeMonthDataFromFile(uploadedFileDate);
			for(String key : dates.keySet()){
				DateState date = dates.get(key);
				dateStateService.addOrUpdateDateState(date);
			}
			request.setAttribute("message", "上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "上传失败：" + e.getMessage());
		}
		return "datestate";
	}
	
	@RequestMapping("/admin/date/print/excel")
	@ResponseBody
	public void printMonthDataSummaryExcel(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<DateState> dates = dateStateService.findAllDateStates();
			SortUtils.sortDateState(dates);
			response.addHeader("Content-Disposition", "attachment;filename=" + new String("日期.xlsx".getBytes()));  
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			OutputStream stream = response.getOutputStream();

			dateStateService.printExcel(stream, dates);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/admin/date/print")
	@ResponseBody
	public JSONObject changeJson(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			Timestamp start = DateUtils.changeDateStrToTimestamp("2017/1/1");
			Timestamp end = DateUtils.changeDateStrToTimestamp("2019/1/1");
			List<DateState> dates = DateUtils.getDateStatesFromStartToEnd(start, end);
			SortUtils.sortDateState(dates);
			JSONArray array = new JSONArray();
			for(DateState date : dates){
				JSONObject temp = date.changeToJson();
				array.add(temp);
				dateStateService.addOrUpdateDateState(date);
			}
			json.put("result", array);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
