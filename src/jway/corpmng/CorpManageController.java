package com.jway.corpmng;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jway.BaseController;

/**
 * 
 * @author jhson
 *
 * <pre>
 * 지사 관리를 담당하는 컨트롤러.
 * </pre>
 */
public class CorpManageController extends BaseController {
	private CorpLoginModel model;

	public CorpManageController(HttpServletRequest req, HttpServletResponse res, HttpSession session)
			throws IOException, ClassCastException, Exception {
		super(req, res, session);
		// TODO Auto-generated constructor stub
		
		model = new CorpLoginModel(db);
	}
	
	public void corpList(){
		setData(model.selectCorpList(param));
		setCount(model.selectCorpListCount(param));
	}
	
	public void corpIpList(){
		List<Map<String,String>> retList = model.selectCorpAllowIP(param);
		setData(retList);
		setCount( (retList != null)? retList.size() : 0 );
	}
	
	public void corpIpAdd(){
		boolean bRet = false;
		String ip = param.get("ip");
		String mac = param.get("mac");
		String key;
		
		ip = this.convertToCClassIP(ip);
		mac = (isEmpty(mac))? "" : mac.toUpperCase();
		
		if (isEmpty(ip) == false){
			ip += ".*";
			key = ip;
		}
		else{
			key = mac;
		}
		
		param.put("ip", ip);
		param.put("mac", mac);
		param.put("id_insert", sessionInfo.getIdUser());
		bRet = model.insertCorpAllowIP(param);
		
		if (bRet){
			AllowedIPDicProvider.getInstance(db).add(key, param.get("id_corp"));
		}
		
		setInfo(bRet);
	}
	
	public void corpSmsAuthAllow(){
		String msg = "";
		String sIP = param.get("ip");
		String sMac = param.get("mac");
		boolean bIpEmpty = isEmpty(sIP);
		boolean bMacEmpty = isEmpty(sMac);
		
		if (bIpEmpty && bMacEmpty){
			toInvalid("IP 혹은 MAC 주소가 비어 있습니다.");
			return;
		}
		
		sIP = (bIpEmpty)? "" : sIP.replaceAll("\\*", "0");
		sMac = (bMacEmpty)? "" : sMac.toUpperCase();
		param.put("ip", sIP);
		param.put("mac", sMac);
		
		param.put("id_sms_auth", model.getSMSAuthSeq() + "");
		param.put("tel", "010-0000-0000");
		param.put("auth_code", "00000");
		param.put("result", "1");
		
		if (model.insertSMSAuth(param)){
			msg = "인증 기록이 추가 되었습니다.";
		}
		else{
			msg = "인증 기록 추가에 실패 하였습니다.";
		}
		
		getResult().setMsg(msg);
	}
	
	public void corpIpDel(){
		boolean bRet = false;
		
		param.put("id_update", sessionInfo.getIdUser());
		bRet = model.deleteCorpAllowIP(param);
		
		if (bRet){
			AllowedIPDicProvider.getInstance(db).remove(param.get("ip"));
		}
		
		setInfo(bRet);
	}
	
	@Override
	public void empty() {
		if (model != null){
			model.empty();
		}
		
		model = null;
		
		super.empty();
	}

}
