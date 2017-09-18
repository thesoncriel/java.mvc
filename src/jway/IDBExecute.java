package com.jway;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
/**
 * <pre>
 * com.jway 패키지의 Model 에서 DB Access 할 때 쓰이는 인터페이스.
 * </pre>
 * @author jhson
 *
 */
public interface IDBExecute extends IEmptiable {
	/**
	 * 자료 조회문 Select를 이용할 때 쓰인다.
	 * @param query 수행 할 쿼리문.
	 * @param param 쿼리문이 ? 를 가진 Prepare SQL일 때 그 것들을 자동 대체할 내용이 담긴 Vector.
	 * @return 결과값이 하나라도 있다면 List, 없다면 null 반환.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String, String>> select(String query, Vector param) throws Exception;
	/**
	 * DDL 혹은 Select를 제외한 DML 을 수행할 때 사용된다.
	 * @param query 수행 할 쿼리문.
	 * @param param 쿼리문이 ? 를 가진 Prepare SQL일 때 그 것들을 자동 대체할 내용이 담긴 Vector.
	 * @return 수행 성공시 true, 아니면 false
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public boolean execute(String query, Vector param) throws Exception;
	/**
	 * 트랜잭션을 시작한다.
	 */
	public void startTransaction() throws SQLException;
	/**
	 * 트랜잭션을 끝낸다.
	 */
	public void endTransaction() throws SQLException;
	/**
	 * <pre>
	 * 했던 작업을 모두 되돌린다.
	 * 단, 작업 전 트랜잭션을 시작 했어야 의도대로 동작 한다.
	 * </pre>
	 */
	public void rollback() throws SQLException;
	/**
	 * <pre>
	 * 했던 작업들을 실제 DB에 반영하고
	 * 자동으로 트랜잭션을 종료 한다.
	 * </pre>
	 */
	public void commit() throws SQLException;
	
	/**
	 * 
	 * <pre>
	 * select 시 결과 Map 의 key 이름을 소문자/대문자로 쓸지를 설정 한다.
	 * true면 소문자, false면 대문자.
	 * 기본은 true.
	 * </pre>
	 * @param val
	 */
	public void setKeyLowerCase(boolean val);
}