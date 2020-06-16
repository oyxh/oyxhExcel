package com.oyxhExcel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ReadExcel {
	private Workbook workbook;
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
	public Workbook getWorkBook(String fileName) throws IOException {
		//创建输入流
		Workbook workbook = null;
	    FileInputStream fis = new FileInputStream(new File(fileName));
	    if(fileName.contains("xlsx")) {
	    	  workbook = new XSSFWorkbook(fis);
	    }else if(fileName.contains("xls")) {
	    	 workbook = new HSSFWorkbook(fis);
	    }
	    //通过构造函数传参
	   return workbook;
	}
	
	/**
	 * 
	* @return 
	 * @Title: getHeader 
	*从解析的行里获取满足条件的表头
	 */
	public int[] getHeader(Map<Integer, String> firstRow) {
		int[] headerPos = new int[6];
		for(int i=0;i<6;i++) {
			headerPos[i] = 255;
		}
		 for (Integer i : firstRow.keySet()) {

		      if(firstRow.get(i).contains("医院")||firstRow.get(i).contains("单位名称")) {
					 headerPos[0] = i;
				 }else if(firstRow.get(i).contains("住院号")||firstRow.get(i).contains("病案号")||firstRow.get(i).contains("syxh")){
					 headerPos[1] = i;
				 }else if(firstRow.get(i).contains("性别")){
					 headerPos[2] = i;
				 }else if(firstRow.get(i).contains("年龄")||firstRow.get(i).contains("月龄")){
					 headerPos[3] = i;
				 }else if(firstRow.get(i).contains("入院时间")||firstRow.get(i).contains("入院日期")){
					 headerPos[4] = i;
				 }else if(firstRow.get(i).contains("诊断")||firstRow.get(i).contains("疾病名称")){
					 headerPos[5] = i;
				 }
		  }
		 return headerPos;
	}
	/**
	 * 从Row中间解析出String的列表,Integer 序号
	 * @param row 
	 * @return 
	 * @return
	 */
	
	public Map<Integer, String> getRowFromExcel(Row row){
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		int start = row.getFirstCellNum();
		int end = row.getLastCellNum();
		Map<Integer,String> rowMap = new HashMap<Integer,String>();
		for(int i=start;i<end;i++) {
			Cell cell = row.getCell(i);
			String cells = "";
			if(cell==null) {
				
			}else {
				if(cell.getCellType() == CellType.FORMULA) {
					if(cell.getCachedFormulaResultType()==CellType.ERROR) {
						cells = "error";
						continue;
					}else {
						CellValue cellValue =  evaluator.evaluate(cell);//evaluator.evaluate(cell);
						if (cellValue == null) {
					           continue;
						}
						if(cellValue.getCellTypeEnum()==CellType.NUMERIC) {
							DecimalFormat df = new DecimalFormat("0");
							cells = df.format(cellValue.getNumberValue());
						}
					}		
				}else if(cell.getCellType() == CellType.NUMERIC) {
					DecimalFormat df = new DecimalFormat("0");
					cells = df.format(cell.getNumericCellValue());
				}
				else {
					cells = cell.toString();
				}	
			}
			rowMap.put(i, cells);
			//System.out.println(cells);
		}
		return rowMap;
			
	}
	
	
	public SheetData getSheetFromExcel(Sheet sheet) {
		SheetData sheetdata = new SheetData();
		if(sheet==null) {
			return sheetdata;
		}else {
			int maxRow = sheet.getLastRowNum();
			//System.out.println(maxRow);
			Row firstLine = sheet.getRow(0);
			if(firstLine != null) {
				sheetdata.setHeader(getRowFromExcel(firstLine));
			}
			for(int row_i=1;row_i<maxRow;row_i++) {
				Row row = sheet.getRow(row_i);
				if(row == null) {   //空行继续下一行
					continue;
				}
				sheetdata.getContent().add(getRowFromExcel(row));
			}
		}
		return sheetdata;
	}
    public void readExcel(File file) {
    	 try {
			this.workbook = getWorkBook(file.getAbsolutePath());
			 
			if(workbook == null) {
				System.out.println("不是excel文件");
			}else {
				
				for(int index = 0;index < workbook.getNumberOfSheets();index++) {
					Sheet sheet = workbook.getSheetAt(index);  //取页面
					if(sheet==null) {
						continue;
					}else {
						SheetData sheetdate = getSheetFromExcel( sheet);
						for(Integer i:sheetdate.getHeader().keySet()) {
							System.out.println(sheetdate.getHeader().get(i));
						}
						handleSheetData(sheetdate,file.getName());
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    
    /**
     * @throws IOException 
     * 
    * @Title: handleSheetData 
    * @Description: 用来处理获取的sheet数据
    * @param @param sheetdate    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void handleSheetData(SheetData sheetData,String fileName) throws IOException {
    	int []headerPos = getHeader(sheetData.getHeader());   //需要获取数据的表头在sheet表头的位置（第几列）
    	for(int i=0;i<headerPos.length;i++) {
    		System.out.println(headerPos[i]);
    	}
    	String outfile =  "G:\\R\\文件处理\\各地州市调查表\\";
        String outTxt = outfile + "result.txt";
        File out = new File(outTxt);
    	if(sheetData.getContent().size()>50) {
    		for(Map<Integer,String> rowData:sheetData.getContent()) {
    			String line = "";
    			if(rowData.size()<5) {
    				continue;
    			}
        		for(int i=0;i<headerPos.length;i++) {
        			//System.out.print(rowData.get(headerPos[i])+"$");
        			if(rowData.get(headerPos[i])!=null) {
        				String cellString = rowData.get(headerPos[i]).replaceAll("[\\t\\n\\r]", "");
        				if(i==3) { //年龄
        					System.out.print(cellString);
        					cellString.replaceAll("个", "");
        					int pos_month = cellString.indexOf("月");
        					if(pos_month > 0) {
        						cellString = cellString.substring(0, pos_month);
        					}
        					int pos_year = cellString.indexOf("岁");
        					if(pos_year >0) {
        						String leftS = cellString.substring(0, pos_year);
        						String rightS = cellString.substring(pos_year+1, cellString.length());
        						int month = 0;
        						if(leftS.matches("^([1-9][0-9]*)$")) {
        							month += Integer.valueOf(leftS)*12;
        							if(rightS.matches("^([1-9][0-9]*)$")) {
        								month += Integer.valueOf(rightS);
        							}
        							
        						} 
        						cellString = String.valueOf(month);
        						
        					}
        					if(!cellString.matches("^([1-9][0-9]*)$")&&!cellString.trim().equals("")) {
        						cellString = "0";
        					}
        					System.out.print(":"+cellString);
        					System.out.println();
        				}
        				
        				line += cellString+"$";
        			}else {
        				line += "$";
        			}
        			
        		}
        		line += "\r\n";
        		FileUtils.writeStringToFile(out,line, "UTF-8", true); 
        	}
    		
    	}
    	   	
    }
}