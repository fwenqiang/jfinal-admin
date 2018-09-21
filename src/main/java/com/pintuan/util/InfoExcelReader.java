package com.pintuan.util;  
  
import org.apache.poi.hssf.usermodel.*;  
import org.apache.poi.poifs.filesystem.POIFSFileSystem;  
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import com.alibaba.druid.pool.vendor.InformixExceptionSorter;
import com.pintuan.base.CoreException;
  
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;  
import java.text.SimpleDateFormat;  
import java.util.*;  
  
/** 
 * Excel工具类 
 * <p/> 
 */  
public class InfoExcelReader extends ExcelReader{
	
	private Integer ignoreLines;
	
	public InfoExcelReader(Integer ignoreLines){
		this.ignoreLines = ignoreLines;
	}

	@Override
	public List<String> optRows(int curRow, List<String> rowList) {
		List<String> rowDataList = new ArrayList<String>();
		if(curRow <=ignoreLines){
			return rowDataList;
		}
		try{
			for(String str:rowList){
				rowDataList.add(str);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rowDataList;
	}  
    
}  
