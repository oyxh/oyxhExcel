package com.oyxhExcel;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;




/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, OpenXML4JException, SAXException
    {
    	FileOutputStream fos = new FileOutputStream("F:\\result\\out.csv");
	       OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

	       CSVPrinter csvPrinter = new CSVPrinter(osw, CSVFormat.DEFAULT);
    	File file = new File( "F:\\result\\test\\");
    	//readOneExcel("F:\\result\\C8\\result2.xlsx");
    	readExcelFromFile(file,csvPrinter);
    	 csvPrinter.flush();
		  csvPrinter.close();
    }

    public static void readExcelFromFile(File file, CSVPrinter csvPrinter) throws IOException, OpenXML4JException, SAXException{
    	
		File[] fs = file.listFiles();
		for(File f:fs){
			if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
				readExcelFromFile(f,csvPrinter);
			if(f.isFile())	{
				//若是文件，直接打印
				System.out.println(f);
				readOneExcel(f.getAbsolutePath(),csvPrinter);
				
			}
				
		}
		
	}
    
    public static void readOneExcel(String filePath, CSVPrinter csvPrinter) throws IOException, OpenXML4JException, SAXException {
    	ParseXlsxExcel excel = new ParseXlsxExcel(filePath,true);
		//ParseXlsxExcel excel = new ParseXlsxExcel("F:\\test.xlsx",true);
	
		HashMap<Integer, HashMap<Integer, String>> list = excel.getExcelList();
		System.out.println(list.size());
		ReadExcel recxel = new ReadExcel(list);

	      
	       recxel.handle(csvPrinter);
		 
    }

}
