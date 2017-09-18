package com.jway.corpmng;

import java.util.*;

import com.jway.*;

public class SMSAuthProvider extends Util implements IEmptiable {
	//private static SMSAuthProvider instance;
	
	private Map<String, SMSAuthDTO> data;
	
	protected static class Singleton{
		private static final SMSAuthProvider instance = new SMSAuthProvider();
	}
	
	protected SMSAuthProvider(IDBExecute db){
		collect(db);
	}
	
	protected SMSAuthProvider(){
		collect(DBExecuteFactory.create("EXBILL"));
	}
	
	public static SMSAuthProvider getInstance(IDBExecute db){
//		if (instance == null){
//			instance = new SMSAuthProvider(db);
//		}
//		
//		return instance;
		return Singleton.instance;
	}
	
	public boolean hasAuth(String ip){
		try{
			String key;
			String[] aKey;
			SMSAuthDTO dto = null;
			//System.out.println(ip + " ::: " + sCClassIP);
			//System.out.println("SMSAuthProv->hasAuth---ip=" + ip);
			
			if (isEmpty(ip)){
				return false;
			}
			
			if (ip.contains(".") == false){
				key = ip;
				aKey = key.split("\\|");
				
				for(String mac : aKey){
					if (data.containsKey(mac)){
						dto = data.get(mac);
						break;
					}
				}
				
				if (dto == null){
					return false;
				}
			}
			else{
				key = convertToCClassIP(ip);
				
				if (!data.containsKey(key)){
					return false;
				}
				
				dto = data.get(key);
			}
			//System.out.println("hasAuth = yes " + dto.getResult());
			
			return dto.getResult() == 1;
		}
		catch(Exception ex){
			ex.printStackTrace();
			
			return false;
		}
	}

	/**
	 * 
	 * <pre>
	 * 특정 IP, 혹은 MAC 에 대하여 SMS 인증 기록 정보를 가져 온다.
	 * </pre>
	 * @param ip 일반적인 IP 혹은 MAC 주소
	 * @return
	 */
	public SMSAuthDTO get(String ip){
		String key;
		String[] aKey;
		
		System.out.println("SMSAuthProvider->get--- ip=" + ip );
		
		if (!isEmpty(ip) && !ip.contains(".")){
			key = ip.toUpperCase();
			aKey = key.split("\\|");
			
			for(String sKey : aKey){
				if (data.containsKey(sKey)){
					return data.get(sKey);
				}
			}
			
			return null;
		}
		else{
			key = convertToCClassIP(ip);
			
			return data.get(key);
		}
		
	}
	
	public void add(SMSAuthDTO dto){
		String ip = dto.getIp();
		String key;
		
		System.out.println("SMSAuthProvider->add --- ip=" + ip);
		
		if (isEmpty(ip)){
			key = dto.getMac();
			
			key = (isEmpty(key))? "" : key.toUpperCase();
		}
		else{
			key = convertToCClassIP(ip);
		}
		
		data.put(key, dto);
	}
	
	public void remove(String ip){
		String key;
		
		if (!isEmpty(ip) && !ip.contains(".")){
			key = ip.toUpperCase();
		}
		else{
			key = convertToCClassIP(ip);
		}
		
		data.remove(key);
	}
	
	public void clear(){
		System.out.println("clear before: " + data.size());
		data.clear();
		System.out.println("clear after: " + data.size());
		//instance = null;
	}
	
	public void collect(IDBExecute db){
		CorpLoginModel model = new CorpLoginModel(db);
		List<SMSAuthDTO> list = model.selectSMSAuth();
		String mac;
		String ip;
		String ccIP;
		
		data = new HashMap<String, SMSAuthDTO>();
		
		for(SMSAuthDTO dto : list){
			ip = dto.getIp();
			ccIP = convertToCClassIP(ip);
			mac = dto.getMac();
			
			System.out.println("collect ::: " + ip + " ::: " + ccIP + " ::: MAC=" + mac);
			
			if (!isEmpty(mac)){
				data.put(mac, dto);
			}
			if (!isEmpty(ccIP)){
				data.put(ccIP, dto);
			}
		}
		
		model.empty();
	}

	public void empty() {
		data.clear();
		data = null;
		//instance = null;
	}
}
