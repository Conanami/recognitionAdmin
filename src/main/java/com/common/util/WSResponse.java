package com.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WSResponse <T>{
	private int respCode;
	private String respDescription;
	
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getRespDescription() {
		return respDescription;
	}
	public void setRespDescription(String respDescription) {
		this.respDescription = respDescription;
	}

	private int total;
	private List<T> rows = new ArrayList<T>();
	
	public void add(T obj){
		rows.add(obj);
	}
	public void addAll(Collection<? extends T> c){
		rows.addAll(c);
	}
	
	public T getItem(int i){
		if (rows.size()<=i) {
			return null;
		}
		return rows.get(i);
	}
	
	public WSResponse(){
		
	}
	
	@Override
	public String toString() {
		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			obm.writeValue(out, this);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return new String(out.toByteArray());
	}
	
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	/**
	 * @return the rows
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
