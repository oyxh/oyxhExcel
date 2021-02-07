package com.oyxhExcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParsePackageFromHospital {
	
	private List<Byte> fragment = new ArrayList<Byte>();//保存上次处理后，剩余的字节
	private int total = 0;

	  /// <summary>
	  /// 对外调用的方法
	  /// </summary>
	  /// <param name="newReceivedData">新接收到的字节数据</param>
	  public void ProcessData(byte[] newReceivedData)
	  {
	    try
	    {
	    	for (byte b :newReceivedData) {
	    		fragment.add(b);
	    	}   //合并新字节
			

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
	  private List<DRDAFrame> ParseFrames()
	  {
	    List<DRDAFrame> Frames = new ArrayList<DRDAFrame>();
	    List<Integer> frameStartIndex = GetFrameFlagIndex();//List<Integer>中第一个为位置，第二个为pointcode
	    int frameLength=0;
	    System.out.println("pos:"+frameStartIndex.get(0));
	    System.out.println(fragment.size());
	    while (frameStartIndex.size() == 2)
	    {
	    	 //System.out.println("pos:"+frameStartIndex.get(0));
	      if(frameStartIndex.get(0) > 8) {
	    	  
	    	// System.out.println( fragment.get(0)+" "+fragment.get(1)+" "+fragment.get(3));
	    	 
	    	  frameLength = getInt(fragment.subList( frameStartIndex.get(0)-9, frameStartIndex.get(0)-7));
	    	  System.out.println("frameLength:"+frameLength);
	    	 //System.out.println("This is a "+ DDMCodeMap.map.get(frameStartIndex.get(1)));
	    	  removeByte(  frameStartIndex.get(0)-9+frameLength);
	    	 // System.out.println(frameStartIndex.get(0)-9+frameLength+" "+fragment.size());
	      }else {
	    	  
	      }
	    /*  int frameEndIndex = GetFrameEndIndex(data, frameStartIndex);//判断帧的结束
	      if (frameEndIndex < 0)//帧不完整
	      {
	        return Frames;
	      }
	      byte[] OneFramebyte = GetOneFrame(data, frameStartIndex, frameEndIndex);
	      Frames.Add(OneFramebyte);
	      //data.RemoveRange(0, frameStartIndex);//可以有这一句，避免不完整的帧，对后续解析造成影响
	      data.RemoveRange(frameStartIndex, frameEndIndex - frameStartIndex);//移除已经处理的数据*/
	      frameStartIndex = GetFrameFlagIndex();
	      System.out.println(frameStartIndex.get(0)+" :"+DDMCodeMap.map.get(frameStartIndex.get(1)));
	    }
	    return Frames;
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
	    for (int i = 1; i < fragment.size() - 1 ; i++)
	    {
	    	tempByte.clear();
	    	tempByte.add(this.fragment.get(i-1));
	    	tempByte.add(this.fragment.get(i));
	    	//System.out.println( getInt(tempByte));
	    	for(Integer codePoint : DDMCodeMap.map.keySet()) {
	    		if(codePoint.intValue() == getInt(tempByte)) {
	    			if(codePoint.intValue()==5292) {
	    				System.out.println(5292+" "+i);
	    			}
	    			if(i>8) {
		    			mapPos.add(i);
		    			mapPos.add(codePoint);
		    			return mapPos;
		    			
	    			}

	    		}
	    	}
	    }
	    return mapPos;//默认值，没有找到帧头
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
		  for (int i=0; i<num; i++) {
			   fragment.remove(0);
		   }
		  total += num;
		  System.out.println("total:"+total);
	  }
	  
	 
	  /// <summary>
	  /// 需要根据实际情况重写
	  /// </summary>
	  /// <param name="datas"></param>
	  /// <returns></returns>
	 
	  /// <summary>
	  /// 获取一个完整的帧的所有字节
	  /// </summary>
	  /// <param name="data"></param>
	  /// <param name="frameStartIndex">帧的开始位置</param>
	  /// <param name="frameEndIndex">帧的结束位置</param>
	  /// <returns></returns>
	
	 

	 


}
