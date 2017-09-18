package com.jway;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.imnetpia.exbill.EXServer;
import com.imnetpia.exbill.common.IDBConnection;
import com.imnetpia.exbill.common.IDBUpdate;
import com.imnetpia.exbill.common.IException;

public class ExserverDBExecute implements IDBExecute {
	protected EXServer bts;
	protected IDBUpdate btsExQry;
	protected Connection conn;
	protected boolean transaction = false;
	protected boolean keyLowerCase = true;
	
	public ExserverDBExecute(EXServer bts){
		this.bts = bts;
	}
	
	public ExserverDBExecute(Object obj){
		this.bts = (EXServer)obj;
	}
	
	public EXServer getExserver(){
		if (bts == null){
			bts = new EXServer();
		}
		
		return bts;
	}
	
	public IDBUpdate getDBUpdate(){
		if (btsExQry == null){
			btsExQry = new IDBUpdate();
		}
		
		return btsExQry;
	}
	
	protected Connection getConnection(){
		if (conn == null){
			try {
				conn = IDBConnection.getConnection();
			} catch (IException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return conn;
	}
	/**
	 * 자료 조회문 Select를 이용할 때 쓰인다.
	 * @param query 수행 할 쿼리문.
	 * @param param 쿼리문이 ? 를 가진 Prepare SQL일 때 그 것들을 자동 대체할 내용이 담긴 Vector.
	 * @return 결과값이 하나라도 있다면 List, 없다면 null 반환.
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,String>> select(String query, Vector param) throws Exception {
		try{
			return (List<Map<String,String>>)getExserver().QuerySelectPSList(query, param, 0, 0, keyLowerCase);
		}
		catch(NullPointerException ex){
			return null;
		}
		catch(ClassCastException ex){
			return null;
		}
	}
	/**
	 * DDL 혹은 Select를 제외한 DML 을 수행할 때 사용된다.
	 * @param query 수행 할 쿼리문.
	 * @param param 쿼리문이 ? 를 가진 Prepare SQL일 때 그 것들을 자동 대체할 내용이 담긴 Vector.
	 * @return 수행 성공시 true, 아니면 false
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public boolean execute(String query, Vector param) throws Exception {
		boolean bRet = getDBUpdate().executeUpdate(getConnection(), query, param);
		
		if (bRet == false){
			throw new Exception(getDBUpdate().errMsg);
		}
		
		return bRet;
	}
	/**
	 * 트랜잭션을 시작한다.
	 */
	public void startTransaction() throws SQLException{
		getConnection().setAutoCommit(false);
	}
	/**
	 * 트랜잭션을 끝낸다.
	 */
	public void endTransaction() throws SQLException{
		getConnection().setAutoCommit(true);
	}
	/**
	 * <pre>
	 * 했던 작업을 모두 되돌린다.
	 * 단, 작업 전 트랜잭션을 시작 했어야 의도대로 동작 한다.
	 * </pre>
	 */
	public void rollback() throws SQLException{
		getConnection().rollback();
		endTransaction();
	}
	/**
	 * <pre>
	 * 했던 작업들을 실제 DB에 반영하고
	 * 자동으로 트랜잭션을 종료 한다.
	 * </pre>
	 */
	public void commit() throws SQLException{
		getConnection().commit();
		endTransaction();
	}
	
	public void setKeyLowerCase(boolean val){
		keyLowerCase = val;
	}

	public void empty() {
		this.bts = null;
		this.btsExQry = null;
		try {
			this.conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println("ExserverDBExecute => already closed.");
			//e.printStackTrace();
		}
	}

}
