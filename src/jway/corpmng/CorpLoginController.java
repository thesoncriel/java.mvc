package com.jway.corpmng;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.*;

import com.jway.*;

/**
 * <pre>
 * 가맹점 로그인 관리를 담당한다.
 * </pre>
 * @author jhson
 *
 */
public class CorpLoginController extends BaseController {
	public static int MAX_SMS_AUTH_TRY = 3;
	private IDBlockProvider block;
	private AllowedIPDictionary allowIP;
	private SMSAuthProvider smsAuth;
	private BaseService service;
	private CorpLoginModel model;
	private String key;
	private String loginId;
	private String _mac;
	private String _ip;
	private boolean aleadyLoginHist = false;

	public CorpLoginController(HttpServletRequest req, HttpServletResponse res,
			HttpSession session) throws IOException, ClassCastException,
			Exception {
		super(req, res, session);
		// TODO Auto-generated constructor stub
		
		String ID = param.get("ID");
		String id = param.get("id");
		Object oMac = session.getAttribute("MAC");
		_mac = (oMac == null)? "" : (String)oMac;
		
		loginId = isEmpty(ID) ? id : ID;
		block = IDBlockProvider.getInstance();
		allowIP = AllowedIPDicProvider.getInstance(db);
		smsAuth = SMSAuthProvider.getInstance(db);
		service = new BaseService();
		model = new CorpLoginModel(db);
		_ip = request.getRemoteAddr();
		
		if (isEmpty(_mac) == false){
			key = _mac;
		}
		else{
			key = _ip;
		}
	}
	
	protected String createAuthCode(){
		Random rand = new Random();
		
		return String.format("%06d", rand.nextInt(999999));
		
		//return StringUtils. .rand.nextInt(999999);
	}
	
	/**
	 * 
	 * <pre>
	 * SMS 인증 내역에서 가맹점 정보를 찾는다.
	 * 만약 없다면 
	 * </pre>
	 * @param ip
	 * @return
	 */
	protected SMSAuthDTO findCorpInfo(String ip){
		boolean bUseMac = !isEmpty(ip) && !ip.contains(".");
		String mac = (bUseMac)? ip : "";
		//String ccIP = convertToCClassIP(ip);
		SMSAuthDTO dto = smsAuth.get(ip);
		String key;
		
				
		if (dto != null){
			return dto;
		}
		
		if (bUseMac){
			key = mac;
		}
		else{
			key = convertToCClassIP(ip);
		}
		
		//sIdCorp = allowIP.getAll().get(key);
		
		if (allowIP.isAllowed(key)){
			throw new RuntimeException("현재 접속한 주소(" + ip + ") 에 대하여 허용된 내역을 찾을 수 없습니다.");
		}
		
		if (bUseMac){
			param.put("ip", "");
			param.put("mac", key);
		}
		else{
			param.put("ip", ip);
			param.put("mac", "");
			param.put("ccip", key);
		}
		
		dto = model.selectOneCorpForSMSAuth(param);
		
		if (dto == null){
			throw new RuntimeException("현재 접속한 주소(" + ip + ") 에 대하여 SMS 인증 내역을 생성할 수 없습니다.");
		}
		
		dto.setId_sms_auth(model.getSMSAuthSeq());
		System.out.println("dto = null? : " + dto);
		
		if (model.insertSMSAuth(dto) == false){
			throw new RuntimeException("현재 접속한 주소(" + ip + ") 에 대하여 SMS 인증 내역을 추가하는 동안 오류가 발생 했습니다.");
		}
		
		smsAuth.add(dto);
		
		return dto;
	}
	
	/**
	 * 
	 * <pre>
	 * 로그인 요청한 IP와 ID가 허용된 것인지 확인한다. 
	 * </pre>
	 * @return
	 */
	public boolean isAllowed(){
		return allowIP.isAllowedBoth(key, loginId);
	}
	/**
	 * 
	 * <pre>
	 * 현재 요청 IP에 대하여 SMS 인증을 마쳤는지 검증한다.
	 * 한번 검증 시도 할 때마다 시도 횟수가 1증가 된다.
	 * 시도 횟수가 {@link MAX_SMS_AUTH_TRY} 이상이 되면 실패하고
	 * 해당 ID는 블락된다.
	 * </pre>
	 * @return 인증 성공 했다면 true, 아니면 false.
	 */
	public boolean hasSMSAuth(){
		try{
			SMSAuthDTO dto = smsAuth.get(key);
			String sTarget = dto.getAuth_code();
			String sUser = param.get("sms_auth");
			
			if (dto.getResult() == 1){
				return true;
			}
			
			if (dto.incTry_count() >= MAX_SMS_AUTH_TRY){
				block.add(loginId);
				dto.setResult(-1);
				model.updateSMSAuth(dto);
				
				return false;
			}
			
			if (sTarget.equals(sUser)){
				dto.setResult(1);
				model.updateSMSAuth(dto);
				
				return true;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public int getTryCount(){
		try{
			return smsAuth.get(key).getTry_count();
		}
		catch(Exception ex){
			return 0;
		}
	}
	
	public boolean sendSMSCode(){
		SMSAuthDTO dto = findCorpInfo(key);
		String sTel;
		String sMsg = "ONS 로그인 인증번호: ";
		
		//System.out.println("CorpLoginController->sendMMSCode---dto=" + dto.toString());
		
		if (dto == null){
			throw new RuntimeException("현재 접속한 주소(" + key + ") 에 대한 사용자 정보를 찾을 수 없습니다.");
		}
		
		sTel = dto.getTel();
		
		if (isEmpty(sTel)){
			throw new RuntimeException("현재 접속한 주소(" + key + ") 에 대하여 SMS 인증을 보내려 하였으나 연락처가 누락되어 있습니다.");
		}
		
		dto.setAuth_code(createAuthCode());
		
		return service.sendSMS(sTel.replaceAll("\\-", ""), sMsg + dto.getAuth_code());
	}
	
	public void setPreventAutoEmpty(boolean val){
		this.preventAutoEmpty = val;
	}
	
	public void smsAuthCodeReq_before(){
		withoutAuth();
	}
	public void smsAuthCodeReq(){
		try{
			if (sendSMSCode()){
				getResult().setMsg("SMS 인증 코드 보내기에 성공 하였습니다.");
			}
			else{
				getResult().setMsg("SMS 인증 코드 보내기에 실패 하였습니다.");
			}
		}
		catch(RuntimeException ex){
			ex.printStackTrace();
			toInvalid(ex.getMessage());
		}
	}
	public void outsideLogin_before(){
		System.out.println("::::::::::outsideLogin_before::::::::::");
		System.out.println(param);
		withoutAuth();
	}
	public void outsideLogin(){
		Map<String,String> mRet = model.selectOneCorpLogin(param);
		
		if (mRet == null){
			toInvalid("ID가 존재하지 않거나 패스워드가 맞지 않습니다.");
		}
		else{
			setData(mRet);
		}
	}
	
	/**
	 * 
	 * <pre>
	 * SMS 인증이 필요한지 여부를 확인한다.
	 * </pre>
	 * @return 필요하면 true, 아니면 false.
	 */
	public boolean isNeedSMSAuth(){
		return smsAuth.hasAuth(key) == false;
	}
	
	public void memCacheClear_before(){
		withoutAuth();
	}
	public void memCacheClear(){
		System.out.println("block:::::");
		block.clear();
		
		System.out.println("allowIP:::::");
		System.out.println("clear before: " + allowIP.getAll().size());
		allowIP.getAll().clear();
		System.out.println("clear after: " + allowIP.getAll().size());
		
		System.out.println("smsAuth:::::");
		smsAuth.clear();
		
		allowIP.collect(db);
		smsAuth.collect(db);
	}
	
	public void corpMacLoginHist(boolean success){
		Map<String,String> mParam = null;
		String id = "";
		
		if (isEmpty(_mac) || aleadyLoginHist){
			return;
		}
		
		id = param.get("ID");
		
		if (isEmpty(id)){
			return;
		}
		
		param.put("id_login", id);
		mParam = model.selectOneCorpLoginUser(param);
		mParam.put("ip", _ip);
		mParam.put("mac", _mac);
		mParam.put("status", (success)? "1" : "0");
		
		model.insertCorpMacLoginHist(mParam);
		aleadyLoginHist = true;
	}

	@Override
	public void empty() {
		if (service != null){
			service.empty();
		}
		if (model != null){
			model.empty();
		}
		block = null;
		allowIP = null;
		smsAuth = null;
		service = null;
		model = null;
		
		super.empty();
	}
}
