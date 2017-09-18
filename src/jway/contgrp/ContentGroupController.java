package com.jway.contgrp;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jway.BaseController;
import com.jway.JSONRequest;

public class ContentGroupController extends BaseController {
	
	private ContentGroupModel model;

	public ContentGroupController(HttpServletRequest req,
			HttpServletResponse res, HttpSession session) throws IOException,
			ClassCastException, Exception {
		super(req, res, session);
		
		model = new ContentGroupModel(db);
	}

	public void contgrpList(){
		setData(model.selectGroupList(param));
		setCount(model.selectGroupListCount());
	}
	public void contgrpModify(){
		long lIdGroup = tryParamLong(param, "id_group", 0);
		String sGroupName = tryParamStr(param, "group_name", "");
		String sIdUpdate = sessionInfo.getIdUser();
		HashMap<String, Object> mInfo = null;
		
		// id_group이 0이면 신규로 처리된다 판단 한다.
		if (lIdGroup == 0){
			lIdGroup = model.getMinUnusedId();
		}
		
		// 신규라서 새 ID를 추출했는데도 그 값이 0이라면 더 이상 없는거다..
		if (lIdGroup == 0){
			toInvalid("컨텐츠 그룹을 더 이상 새로 만들 수 없습니다. 30개가 모두 사용된 것 같습니다.");
			
			return;
		}
		
		// 컨텐츠 그룹 이름이 없거나 비어 있다면 적용 불가 하다.
		if (isEmpty(sGroupName)){
			toInvalid("컨텐츠 그룹 이름을 지정해야 합니다.");
			
			return;
		}
		// 컨텐츠 그룹 이름이 너무 길면 오류 뿜뿜..
		if (sGroupName.length() > 50){
			toInvalid("컨텐츠 그룹 이름이 너무 깁니다. (최대 50자)");
			
			return;
		}
		
		mInfo = new HashMap<String, Object>();
		
		param.put("id_group", lIdGroup + "");
		param.put("id_update", sIdUpdate);
		
		mInfo.put("id_group", lIdGroup);
		mInfo.put("success", model.updateGroup(param));
		
		setInfo(mInfo);
	}
	public void contgrpDelete(){
		int iIdGroup = tryParamInt(param, "id_group", 0);
		String sIdUpdate = sessionInfo.getIdUser();
		
		if (iIdGroup <= 0){
			toInvalid("그룹ID가 잘못 되었습니다.");
			
			return;
		}
		
		param.put("id_update", sIdUpdate);
		
		setInfo(model.deleteGroup(param));
	}
	
	public void contList(){
		setData(model.selectContentList(param));
		setCount(model.selectContentListCount(param));
	}
	public void contApply(){		
		setInfo(contetsGroupApply(param));
	}
	
	protected HashMap<String, Object> contetsGroupApply(Map<String, String> param){
		long lIdGroup = tryParamLong(param, "id_group", 0);
		String sIdContents = tryParamStr(param, "id_contents", "");
		String sApply = tryParamStr(param, "apply", "");
		Map<String, String> mApplied = null;
		long lIdGroupInfo = 0;
		boolean isNew = false;
		String sIdUpdate = sessionInfo.getIdUser();
		boolean existsGroup = model.existsGroup(param);
		HashMap<String, Object> mInfo = null;
		
		// 값이 의미가 없거나 2^32를 초과 한다면 넘기지 않는다.
		if (lIdGroup <= 0 || lIdGroup > ((long)1<<30) || !existsGroup){
			toInvalid("그룹ID가 잘못 되었습니다.");
			
			return null;
		}
		
		// 컨텐츠ID가 비어있거나 null이면 넘기지 않는다.
		if (isEmpty(sIdContents)){
			toInvalid("컨텐츠ID가 잘못 되었습니다.");
			
			return null;
		}
		
		mApplied = model.selectOneContApply(param);
		isNew = mApplied == null;
		
		if ("yes".equals(sApply) && isNew){
			lIdGroupInfo = lIdGroup;
		}
		else {
			lIdGroupInfo = tryParamLong( mApplied, "id_group_info", 0 );
			
			// 새로 적용 시키는 것이면 OR 연산
			if ("yes".equals(sApply)){
				lIdGroupInfo |= lIdGroup;
			}
			// 빼는 것이면 XOR 연산
			else if ("no".equals(sApply)){
				if ((lIdGroupInfo & lIdGroup) > 0){
					lIdGroupInfo ^= lIdGroup;
				}
			}
			// 전혀 엉뚱한 값이면 오류 뿜뿜
			else{
				toInvalid("적용 수행 여부 값이 잘못 되었습니다.");
				
				return null;
			}
		}

		param.put("id_group_info", lIdGroupInfo + "");
		
		mInfo = new HashMap<String, Object>();
		mInfo.put("id_group", lIdGroupInfo);

		// 컨텐츠 그룹 적용 정보를 검색 했는데 없다면 신규로 만들어야 한다.
		if (isNew){
			param.put("id_insert", sIdUpdate);
			mInfo.put("success", model.insertContApply(param));
		}
		else{
			param.put("id_update", sIdUpdate);
			mInfo.put("success", model.updateContApply(param));
		}
		
		return mInfo;
	}

	@SuppressWarnings("rawtypes")
	public void contApplyAll(){
		JSONRequest jsonReq = JSONRequest.parse(param.get("json"));
		
		List<Map> list = jsonReq.getReqList();
		HashMap<String, String> mParam = new HashMap<String, String>();
		ArrayList<Map<String, Object>> retList = new ArrayList<Map<String,Object>>();
		Map<String, Object> retInfo;
		int iSuccCnt = 0;
		
		model.startTransaction();
		
		for(Map m : list){
			mParam.put("id_group", m.get("id_group").toString());
			mParam.put("id_contents", m.get("id_contents").toString());
			mParam.put("apply", m.get("apply").toString());
			
			retInfo = contetsGroupApply(mParam);
			
			if (retInfo != null){
				retList.add(retInfo);
				
				if ((Boolean)retInfo.get("success")){
					iSuccCnt++;
				}
			}
			// 결과가 null 이면 중단 한다.
			else{
				break;
			}
		}
		
		// 하나라도 실패 했다면 rollback 한다.
		if (iSuccCnt == list.size()){
			model.commit();
		}
		else{
			model.rollback();
		}
		
		setInfo(retList);
		setCount(iSuccCnt);
	}
	
	public void prodList(){
		setData(model.selectProdList(param));
		setCount(model.selectProdListCount(param));
	}
	public void prodApply(){
		long lIdGroup = tryParamLong(param, "id_group", 0);
		String sIdProd = tryParamStr(param, "id_prod", "");
		String sApply = tryParamStr(param, "apply", "");
		Map<String, String> mApplied = null;
		long lIdGroupInfo = 0;
		boolean isNew = false;
		String sIdUpdate = sessionInfo.getIdUser();
		boolean existsGroup = model.existsGroup(param);
		
		// 값이 의미가 없거나 2^32를 초과 한다면 넘기지 않는다.
		if (lIdGroup < 0 || lIdGroup >= ((long)1<<32) || !existsGroup){
			toInvalid("그룹ID가 잘못 되었습니다.");
			
			return;
		}
		
		// 상품ID가 비어있거나 null이면 넘기지 않는다.
		if (isEmpty(sIdProd)){
			toInvalid("상품ID가 잘못 되었습니다.");
			
			return;
		}
		
		mApplied = model.selectOneProdApply(param);
		isNew = mApplied == null;
		
		if ("yes".equals(sApply) && isNew){
			lIdGroupInfo = lIdGroup;
		}
		else{
			lIdGroupInfo = tryParamLong( mApplied, "id_group_info", 0 );
			
			// 새로 적용 시키는 것이면 OR 연산
			if ("yes".equals(sApply)){
				lIdGroupInfo |= lIdGroup;
			}
			// 빼는 것이면 XOR 연산
			else if ("no".equals(sApply)){
				if ((lIdGroupInfo & lIdGroup) > 0){
					lIdGroupInfo ^= lIdGroup;
				}
			}
			// 전혀 엉뚱한 값이면 오류 뿜뿜
			else{
				toInvalid("적용 수행 여부 값이 잘못 되었습니다.");
			}	
		}

		param.put("id_group_info", lIdGroupInfo + "");
		
		System.out.println(param);

		// 컨텐츠 그룹 적용 정보를 검색 했는데 없다면 신규로 만들어야 한다.
		if (isNew){
			param.put("id_insert", sIdUpdate);
			setInfo(model.insertProdApply(param));
		}
		else{
			param.put("id_update", sIdUpdate);
			setInfo(model.updateProdApply(param));
		}
	}
		
	@Override
	public void empty() {
		if(model != null){
			model.empty();
			model = null;
		}
		super.empty();
	}
}
