package com.jway.corpmng;

import java.util.Map;
import com.jway.*;

public class SMSAuthDTO extends BaseMapDTO {
	private int id_sms_auth;
	private String id_corp;
	private String id_login;
	private String ip;
	private String mac;
	private String tel;
	private String auth_code;
	private int result = 0;
	private int try_count = 0;
	
	public SMSAuthDTO(){
		
	}
	@SuppressWarnings("rawtypes")
	public SMSAuthDTO(Map data) {
		fromMap(data);
	}
	
	public int getId_sms_auth() {
		return id_sms_auth;
	}
	public void setId_sms_auth(int id_sms_auth) {
		this.id_sms_auth = id_sms_auth;
	}
	public String getId_corp() {
		return id_corp;
	}
	public void setId_corp(String id_corp) {
		this.id_corp = id_corp;
	}
	public String getId_login() {
		return id_login;
	}
	public void setId_login(String id_login) {
		this.id_login = id_login;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	public int getTry_count(){
		return try_count;
	}
	public void setTry_count(int try_count){
		this.try_count = try_count;
	}
	
	public int incTry_count(){
		return ++try_count;
	}
	
	@Override
	public String toString() {
		return toMap().toString();
	}
}
