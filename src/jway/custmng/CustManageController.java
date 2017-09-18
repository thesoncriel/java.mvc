package com.jway.custmng;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jway.*;

public class CustManageController extends CustSvcImgController {
	
	private boolean unpmtInfoRequestOnly = false;

	public CustManageController(HttpServletRequest req,
			HttpServletResponse res, HttpSession session) throws IOException,
			ClassCastException, Exception {
		super(req, res, session);
	}
	
	public void clientEnable(){
		CustClientModel model = new CustClientModel(db);
	    
		param.put("id_update", sessionInfo.getIdUser());		
	    json.setData( model.updateClientEnable(param) );
	    
	    model.empty();
	}
	
	public void clientList(){
		CustClientModel model = new CustClientModel(db);

	    json.setData(model.selectClientList(param));
	    json.setInfo(model.selectOneCustInfo(param));

	    model.empty();
	}
	
	public void custDetail_before(){
		if ("jway".equals(param.get("from")) == true){
			withoutAuth();
			unpmtInfoRequestOnly = true;
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void custDetail(){
		CustManageModel model = new CustManageModel(db);
		CustSvcModel modelSvc = new CustSvcModel(db);
		BaseService service = new BaseService();
		StringBuilder sbCallUrl;
		String sExternalRes = "";
		JSONResponse jsonExternal;
		java.util.Map mCustDetail = new HashMap();
		
		if (unpmtInfoRequestOnly == false){
			mCustDetail.putAll(model.selectOneCustDetail(param));
		    json.setInfo(modelSvc.selectCustSvcList(param));
		}
		
		
		// 제이웨이 ONS 에서 라인넷 정보를 호출 했을 때 이것만 준다
		mCustDetail.putAll(model.selectOneCustDetailUnpmt(param));
		
		json.setData(mCustDetail);
		
	    
	    if ("linenet".equals(param.get("with"))){
	    	sbCallUrl = new StringBuilder();
	    	sbCallUrl.append("http://cns.ontown.co.kr/ctrl/custmng/cust.detail.jsp");
	    	sbCallUrl.append("?")
	    	.append("id_cust")
	    	.append("=")
	    	.append(param.get("id_cust"))
	    	.append("&from=jway");
	    	
	    	sExternalRes = service.remoteHttpGet(sbCallUrl.toString(), 10000).toString();
		    jsonExternal = JSONResponse.parse(sExternalRes);
	    	json.setEtc(jsonExternal.getData());
	    }
	    
	    model.empty();
	    modelSvc.empty();
	}
	
	public void custList(){
		CustManageModel model = new CustManageModel(db);
	    
	    param.put("id_manage_corp", sessionInfo.getIdCorp());
	    json.setData(model.selectCustList(param));
	    
	    model.empty();
	}
	
	public void periodList(){
		CustClientModel model = new CustClientModel(db);
	    
	    json.setData(model.selectPeriodServerList(param));

	    model.empty();
	}
	
	public void periodSubmit(){
		BaseService service = new BaseService();
		HashMap<String, Object> mInfo = new HashMap<String, Object>();
		
		mInfo.put("resCode", service.remoteHttpCall(param));
		
		json.setInfo(mInfo);
	    json.getResult().addErr(service.getErr());
	}
	
	public void regreqImg_before() throws IOException{
		toJPEG();
	}
	
	public void regreqImg() throws IOException{
		CustSvcImgService service = new CustSvcImgService();
		
		service.writeRegReqImgFile(response.getOutputStream(), param.get("id_reg_seq"));
	}
	
	public void regreqImgUpload(){
		CustSvcImgService service = new CustSvcImgService();
		CustManageModel model = new CustManageModel(db);
		int idRegSeq = -1;
		
		param.put("id_corp", sessionInfo.getIdCorp());
		param.put("id_insert", sessionInfo.getIdUser());
		
		if (model.insertRegReq(param)){
			idRegSeq = model.getLastInserted();
						
			if(service.makeRegReqImgFile(parameterToFiles(request).get("regreqimg"), idRegSeq)){
				json.setData(CustSvcImgService.IMAGE_URI + idRegSeq);
			}
			else{
				json.getResult()
				.setValid(false)
				.setState(false)
				.setMsg("업로드된 자료 변환 시 오류가 발생 되었습니다.")
				.addErr(service.getErr())
				.addErr(model.getErr());
			}
		}
		else{
			json.getResult()
			.setValid(false)
			.setState(false)
			.setMsg("자료 삽입에 실패 했습니다.")
			.addErr(service.getErr())
			.addErr(model.getErr());
		}
		
		model.empty();
		service.empty();
	}
	
	public void regreqAddByRemote_before(){
		if (XSA_CODE.equals(tryParamStr(param, "xsa", ""))){
			withoutAuth();
		}
	}
	public void regreqAddByRemote(){
		CustSvcImgService service = new CustSvcImgService();
		CustManageModel model = new CustManageModel(db);
		
		if (model.insertRegReq(param)){
			setInfo(model.getLastInserted());
		}
		else{
			toInvalid("가맹점 등록 요청 추가에 실패 했습니다.");
			setInfo(-1);
		}
		
		model.empty();
		service.empty();
	}
	
	public void regreqList(){
		CustManageModel model = new CustManageModel(db);
		
		if ("1".equals(param.get("allreq")) == false){
			param.put("id_corp", sessionInfo.getIdCorp());
		}

	    json.setData(model.selectRegReqList(param));
	    json.setCount(model.selectRegReqListCount(param));
	    
	    model.empty();
	}
	
	public void regreqDtl(){
		CustManageModel model = new CustManageModel(db);
		
		setData(model.selectOneCustRegReqDtl(param));
		
		model.empty();
	}
	
	public void regreqAllow(){
		CustManageModel model = new CustManageModel(db);
		
		param.put("req_state", "Y");
		param.put("id_reg", sessionInfo.getIdUser());
		json.setData(model.updateRegReq(param));
		
		model.empty();
	}
	
	public void regreqCancel(){
		CustManageModel model = new CustManageModel(db);
		
		param.put("req_state", "C");
		param.put("id_reg", sessionInfo.getIdUser());
		param.put("id_cust", "");
		json.setData(model.updateRegReq(param));
		
		model.empty();
	}
	
	public void otpLogList(){
		CustManageModel model = new CustManageModel(db);
		
		json.setData(model.selectOTPLogList(param));
		json.setCount(model.selectOTPLogListCount(param));
		
		model.empty();
	}
	
	public void custEmailList_before(){
		if (XSA_CODE.equals(param.get("xsa")) == true){
			withoutAuth();
		}
	}
	
	public void custEmailList(){
		CustManageModel model = new CustManageModel(db);
		String sIdCusts = tryParamStr(param, "id_custs", "");
		
		if (!isEmpty(sIdCusts) && !sIdCusts.matches("[A-z]+")){
			setData(model.selectCustEmail( param ));
		}
		
		model.empty();
	}
}
