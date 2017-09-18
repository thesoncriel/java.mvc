package com.jway.custmng;

import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jway.BaseService;

public class CustSvcImgService extends BaseService implements FileFilter{
	public final static String REG_REQ_IMG_PATH = "/regreq/";
	public final static String SVC_IMG_PATH = "/image_contract/";
	public final static String IMAGE_URI = "/ctrl/custmng/regreq.img.jsp?id_reg_seq=";
	
	private int size;
	
	public List<Map<String, String>> getFileInfoList(){
		return this.getFileInfoList("/image_contract", -1);
	}
	
	public List<Map<String, String>> getFileInfoList(String path, int size){
		File dir = new File(path);
		File[] aFile = null;
		String name = "";
		String sSize = "";
		String sIdCustSvc = "";
		String sSeq = "";
		Map<String, String> map = null;
		List<Map<String, String>> listResult = new ArrayList<Map<String, String>>();
		
		if (size < 0){
			aFile = dir.listFiles();
		}
		else{
			this.size = size;
			aFile = dir.listFiles(this);
		}
		
		for(File file : aFile ){
			name 		= file.getName();
			sIdCustSvc 	= extractIdCustSvc(name);
			sSeq 		= extractSeq(name);
			sSize 		= file.length() + "";
			
			map = new HashMap<String, String>();
			map.put("file_name", name);
			map.put("id_cust_svc", sIdCustSvc);
			map.put("seq", sSeq);
			map.put("file_size", sSize);
			
			listResult.add(map);
		}
		
		return listResult;
	}
	
	public String extractIdCustSvc(String value){
		String sVal = "";
		
		try{
			sVal = value.replaceAll("_(1|2|3)\\.jpg", "");
			Long.parseLong(sVal);
			
			return sVal;
		}
		catch(Exception ex){
			return "0";
		}
	}
	
	public String extractSeq(String value){
		String sVal = "";
		
		try{
			sVal = value.replaceAll("[0-9]+_", "").replaceAll("\\.jpg", "");
			Integer.parseInt(sVal);
			
			return sVal;
		}
		catch(Exception ex){
			return "0";
		}
	}

	public boolean accept(File pathname) {
		return pathname.length() > this.size;
	}
	
	public String zeroLeadingName(int value){
		return String.format("%09d", value);
	}
	
	public boolean makeRegReqImgFile(File file, int idRegReq){
		return copyFile(file, REG_REQ_IMG_PATH + zeroLeadingName(idRegReq) + ".jpg");
	}
	
	public boolean makeSvcImgFile(File file, String idCustSvc, String seq){
		return copyFile(file, SVC_IMG_PATH + idCustSvc + "_" + seq + ".jpg");
	}
	
//	public boolean renameSvcImgFile(String tempName, String idCustSvc, String seq){
//		return moveFile(SVC_IMG_PATH + tempName, SVC_IMG_PATH + idCustSvc + "_" + seq + ".jpg");
//	}
	
	//<%@ page trimDirectiveWhitespaces="true"%>
	/**
	 * - /regreq/ 경로에 있는 이미지를 인수로 들어간 스트림에 출력 한다.<br/>
	 * 
	 * @param outs
	 * 	출력 할 OutputStream 객체. jsp 에서 쓸 경우 response.getOutputStream() 으로 사용.
	 * @param idRegReq
	 * @return
	 */
	
	public boolean writeRegReqImgFile(OutputStream outs, String idRegReq){
		return writeFileToStream(outs, REG_REQ_IMG_PATH + zeroLeadingName( Integer.parseInt(idRegReq) ) + ".jpg");
	}
	
	public boolean writeSvcImgFile(OutputStream outs, String idCustSvc, String seq){
		return writeFileToStream(outs, SVC_IMG_PATH + idCustSvc + "_" + seq + ".jpg");
	}
}
