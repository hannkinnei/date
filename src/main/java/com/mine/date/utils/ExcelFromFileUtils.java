package com.mine.date.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelFromFileUtils {
	/**
	 * @param file
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Workbook getWorbookFromFile(File file){
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			Workbook xls = WorkbookFactory.create(in);
			return xls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Workbook extractPTONewInfoXlsx(MultipartFile uploadedFile) throws IOException{
		File myFile = File.createTempFile("tmp", null);
    	uploadedFile.transferTo(myFile);
    	myFile.deleteOnExit();
        FileInputStream fis = new FileInputStream(myFile);
        Workbook xls = null;
		try {
			xls = WorkbookFactory.create(fis);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return xls;
	}
	
	public static Workbook checkWorkbook(Object o){
		if(o == null){
			return null;
		}
		if(!(o instanceof Workbook)){
			return null;
		}
		return (Workbook) o;
	}
	
	/**
	 * 
	 * @param xls
	 * @param sheetName
	 * @return
	 */
	public static Sheet getSheetFromXlsBySheetName(Workbook xls, String sheetName){
		if(checkWorkbook(xls) == null){
			return null;
		}
		return xls.getSheet(sheetName);
	}
	
	public static List<String> getSheetNamesFromXls(Workbook xls){
		if(checkWorkbook(xls) == null){
			return null;
		}
		List<String> sheetNames = new ArrayList<String>();
		for(int i = 0; i < xls.getNumberOfSheets();i++){
			sheetNames.add(xls.getSheetName(i));
		}
		return sheetNames;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValueFromCell(Cell cell, Class<T> c){
		if(cell == null || c == null){
			return null;
		}
		Object result = null;
		switch(cell.getCellTypeEnum()){
		case STRING:
			String tempString = cell.getStringCellValue();
			if(String.class.equals(c)){
				result = tempString;
			}
			if(Integer.class.equals(c)){
				result = Integer.valueOf(tempString);
				result = result == null ? 0 : result;
			}
			if(Double.class.equals(c)){
				result = Double.valueOf(tempString);
				result = result == null ? 0 : result;
			}
		case BLANK:
			break;
		case BOOLEAN:
			boolean tempBoolean = cell.getBooleanCellValue();
			if(String.class.equals(c)){
				result = tempBoolean == true ? "true" : "false";
			}
			if(Integer.class.equals(c)){
				result = tempBoolean == true ? 1 : 0;
			}
			if(Double.class.equals(c)){
				result = tempBoolean == true ? 1 : 0;
			}
			break;
		case ERROR:
			break;
		case FORMULA:
			break;
		case NUMERIC:
			double tempDouble = cell.getNumericCellValue();
			if(String.class.equals(c)){
				result = String.valueOf(tempDouble);
			} else if(Integer.class.equals(c)){
				result = (int) tempDouble;
			} else if(Double.class.equals(c)){
				result = tempDouble;
			} else{
				result = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case _NONE:
			break;
		default:
			break;
		}
		
		if(result == null){
			if(Integer.class.equals(c)){
				result = Integer.valueOf(0);
			}
			if(Double.class.equals(c)){
				result = Double.valueOf(0.0);
			}
		}
		return (T) result;
	}
	
}
