package com.jway;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.imnetpia.exbill.common.IDBConnection;

public class OutsideDBExecute implements IDBExecute {
	private Connection conn;
	private boolean keyLowerCase = true;
	private SimpleDateFormat dateFormat;
	protected String host = "";
	protected String id = "";
	protected String pw = "";

	public void empty() {
		close();
		
		conn = null;
		dateFormat = null;
	}

	@SuppressWarnings("rawtypes")
	public List<Map<String, String>> select(String query, Vector param) throws Exception {
		PreparedStatement pstmt;
		ResultSet result;
		ArrayList<Map<String,String>> retList;
		HashMap<String,String> mRow;
		int iColCount = 0;
		int iCol = 0;
		ResultSetMetaData meta = null;
		ArrayList<String> fieldList = null;
		ArrayList<Integer> fieldType = null;
		Date dateColData = null;
		
		checkParamCount(query, param);
		
		open();
		
		pstmt = applyParams(conn.prepareStatement(query), param);
		result = pstmt.executeQuery();
		
		if (result == null){
			return null;
		}
		
		retList = new ArrayList<Map<String,String>>();
		
		
		
		
		
		while(result.next()){
			if (fieldList == null){
				fieldList = new ArrayList<String>();
				fieldType = new ArrayList<Integer>();
				meta = result.getMetaData();
				iColCount = meta.getColumnCount();
				
				for(iCol = 1; iCol <= iColCount; iCol++){
					fieldList.add( 
						(keyLowerCase) 
						? meta.getColumnName(iCol).toLowerCase()
						: meta.getColumnName(iCol).toUpperCase()
					);
					fieldType.add(meta.getColumnType(iCol));
				}
			}
			
			mRow = new HashMap<String, String>();

			for(iCol = 0; iCol < iColCount; iCol++){
				if (fieldType.get(iCol) == Types.DATE){
					dateColData = result.getDate(iCol + 1);
					mRow.put(fieldList.get(iCol), (dateColData == null)? null : getDateFormat().format(dateColData));
				}
				else{
					mRow.put(fieldList.get(iCol), result.getString(iCol + 1));
				}
			}
			
			retList.add(mRow);
		}
		
		pstmt.close();
		
		return retList;
	}

	@SuppressWarnings("rawtypes")
	public boolean execute(String query, Vector param) throws Exception {
		PreparedStatement pstmt;
		int iResult = 0;
		
		checkParamCount(query, param);
		
		open();
		
		pstmt = conn.prepareStatement(query);
		
		if (param != null && param.size() > 0){
			pstmt = applyParams(pstmt, param);
		}
		
		iResult = pstmt.executeUpdate();
		
		pstmt.close();
		
		return iResult >= 0;
	}
	
	@SuppressWarnings("rawtypes")
	protected PreparedStatement applyParams(PreparedStatement pstmt, Vector param) throws SQLException{
		int index = 0;
		
		for(Object oVal : param){
			index++;
			
			if (oVal instanceof String){
				pstmt.setString(index, (String)oVal);
			}
			else if (oVal instanceof Integer){
				pstmt.setInt(index, (Integer)oVal);
			}
			else if (oVal instanceof Double){
				pstmt.setDouble(index, (Double)oVal);
			}
			else{
				pstmt.setString(index, oVal.toString());
			}
		}
		
		return pstmt;
	}
	
	protected SimpleDateFormat getDateFormat(){
		if (dateFormat == null){
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		}
		
		return dateFormat;
	}
	
	@SuppressWarnings("rawtypes")
	protected void checkParamCount(String query, Vector param) throws SQLException{
		int iQuestionMarkCount = StringUtils.countMatches(query, "?");
		
		if (param == null || (param.size() != iQuestionMarkCount)){
			throw new SQLException("Prepare Statement SQL and Vector parameter counts do not match.");
		}
	}

	public void startTransaction() throws SQLException {
		if (conn != null){
			conn.setAutoCommit(false);
		}
	}

	public void endTransaction() throws SQLException {
		if (conn != null){
			conn.setAutoCommit(true);
		}
	}

	public void rollback() throws SQLException {
		if (conn != null){
			conn.rollback();
		}
	}

	public void commit() throws SQLException {
		if (conn != null){
			conn.commit();
		}
	}

	public void setKeyLowerCase(boolean val) {
		this.keyLowerCase = val;
	}
	
	public boolean open(){
		try {
			if (conn == null){
				if ("yes".equals(System.getProperty("ONS_DEV"))){
					System.out.println("OutsideDBExecute::open --> Dev Mode");
					conn = IDBConnection.getConnection();
				}
				else{
					System.out.println("OutsideDBExecute::open --> Normal Mode");
					DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
					
					conn = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":1521:orcl", id, pw);
				}
			}
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			return false;
		}
	}
	
	public boolean close(){
		try {
			if (conn != null && conn.isClosed()){
				conn.close();
			}
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			return false;
		}
	}

}
