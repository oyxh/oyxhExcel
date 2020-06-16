package com.oyxhExcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetData {
	private Map<Integer, String> header;
	private List<Map<Integer,String>> content;
	SheetData(){
		this.header = new HashMap<Integer,String>();
		this.content = new ArrayList<Map<Integer,String>>();
	}
	public Map<Integer, String> getHeader() {
		return header;
	}
	public void setHeader(Map<Integer, String> header) {
		this.header = header;
	}
	public List<Map<Integer, String>> getContent() {
		return content;
	}
	public void setContent(List<Map<Integer, String>> content) {
		this.content = content;
	}
	

}
