package com.jway;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController extends Util implements IEmptiable{
	protected final static String XSA_CODE = "dudtkdons_$^_^";// 영상ons_$^_^
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	private PrintWriter outs;
	protected IDBExecute db;
	protected JSONResponse json;
	protected SessionInfo sessionInfo;
	protected RequestFile reqFile;
	private List<IEmptiable> emptiables;
	/**
	 * <pre>
	 * 요청된 파라메터를 Map 형태로 간결하게 쓸 수 있도록 정리된 것.
	 * 단, multipart로 전송되었다면 paramFile 을 사용해야 한다.
	 * </pre>
	 */
	protected Map<String,String> param;
	/**
	 * <pre>
	 * 요청 파라메터 중 File이 포함되어있을 경우 사용한다.
	 * enctype=multipart/form-data 형식으로 전달 될 경우에 한한다.
	 * 일반적인 파라메터 처리는 param 을 쓰면 된다. 
	 * </pre>
	 */
	protected Map<String,File> paramFile;
	
	private boolean ran = false;
	private boolean contentTypeSetup = false;
	private boolean withoutAuthenticated = false;
	/**
	 * <pre>
	 * 이진(binary) 출력 모드로 바꾼다.
	 * Image 나 Video 등 기타 다른 이진 형태의
	 * 파일 출력에 쓰인다.
	 * </pre>
	 */
	protected boolean binaryMode = false;
	/**
	 * 자동으로 json 출력하는 것을 방지 한다.
	 */
	protected boolean preventAutoPrint = false;
	/**
	 * 자동으로 empty 를 수행하는 것을 방지 한다.
	 */
	protected boolean preventAutoEmpty = false;
	
	public BaseController(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException, ClassCastException, Exception{
		this(req, res, session, true);
	}
	
	public BaseController(HttpServletRequest req, HttpServletResponse res, HttpSession session, boolean autoDB) throws IOException, ClassCastException, Exception{
		this.request = req;
		this.response = res;
		this.session = session;
		
		if (autoDB){
			this.db = DBExecuteFactory.create(session.getAttribute("bts"));
			this.sessionInfo = new SessionInfo(session, request, this.db);
		}
		
		request.setCharacterEncoding("utf-8");
		this.json = new JSONResponse();
		this.param = this.parameterReplacement(request.getParameterMap());
		this.reqFile = new ExserverRequestFile();
		
		// 환경변수 이용해서 테섭 여부 판변할랬는데 잘 안되어서 이걸 박아넣음 -_-;;
		// 다른 좋은 방법 있으면 사용하시길 ㅠㅠ...
		if ("192.168.1.34".equals(request.getRemoteHost())){
			System.setProperty("ONS_DEV", "yes");
		}
		
		this.emptiables = new ArrayList<IEmptiable>();
		addEmptiable(this.json);
		addEmptiable(this.reqFile);
		addEmptiable(this.db);
	}
	
	private void initParam(){
		try{
			if (contentTypeSetup == false){
				toText();
			}
			
			if (sessionInfo != null && sessionInfo.isDebug()){
				param.put("__debug", "true");
				json.getResult().setDebug(param);
			}
		}
		catch(Exception ex){
			param = new HashMap<String, String>();
			json.getResult()
			.addErr(ex.getMessage())
			.setValid(false);
		}
	}
	
	public BaseController withoutAuth(){
		this.withoutAuthenticated = true;
		
		return this;
	}
	
	public BaseController toText() throws UnsupportedEncodingException{
		response.setHeader("Content-Type","text/html");
		
		binaryMode = false;
		contentTypeSetup = true;
		
		return this;
	}
	
	public BaseController toBinary(String contentType){
		response.setHeader("Content-Type", contentType);
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		
		binaryMode = true;
		contentTypeSetup = true;
		
		return this;
	}
	
	public BaseController toJPEG() throws UnsupportedEncodingException{
		return toBinary("image/jpeg");
	}
	
	/**
	 * 
	 * <pre>
	 * 문자열을 직접 출력한다.
	 * 이 메스드를 수행하면 기본적으로 preventAutoPrint = true 를 수행하여
	 * 내부 json의 자동 출력을 방지한다.
	 * </pre>
	 * @param val
	 * @return
	 */
	public BaseController print(String val){
		try{
			preventAutoPrint = true;
			if (this.outs == null){
				this.outs = response.getWriter();
			}
			outs.print(val);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, String> parameterReplacement(Map param) throws Exception{
		HashMap<String, String> mRet = new HashMap<String, String>();
		Iterator iter = param.keySet().iterator();
		String[] aTmp = null;
		String key = "";
		
		while(iter.hasNext()){
			key = iter.next().toString();
			aTmp = (String[])param.get(key);
			
			if (aTmp.length == 1){
				mRet.put(key, aTmp[0].trim());
			}
			else{
				throw new Exception("parameter '" + key + "' is not single string.");
			}
		}
		
		return mRet;
	}
	
	public boolean paramReq(String... keys){
		return paramReq(param, keys);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean paramReq(Map param, String... keys){
		boolean bRet = true;
		Object val = null;
		JSONResponse json = this.json;
		
		for(String key : keys){
			if (param.containsKey(key) == false){
				bRet = false;
				
				json.getResult()
				.setValid(false)
				.addErr("parameter '" + key + "' is necessary.");
			}
			else{
				val = param.get(key);
				
				if (val == null || "".equals(val.toString())){
					json.getResult()
					.addErr("parameter '" + key + "' has required. but it's empty. check it please.");
					
					bRet = false;
				}
			}
		}
		
		return bRet;
	}

	
	public Map<String, File> parameterToFiles(String... keys){
		return parameterToFiles(request, keys);
	}
	
	public Map<String, File> parameterToFiles(HttpServletRequest req, String... keys){
		return reqFile.setRequest(req).get(keys);
	}
	
	public File getFile(String key){
		if (paramFile == null){
			paramFile = parameterToFiles();
		}
		
		return paramFile.get(key);
	}
	
	public String getFileString(String key){
		try {
			File file = getFile(key);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String sTmp = "";
			StringBuilder sb = new StringBuilder();
			
			sTmp = br.readLine();
			
			while(sTmp != null){
				sb.append(sTmp);
				sTmp = br.readLine();
			}
			
			br.close();
			fr.close();
			
			return sb.toString();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		return "";
		
	}
	
	public int applyParamFromFile(String... keys){
		int iSucc = 0;
		
		for(String key : keys){
			try{
				param.put(key, getFileString(key));
				iSucc++;
			}
			catch(Exception ex){
				param.put(key, "");
				ex.printStackTrace();
			}
		}
		
		return iSucc;
	}
	
	/***
	 * 컨트롤러 역할를 수행하고 내부를 자동으로 비운다.<br/>
	 * 만약 별도로 print 명령을 수행하지 않았거나 binary mode가 아니라면 내부 json 객체를 자동으로 문자열로 출력 한다.
	 * @param name 수행될 메서드명.
	 * @return
	 */
	public void run(String name) throws Exception{
		java.lang.reflect.Method method = null;
		boolean isNext = true;
		
		if (ran){
			throw new Exception("Controller has already been run.");
		}
		
		isNext = checkBeforeMethod(name);
		//System.out.println("checkBeforeMethod : " + isNext);
		isNext = checkSession(name, isNext);
		//System.out.println("checkSession : " + isNext);

		initParam();
		
		//System.out.println("initParam executed");
		
		if (isNext){
			//System.out.println(name + " method executed");
			method = this.getClass().getMethod(name);
			method.invoke(this);
		}
		
		if (binaryMode || preventAutoPrint){}
		else{
			print(json.toString());
		}
		
		if (preventAutoEmpty == false){
			empty();
		}
		
		this.ran = true;
	}
	
	protected boolean checkSession(String name, boolean isNext, String redirectUrl) {
		boolean bRet = ((sessionInfo != null && sessionInfo.isLogin()) || 
				withoutAuthenticated || isNext);
		
		if (bRet == false){
			json.getResult()
			.setAuth(false)
			.setMsg("세션이 종료되어 로그인 페이지로 이동합니다.")
			.setRedirect(redirectUrl);
		}
		
		return bRet;
	}
	
	protected boolean checkSession(String name, boolean isNext) {
		return checkSession(name, isNext, "login");
	}
	
	protected boolean checkBeforeMethod(String name){
		java.lang.reflect.Method method = null;
		Object objNext = null;
		boolean isNext = false;
		
		try{
			method = this.getClass().getMethod(name + "_before");
			objNext = method.invoke(this);
			
			if (objNext != null){
				isNext = (Boolean)objNext;
			}
		}
		catch(NoSuchMethodException ex){}
		catch(Exception ex){}
		
		return isNext;
	}

	public JSONResponse setData(Object data){
		return json.setData(data);
	}
	public JSONResponse setInfo(Object info){
		return json.setInfo(info);
	}
	public JSONResponse setCount(int count){
		return json.setCount(count);
	}
	public JSONResult getResult(){
		return this.json.getResult();
	}
	public JSONResponse setEtc(Object etc) {
		return this.json.setEtc(etc);
	}
	/**
	 * 
	 * <pre>
	 * 컨트롤러가 제거될 때 함께 제거되길 원하는 멤버 객체가 있다면
	 * 이 곳에 추가 등록을 해 준다.
	 * {@link com.jway.IEmptiable}를 구현 한 객체여야 한다.
	 * </pre>
	 * @param emptiable {@link com.jway.IEmptiable}
	 */
	protected void addEmptiable(IEmptiable emptiable){
		this.emptiables.add(emptiable);
	}
	
	public void empty(){
		if (emptiables != null){
			for(IEmptiable emptiable : this.emptiables){
				if (emptiable != null){
					emptiable.empty();
				}
			}
			
			emptiables.clear();
		}
		emptiables = null;

		if (this.param != null){
			this.param.clear();
		}
				
		this.request = null;
		this.response = null;
		this.session = null;
		this.outs = null;
		this.db = null;
		this.json = null;
		this.sessionInfo = null;
		this.param = null;
		this.reqFile = null;
	}
	/**
	 * 
	 * <pre>
	 * 현재 요청에 대한 응답을 파일 첨부로 바꾼다.
	 * </pre>
	 * @param filename 사용자가 받게될 파일명
	 * @return
	 */
	public BaseController attachment(String filename){
		binaryMode = true;
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		response.setHeader("Content-Description", "JSP Generated Data");
		
		return this;
	}
	
	/**
	 * 
	 * <pre>
	 * 현재 요청에 대한 응답을 파일 첨부로 바꾼다.
	 * 파일명과 확장자 사이에 현재 날짜와 시간을 지정된 포맷(yyyyMMdd_HHmmss) 문자열을 추가한
	 * 파일명으로 사용자가 다운받게 한다.
	 * </pre>
	 * @param filename 확장자가 빠진 파일명
	 * @param ext 파일 확장자
	 * @return
	 */
	public BaseController attachment(String filename, String ext){
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String sDate = format.format(now);
		StringBuilder sb = new StringBuilder();
		
		sb.append(filename)
		.append("-")
		.append(sDate)
		;
		
		if (ext.startsWith(".") == false){
			sb.append(".");
		}
		
		sb.append(ext);
		
		return attachment( sb.toString() );
	}
	
	protected BaseController toInvalid(String msg){
		getResult().setValid(false).setMsg(msg);
		
		return this;
	}
	
	protected boolean invalid(String key, String msg){
		String val = param.get(key);
		
		//System.out.print(key + "=" + msg);
		
		if (val == null || "".equals(val)){
			toInvalid(msg);
			
			return true;
		}
		
		return false;
	}
		
	protected boolean invalid(boolean bRet, String msg){
		//System.out.print(bRet + "=" + msg);
		
		if (!bRet){
			return false;
		}
		
		toInvalid(msg);
		
		return true;
	}
	
	protected boolean invalid(String key, String regex, String msg){
		//System.out.print(key + "=" + msg);
		if (Pattern.matches(regex, param.get(key))){
			//System.out.println(key + "=yes");
			return false;
		}
		
		toInvalid(msg);
		
		return true;
	}
	
	protected boolean equalsParam(String key, String val){
		String sVal = "";
		
		if (param.containsKey(key) == false){
			return false;
		}
		
		sVal = param.get(key);
		
		if (val.equals(sVal)){
			return true;
		}
		
		return false;
	}
	
	/*
	public static Map<String, String> parameterSetDefault(Map<String, String> param, String... defKeyVal){
		try{
			int iLen = defKeyVal.length;
			String key = "";
			String def = "";
			
			for(int i = 0; i < iLen;){
				key = defKeyVal[i++];
				def = defKeyVal[i++];
				
				
			}
		}
		catch(Exception ex){
			return param;
		}
	}*/
}
