package com.oyxhExcel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParsePackageFromHospital {
	
	private List<Byte> fragment = new ArrayList<Byte>();//保存上次处理后，剩余的字节
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
	    System.out.println(fragment.size());
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
	    			//System.out.println("ok3:" + codePoint);
	    			if(i-lastDdmStart>8) {
	    				frameLength = getInt(fragment.subList(i-9, i-7)); //DDM长度
	    				System.out.println("i:" + i);
	    				System.out.println("frameLength:" + frameLength);
	    				if(frameLength + lastDdmStart >= fragment.size()) {
	    					break;
	    				}
	    				List<Byte> ddmByte = fragment.subList(i+1, i-9+frameLength+1);
	    				//boolean isDDMString = isDRDAFrame(ddmByte);  //是否ddm串
	    				coverteByteToString(ddmByte);
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
	 
	 
	  private void coverteByteToString(List<Byte> ddmByte) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		 byte[] bbbb = new byte[ddmByte.size()];
		 int index = 0;
		 for(byte b:ddmByte) {
			 bbbb[index++] = b;
		 }
		 System.out.println(new String(bbbb,"GBK"));
		  char[] chars = new char[ddmByte.size()];
		  for (int i = 0; i < ddmByte.size(); ++i) {
		      chars[i] = (char) (ddmByte.get(i) & 0xFF);
		     String  temp = Integer.toHexString(ddmByte.get(i) & 0xFF);
		      System.out.print( temp);
		  }
		  System.out.println();
		  String s = new String(chars);
		  //System.out.println(s);
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
