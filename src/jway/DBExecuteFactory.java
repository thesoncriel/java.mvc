package com.jway;

import com.imnetpia.exbill.EXServer;

public class DBExecuteFactory {
	public static IDBExecute create(String type){
		if ("EXBILL".equals(type)){
			return new ExserverDBExecute(new EXServer());
		}
		
		if ("ONS_OUTSIDE".equals(type)){
			return new OutsideDBExecute();
		}
		
		return null;
	}
	
	public static IDBExecute create(Object bts){
		if (bts == null){
			return create("EXBILL");
		}
		
		if (bts instanceof EXServer){
			return new ExserverDBExecute(bts);
		}
		
		return null;
	}
}
