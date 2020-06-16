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
			System.out.println(cells);
		}
		return rowMap;
			
	}
	
	
	public List<Map<Integer, String>> getSheetFromExcel(Sheet sheet) {
		
		List<Map<Integer,String>> sheetContent = new ArrayList<Map<Integer,String>>();
		if(sheet==null) {
			return sheetContent;
		}else {
			int maxRow = sheet.getLastRowNum();
			//System.out.println(maxRow);
			for(int row_i=0;row_i<maxRow;row_i++) {
				Row row = sheet.getRow(row_i);
				if(row == null) {   //空行继续下一行
					continue;
				}
				sheetContent.add(getRowFromExcel(row));
			}
		}
		return sheetContent;
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
						getSheetFromExcel( sheet);
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
}