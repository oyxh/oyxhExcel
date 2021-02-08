package com.oyxhExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
    	for(Integer a:DDMCodeMap.map.keySet()) {
    		//System.out.println(a);
    	}
    	readByteFromFile();
    	//readAllExcel();
    }
    
    public static void readByteFromFile() throws IOException {
    	FileInputStream in=new FileInputStream("E:\\result\\test");
        //FileOutputStream中的文件不存在，将自动新建文件
        OutputStream out=new FileOutputStream("E:\\result\\test_result.txt");
        byte[] buff=new byte[1024];
        ParsePackageFromHospital ppfh= new ParsePackageFromHospital();
        int b;
        long beginTime=System.currentTimeMillis();
        int i=0;
        while ((b=in.read(buff))!=-1) {
        	System.out.println(i++);
        	ppfh.ProcessData(buff);
        	if(i>3) {
        		//break;
        	}
          //out.write(buff,0,b);
        }
        long endTime=System.currentTimeMillis();
        System.out.println("运行时长为: "+(endTime-beginTime)+"毫秒");
        in.close();
        out.close();
        System.out.println("正常运行！");
      }
    
    
    public static void readAllExcel() throws IOException, OpenXML4JException, SAXException {
    	FileOutputStream fos = new FileOutputStream("F:\\result\\out2.csv");
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
		excel.close();
		System.out.println(list.size());
		ReadExcel recxel = new ReadExcel(list);
		
	      
	       recxel.handle(csvPrinter);
		 
    }

}
