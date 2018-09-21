package com.pintuan.util;  
  
import org.apache.poi.hssf.usermodel.*;  
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;  
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.alibaba.druid.pool.vendor.InformixExceptionSorter;
import com.pintuan.base.CoreException;
import com.supyuan.util.StrUtils;
  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;  
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;  
import java.util.*;  
import java.util.jar.Attributes;
  
/** 
 * Excel工具类 
 * <p/> 
 */  
public abstract class ExcelReader extends DefaultHandler {  
  
	private SharedStringsTable sst;
	private int curRow = 0;
	private int totalRows = 0;
	private int totalCells = 0;
	
	private  String userDefindStyle = "MM/dd/yyyy";
	private  String lastContents;
	private  boolean nextIsString;
	private List<String> rowlist = new ArrayList<String>();
	private  int curCol = 0;
	
	public List<List<String>> read(File file,int sheetIndex){
		if(file==null || !file.exists()){
			throw new CoreException("file un exist");
		}
		
		if(!file.getName().matches("^.+\\.(?i)((xls)|(xlsx))$")){
			throw new CoreException("file patern error");
		}
		
		boolean isExcel2003 = true;
		if(file.getName().matches("^.+\\.(?i)(xlsx)$")){
			isExcel2003 = false;
		}
		List<List<String>> fileList = new ArrayList<List<String>>();
		if(isExcel2003){
			fileList = readByGe(file,sheetIndex -1);
		}else{
			readByXml(file,sheetIndex);
		}
		
		return fileList;
	}
	
	private List<List<String>> readByGe(File file,int sheetIndex){
		List<List<String>> fileList = new ArrayList<List<String>>();
		try{
			Workbook wb = new HSSFWorkbook(new FileInputStream(file));
			Sheet sheet = wb.getSheetAt(sheetIndex);
			this.totalRows = sheet.getPhysicalNumberOfRows();
			if(this.totalRows>=1&&sheet.getRow(0)!=null){
				this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
			}
			for(int r = 0;r<totalRows;r++){
				Row row = sheet.getRow(r);
				if(row == null){
					continue;
				}
				for(short c = 0;c<totalCells;c++){
					Cell cell = row.getCell(c);
					String cellValue = "";
					if(cell==null){
						rowlist.add(cellValue);
						continue;
					}
					if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
						if(HSSFDateUtil.isCellDateFormatted(cell)){
							cellValue = formatDate(userDefindStyle,cell.getDateCellValue());
						}else{
							cellValue = getRightStr(cell.getNumericCellValue()+"");
						}
					}else if(Cell.CELL_TYPE_STRING==cell.getCellType()){
						cellValue = cell.getStringCellValue();
					}else if(Cell.CELL_TYPE_BOOLEAN==cell.getCellType()){
						cellValue = cell.getBooleanCellValue()+"";
					}else {
						cellValue = cell.toString();
					}
					rowlist.add(cellValue);
				}
				List<String> rowDataList = optRows(r+1,rowlist);
				if(!emptyList(rowDataList)){
					System.out.println("---------"+rowDataList);
					fileList.add(rowDataList);
				}
				rowlist.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new CoreException("read excel error");
		}
		return fileList;
	}
   
	
	private boolean emptyList(List<String> rowDataList){
		if(rowDataList==null||rowDataList.size()<=0){
			return true;
		}
		for(String str:rowDataList){
			if(StrUtils.isNotEmpty(str)) return false;
		}
		return true;
	}
	
	private void readByXml(File file,int sheetIndex){
		List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
		try{
			OPCPackage pkg = OPCPackage.open(file.getAbsolutePath());
			XSSFReader xssfReader = new XSSFReader(pkg);
			SharedStringsTable sst = xssfReader.getSharedStringsTable();
			XMLReader parser = this.fetchSheetParser(sst);
			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			int currSheetIndex = 0;
			while(sheets.hasNext()){
				currSheetIndex++;
				InputStream sheet = sheets.next();
				if(currSheetIndex==sheetIndex){
					curRow = 0;
					InputSource sheetSource = new InputSource(sheet);
					parser.parse(sheetSource);
				}
				sheet.close();
			}
		}catch(Exception e){
			throw new CoreException("read excel error");
		}
	}
	
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException{
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst = sst;
		parser.setContentHandler(this);
		return parser;
	}
    
	
	private String getRightStr(String sNum){
		DecimalFormat decimalFormat = new DecimalFormat("#.######");
		String resultStr = decimalFormat.format(new Double(sNum));
		if(resultStr.matches("^[-+]?\\d+\\.[0]+$")){
			resultStr = resultStr.substring(0,resultStr.indexOf("."));
		}
		return resultStr;
	}
	
	public abstract List<String> optRows(int curRow,List<String> rowList);
	
	
	public void startElement(String uri,String localName,String name,Attributes attributes){
		if(name.equals("c")){
			String cellType = attributes.getValue("t");
			if(cellType!=null&&cellType.equals("s")){
				nextIsString = true;
			}else{
				nextIsString = false;
			}
		}
		lastContents = "";
	}
	
	public void endElement(String uri,String localName,String name){
		if(nextIsString){
			try{
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(name.equals("v")){
			String value = lastContents.trim();
			value = value.equals("")?" ":value;
			rowlist.add(curCol,value);
			curCol++;
		}else{
			if(name.equals("row")){
				try{
					optRows(curRow+1,rowlist);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				rowlist.clear();
				curRow++;
				curCol = 0;
			}
		}
	}
	
	public void characters(char[] ch,int start,int length){
		lastContents+=new String(ch,start,length);
	}
	
	public static String formatDate(String style,Date date){
		SimpleDateFormat df = new SimpleDateFormat(style);
		return df.format(date);
	}
}  
