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
public class Excel {  
  
   private FileOutputStream fileOutputStream;
   private String filePath;
   private String fileName;
   private HSSFWorkbook workbook;
   private Map<String,HSSFSheet> sheets;
   private Map<String,Map<Integer,HSSFRow>> rows;
   
   public Excel(String filePath,String fileName){
	   this.filePath = filePath;
	   this.fileName = fileName;
	   try{
		   this.fileOutputStream = new FileOutputStream(filePath+fileName);
		   this.workbook = new HSSFWorkbook();
		   this.sheets = new HashMap<String,HSSFSheet>();
		   this.rows = new HashMap<String,Map<Integer,HSSFRow>>();
	   }catch(FileNotFoundException e){
		   e.printStackTrace();
	   }
   }
	   
	   public HSSFSheet createSheet(String sheetName){
		   if(null!=this.sheets.get(sheetName)) return this.sheets.get(sheetName);
		   HSSFSheet sheet = this.workbook.createSheet(sheetName);
		   this.sheets.put(sheetName, sheet);
		   return sheet;
	   }
	   
	   public HSSFRow createRow(String sheetName,int rowNum){
		   if(null==this.rows.get(sheetName)){
			   Map<Integer,HSSFRow> rowMap = new HashMap<Integer,HSSFRow>();
			   this.rows.put(sheetName, rowMap);
		   }
		   if(null!=this.rows.get(sheetName).get(rowNum)){
			   return this.rows.get(sheetName).get(rowNum);
		   }
		   HSSFRow row = this.sheets.get(sheetName).createRow(rowNum);
		   this.rows.get(sheetName).put(rowNum, row);
		   return row;
	   }
	   
	   public void writeExcel(){
		   try{
			   this.workbook.write(this.fileOutputStream);
			   this.fileOutputStream.close();
		   }catch(IOException e){
			   e.printStackTrace();
			   //创建excel失败
		   }
	   }
	   
	   
	   public void createCell(String sheet,int rowNum,int cellNum,HSSFCellStyle style,String value){
		   HSSFCell cell = this.rows.get(sheet).get(rowNum).createCell(cellNum);
		   cell.setCellValue(value);
		   if(null !=style){
			   cell.setCellStyle(style);
		   }
	   }
	   
      public void createCell(String sheet,int rowNum,int cellNum,String value){
    	  this.createCell(sheet, rowNum, cellNum, null,value);
      }
      
      public HSSFCellStyle fontStyle(short color,short bgColor){
    	  HSSFFont font = this.workbook.createFont();
    	  font.setColor(color);
    	  HSSFCellStyle style = this.workbook.createCellStyle();
    	  // style.setAlignment(2);
    	  style.setFillForegroundColor(bgColor);
    	  //style.setFillPattern(HSSFCellStyle.so);
    	  style.setFont(font);
    	  return style;
      }
    
      public void dataValidate(String sheetName,int cellNum,String[] pos){
    	  DataValidationHelper helper = this.sheets.get(sheetName).getDataValidationHelper();
    	  CellRangeAddressList addressList = new CellRangeAddressList(1,1000,cellNum,cellNum);
    	  DataValidationConstraint constraint = helper.createExplicitListConstraint(pos);
    	  DataValidation dataValidation = helper.createValidation(constraint, addressList);
    	  
    	  if(dataValidation instanceof XSSFDataValidation){
    		  dataValidation.setSuppressDropDownArrow(true);
    		  dataValidation.setShowErrorBox(true);
    	  }else{
    		  dataValidation.setSuppressDropDownArrow(false);
    	  }
    	  
    	  this.sheets.get(sheetName).addValidationData(dataValidation);
      }
      
      /**
       * 
       * @param filePath
       * @param fileName
       * @param sheetIndex   从1开始
       * @return
       */
      public static List<List<String>> readExcel(String filePath,String fileName,int sheetIndex){
    	  File file = new File(filePath+fileName);
    	  if(!file.exists()){
    		  throw new CoreException();
    	  }
    	  List<List<String>> fileList = new ArrayList<List<String>>();
    	  if(file.getName().matches("^.+\\.(?i)((xls)|(xlsx))$")){
    		  InfoExcelReader reader;
    		  try{
    			  reader = new InfoExcelReader(0);
    			  fileList = reader.read(file,sheetIndex);
    		  }catch(Exception e){
    			  e.printStackTrace();
    			  throw new CoreException("read excel error");
    		  }
    	  }else{
    		  throw new CoreException("file patern error");
    	  }
    	  return fileList;
      }
      
      public static List<List<String>> readExcel(String filePath,int sheetIndex){
    	  File file = new File(filePath);
    	  if(!file.exists()){
    		  throw new CoreException();
    	  }
    	  List<List<String>> fileList = new ArrayList<List<String>>();
    	  if(file.getName().matches("^.+\\.(?i)((xls)|(xlsx))$")){
    		  InfoExcelReader reader;
    		  try{
    			  reader = new InfoExcelReader(0);
    			  fileList = reader.read(file,sheetIndex);
    		  }catch(Exception e){
    			  e.printStackTrace();
    			  throw new CoreException("read excel error");
    		  }
    	  }else{
    		  throw new CoreException("file patern error");
    	  }
    	  return fileList;
      }
      
      /**
       * 读excel
       * @param file
       * @return
       */
      public static List<List<String>> readExcel(File file){
    	  if(null ==file||!file.exists()){
    		  throw new CoreException("file not exist");
    	  }
    	  List<List<String>> fileList = new ArrayList<List<String>>();
    	  if(file.getName().matches("^.+\\.(?i)((xls)|(xlsx))$")){
    		  InfoExcelReader reader;
    		  try{
    			  reader = new InfoExcelReader(0);
    			  fileList = reader.read(file,1);
    		  }catch(Exception e){
    			  e.printStackTrace();
    			  throw new CoreException("read excel error");
    		  }
    	  }else{
    		  throw new CoreException("file patern error");
    	  }
    	  return fileList;
      }
      
      public static void main(String[] args){
    	  String filePath = "D:\\temp\\";
    	  String fileName=  "20180519.xls";
    	  //List<List<String>> list = Excel.readExcel(filePath, fileName);
    	  List<List<String>> userList = Excel.readExcel(filePath, fileName,1);
    	  //System.out.println(userList);
    	  int i = 0;
  		  for(List<String> user:userList) {
  			System.out.println(user.get(3)+"|"+user.get(4)+"|"+user.get(5)+"|"+user.get(6));
  			i++;
  			if(i>3) break;
  		  }
      }
     
    
}  
