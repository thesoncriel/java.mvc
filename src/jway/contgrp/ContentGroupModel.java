package com.jway.contgrp;

import java.util.*;

import com.jway.BaseModel;
import com.jway.IDBExecute;

/**
 * 컨텐츠 그룹 관련 업무를 조회/설정할 수 있는 모델
 * @author jhson
 *
 */
public class ContentGroupModel extends BaseModel {

    public ContentGroupModel(IDBExecute db) {
        super(db);
        // TODO Auto-generated constructor stub
    }
    
    /*******************************************************************************
    그룹 조회 및 설정부
    *******************************************************************************/

    /**
     * <pre>
     * 설정된 모든 컨텐츠 그룹 정보를 가져 온다.
     * 페이징 가능.
     * </pre>
     * @param param
     * @return
     */
    public List<Map<String, String>> selectGroupList(Map<String, String> param){
        String sQuery = ""+
                "";
        
        return selectDynamicWhere(sQuery, param, true);
    }
    
    /**
     * <pre>
     * 설정된 모든 컨텐츠 그룹 정보의 전체 개수를 가져 온다.
     * </pre>
     * @return
     */
    public int selectGroupListCount(){
        String sQuery = "" +
                "";
        
        return selectCount(sQuery);
    }
    
    /**
     * 
     * <pre>
     * 현재 미사용중인 그룹ID 중 가장 작은 값을 가져 온다.
     * 그룹ID를 신규로 생성 시 필요하다.
     * </pre>
     * @return
     */
    public long getMinUnusedId(){
        String sQuery = "" +
                "";
        
        try{
        	return Long.parseLong( selectOne(sQuery).get("id_group") );
        }
        catch(Exception ex){
        	return 0;
        }
    }
    /**
     * <pre>
     * 컨텐츠 그룹 내용을 갱신 한다.
     * </pre>
     * @param param group_name, id_update, id_group
     * @return
     */
    public boolean updateGroup(Map<String, String> param){
        String sQuery = "" +
                "";
        
        return execute(sQuery, param, "group_name", "id_update", "id_group");
    }
    /**
     * 
     * <pre>
     * 특정 컨텐츠 그룹 내용을 삭제 한다.
     * 정확히는 삭제가 아니라 사용 안함(unused)로 바꾼다.
     * (기능 구조상 삭제하면 안됨)
     * </pre>
     * @param param
     * @return
     */
    public boolean deleteGroup(Map<String, String> param){
        String sQuery = "" +
                "";
        
        return execute(sQuery, param, "id_update", "id_group");
    }
    /**
     * 
     * <pre>
     * 특정 그룹ID로 해당 그룹이 실제 존재 하는지 확인 한다.
     * </pre>
     * @param param id_group 필수
     * @return 있으면 true, 아니면 false
     */
    public boolean existsGroup(Map<String, String> param){
    	String sQuery = "";
    	
    	return selectOne(sQuery, param, "id_group") != null;
    }
    
    /*******************************************************************************
    컨텐츠 조회 및 그룹 적용부
    *******************************************************************************/
    
    /**
     * 
     * <pre>
     * 컨텐츠 목록을 가져 온다.
     * 페이징 가능.
     * </pre>
     * @param param id_contents, title, nm_cust 를 통한 검색 가능.
     * @return
     */
    public List<Map<String,String>> selectContentList(Map<String, String> param){
        String sQuery = "" +
                "";
        
        return selectDynamicWhere(sQuery, param, true);
    }
    /**
     * 
     * <pre>
     * 컨텐츠 목록의 총 개수를 가져 온다.
     * </pre>
     * @param param
     * @return
     */
    public int selectContentListCount(Map<String, String> param){
        String sQuery = "" +
        	"";
        
        return selectCountDynamicWhere(sQuery, param);
    }
    
    /**
     * 
     * <pre>
     * ID_CONTENTS 를 이용하여 특정 컨텐츠의 컨텐츠 그룹 적용 현황 1개를 가져 온다.
     * </pre>
     * @param param id_contents 필수
     * @return
     */
    public Map<String,String> selectOneContApply(Map<String,String> param){
        String sQuery = "" +
                "";
        
        return selectOne(sQuery, param, "id_contents");
    }
    
    /**
     * 
     * <pre>
     * 특정 컨텐츠에 컨텐츠 그룹 적용 내용을 추가 시킨다.
     * </pre>
     * @param param id_contents, id_group_info, id_insert 필수
     * @return
     */
    public boolean insertContApply(Map<String, String> param){
        String sQuery = "" +
                "";
        
        return execute(sQuery, param, "id_contents", "id_group_info", "id_insert");
    }
    
    /**
     * 
     * <pre>
     * 특정 컨텐츠의 그룹 적용 내용을 변경 한다.
     * </pre>
     * @param param id_contents, id_group_info, id_update 필수
     * @return
     */
    public boolean updateContApply(Map<String, String> param){
        String sQuery = ""+
                "";
        
        return execute(sQuery, param, "id_group_info", "id_update", "id_contents");
    }
    
    
    /*******************************************************************************
    상품목록 관련
    *******************************************************************************/
    
    /**
     * 
     * <pre>
     * 시네호텔과 관련된 모든 상품 목록을 컨텐츠 그룹과 연관지어 목록으로 출력 한다.
     * 내부 정렬 시 1차적으론 그룹 적용여부, 2차적으론 상품명을 기준으로 오른차순(Descending) 이다.
     * 페이징 가능.
     * </pre>
     * @param param nm_prod 선택. 상품명 검색용
     * @return
     */
    public List<Map<String, String>> selectProdList(Map<String, String> param){
        String sQuery = "" +
                "";
        
        template("nm_prod", "     AND PROD.NM_PROD LIKE '%{nm_prod}%' ");
        
        return selectDynamicWhere(sQuery, param, true);
    }
    
    /**
     * 
     * <pre>
     * 시네호텔과 관련된 모든 상품 목록의 개수를 출력 한다.
     * </pre>
     * @param param nm_prod 선택. 상품명 검색용
     * @return
     */
    public int selectProdListCount(Map<String, String> param){
        String sQuery = "" +
                "";
        
        return selectCountDynamicWhere(sQuery, param);
    }
    
    /**
     * 
     * <pre>
     * 특정상품과 연관된 컨텐츠 그룹 설정 내용 1개를 가져 온다.
     * </pre>
     * @param param id_prod 필수
     * @return
     */
    public Map<String,String> selectOneProdApply(Map<String,String> param){
        String sQuery = "" +
                "";
        
        return selectOne(sQuery, param, "id_prod");
    }
    
    /**
     * 
     * <pre>
     * 특정상품과 연관된 컨텐츠 그룹 설정 내용을 추가 한다.
     * </pre>
     * @param param id_prod, id_group_info, id_insert
     * @return
     */
    public boolean insertProdApply(Map<String,String> param){
        String sQuery = "" +
                "";
        
        return execute(sQuery, param, "id_prod", "id_group_info", "id_insert");
    }
    
    /**
     * 
     * <pre>
     * 특정상품과 연관된 컨텐츠 그룹 설정 내용을 변경 한다.
     * </pre>
     * @param param id_prod, id_group_info, id_update
     * @return
     */
    public boolean updateProdApply(Map<String,String> param){
        String sQuery = "" +
                "";
        
        return execute(sQuery, param, "id_group_info", "id_update", "id_prod");
    }
}
