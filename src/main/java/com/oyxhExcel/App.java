package com.oyxhExcel;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    	
    	long start = System.currentTimeMillis();
		//ParseXlsxExcel excel = new ParseXlsxExcel("F:\\result\\C4\\result8-1.xlsx",true);
		ParseXlsxExcel excel = new ParseXlsxExcel("F:\\test.xlsx",true);
		excel.close();
		HashMap<Integer, HashMap<Integer, String>> list = excel.getExcelList();
		list.forEach((key, value) -> {
			System.out.println("第"+key+"行");
			for (Integer sonkey : value.keySet()) {
		        System.out.println("第" + sonkey + "列的值:" + value.get(sonkey));
		    }
	    });
		long end = System.currentTimeMillis();
		
		System.out.println(list.size());//19677984
		System.out.println(end-start); //16389ms
		/*for(String str:list) {
			System.out.println(str);
		}*/
	 
    }

    public static void readExcelFromFile(File file){
    	
		File[] fs = file.listFiles();
		for(File f:fs){
			if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
				readExcelFromFile(f);
			if(f.isFile())	{
				//若是文件，直接打印
				System.out.println(f);
				
			}
				
		}
	}

}
