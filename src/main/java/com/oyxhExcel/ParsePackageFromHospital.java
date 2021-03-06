package com.oyxhExcel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParsePackageFromHospital {
	
	private List<Byte> fragment = new ArrayList<Byte>();//保存上次处理后，剩余的字节
	Map<String,String> attrNameAndInfo = new HashMap<String,String>();
	private int total = 0;
	private int count = 0;

	  /// <summary>
	  /// 对外调用的方法
	  /// </summary>
	  /// <param name="newReceivedData">新接收到的字节数据</param>
	  public void ProcessData(byte[] newReceivedData)
	  {
		  count++;
		  System.out.println("count:"+count);
	    try
	    {	
	    	for (byte b :newReceivedData) {
	    		fragment.add(b);
	    	}   //合并新字节
			
	    	System.out.println("ok");

	     List<DRDAFrame> frames = ParseFrames();//解析帧
	     /*  if (frames.Count > 0)
	      {
	        try
	        {
	          for (int i = 0; i < frames.Count; i++)
	          {
	            log(string.Format("处理数据：{0}", frames[i]));
	            ProcessFrame(frames[i]);//处理帧
	          }
	 
	 
	        }
	        catch (Exception ex)
	        {
	 
	 
	          throw ex;
	        }
	      }
	      SaveFragment(data);//保存此次处理后剩余的片段
*/	    }
	    catch (Exception ex)
	    {
	      //log("处理设备数据出错。");
	    }
	  }
	 
	 
	  /// <summary>
	  /// 循环解析帧
	  /// </summary>
	  /// <param name="data"></param>
	  /// <returns></returns>
	  private List<DRDAFrame> ParseFrames() throws UnsupportedEncodingException
	  {
	    List<DRDAFrame> Frames = new ArrayList<DRDAFrame>();
	
	   // List<Integer> frameStartIndex = GetFrameFlagIndex();//List<Integer>中第一个为位置，第二个为pointcode
	    int frameLength=0;
	    //System.out.println(fragment.size());
	    int lastDdmStart = 0;  //上一个ddm的起始点
	    List<Byte> tempByte = new ArrayList<Byte>();
	    boolean arriveEnd = true ;
	    for (int i = 1; i < fragment.size() - 1 && arriveEnd;i++ ){
	    	//System.out.println("i1:"+ i+": "+fragment.size());
	    	tempByte.clear();
	    	tempByte.add(this.fragment.get(i-1));
	    	tempByte.add(this.fragment.get(i));
	    	//System.out.println( getInt(tempByte));
	    	for(Integer codePoint : DDMCodeMap.map.keySet()) {
	    		//System.out.println("ok2:" + codePoint);
	    		if(codePoint.intValue() == getInt(tempByte)) {
	    			//System.out.println("ok3:" + Integer.toHexString(codePoint));
	    			if(i-lastDdmStart>8) {
	    				frameLength = getInt(fragment.subList(i-9, i-7)); //DDM长度
	    				//System.out.println("i:" + i);
	    				//System.out.println("frameLength:" + frameLength);
	    				if(frameLength + lastDdmStart >= fragment.size()) {
	    					break;
	    				}
	    				List<Byte> ddmByte = fragment.subList(i+1, i-9+frameLength+1);
	    				//boolean isDDMString = isDRDAFrame(ddmByte);  //是否ddm串
	    				if(codePoint.intValue()== 0x2411) {
	    					coverteSQLNameByteToString(ddmByte);
	    				}else if(codePoint.intValue()== 0x241b) {
	    					coverteByteToString(ddmByte);
	    				}
	    				//coverteByteToString(ddmByte);
	    				/*if(isDDMString) {     //是DDMString

	    				}*/
	    				lastDdmStart = i-9+frameLength ;
    					i = lastDdmStart -1 ;
    					//System.out.println("daozhelile ma 2 "+i+":"+fragment.size());
	    			}
	    			break;
	    		}
	    		
	    	}
	    	//System.out.println("daozhelile ma "+i+":"+fragment.size());
	    	//System.out.println("i:"+ i+": "+fragment.size());
	    }
	    //System.out.println("daozhelile ma ");
	    removeByte(lastDdmStart);
	    return Frames;
	  }
	 
	 
	  private void coverteSQLNameByteToString(List<Byte> ddmByte) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		  //System.out.println("get attr"+byte2Hex(ddmByte));
		  this.attrNameAndInfo.clear();
		  List<Byte> tempByte = new ArrayList<Byte>();
		  int arrtNum = 0;
		  int arrLength = 0;
		  tempByte.clear();
		  tempByte.add(ddmByte.get(81));
		  tempByte.add(ddmByte.get(80));
		 // System.out.println("get attr len"+byte2Hex(tempByte));
		  arrtNum = getInt(tempByte);
		 // System.out.println(arrtNum);
		  for(int i = 82;i<ddmByte.size()-1;) {   //第80个开始是字段名
			  System.out.println(i);
			  for(int j=0;j<arrtNum;j++) {
				  List<Byte> attrInfo = ddmByte.subList(i+12, i+16);
				  System.out.println("attrInfo:"+byte2Hex(attrInfo));
				  tempByte.clear();
				  tempByte.add(ddmByte.get(i+27));
				  tempByte.add(ddmByte.get(i+28));
				  arrLength = getInt(tempByte);
				  List<Byte> attrName = ddmByte.subList(i+29, i+29+arrLength);
				//  System.out.println(byte2Hex(attrName));
				//  System.out.println(byte2String(attrName));
				  i += 41+arrLength;
				 // this.attrNameAndInfo.put(byte2String(attrName), byte2Hex(attrInfo));
			  }
		  }
		
	}


	private void coverteByteToString(List<Byte> ddmByte) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		  System.out.println("Date is this:");
		 byte[] bbbb = new byte[ddmByte.size()];
		 int index = 0;
		 List<Byte> tempByte = new ArrayList<Byte>();
		 boolean isLenthPos = true;
		 int secLength = 0;
		 for(int i=2;i<ddmByte.size() - 1;i++) {  //字节前两位为0xff00 开始
			 if(isLenthPos) {
				 tempByte.clear();
				 tempByte.add(ddmByte.get(i));
				 tempByte.add(ddmByte.get(i+1));
				 secLength = getInt(tempByte);
				 i = i+ 1;
				 isLenthPos = false;
			 }else {
				 if(secLength==0) {
					 bbbb[index++] = 38;
					 isLenthPos = true;
				 }else {
					 bbbb[index++] = ddmByte.get(i);
					 secLength--;
				 } 
			 }	
		 }
		// System.out.println(new String(bbbb,"GBK"));
		// System.out.println(byte2Hex(ddmByte));
		  //System.out.println(s);
	}
	  public  String byte2Hex(List<Byte> bytes) {
		  StringBuffer stringBuffer = new StringBuffer();
		  String temp = null;
		  for (int i = 0; i < bytes.size(); i++) {
			  temp = Integer.toHexString(bytes.get(i) & 0xFF);
			  if (temp.length() == 1) {
				  // 1得到一位的进行补0操作
				  stringBuffer.append("0");
				  }
			  stringBuffer.append(temp);
			  }
		  return stringBuffer.toString();
		  }
	  public  String byte2String(List<Byte> bytes) throws UnsupportedEncodingException {
		  
			 byte[] bbbb = new byte[bytes.size()];
		  String temp = null;
		  for (int i = 0; i < bytes.size(); i++) {
			  bbbb[i] = bytes.get(i);			  
		
			  }
		  return new String(bbbb,"GBK");
		  }

	/// <summary>
	  /// 需要根据实际情况重写
	  /// </summary>
	  /// <param name="frame"></param>
	  protected void ProcessFrame(byte[] frame)
	  {
	    int commandTypeIndex = 2;//第三个字节规定命令类型
	    int commandByte = frame[commandTypeIndex];
	    switch (commandByte) //根据命令的不同，生成的command分别处理,使用命令模式
	    {
	      case 1:
	        //
	        break;
	      case 2:
	        break;
	      case 3:
	        break;
	    }
	 
	 
	  }
	 
	 
	  /// <summary>
	  /// 处理解析帧后，剩余的数据
	  /// </summary>
	  /// <param name="frag"></param>
	/*  protected void SaveFragment(List<byte> frag)
	  {
	    int maxFragmentLength = 1000; //未处理的帧的最大长度
	    //遗留数据片段过长，有问题。未防止内存压力过大，需要清空fragment
	    if (frag.Count > maxFragmentLength)
	    {
	      frag = new List<byte>();
	    }
	    fragment.Clear();
	    fragment.AddRange(frag);
	    if (frag.Count > 0)
	    {
	      log(string.Format("剩余数据片段：{0}", frag));
	    }
	 
	 
	  }*/
	 
	 
	  /// <summary>
	  /// 需要根据实际情况重写
	  /// </summary>
	  /// <param name="data"></param>
	  /// <returns>返回endPoint的位置和endPoint
	  private List<Integer> GetFrameFlagIndex()
	  {
		  List<Integer>  mapPos = new ArrayList<Integer>();
		 List<Byte> tempByte = new ArrayList<Byte>();
		 
	    //System.out.println(lastDdmStart);
	    return mapPos;//默认值，没有找到帧头
	  }
	  
	  private boolean isDRDAFrame(List<Byte> ddmByte) {
		  boolean rFlag = true;
		  int frameLength = getInt(ddmByte.subList(0, 2));
		  int cusor = 10;
		  while(cusor < frameLength) {
			  int paralength = getInt(ddmByte.subList(cusor, cusor+2));
			  if(paralength < 5) {
				  break;
			  }
			  cusor += getInt(ddmByte.subList(cusor, cusor+2));
		  }
		  if(cusor == frameLength) {
			  rFlag = true;
		  }else {
			  rFlag = false;
		  }
		  return rFlag;
	  }
	  /**
	   * 
	  * @Title: getInt 
	  * @Description: TODO(这里用一句话描述这个方法的作用) 
	  * @param @param biteArray
	  * @param @return    设定文件 
	  * @return int    返回类型 获取字节的16进制结果 
	  * @throws
	   */
	  public int getInt(List<Byte> biteArray) {
		  return 	(0x0000ff00 	& (biteArray.get(0)  << 8))   | 
					(0x000000ff 	&  biteArray.get(1) );

	  }
	 
	  
	  /**
	   * 
	  * @Title: remove 
	  * @Description: TODO(将私有数据List<Byte> fragment的前 num 个byte移除) 
	  * @param @param num    设定文件 
	  * @return void    返回类型 
	  * @throws
	   */
	  public void removeByte(int num) {
		  System.out.println("removebye");
		  for (int i=0; i<num; i++) {
			   fragment.remove(0);
		   }
		  total += num;
		  System.out.println("total:"+total);
	  }

}
