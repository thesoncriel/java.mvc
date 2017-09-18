package com.jway;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONResponse implements IEmptiable{
	private Object data;
	private Object info;
	private Object etc;
	private int count = 0;
	private JSONResult result;
	private ObjectMapper json;
	
	public static JSONResponse parse(String src){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(src, JSONResponse.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JSONResponse(){
		this.result = new JSONResult();
		this.json = new ObjectMapper();
	}
	
	public Object getData(){
		return data;
	}
	public JSONResponse setData(Object data){
		this.data = data;
		
		return this;
	}
	
	public Object getInfo(){
		return info;
	}
	public JSONResponse setInfo(Object info){
		this.info = info;
		
		return this;
	}
	
	public Object getEtc() {
		return etc;
	}
	public JSONResponse setEtc(Object etc) {
		this.etc = etc;
		
		return this;
	}

	public int getCount(){
		return count;
	}
	public JSONResponse setCount(int count){
		this.count = count;
		
		return this;
	}
	
	public JSONResult getResult(){
		return this.result;
	}
	
	public String toString(Object target){
		try {
			return json.writeValueAsString(target);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "{}";
	}
	
	public String toString(){
		return toString(this);
	}

	public void empty(){
		if (this.result != null){
			this.result.empty();
		}
		
		this.result = null;
		this.json = null;
		this.data = null;
		this.etc = null;
	}
}
