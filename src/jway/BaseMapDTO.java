package com.jway;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * Map 자료를 바탕으로 DTO private field에 자동으로 값을 넣어주거나 Map 으로 변환이 가능한 클래스.
 * </pre>
 * @author jhson
 *
 */
public class BaseMapDTO {
	public BaseMapDTO(){
		
	}
	@SuppressWarnings("rawtypes")
	public BaseMapDTO(Map data){
		fromMap(data);
	}
	/**
	 * <pre>
	 * Map 의 모든 데이터를 DTO로 옮긴다.
	 * 단, DTO의 Field Name 과 Map의 Key Name 이 반드시 일치 해야 한다.
	 * Map에 해당 자료가 없다면 그 필드는 기본값 (클래스별로 다름)으로 채워진다.
	 * </pre>
	 * @param data
	 */
	@SuppressWarnings("rawtypes")
	public void fromMap(Map data){
		Field[] fields = this.getClass().getDeclaredFields();
		Method method = null;
		Object oVal = null;
		Class cls = null;
		String sName = "";
		String sMethodName = "";
		String sVal = "";
		SimpleDateFormat dtFmt = null;
		
		//System.out.println(data.toString());
		
		for(Field field : fields){
			if (field.getModifiers() != Modifier.PRIVATE){
				continue;
			}
			
			cls = field.getType();
			sName = field.getName();
			sMethodName = "set" + StringUtils.capitalize( sName );
			
			//System.out.println("fromMap --- " + )
			
			try{
				sVal = data.get(sName).toString();
			}
			catch(NullPointerException ex){
				continue;
			}

			try{
				if (cls == Integer.TYPE){
					oVal = Integer.parseInt(sVal);
				}
				else if (cls == Double.TYPE){
					oVal = Double.parseDouble(sVal);
				}
				else if (cls == Date.class){
					if (dtFmt == null){
						dtFmt = new SimpleDateFormat("yyyy-MM-dd");
					}
					oVal = dtFmt.parse(sVal);
				}
				else{
					oVal = sVal;
				}
				
				try {
					method = this.getClass().getDeclaredMethod(sMethodName, cls);
					method.invoke(this, oVal);
					//field.set(this, oVal);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NumberFormatException ex){
				ex.printStackTrace();
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
		} // end for
	}
	/**
	 * <pre>
	 * DTO의 모든 private field 들(관련된 get 메서드가 있어야함) 의 값을
	 * HashMap으로 옮긴 후 반환 한다.
	 * </pre>
	 * @return
	 */
	public HashMap<String, String> toMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		Field[] fields = this.getClass().getDeclaredFields();
		Method method = null;
		String sName = "";
		String sMethodName = "";
		SimpleDateFormat dtFmt = null;
		
		for(Field field : fields){
			if (field.getModifiers() != Modifier.PRIVATE){
				continue;
			}
			
			sName = field.getName();
			sMethodName = "get" + StringUtils.capitalize( sName );
			
			try {
				method = this.getClass().getDeclaredMethod(sMethodName);
				
				if (field.getType() == Date.class){
					if (dtFmt == null){
						dtFmt = new SimpleDateFormat("yyyy-MM-dd");
					}
					map.put(sName, dtFmt.format(method.invoke(this)));
				}
				else{
					map.put(sName, method.invoke(this).toString());
				}
				
			} catch (Exception e) {
				map.put(sName, null);
			}
		}
		
		return map;
	}
}
