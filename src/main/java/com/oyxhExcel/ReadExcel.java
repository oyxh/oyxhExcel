package com.oyxhExcel;




import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;



public class ReadExcel {
	HashMap<Integer, HashMap<Integer, String>> list;
	public ReadExcel(HashMap<Integer, HashMap<Integer, String>> list) {
		this.list = list;
	}
	
	public void handle(CSVPrinter csvPrinter) throws IOException {
			  
		 for (Integer row : list.keySet()) {     
		      //System.out.println("第"+row + "行:" );
		      Map<Integer, String> rowContent = list.get(row);
		      String[] patientInfo = null;
		      String[][] table = null;
		      for(Integer column : rowContent.keySet()) {	    	  
		    	  if(column.intValue() == 1 ) {
		    		  //System.out.println("第"+column + "列:" + rowContent.get(column) );
		    		 patientInfo = getArray(rowContent.get(column));
		    	  }
		    	  if(column.intValue() == 6 ) {
		    		  //System.out.println("第"+column + "列:" + rowContent.get(column) );
		    		  table = getListFromOneCell(rowContent.get(column));
		    		  table = reverse(table);		    		 
		    	  }		    	  
		      }
		      if(table !=null ) {
		    	  int minNum = table.length < 2?table.length:2;
			      for(int i = 0;i<minNum;i++) {
			    	  List<String> record = new ArrayList<String>();
	    			  for(int j = 0;j<patientInfo.length;j++) {
	    				  //System.out.print(patientInfo[j]+" ");
	    				  record.add(patientInfo[j]);
	    			  }
	    			 
	    			  //csvPrinter.printRecord("张三", 20, "湖北");
	    			  for(int j =0;j<table[i].length;j++) {
	    				  record.add(table[i][j]);
	    				  //System.out.print(table[i][j]+" ");
	    			  }
	    			  csvPrinter.printRecord(record);
	    			  //System.out.println();
	    		  }
		      }

		 }
	   
	}
	
	/**
	 * 
	* @Title: getListFromOneCell 
	* @Description: TODO(获取单元格里的表格，以换行和制表符为界) 
	 */
	public String[][] getListFromOneCell(String cell){
			cell = cell.replaceAll("\r\n\t", "@\t");
			//System.out.println(cell);
			String[] rows = StringUtils.split(cell,"@");
			String[][] genTable = new String[rows.length][] ;
			int index = 0;
			for(String rowString:rows) {
				String []rowCell = getArray(rowString);
				genTable[index++] = rowCell;
			}
		return genTable;
	}
	
	/**
	 * 
	* @Title: getArray 
	* @Description: TODO(获取一行的字符数组，用Tab分开) 
	 */
	public String[] getArray(String rowString) {
		String[] rows = StringUtils.splitPreserveAllTokens(rowString,"\t");
				
		return rows;
	}
	
	   // 将矩阵转置
   public  String[][] reverse(String[][] table) {

	   int m = table.length;
	   int n = table[0].length;
	   String[][] reverseTable = new String[n-1][m-1];
        for (int i = 0; i < m - 1; i++) {
            for (int j = 1; j < n ; j++) {  	
            	reverseTable[j-1][i] = table[i][j];
             }
            
        }
        return reverseTable;
    }
	     
   public void testWrite() throws Exception {
       FileOutputStream fos = new FileOutputStream("E:/cjsworkspace/cjs-excel-demo/target/abc.csv");
       OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

       CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("姓名", "年龄", "家乡");
       CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);

//       csvPrinter = CSVFormat.DEFAULT.withHeader("姓名", "年龄", "家乡").print(osw);

       for (int i = 0; i < 10; i++) {
           csvPrinter.printRecord("张三", 20, "湖北");
       }

       csvPrinter.flush();
       csvPrinter.close();

   }
	
}