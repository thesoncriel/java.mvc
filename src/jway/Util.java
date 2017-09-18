package com.jway;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

public class Util {
	/*
	 * 모델에서 쓰이는 유틸들
	 */
	/**
	 * <pre>
	 * 특정 값에 대하여 정수형 시도를 하고 반환 한다.
	 * 시도가 실패 한다면 기본값을 반환 한다.
	 * </pre>
	 * @param val 정수형 변환 시도 할 값
	 * @param def 기본값
	 * @return 변환된 정수
	 */
	public int tryParseInt(Object val, int def){
		try{
			return Integer.parseInt(val.toString());
		}
		catch(Exception ex){
			return def;
		}
	}
	
	/**
	 * <pre>
	 * 맵 파라메터에서 키에 대응되는 값을 정수(Integer)로 바꿔서 반환 한다.
	 * 만약 전환에 실패 하거나 값이 비었다면 기본값을 전달 한다.
	 * </pre>
	 * @param param 가져 올 맵 파라메터
	 * @param key 값을 빼 올 키
	 * @param def 기본값
	 * @return 변환된 정수
	 */
	@SuppressWarnings("rawtypes")
	public int tryParamInt(Map param, String key, int def){
		String val = "";
		
		try{
			val = (String)param.get(key);
		}
		catch(Exception ex){
			try{
				val = ((String[])param.get(key))[0];
			}
			catch(Exception ex2){
				
			}
		}
		
		if ("".equals(val)){
			return def;
		}
		
		try{
			return Integer.parseInt(val);
		}
		catch(Exception ex){
			return def;
		}
	}
	
	/**
	 * <pre>
	 * 맵 파라메터에서 키에 대응되는 값을 큰 정수(Long)로 바꿔서 반환 한다.
	 * 만약 전환에 실패 하거나 값이 비었다면 기본값을 전달 한다.
	 * </pre>
	 * @param param 가져 올 맵 파라메터
	 * @param key 값을 빼 올 키
	 * @param def 기본값
	 * @return 변환된 정수
	 */
	@SuppressWarnings("rawtypes")
	public long tryParamLong(Map param, String key, long def){
		String val = "";
		
		try{
			val = (String)param.get(key);
		}
		catch(Exception ex){
			try{
				val = ((String[])param.get(key))[0];
			}
			catch(Exception ex2){
				
			}
		}
		
		if ("".equals(val)){
			return def;
		}
		
		try{
			return Long.parseLong(val);
		}
		catch(Exception ex){
			return def;
		}
	}
	/**
	 * <pre>
	 * 맵 파라메터에서 키에 대응되는 값을 trueVal 인수의 값과 비교하여
	 * 같으면 true, 아니면 false를 반환한다.
	 * </pre>
	 * @param param 가져 올 맵 파라메터
	 * @param key 값을 빼 올 키
	 * @param trueVal true 일 때의 값. 
	 * @return 변환된 부울값
	 */
	@SuppressWarnings("rawtypes")
	public boolean tryParamBool(Map param, String key, String trueVal){
		String val = "";
		
		try{
			val = (String)param.get(key);
		}
		catch(Exception ex){
			try{
				val = ((String[])param.get(key))[0];
			}
			catch(Exception ex2){
				
			}
		}
		
		if (isEmpty(val)){
			return false;
		}
		
		try{
			return trueVal.equals(val);
		}
		catch(Exception ex){
			return false;
		}
	}
	
	public String tryCheckStr(String val, String def){
		if (val == null && "".equals(val) == false){
			return val;
		}
		
		return def;
	}
	
	/**
	 * <pre>
	 * 맵 파라메터에서 키에 대응되는 값을 단일 문자열 (Single String)로 바꿔서 반환 한다.
	 * 만약 전환에 실패 할 경우 빈 문자열 ("")로 전달 한다.
	 * </pre>
	 * @param param 가져 올 맵 파라메터
	 * @param key 값을 빼 올 키
	 * @return 변환된 단일 문자열
	 */
	@SuppressWarnings("rawtypes")
	public String tryValueStr(Map param, String key){
		Object oVal;
		
		try{
			oVal = param.get(key);
			
			if (oVal instanceof String){
				return (String)oVal;
			}
			
			if (oVal instanceof String[]){
				return ((String[])oVal)[0];
			}
			
			return oVal.toString();
			
		}
		catch(Exception ex){
			return "";
		}
	}
	
	/**
	 * <pre>
	 * 맵 형태의 파라메터에서 키'에 대응되는 값이 
	 * 비어있는지 여부를 확인하고 그 값을 전달 한다.
	 * 만약 null 이거나 비어있다면 
	 * 기본값(def)을 대신 넣어주고 그 값을 전달 한다.
	 * </pre>
	 * @param param 확인할 파라메터
	 * @param key 확인할 키
	 * @param def 없을 때 대신할 기본값
	 * @return key에 대응되는 값. 없으면 기본값이 나옴.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String tryParamStr(Map param, String key, String def){
		String sRes = tryValueStr(param, key);
		
		if (sRes == null || "".equals(sRes)){
			sRes = def;
			param.put(key, sRes);
		}
		
		return sRes;
	}
	
	/**
	 * <pre>
	 * 해당 값이 유효한지 검사 한다.
	 * 값이 들어있다면 true, null 이거나 비어 있다면 false를 반환 한다.
	 * </pre>
	 * @param val 검사 할 값
	 * @return 결과
	 */
	public boolean isValid(Object val){
		String sVal = null;
		
		try{
			sVal = (String)val;
			
			return sVal != null && "".equals(sVal) == false;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	/**
	 * <pre>
	 * 해당 문자열이 비어있거나 null인지 확인한다.
	 * </pre>
	 * @param str
	 * @return 비거나 null이면 true, 아니면 false.
	 */
	public boolean isEmpty(String str){
		if (str == null || "".equals(str)){
			return true;
		}
		
		return false;
	}
	/**
	 * <pre>
	 * 해당 문자열이 비어있거나 null인지 확인한다.
	 * </pre>
	 * @param val
	 * @return 비거나 null이면 true, 아니면 false.
	 */
	public boolean isEmpty(Object val){
		if (val == null || "".equals(val.toString())){
			return true;
		}
		
		return false;
	}
	/**
	 * 
	 * <pre>
	 * 해당 파라메터에서 key 에 대응되는 값이 null, 혹은 빈 스트링인지 여부를 확인 한다.
	 * </pre>
	 * @param param 확인하고 싶은 파라메터, 혹은 Map Collection.
	 * @param key 값을 확인하고 싶은 key.
	 * @return 비거나 null이면 true, 아니면 false.
	 */
	public boolean isEmpty(Map<String,String> param, String key){
		if (param == null || !param.containsKey(key)){
			return true;
		}
		
		return isEmpty(param.get(key));
	}
	
	/**
	 * <pre>
	 * 맵 형식으로 구성된 파라메터값을 지정된 key 목록을 이용하여 Vector 에 삽입한다.
	 * </pre>
	 * @param param 가져올 값이 있는 맵
	 * @param vec 값을 추가 할 벡터
	 * @param keys 맵에서 값을 가져올 키 목록
	 * @return 값이 추가된 벡터
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector addParamsToVector(Map param, Vector vec, String... keys){
		String val;
		
		if (param == null){
			return vec;
		}
		
		for(String key : keys){
			val = tryValueStr(param, key);
			vec.add(val);
		}
		
		return vec;
	}
	
	/**
	 * 
	 * <pre>
	 * 날짜 문자열을 Date 객체로 바꿔준다.
	 * </pre>
	 * @param sDate 문자열로 된 날짜
	 * @param separator 날짜의 연/월/일을 구분할 구분자.
	 * @param firstDate 그 달의 첫날로 강제 지정할지의 여부.
	 * @return
	 */
	public Date dateParse(String sDate, String separator, boolean firstDate){
		Calendar cal = Calendar.getInstance();
		String[] saDate = sDate.split("\\" + separator);
		
		if (saDate.length == 2 || firstDate){
			cal.set(Integer.parseInt(saDate[0]), Integer.parseInt(saDate[1]) - 1, 1);
		}
		else if (saDate.length > 2){
			cal.set(Integer.parseInt(saDate[0]), Integer.parseInt(saDate[1]) - 1, Integer.parseInt(saDate[2]));
		}
		else{
			System.out.println("dateParse::: sDate=" + sDate + "|separator=" + separator);
			throw new RuntimeException("날짜 형식이 맞지 않습니다.");
		}
		
		return cal.getTime();
	}
	
	/**
	 * 
	 * <pre>
	 * 날짜 문자열을 Date 객체로 바꿔준다.
	 * </pre>
	 * @param sDate 문자열로 된 날짜
	 * @param separator 날짜의 연/월/일을 구분할 구분자.
	 * @return
	 */
	public Date dateParse(String sDate, String separator){
		return dateParse(sDate, separator, false);
	}
	/**
	 * 
	 * <pre>
	 * 날짜 문자열을 Date 객체로 바꿔준다.
	 * </pre>
	 * @param sDate bar(-), 혹은 dot(.) 으로 연월일이 구분 되어진 날짜 데이터. ex: 2017-01-02, 2017.06.01
	 * @return
	 */
	public Date dateParse(String sDate){
		if (sDate.contains("-")){
			return dateParse(sDate, "-");
		}
		else if (sDate.contains(".")){
			return dateParse(sDate, ".");
		}
		else if (sDate.contains("/")){
			return dateParse(sDate, "/");
		}
		
		throw new RuntimeException("날짜를 변환할 때는 구분자로 '-' 나 '.' 를 사용해야 합니다.");
	}
	
	/**
	 * 
	 * <pre>
	 * 날짜값을 지정된 포멧으로 바꿔 준다.
	 * </pre>
	 * @param sDate yyyy-MM-dd 형태의 문자열
	 * @param fmt 변경할 포멧
	 * @return 변경된 문자열
	 */
	public String dateFormat(String sDate, String fmt){
		return dateFormat(dateParse(sDate, "-"), fmt);
	}
	/**
	 * 
	 * <pre>
	 * 날짜값을 지정된 포멧으로 바꿔 준다.
	 * </pre>
	 * @param date 목표가 되는 날짜값
	 * @param fmt 변경할 포멧
	 * @return 변경된 문자열
	 */
	public String dateFormat(Date date, String fmt){
		SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
		
		return dateFmt.format(date);
	}
	
	/**
	 * <pre>
	 * 현재 날짜와 시간을 지정된 포맷으로 출력 한다.
	 * </pre>
	 * @return
	 */
	public String getFormatedNowDate(String fmt){
		SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
		
		return dateFmt.format(new Date());
	}
	
	/**
	 * <pre>
	 * 현재 날짜와 시간을 지정된 포맷으로 출력 한다.
	 * 포맷은 기본 값인 yyyyMMdd_hhmmss 로 자동 지정 된다. 
	 * </pre>
	 * @return
	 */
	public String getFormatedNowDate(){
		return getFormatedNowDate("yyyyMMdd_hhmmss");
	}
	
	/**
	 * 
	 * <pre>
	 * 특정 날짜에 월(Month)를 더한 Date 객체를 되돌린다.
	 * </pre>
	 * @param date 다시 계산하고픈 날짜 객체.
	 * @param month 더할 월수
	 * @return 계산된 Date 객체
	 */
	public Date addMonth(Date date, int month){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		
		return cal.getTime();
	}
	/**
	 * 
	 * <pre>
	 * 특정 날짜에 일(Date)를 더한 Date 객체를 되돌린다.
	 * </pre>
	 * @param date 다시 계산하고픈 날짜 객체.
	 * @param val 더할 일수
	 * @return 계산된 Date 객체
	 */
	public Date addDate(Date date, int val){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		cal.add(Calendar.DATE, val);
		
		return cal.getTime();
	}
	/**
	 * 
	 * <pre>
	 * 특정 날짜 월의 마지막 날에 대응되는 날짜 객체를 되돌린다.
	 * </pre>
	 * @param date 계산할 날짜
	 * @return 마지막날로 계한된 날짜.
	 */
	public Date getLastDateOf(Date date){
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DAY_OF_MONTH));
		
		return cal.getTime();
	}
	
	/**
	 * 
	 * <pre>
	 * 특정 IP를 C-Class IP로 바꿔준다.
	 * </pre>
	 * @param ip
	 * @return
	 */
	public String convertToCClassIP(String ip){
		if (isEmpty(ip)){
			return "";
		}
		
		String[] saIp = ip.split("\\.");
		String sRet = saIp[0] + "." + saIp[1] + "." + saIp[2];
		
//		System.out.println("saIp.length = " + saIp.length);
//		System.out.println(sRet);
		
		return sRet;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 특정 날짜에 시간초(Seconds)를 더하여 계산한다.
	 * </pre>
	 * @param date 계산하고 싶은 날짜
	 * @param sec 더할 초(Seconds)
	 * @return 계산된 날짜
	 */
	public static Date addSeconds(Date date, int sec){
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.add(Calendar.SECOND, sec);
		
		return cal.getTime();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void log4debug(String sQuery, Map param, Vector vecParam){
		System.out.println( "elem======>>" +  vecParam);
		System.out.println( sQuery );
		
		if (param != null){
			if ("true".equals(param.get("__debug"))){
				param.put("sql", sQuery);
			}
		}
	}
	
}
