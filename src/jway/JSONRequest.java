package com.jway;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONRequest implements IEmptiable {
	@SuppressWarnings("rawtypes")
	private List<Map> reqList;
	
	public static JSONRequest parse(String src){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(src, JSONRequest.class);
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

	@SuppressWarnings("rawtypes")
	public List<Map> getReqList(){
		return this.reqList;
	}

	@SuppressWarnings("rawtypes")
	public void setReqList(List<Map> list){
		this.reqList = list;
	}

	@SuppressWarnings("rawtypes")
	public void empty() {
		if (reqList != null){
			for(Map map : reqList){
				map.clear();
			}
			reqList.clear();
			reqList = null;
		}
	}
}
