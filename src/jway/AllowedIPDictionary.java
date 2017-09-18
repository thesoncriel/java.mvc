package com.jway;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 허용된 IP 목록을 가지며
 * 특정 IP의 허용 여부를 확인하거나
 * 허용 IP를 추가/삭제 하는 기능을 가진다.
 * 주로 IPFilter 에서 쓰인다.
 * </pre>
 * @author jhson
 *
 */
public interface AllowedIPDictionary {
	/**
	 * 
	 * <pre>
	 * 등록된 모든 허용 IP 내역을 가져 온다.
	 * Map 내 key는 C-Class IP, value는 지사ID(ID_CORP) 이다.
	 * </pre>
	 * @return
	 */
	public Map<String, List<String>> getAll();
	/**
	 * 
	 * <pre>
	 * 특정 IP(혹은 MAC)에 대하여 허용된 것인지 확인한다.
	 * </pre>
	 * @param ip
	 * @return
	 */
	public boolean isAllowed(String ip);
	/**
	 * 
	 * <pre>
	 * 특정 IP 및 ID 에 대하여 모두 혀용된 것인지 확인한다.
	 * </pre>
	 * @param ip
	 * @param id
	 * @return
	 */
	public boolean isAllowedBoth(String ip, String id);
	/**
	 * 
	 * <pre>
	 * 특정 IP(혹은 MAC)와 ID를 허용 목록에 추가 한다.
	 * </pre>
	 * @param ip
	 */
	public void add(String ip, String id);
	/**
	 * 
	 * <pre>
	 * 특정 IP를 허용 목록에서 삭제 한다.
	 * </pre>
	 * @param ip
	 */
	public void remove(String ip);
	
	/**
	 * <pre>
	 * 자료를 수집하여 메모리에 적재한다.
	 * </pre>
	 * @param db
	 */
	public void collect(IDBExecute db);
}
