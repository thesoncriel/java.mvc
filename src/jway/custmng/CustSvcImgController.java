package com.jway.custmng;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jway.BaseController;
import com.jway.JSONRequest;

public class CustSvcImgController extends BaseController {

	public CustSvcImgController(HttpServletRequest req,
			HttpServletResponse res, HttpSession session) throws IOException,
			ClassCastException, Exception {
		super(req, res, session);
	}
	public CustSvcImgController(HttpServletRequest req,
			HttpServletResponse res, HttpSession session, boolean autoDB) throws IOException,
			ClassCastException, Exception {
		super(req, res, session, autoDB);
	}

	public void svcimgAdd(){
		CustSvcImgModel model = new CustSvcImgModel(db);
		CustSvcImgService service = new CustSvcImgService();
		File file;
		
		file = getFile("svcimg");
		service.makeSvcImgFile(file, param.get("id_cust_svc"), param.get("seq"));
		
		param.put("id_update", sessionInfo.getIdUser());
		param.put("file_size", file.length() + "");
		
		if (model.getSvcImgExists(param) == false){
			json.setData(model.insertSvcImg(param));
		}
		else{
			json.setData(model.updateSvcImg(param));
		}
		
		model.empty();
	}
	
	public void svcimgDelete(){
		CustSvcImgModel model = new CustSvcImgModel(db);
		
		json.setData(model.deleteSvcImg(param));
		
		model.empty();
	}
	
	@SuppressWarnings("rawtypes")
	public void svcimgFileList(){
		CustSvcImgService service = new CustSvcImgService();
		List list = service.getFileInfoList();
		
		json.setData(list);
		json.setCount(list.size());
	}
	public void svcimgFileSizeUpdate(){
		CustSvcImgModel model = new CustSvcImgModel(db);
		JSONRequest jsonReq = null;
		int iResult = 0;
		
		try{
			jsonReq = JSONRequest.parse(param.get("json"));
			iResult = model.updateImgFileSizeAtOnce(jsonReq.getReqList());
			
			json.setData(iResult)
			.setCount(iResult);
		}
		catch(ClassCastException ex){
			json.getResult().addErr("요청 데이터 구조가 잘 못 되었습니다.");
			System.out.println( this.getClass().toString() + " : err = " + ex.toString());
			json.getResult()
			.setValid(false)
			.setState(false);
		}
		catch(Exception ex){
			json.getResult().addErr("업데이트 작업 중 오류가 발생 했습니다.");
			System.out.println( this.getClass().toString() + " : err = " + ex.toString());
			json.getResult()
			.setState(false);
		}
	}
	
	public void svcimgImg_before() throws IOException{
		toJPEG();
	}
	
	public void svcimgImg() throws IOException{
		CustSvcImgService service = new CustSvcImgService();
		
		service.writeSvcImgFile(response.getOutputStream(), param.get("id_cust_svc"), param.get("seq"));
	}
	
	public void svcimgInfoList(){
		CustSvcImgModel model = new CustSvcImgModel(db);
		
		json.setData(model.selectInfoList(param));
		json.setCount(model.selectInfoListCount(param));
	}
	
	public void svcimgList(){
		CustSvcModel model = new CustSvcModel(db);
		CustSvcImgModel modelSub = new CustSvcImgModel(db);
	    
	    json.setData(model.selectCustSvcList(param));
	    json.setInfo(modelSub.selectSvcImgList(param));
	    
	    model.empty();
	}
	
	public void svcimgModifyDefault(){
		CustSvcImgModel model = new CustSvcImgModel(db);
		
		param.put("id_update", sessionInfo.getIdUser());
		json.setData(model.updateSvcImgDefaultYes(param));
		json.setInfo(model.updateSvcImgDefaultNoExcept(param));
		
		model.empty();
	}
	
	public void svcimgModify(){
		CustSvcImgModel model = new CustSvcImgModel(db);
		CustSvcImgService service = new CustSvcImgService();
		File file = getFile("svcimg");
		
		applyParamFromFile("id_cust_svc", "seq");
		
		service.makeSvcImgFile(file, param.get("id_cust_svc"), param.get("seq"));
		param.put("file_size", file.length() + "");
		
		if (model.getSvcImgExists(param) == false){
			param.put("id_insert", sessionInfo.getIdUser());
			json.setData(model.insertSvcImg(param));
		}
		else{
			param.put("id_update", sessionInfo.getIdUser());
			json.setData(model.updateSvcImg(param));
		}
		
		model.empty();
	}
}
