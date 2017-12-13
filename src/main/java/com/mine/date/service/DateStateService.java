package com.mine.date.service;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mine.date.mapper.DateStateMapper;
import com.mine.date.model.DateState;
import com.mine.date.utils.DateUtils;
import com.mine.date.utils.ExcelFromFileUtils;
import com.mine.date.utils.SortUtils;

@Service
public class DateStateService {
	private static CacheService cache = CacheService.getInstance();
	@Autowired
	private DateStateMapper mapperDateState;
	public int addOrUpdateDateState(DateState date){
		if(date == null || mapperDateState == null){
			return 0;
		}
		cache.deleteDateStateCache();
		DateState dateState = mapperDateState.findDateStateByDate(date.getYear(), date.getMonth(), date.getDay());
		if(dateState == null){
			return mapperDateState.addDateState(date);
		} else{
			return mapperDateState.updateDateState(date);
		}
	}
	public DateState findDateState(int year, int month, int day){
		DateState dateStateTemp = new DateState(year, month, day);
		DateState dateState = cache.getQueryDateState().get(dateStateTemp.getDateStr());
		if(dateState == null){
			dateState = mapperDateState.findDateStateByDate(year, month, day);
			if(dateState == null){
			} else{
				cache.getQueryDateState().put(dateState.getDateStr(), dateState);
			}
		}
		return dateState;
	}
	public List<DateState> findAllDateStates(){
		List<DateState> dateStates = cache.getQueryDateStates().get(DateState.QUERY_ALL_DATESTATES);
		if(dateStates == null || dateStates.size() == 0){
			dateStates = mapperDateState.findAllDateStates();
			cache.getQueryDateStates().put(DateState.QUERY_ALL_DATESTATES, dateStates);
		}
		if(dateStates == null){
			return new ArrayList<DateState>();
		}
		return dateStates;
	}
    public Map<String, DateState> extractEmployeeMonthDataFromFile(MultipartFile uploadedFile) throws IOException{
		Workbook xls = ExcelFromFileUtils.extractPTONewInfoXlsx(uploadedFile);
		if(ExcelFromFileUtils.checkWorkbook(xls) == null){
			return null;
		}
		String sheetName = xls.getSheetName(0);
		Sheet sheet = xls.getSheet(sheetName);
		return extractDateStateFromXls(sheet);
	}
	private Map<String, DateState> extractDateStateFromXls(Sheet sheet) {
		Map<String, DateState> result = new LinkedHashMap<String, DateState>();
    	int rowNumber = sheet.getLastRowNum();
    	
    	for(int row = 2;row <= rowNumber; row++){
    		DateState date = buildDateState(sheet.getRow(row));
    		if(date != null){
    			result.put(date.getDateStr(), date);
    		}
    	}
    	return result;
	}
	private DateState buildDateState(Row row) {
		if(row == null){
			return null;
		}
		DateState date = new DateState();
		
		Cell cellYear = row.getCell(0);
		Cell cellMonth = row.getCell(1);
		Cell cellDay = row.getCell(2);
		Cell cellDateState = row.getCell(3);
		if(cellYear == null || cellMonth == null || cellDay == null || cellDateState == null){
			return null;
		}
		int year = ExcelFromFileUtils.getValueFromCell(cellYear, Integer.class);
		int month = ExcelFromFileUtils.getValueFromCell(cellMonth, Integer.class);
		int day = ExcelFromFileUtils.getValueFromCell(cellDay, Integer.class);
		int dateState = ExcelFromFileUtils.getValueFromCell(cellDateState, Integer.class);
		if(year <= 0 || month <= 0 || month > 12 || day <= 0 || day > 31 || dateState < 0 || dateState > 2){
			return null;
		}	
		date.setYear(year);
		date.setMonth(month);
		date.setDay(day);
		date.setDateState(dateState);
		
		Cell cellReasonForHoliday = row.getCell(4);
		String reasonForHoliday = DateState.REMARK_NO;
		if(cellReasonForHoliday != null){
			reasonForHoliday = ExcelFromFileUtils.getValueFromCell(cellReasonForHoliday, String.class);
		}
		date.setRemark(reasonForHoliday);
		date.setWeekday(DateUtils.getWeekDayFromYearMonthDay(year, month, day));
		
		return date;
	}
	public boolean printExcel(OutputStream stream, List<DateState> dates) {
		if(dates == null){
			return false;
		}
		SortUtils.sortDateState(dates);
		Logger log = LoggerFactory.getLogger(DateStateService.class);
		@SuppressWarnings("resource")
		Workbook hwb = new XSSFWorkbook();
		CellStyle style = hwb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		Sheet sheet = hwb.createSheet(
				"日期表" + DateUtils.formatTimestamp(new Timestamp(System.currentTimeMillis())).substring(0, 7).replace("-", "."));
		Row row = sheet.createRow(0);
		sheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)3));
		Cell cell = null;
		cell = row.createCell(0);
		cell.setCellValue("时段为：0-工作日、1-休息日、2-节假日");
		cell.setCellStyle(style);
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("年");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("月");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("日");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("时段");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		for (int i = 0; i < dates.size(); i++) {
			DateState date = dates.get(i);
			row = sheet.createRow(i + 2);
			row.createCell(0).setCellValue(date.getYear());
			row.createCell(1).setCellValue(date.getMonth());
			row.createCell(2).setCellValue(date.getDay());
			row.createCell(3).setCellValue(date.getDateState());
			row.createCell(4).setCellValue(date.getRemark());
		}
		try {
			hwb.write(stream);
			return true;
		} catch (Exception e) {
			log.error("fail");
			return false;
		}
	}
}
