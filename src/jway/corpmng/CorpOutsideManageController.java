package com.jway.corpmng;

import java.io.IOException;
import javax.servlet.http.*;
import java.util.*;

import com.jway.*;
import com.jway.custmng.*;

/**
 * 
 * @author jhson
 *
 * <pre>
 * 지사전용 관리기능을 위한 컨트롤러.
 * 주로 /corp/ URI 로 접근하는 업무들을 담당한다.
 * </pre>
 */
public class CorpOutsideManageController extends BaseController {
	public final static String SESSION_LOGIN_KEY = "sessionInfoOutside";
	private CustManageModel model;
	private CustSvcImgService service;

	public CorpOutsideManageController(HttpServletRequest req, HttpServletResponse res, HttpSession session)
			throws IOException, ClassCastException, Exception {
		super(req, res, session, false);
		this.db = DBExecuteFactory.create("ONS_OUTSIDE");
		
		model = new CustManageModel(db);
		service = new CustSvcImgService();
		
		try{
			sessionInfo = (SessionInfo)session.getAttribute(SESSION_LOGIN_KEY);
		}
		catch(Exception ex){
			sessionInfo = null;
		}
		//System.out.println("CorpOutsideManageController constructor");
	}

	@Override
	protected boolean checkSession(String name, boolean isNext) {
		if ("corpOutsideLogin".equals(name)){
			return true;
		}
		
		return super.checkSession(name, isNext, "/corp/");
	}
	
	public void corpOutsideLogin_before(){
		if (hasBackendRedirect()){
			//System.out.println("corpOutsideLogin_before - yes");
			preventAutoPrint = true;
			preventAutoEmpty = true;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void corpOutsideLogin() throws IOException{
		//System.out.println("param size :::: " + param.size());
		StringBuilder sb = service.remoteHttp("GET", "http://localhost/ctrl/corpmng/outside.login.jsp", param, 3);
		JSONResponse jsonRes;
		String sRedirectPth = "/corp/main.jsp";
		
		try{
			jsonRes = JSONResponse.parse(sb.toString());
			
			if (jsonRes.getResult().getValid() == false){
				throw new RuntimeException( jsonRes.getResult().getMsg() );
			}
			
			sessionInfo = new SessionInfoOutside(session, request, (Map)jsonRes.getData());
			session.setAttribute(SESSION_LOGIN_KEY, sessionInfo);
			
			if (hasBackendRedirect()){
				response.sendRedirect(sRedirectPth);
			}
			
			return;
		}
		catch(RuntimeException ex){
			toInvalid(ex.getMessage());
		}
		catch(Exception ex){
			ex.printStackTrace();
			
			toInvalid("로그인 호출이 잘못 되었습니다. 서버상태가 올바른지 확인하여 주십시오.");
		}
	}
	
	protected boolean hasBackendRedirect(){
		//System.out.println("hasBackendRedirect");
		//System.out.println(param);
		//System.out.println("param size = " + param.size());
		
		return (param.containsKey("backend_redirect"));
	}
	
	public void corpOutsideLogout() throws IOException{
		String sRedirectPth = "/corp/";
		session.removeAttribute(SESSION_LOGIN_KEY);
		
		if (hasBackendRedirect()){
			preventAutoPrint = true;
			response.sendRedirect(sRedirectPth);
		}
		else{
			getResult()
			.setMsg("로그아웃 되었습니다.")
			.setRedirect(sRedirectPth);
		}
	}
	
	public void corpOutsideLoginCheck_before(){
		withoutAuth();
	}
	public void corpOutsideLoginCheck(){
		getResult().setAuth( sessionInfo != null );
	}
	
	public void custRegreqList(){
		System.out.println("custRegreqList begin");
		
		param.put("id_corp", sessionInfo.getIdCorp());
		//param.put("req_state", "Y");
		
		setData(model.selectRegReqList(param));
		setCount(model.selectRegReqListCount(param));
	}
	
	public void custRegreqAdd(){
		boolean bSucc = false;
		int iSeq = 0;
		int iIdRegSeq = tryParamInt(param, "id_reg_seq", 0);
		//StringBuilder sbRemote = null;
//		JSONResponse res = null;
		
		
		
		try{
			param.put("id_corp", sessionInfo.getIdCorp());
			param.put("nm_corp", sessionInfo.getNmCorp());
			param.put("id_insert", sessionInfo.getIdUser());
//			param.put("xsa", XSA_CODE);
			
//			sbRemote = service.remoteHttp("POST", "http://localhost/ctrl/custmng/regreq.add.by.remote.jsp", param, 3);
//			
//			if (sbRemote.length() < 10){
//				throw new RuntimeException("ONS 외부 서버와 통신에 실패 하였습니다.");
//			}
//			
//			res = JSONResponse.parse(sbRemote.toString());
//			
//			if (res == null){
//				throw new RuntimeException("ONS 외부 서버와 통신은 하였으나 받은 자료가 잘 못 되었습니다.");
//			}
//			if (res.getResult().getValid() == false){
//				throw new RuntimeException("ONS 외부 서버와 통신은 하였으나 전달된 값이 유효하지 않았습니다.");
//			}
			
//			iSeq = (Integer)res.getInfo();
//			
//			param.put("id_reg_seq", iSeq + "");
			model.startTransaction();
			
			if (iIdRegSeq > 0){
				param.put("req_state", "C");
				param.put("id_reg", sessionInfo.getIdUser());
				param.put("id_cust", "");
				
				if (model.updateRegReq(param) == false){
					throw new RuntimeException("기존 등록 내역을 취소하려 했으나 실패 했습니다.");
				}
				
				param.remove("id_reg_req");
			}
			
			if (bSucc = model.insertRegReq(param)){
				iSeq = model.getLastInserted();
				
				if (service.makeRegReqImgFile(parameterToFiles(request).get("regreqimg"), iSeq)){
					
					setData(CustSvcImgService.IMAGE_URI + iSeq);
					setEtc(iSeq);
					model.commit();
				}
				else{
					throw new RuntimeException("업로드된 자료 변환 시 오류가 발생 되었습니다.");
				}
			}
			else{
				throw new RuntimeException("자료 삽입에 실패 했습니다.");
			}
		}
		catch(RuntimeException ex){
			model.rollback();
			
			toInvalid(ex.getMessage());
		}
		
		setInfo(bSucc);
	}
	
	public void custRegreqDtlAdd(){
		int iIdRegSeq = tryParamInt(param, "id_reg_seq", 0);
		int iTpStore = tryParamInt(param, "tp_store", 0);
		
		try{
			param.put("id_corp", sessionInfo.getIdCorp());
			//param.put("nm_corp", sessionInfo.getNmCorp());
			param.put("id_insert", sessionInfo.getIdUser());
			
			model.startTransaction();
			
			if (iIdRegSeq == 0){
				if (!model.insertRegReq(param)){
					throw new RuntimeException("와우시네 등록 요청에 실패 하였습니다.");
				}
			}
			// 와우시네 등록일 경우
			if (iTpStore == 5){
				//param.put("id_manage_corp", sessionInfo.getIdCorp());
				if (iIdRegSeq > 0){
					if (!model.deleteCustRegReqDtl(param)){
						throw new RuntimeException("와우시네 등록 요청 상세 변경에 실패 하였습니다.");
					}
				}
				
				if (!model.insertCustRegReqDtl(param)){
					throw new RuntimeException("와우시네 등록 요청 상세 추가에 실패 하였습니다.");
				}
			}
			else{
				throw new RuntimeException("와우시네가 아닌데 상세 등록 요청을 하였습니다.");
			}
			
			model.commit();
			
			getResult().setRedirect("cust/regreqlist");
		}
		catch(RuntimeException ex){
			model.rollback();
			
			toInvalid(ex.getMessage());
		}
	}
	
	public void custRegreqImg() throws IOException{
		service.writeRegReqImgFile(response.getOutputStream(), param.get("id_reg_seq"));
	}
	
	public void custRegreqDtl(){
		setData(model.selectOneCustRegReqDtl(param));
	}
	
	public void corpAndProdList(){
		HashMap<String, Object> mData = new HashMap<String, Object>();
		
		mData.put("mng_corp", model.selectManageCorpList());
		mData.put("uchi_corp", model.selectUchiCorpList());
		mData.put("prod_wow", model.selectProdList());
		mData.put("id_corp", sessionInfo.getIdCorp());
		mData.put("nm_corp", sessionInfo.getNmCorp());
		
		setData(mData);
	}
	
	public void corpUserList(){
		if (isEmpty(param, "id_uchicorp") == false){
			param.put("id_corp", param.get("id_uchicorp"));
		}
		
		setData(model.selectCorpUser(param));
	}
	
	@Override
	public void empty() {
		super.empty();
		
		if (model != null){
			model.empty();
		}
		if (service != null){
			service.empty();
		}
		
		model = null;
		service = null;
		
		//System.out.println("CorpOutsideManageController empty");
	}
}