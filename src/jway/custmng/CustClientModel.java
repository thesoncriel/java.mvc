package com.jway.custmng;

import java.util.List;
import java.util.Map;

import com.jway.*;

public class CustClientModel extends CustManageModel {

	public CustClientModel(IDBExecute db) {
		super(db);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public List selectClientList(Map param){
		String sQuery = "" +
				"";
		
		return select(sQuery, param, "id_cust");
	}
	
	@SuppressWarnings("rawtypes")
	public boolean updateClientEnable(Map param){
		String sQuery = "" +
				"";
		
		return execute(sQuery, param, "yn_use", "id_update", "id_cust", "client_mac");
	}
	
	@SuppressWarnings("rawtypes")
	public List selectPeriodServerList(Map param){
		String sQuery = "" +
				""
				;
		
		if (isWOWMode(param)){
			return selectPeriodServerListByWOW(param);
		}
		
		return select(sQuery, param);
	}
	
	@SuppressWarnings("rawtypes")
	public List selectPeriodServerListByWOW(Map param){
		String sQuery = "" +
				"";
		
		return select(sQuery, param);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean isWOWMode(Map param){
		try{
			return "WOW".equals(param.get("mode").toString());
		}
		catch(Exception ex){
			return false;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public Map selectOneCustInfo(Map param){
		String sQuery = "" +
				"";
		
		return selectOne(sQuery, param, "id_cust");
	}
	
}
