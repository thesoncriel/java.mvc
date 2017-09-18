package com.jway;

import java.util.*;

/**
 * <pre>
 * ID 차단 기능을 제공하는 클래스.
 * Singleton Pattern 으로 운용된다.
 * </pre>
 * @author jhson
 *
 */
public class IDBlockProvider extends Util {
	protected static IDBlockProvider instance;
	
	private HashMap<String, Date> blockID;
	private int blockTime = 0;
	private Date dateNow;
	
	public static IDBlockProvider getInstance(int blockTime){
		if (instance == null){
			instance = new IDBlockProvider(blockTime);
		}
		
		return instance;
	}
	
	public static IDBlockProvider getInstance(String blockTime){
		try{
			return getInstance(Integer.parseInt(blockTime));
		}
		catch(Exception ex){
			System.out.println("blockTime 설정 값 이상으로 인하여 기본값으로 운용 됩니다.");
			
			return getInstance();
		}
		
	}
	
	public static IDBlockProvider getInstance(){
		return getInstance(3600);
	}
	
	protected IDBlockProvider(int blockTime){
		this.blockTime = blockTime;
		this.blockID = new HashMap<String, Date>();
	}
	
	/**
	 * 
	 * <pre>
	 * 해당 ID가 차단 되었는지 확인한다.
	 * 매 확인전, removeAllOldBlockID 를 수행하여
	 * 오래된 BlockID는 자동으로 삭제한다.
	 * </pre>
	 * @param id 확인하고 싶은 ID.
	 * @return 차단되었다면 true, 아니면 false.
	 */
	public boolean hasBlocked(String id){
		removeAllOldBlockID();
		
		return blockID.containsKey(id);
	}
	
	/**
	 * 
	 * <pre>
	 * 해당 ID가 차단 되었는지를 확인하고
	 * 차단 시간이 지났을 경우 자동으로 내부에 차단된 ID를 제거 한다.
	 * 내부적으로 hasBlocked를 수행한다.
	 * </pre>
	 * @param id
	 * @return 지났거나 ID가 존재하지 않는다면 true, 지나지 않았다면 false.
	 */
	public boolean hasTimeout(String id){
		if (!hasBlocked(id)){
			return true;
		}
		
		if (blockID.get(id).before(now())){
			remove(id);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * <pre>
	 * 차단된 ID에 대하여 남은 차단 시간을 구한다.
	 * 만약 차단된 ID가 아니라면 0을 반환 한다.
	 * 내부적으로 hasTimeout을 수행한다.
	 * </pre>
	 * @param id 남은 차단 시간을 구할 ID.
	 * @return 차단된 시간. 단위는 milliseconds.
	 */
	public int leftTime(String id){
		if (hasTimeout(id) == false){
			return 0;
		}
		
		return blockID.get(id).compareTo(now());
	}
	public void add(String id){
		blockID.put(id, addSeconds(new Date(), blockTime));
	}
	
	public void remove(String id){
		blockID.remove(id);
	}
	
	public void clear(){
		System.out.println("clear before: " + blockID.size());
		blockID.clear();
		System.out.println("clear after: " + blockID.size());
	}

	protected Date now(){
		if (dateNow == null){
			dateNow = new Date();
		}
		
		return dateNow;
	}
	
	protected void removeAllOldBlockID(){
		Date dateNow;
		
		if (blockID.size() < 100){
			return;
		}
		
		dateNow = new Date();
		
		for(String key : blockID.keySet()){
			if (blockID.get(key).before(dateNow)){
				blockID.remove(key);
			}
		}
	}
}
