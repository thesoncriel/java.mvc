package com.jway.corpmng;

import java.util.*;
import com.jway.*;

/**
 * <pre>
 * 허용 IP 목록을 스스로 가지며 해당 내용에 대한 서비스를 제공한다.
 * 허용 IP 검증 시 C-Class IP (첫번째~세번째 번호까지)를 이용하며
 * 등록된 허용 IP 목록 역시 C-Class IP를 이용한다.
 * Singleton Pattern 으로 운용된다.
 * </pre>
 * @author jhson
 * {@link com.jway.AllowedIPDictionary}
 */
public class AllowedIPDicProvider extends Util implements AllowedIPDictionary {
	private static AllowedIPDicProvider instance;
	
	private HashMap<String, List<String>> allowIP;
	
	protected AllowedIPDicProvider(IDBExecute db){
		collect(db);
	}
	/**
	 * 
	 * <pre>
	 * 해당 클래스에 대한 인스턴스를 가져 온다.
	 * </pre>
	 * @param db 사용할 Table이 참조된 {@link com.jway.IDBExecute} 클래스.
	 * @return
	 */
	public synchronized static AllowedIPDictionary getInstance(IDBExecute db){
		if (instance == null){
			instance = new AllowedIPDicProvider(db);
		}
		
		return instance;
	}
	/**
	 * 
	 * <pre>
	 * 해당 클래스에 대한 인스턴스를 가져 온다.
	 * 자동으로 EXBILL 출신의 DB를 이용한다.
	 * </pre>
	 * @return
	 */
	public synchronized static AllowedIPDictionary getInstance(){
		IDBExecute db;
		
		if (instance == null){
			db = DBExecuteFactory.create("EXBILL");
			instance = new AllowedIPDicProvider(db);
			db.empty();
			db = null;
		}
		
		return instance;
	}

	public Map<String, List<String>> getAll() {
		return allowIP;
	}
	
	/**
	 * <pre>
	 * 허용된 IP 여부를 판변 한다.
	 * C-Class IP를 이용한다.
	 * </pre>
	 * @param ip 허용 여부를 확인하고 싶은 C-Class IP 혹은 MAC 주소. (대소문자 구별 없음)
	 */
	public boolean isAllowed(String ip) {
		String mac;
		String[] aMac;
		
		if (!isEmpty(ip) && !ip.contains(".")){
			mac = ip.toUpperCase();
			aMac = mac.split("\\|");
			
			for(String sMac : aMac){
				if (allowIP.containsKey(sMac)){
					return true;
				}
			}
			
			return false;
		}
		
		return allowIP.containsKey(convertToCClassIP(ip));
	}
	
	public boolean isAllowedBoth(String ip, String loginId) {
		String mac;
		String[] aMac;
		
		//System.out.println("AllowedIPDic->isAllowedBoth--- ip=" + ip + ", loginId=" + loginId);
		
		if (!isEmpty(ip) && !ip.contains(".")){
			mac = ip.toUpperCase();
			aMac = mac.split("\\|");
			
			for(String sMac : aMac){
				if (checkAllowedBoth(sMac, loginId)){
					return true;
				}
			}
			
			return false;
		}
		
		return checkAllowedBoth(convertToCClassIP(ip), loginId);
	}
	
	protected boolean checkAllowedBoth(String ccIP, String loginId){
		List<String> loginIdList;
		
		if (allowIP.containsKey(ccIP)){
			loginIdList = allowIP.get(ccIP);
			
			if (loginIdList.contains(loginId)){
				return true;
			}
		}
		
		return false;
	}
	
	public void collect(IDBExecute db){
		List<Map<String,String>> list;
		CorpLoginModel model;
		String mac;
		String ip;
		String sIdLogin;
		
		allowIP = new HashMap<String, List<String>>();
		model = new CorpLoginModel(db);
		list = model.selectAllCorpAllowIP();
		
		for(Map<String,String> item : list){
			sIdLogin = item.get("id_login");
			ip = item.get("ip");
			mac = item.get("mac");
			
			if (isEmpty(ip) == false){
				put(ip, sIdLogin);
			}
			if (isEmpty(mac) == false){
				put(mac, sIdLogin);
			}
		}
		
		model.empty();
		model = null;
		
		if (list != null){
			list.clear();
			list = null;
		}
		
		//System.out.println(allowIP.toString());
	}
	
	protected void put(String key, String idLogin){
		List<String> loginIdList;
		
		//System.out.println("AllowedIPDic->put--key=" + key + ",idLogin=" + idLogin);
		
		if (allowIP.containsKey(key)){
			allowIP.get(key).add(idLogin);
		}
		else{
			loginIdList = new ArrayList<String>();
			loginIdList.add(idLogin);
			allowIP.put(key, loginIdList);
		}
	}

	public void add(String ip, String idLogin) {
		put(ip, idLogin);
	}

	public void remove(String ip) {
		allowIP.remove(ip);
	}
	

}
