package com.oyxhExcel;

import java.io.File;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        File file  = new File("D:\\各地州市调查表\\各地州市调查表\\益阳（12）\\三级（5个）\\益阳市中心医院\\");
        ReadExcel obj = new ReadExcel();
        readExcelFromFile(file,obj);
    }
/*    public static void readExcel() {
        ReadExcel obj = new ReadExcel();
        // 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下
        File file = new File("D:\\各地州市调查表\\各地州市调查表\\常德（6）\\二级（3个）\\（缺表3）安乡县人民医院新冠疫情期儿科医疗现状调查表.xlsx");
        File file1 = new File("D:\\各地州市调查表\\各地州市调查表\\益阳（12）\\三级（5个）\\益阳市中心医院\\益阳市中心医院2017-2020分科登记台账(2).xls");
        obj.readExcel(file);
        
        obj.readExcel(file1);
        

    }*/
    public static void readExcelFromFile(File file,ReadExcel obj){
    	
		File[] fs = file.listFiles();
		for(File f:fs){
			if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
				readExcelFromFile(f,obj);
			if(f.isFile())	{
				//若是文件，直接打印
				System.out.println(f);
				 obj.readExcel(f);
			}
				
		}
	}

}
