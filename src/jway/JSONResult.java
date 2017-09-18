package com.jway;

import java.util.ArrayList;
import java.util.List;

public class JSONResult implements IEmptiable{
	private String msg = "";
	private String redirect = "";
	private boolean valid = true;
	private boolean auth = true;
	private boolean state = true;
	@SuppressWarnings("rawtypes")
	private List err;
	private Object debug;
	
	@SuppressWarnings("rawtypes")
	public JSONResult(){
		this.err = new ArrayList();
	}
	
	public String getMsg(){
		return this.msg;
	}
	public JSONResult setMsg(String msg){
		this.msg = msg;
		
		return this;
	}
	
	public String getRedirect(){
		return this.redirect;
	}
	public JSONResult setRedirect(String redirect){
		this.redirect = redirect;
		
		return this;
	}
	
	public boolean getValid(){
		return this.valid;
	}
	public JSONResult setValid(boolean valid){
		this.valid = valid;
		
		return this;
	}
	
	public boolean getAuth(){
		return this.auth;
	}
	public JSONResult setAuth(boolean auth){
		this.auth = auth;
		
		return this;
	}
	
	public boolean getState(){
		return this.state;
	}
	public JSONResult setState(boolean state){
		this.state = state;
		
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public List getErr(){
		return this.err;
	}
	@SuppressWarnings("unchecked")
	public JSONResult addErr(Object err){
		this.err.add(err);
		
		return this;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONResult addErr(List errList){
		this.err.addAll(errList);
		
		return this;
	}

	public Object getDebug() {
		return debug;
	}
	public JSONResult setDebug(Object debug) {
		this.debug = debug;
		
		return this;
	}
	
	public void empty(){
		if (this.err != null){
			this.err.clear();
		}
		this.err = null;
		this.debug = null;
	}
}