package com.jway;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class BaseModel extends Util implements IEmptiable{
	/**
	 * ONS 오라클 내부에 암호화된 자료를 해제할 때 쓰이는 키.
	 */
	protected final static String CRYPTO_DECRIPT = "";
	/*
	 * Model 용 유틸들
	 * 자주 쓰인다면 상속화 해도 좋겠음..
	 */
	protected IDBExecute db;
	protected LinkedHashMap<String, String> template;
	protected List<String> err;
	protected Map<String, String> whereInfo;
	protected List<String> whereAdd;
	protected boolean keyLowerCase = true;
	
	public BaseModel(IDBExecute db){
		this.db = db;
		this.template = new LinkedHashMap<String, String>();
		this.err = new ArrayList<String>();
		this.whereInfo = new LinkedHashMap<String, String>();
		this.whereAdd = new ArrayList<String>();
	}
	
	protected String listToJoined(List<String> list){
		StringBuilder sb = new StringBuilder();
		
		for(String item : list){
			if (sb.length() > 3){
				sb.append(",");
			}
			sb.append("'")
			.append(item)
			.append("'");
		}
		
		return sb.toString();
	}
	
	protected void template(String key, String template){
		this.template.put(key, template);
	}
	
	protected void removeTemplateKey(String key){
		template.remove(key);
	}
	
	protected boolean hasTemplate(String key){
		return template.containsKey(key);
	}
	
	protected String replaceParameterTemplate(String sQuery, String key, Object val){
		try{
			return sQuery.replaceAll("\\{" + key + "\\}", val.toString());
		}
		catch(Exception ex){
			System.out.println("BaseModel.replaceParameterTemplate - Error : q=" + sQuery + ", k=" + key + ", v=" + val);
			System.out.println(ex);
			err.add(ex.getMessage());
		}
		
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	protected String replaceParameterTemplate(String sQuery, String key, Map param){
		return replaceParameterTemplate(sQuery, key, param.get(key));
	}
	
	//protected void orderby(String key)
	
	@SuppressWarnings("rawtypes")
	protected Map<String,String> selectOneDynamicWhere(String sQuery, Map<String, String> mWhereInfo, Map param, String... keys){
		try{
			return selectDynamicWhere(sQuery, mWhereInfo, param, false, keys).get(0);
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
		}
		
		return null;
	}
	@SuppressWarnings("rawtypes")
	protected Map<String,String> selectOneDynamicWhere(String sQuery, Map param, String... keys){
		return selectOneDynamicWhere(sQuery, this.whereInfo, param, keys);
	}
	@SuppressWarnings("rawtypes")
	protected <E extends BaseMapDTO> E selectOneDynamicWhere(Class<E> genClass, String sQuery, Map param, String... keys){
		Map map = selectOneDynamicWhere(sQuery, param, keys);
		
		if (map == null){
			return null;
		}
		
		try {
			Constructor<E> ctor = genClass.getConstructor(Map.class);
			
			return ctor.newInstance(new Object[] {map});
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Map<String,String>> selectDynamicWhere(String sQuery, Map param, String... keys){
		return selectDynamicWhere(sQuery, param, false, keys);
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Map<String,String>> selectDynamicWhere(String sQuery, Map param, boolean withPaging, String... keys){
		List<Map<String,String>> list = selectDynamicWhere(sQuery, this.whereInfo, param, withPaging, keys);
		
		clearWhere();
		
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	protected <E extends BaseMapDTO> ArrayList<E> selectDynamicWhere(Class<E> genClass, String sQuery, Map param, boolean withPaging, String... keys){
		List list = selectDynamicWhere(sQuery, this.whereInfo, param, withPaging, keys);
		clearWhere();
		
		ArrayList<E> retList = new ArrayList<E>();
		
		try {
			Constructor<E> ctor = genClass.getConstructor(Map.class);
			
			for(Object oRow : list){
				retList.add(ctor.newInstance(new Object[] {(Map)oRow}));
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}

		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Map<String,String>> selectDynamicWhere(String sQuery, Map<String, String> mWhereInfo, Map param, String... keys){
		return selectDynamicWhere(sQuery, mWhereInfo, param, false, keys);
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map<String,String>> selectDynamicWhere(String sQuery, Map<String, String> mWhereInfo, Map param, boolean withPaging, String... keys){
		StringBuilder sb = new StringBuilder();
		ArrayList<String> keyList = new ArrayList<String>();
		String[] arrKey = new String[0];
//		String sSubWhere = "";
//		Object oVal;
//		int iQMarkCount = 0;
//		String sWhereInfo = "";
		
		if (mWhereInfo == null){
			return select(sQuery, param, keys);
		}
		
		// Statement Query 처리
		for(String key : keys){
			keyList.add(key);
		}
		
		// 템플릿 처리
//		for(String key : this.template.keySet()){
//			oVal = param.get(key);
//			
//			if (isValid(oVal)){
//				sSubWhere = replaceParameterTemplate(this.template.get(key), key, oVal);
//				iQMarkCount = StringUtils.countMatches(sSubWhere, "?");
//				
//				for(int i = 0; i < iQMarkCount; i++){
//					keyList.add(key);
//				}
//				
//				sb.append(" ");
//				sb.append( sSubWhere );
//				sb.append(" ");
//				
//				//System.out.println("selectDynamicWhere.template = " + sSubWhere);
//			}
//		}
//
//		// 추가 where절 처리
//		for(String key : mWhereInfo.keySet()){
//			oVal = param.get(key);
//			
//			if (isValid(oVal)){
//				sWhereInfo = mWhereInfo.get(key);
//				sb.append( " " )
//				.append( sWhereInfo )
//				.append( " " );
//				
//				if (sWhereInfo.contains("?")){
//					keyList.add(key);
//				}
//			}
//		}
//		
//		for(String subQuery : whereAdd){
//			sb.append( " " )
//			.append( subQuery )
//			.append( " " );
//		}
		
		sb = dynamicWhereProcess(sQuery, mWhereInfo, param, keyList);

		sQuery = sQuery.replace("@WHERE@", sb.toString());
		this.template.clear();
		mWhereInfo.clear();
		
		if (withPaging){
			return selectWithPaging(sQuery, param, keyList.toArray(arrKey));
		}
		
		return select(sQuery, param, keyList.toArray(arrKey));
	}
	
	@SuppressWarnings("rawtypes")
	private StringBuilder dynamicWhereProcess(String sQuery, Map<String, String> mWhereInfo, Map param, List<String> keyList){
		Object oVal;
		String sSubWhere;
		int iQMarkCount;
		StringBuilder sb = new StringBuilder();
		String sWhereInfo;
		
		// 템플릿 처리
		for(String key : this.template.keySet()){
			oVal = param.get(key);
			
			if (isValid(oVal)){
				sSubWhere = replaceParameterTemplate(this.template.get(key), key, oVal);
				iQMarkCount = StringUtils.countMatches(sSubWhere, "?");
				
				for(int i = 0; i < iQMarkCount; i++){
					keyList.add(key);
				}
				
				sb.append(" ");
				sb.append( sSubWhere );
				sb.append(" ");
				
				//System.out.println("selectDynamicWhere.template = " + sSubWhere);
			}
		}

		// 추가 where절 처리
		for(String key : mWhereInfo.keySet()){
			oVal = param.get(key);
			
			if (isValid(oVal)){
				sWhereInfo = mWhereInfo.get(key);
				sb.append( " " )
				.append( sWhereInfo )
				.append( " " );
				
				if (sWhereInfo.contains("?")){
					keyList.add(key);
				}
			}
		}
		
		for(String subQuery : whereAdd){
			sb.append( " " )
			.append( subQuery )
			.append( " " );
		}

		sQuery = sQuery.replace("@WHERE@", sb.toString());
		this.template.clear();
		mWhereInfo.clear();
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Map<String,String>> select(String sQuery){
		Vector vecParam = new Vector();
		
		log4debug(sQuery, null, vecParam);
		
		try{
			db.setKeyLowerCase(keyLowerCase);
			return db.select(sQuery, vecParam);
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
		}
		
		return new ArrayList<Map<String,String>>();
	}
	
	@SuppressWarnings("rawtypes")
	protected <E extends BaseMapDTO> ArrayList<E> select(Class<E> genClass, String sQuery){
		List list = select(sQuery);
		ArrayList<E> retList = new ArrayList<E>();
		
		try {
			Constructor<E> ctor = genClass.getConstructor(Map.class);
			
			for(Object oRow : list){
				retList.add(ctor.newInstance(new Object[] {(Map)oRow}));
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}

		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Map<String,String>> select(String sQuery, Map param, String... keys){
		Vector vecParam = new Vector();
		
		addParamsToVector(param, vecParam, keys);
		log4debug(sQuery, param, vecParam);
		
		try{
			db.setKeyLowerCase(keyLowerCase);
			return db.select(sQuery, vecParam);
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
		}
		
		return new ArrayList<Map<String,String>>();
	}
	
	protected Map<String,String> selectOne(String sQuery){
		try{
			db.setKeyLowerCase(keyLowerCase);
			return select(sQuery).get(0);
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
		}
		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<String, String> selectOne(String sQuery, Map param, String... keys){
		try{
			List<Map<String,String>> retList = select(sQuery, param, keys);
			
			if (retList == null || retList.size() == 0){
				System.out.println("BaseModel->selectOne : data size is 0.");
				return null;
			}
			return retList.get(0);
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
		}
		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	protected <E extends BaseMapDTO> E selectOne(Class<E> genClass, String sQuery, Map param, String... keys){
		Map map = selectOne(sQuery, param, keys);
		
		if (map == null){
			return null;
		}
		
		try {
			Constructor<E> ctor = genClass.getConstructor(Map.class);
			
			return ctor.newInstance(new Object[] {map});
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	protected List<Map<String, String>> selectWithPaging(String sQuery){
		return selectWithPaging(sQuery, null);
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Map<String, String>> selectWithPaging(String sQuery, Map param, String... keys){
		int iPage = tryParamInt(param, "page", 1);
		int iCount = tryParamInt(param, "count", 10);
		String query = "SELECT * FROM (SELECT ROWNUM AS RNUM, Z__.* FROM( " + sQuery + " ) Z__ where rownum <= " + (iPage * iCount) + " ) where rnum > " + ((iPage - 1) * iCount) + " ";
		
		return select(query, param, keys);
	}
	/**
	 * 
	 * <pre>
	 * SQL 결과 내용 1개를 대상으로 하며,
	 * 그 행(Row)의 항목중 CNT 로 명칭된 것의 값을
	 * 정수로 변환하여 반환한다.
	 * </pre>
	 * @param sQuery
	 * @return
	 */
	protected int selectCount(String sQuery){
		try{
			return Integer.parseInt( selectOne(sQuery).get("cnt").toString() );
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
			
			return 0;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * SQL 결과 내용 1개를 대상으로 하며,
	 * 그 행(Row)의 항목중 CNT 로 명칭된 것의 값을
	 * 정수로 변환하여 반환한다.
	 * </pre>
	 * @param sQuery
	 * @param param
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected int selectCount(String sQuery, Map param, String... keys){
		try{
			return Integer.parseInt( selectOne(sQuery, param, keys).get("cnt").toString() );
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
			
			return 0;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * SQL 결과 내용 1개를 대상으로 하며,
	 * 그 행(Row)의 항목중 CNT 로 명칭된 것의 값을
	 * 정수로 변환하여 반환한다.
	 * </pre>
	 * @param sQuery
	 * @param param
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected int selectCountDynamicWhere(String sQuery, Map param, String... keys){
		try{
			return Integer.parseInt( selectOneDynamicWhere(sQuery, this.whereInfo, param, keys).get("cnt").toString() );
		}
		catch(Exception e){
			e.printStackTrace();
			err.add(e.getMessage());
			
			return 0;
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean execute(String sQuery, Map param, String... keys){
		Vector vecParam = new Vector();
		
		addParamsToVector(param, vecParam, keys);
		log4debug(sQuery, param, vecParam);
		
		try{
			return db.execute(sQuery, vecParam);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("model execute error : " + e.getMessage());
			err.add(e.getMessage());
		}
		
		return false;
	}
	
	protected boolean execute(String sQuery){
		return execute(sQuery, null);
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean executeDynamicWhere(String sQuery, Map param, String... keys){
		return executeDynamicWhere(sQuery, this.whereInfo, param, keys);
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean executeDynamicWhere(String sQuery, Map<String, String> mWhereInfo, Map param, String... keys){
		LinkedList<String> keyList = new LinkedList<String>();
		String[] arrKey = new String[0];
		StringBuilder sb = new StringBuilder();
		Set<String> keyset = null;
		String sSubWhere = "";
		Object oVal;
		
		if (mWhereInfo == null){
			return execute(sQuery, param, keys);
		}
		

		
		for(String key : keys){
			keyList.add(key);
		}

		keyset = mWhereInfo.keySet();
		
		for(String key : keyset){
			oVal = param.get(key);
			
			if (isValid( oVal )){
				sSubWhere = mWhereInfo.get(key);
				
				if (hasTemplate(key)){
					sSubWhere = replaceParameterTemplate(sSubWhere, key, oVal);
					keyList.remove(key);
				}
				else{
					keyList.add(key);
				}
				
				sb.append( " " );
				sb.append( sSubWhere );
				sb.append( " " );
			}
			else{
				keyList.remove(key);
			}
		}
		
		for(String subQuery : whereAdd){
			sb.append( " " )
			.append( subQuery )
			.append( " " );
		}
		
		sQuery = sQuery.replace("@WHERE@", sb.toString());
		mWhereInfo.clear();
		//dynamicWhereProcess(sQuery, this.whereInfo, param, keyList).toString()
		return execute(sQuery, param, keyList.toArray(arrKey));
	}
	
	public List<String> getErr(){
		return err;
	}
	
	protected void addErr(String msg){
		err.add(msg);
	}
	
	protected void where(String key, String subQuery){
		whereInfo.put(key, subQuery);
	}
	
	protected void where(String subQuery){
		whereAdd.add(subQuery);
	}
	
	protected void clearWhere(){
		whereInfo.clear();
	}
	
	public void startTransaction(){
		try{
			db.startTransaction();
		}
		catch(SQLException ex){
			ex.printStackTrace();
			addErr("Transaction Start Fail");
		}
	}
	public void endTransaction(){
		try {
			db.endTransaction();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErr("Transaction End Fail");
		}
	}
	public void rollback(){
		try {
			db.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErr("Rollback Fail");
		}
	}
	public void commit(){
		try {
			db.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErr("Commit Fail");
		}
	}
	
	public void empty(){
		if (this.template != null){
			this.template.clear();
		}
		if (this.err != null){
			this.err.clear();
		}
		if (this.whereInfo != null){
			this.whereInfo.clear();
		}
		if (this.whereAdd != null){
			this.whereAdd.clear();
		}
		
		this.db = null;
		this.template = null;
		this.err = null;
		this.whereInfo = null;
		this.whereAdd = null;
	}
}