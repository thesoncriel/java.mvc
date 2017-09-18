package com.jway.lnerp;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import com.jway.*;
import com.jway.custmng.CustManageModel;

public class LinenetERPController extends BaseController {
	private final static String TEMPLATE_CUST_NEW = "cust_new_template.xls";
	private final static String TEMPLATE_SALES = "sales_template.xls";
	private final static String TEMPLATE_SALES_TAX_BILL_CINEHOTEL = "sales_tax_bill_cinehotel_template.xls";
	private final static String TEMPLATE_SALES_TAX_BILL_WOWCINE = "sales_tax_bill_wowcine_template.xls";
	private final static String EXCEL_PATH = "/excel/";
	private final static String PROGRESS_INFO_KEY = "lnerp_custErpUpload";
	private static final String ONS_CUST_EMAIL_REQUEST_URL = "http://ons.ontown.co.kr/ctrl/custmng/cust.email.list.jsp";
	private HashMap<String,Object> progressInfo;
	//private final static String XLS_TEMPLATE_CUST_MOD_REQ = "cust_mod_req.xls";
	private ExcelService excel;
	private LinenetERPModel model;
	private BaseService service;
	
	private ArrayList<String> errors;

	public LinenetERPController(HttpServletRequest req, HttpServletResponse res, HttpSession session)
			throws IOException, ClassCastException, Exception {
		super(req, res, session);

		excel = new ExcelService();
		model = new LinenetERPModel(db);
		service = new BaseService();
		
		addEmptiable(excel);
		addEmptiable(model);
		addEmptiable(service);
	}
	
	protected ArrayList<String> getErrors(){
		if (errors == null){
			errors = new ArrayList<String>();
		}
		
		return errors;
	}
	
	// 신규 거래처 부분
	
	@SuppressWarnings("unchecked")
	protected Map<String,Object> getProgressInfo(){
		if (progressInfo == null){
			try{
				progressInfo = (HashMap<String, Object>) session.getAttribute(PROGRESS_INFO_KEY);
				
				if (progressInfo == null){
					throw new Exception();
				}
			}
			catch(Exception ex){
				//ex.printStackTrace();
				progressInfo = new HashMap<String, Object>();
				session.setAttribute(PROGRESS_INFO_KEY, progressInfo);
			}
			
		}
		
		return progressInfo;
	}
	
	@SuppressWarnings("rawtypes")
	protected void clearProgressInfo(){
		Object objError = getProgressInfo().get("error");
		
		if (objError != null && (objError instanceof List)){
			((List)objError).clear();
		}
		
		getProgressInfo().clear();
		getProgressInfo().put("total", 5);
		getProgressInfo().put("curr", 0);
		getProgressInfo().put("error", null);
	}
	
	/**
	 * 
	 * <pre>
	 * 지정된 파일에서 엑셀 자료를 List 형태로 가져 온다.
	 * 단순히 자료만 가져오는 것이 아닌
	 * 파일 및 자료 상황별로 아래와 같은 체크를 하고 
	 * 그에 맞는 메시지를 RuntimeException 으로 throw 한다.
	 * </pre>
	 * <ul>
	 * <li>파일의 null 여부</li>
	 * <li>자료 읽어옴의 성공 여부</li>
	 * <li>파일 존재 여부</li>
	 * <li>가져온 자료가 비어있음의 여부</li>
	 * </ul>
	 * @param file 엑셀파일
	 * @param identityName 해당 엑셀 파일의 명칭. 오류 출력 시 이 내용을 참조 한다.
	 * @param start 해당 엑셀에서 자료를 가져올 행의 시작 위치. index 임 (0부터 시작)
	 * @param fieldNames 엑셀 자료의 각 필드에 대응되는 key 값들.
	 * @return
	 */
	protected List<Map<String,String>> getDataFromExcelFile(File file, String identityName, int start, String... fieldNames){
		List<Map<String,String>> retList;
		
		if (file == null){
			throw new RuntimeException(identityName + " 엑셀 파일을 업로드 해 주세요.");
		}
					
		if (excel.load(file) == false){
			throw new RuntimeException(identityName + " 엑셀 파일을 읽어오는데 실패 했습니다.\n파일 형식이 잘못 되었을 수 있습니다.\n(.xls만 가능)");
		}
					
		if (file == null || !file.exists()){
			throw new RuntimeException("파일 용량이 너무 큽니다.\n다시 업로드를 시도 하거나\n파일 크기를 1MB 미만으로 줄여서 다시 업로드 해 주십시요.");
		}
					
		retList = excel.readExcelToList(file, start, fieldNames);
		
		if (retList == null || retList.size() == 0){
			throw new RuntimeException(identityName + " 엑셀 파일이 비어 있습니다.");
		}
		
		return retList;
	}
	
	/* ************************************************
	신규 거래처 부분
	***************************************************/
	
	protected String[] getCustErpFieldNames(){
		return new String[]{
			 "" //번호
			,"id_cust_erp" //코드
			,"nm_cust_erp" //거래처명
			,"ssn_erp" //사업자번호
			,"" //주민등록번호
			,"" //구분
			,"nm_ceo_erp" //대표자명
			,"" //업태
			,"" //종목
			,"" //사업장주소(우편번호)
			,"" //사업장주소
			,"" //전화번호
			,"" //Fax번호
			,"" //담당사원
			,"" //사용
			,"" //거래처분류명
			,"" //업체담당자명
			,"" //담당자전화
			,"" //담당자핸드폰
			,"email_erp" //E-mail 주소
			,"" //금융기관
			,"" //계좌번호
			,"" //예금주
			,"id_cust" // 가맹점 번호
		};
	}

	public void custErpUpload(){
		HashMap<String,Object> mInfo = new HashMap<String, Object>();
		List<Map<String,String>> retList;
		String sIdUser = sessionInfo.getIdUser();
		int iUpdCnt = 0;
		int iInsCnt = 0;
		int iTotal = 0;
		int iErpCustInfoCount = 0;
		boolean bIdCustEmpty = false;
		
		clearProgressInfo();
		
		try{
			retList = getDataFromExcelFile(
					getFile("erp_cust_excel"), 
					"거래처등록", 4, 
					getCustErpFieldNames());
						
			model.startTransaction();
			
			getProgressInfo().put("total", retList.size());
			
			for(Map<String,String> row : retList){
				if (getProgressInfo().containsKey("stop") && "yes".equals(getProgressInfo().get("stop").toString()) ){
					throw new RuntimeException("작업이 중지 되었습니다.");
				}
				iTotal++;
				bIdCustEmpty = isEmpty(row, "id_cust");
				
				if (isEmpty(row, "ssn_erp") && bIdCustEmpty){
					continue;
				}
				
				if (row.get("ssn_erp").contains("0--")){
					if (bIdCustEmpty){
						continue;
					}
					
					row.put("ssn_erp", "");
				}
				
				if (row.get("nm_cust_erp").contains("(키텍)")){
					continue;
				}
				
				// 입력된 엑셀에 가맹점ID가 포함 되어 있다면
				// ERP 가맹점 정보를 재정의 하겠다는 의미이므로 삭제 한다. 
				if (!bIdCustEmpty){
					model.deleteERPCustInfo(row);
					//getErrors().add(row.get("id_cust") + " 거래처를 삭제하고 다시 입력합니다.");
					//getProgressInfo().put("error", getErrors());
				}
				
				iErpCustInfoCount = model.getERPCustInfoCount(row);
				
				if (iErpCustInfoCount != 1 && model.getCustInfoCount(row) > 1){
					getErrors().add("사업자등록번호가 중복 발견 되었습니다: " + row.get("id_cust_erp") + "|" + row.get("nm_cust_erp") + "|" + row.get("ssn_erp"));
					getProgressInfo().put("error", getErrors());
				}
				
				
				
				if (!bIdCustEmpty || (iErpCustInfoCount == 0)){
					row.put("id_insert", sIdUser);
					
					if (model.insertERPCustInfo(row) == false){
						getErrors().add("거래처 추가에 실패 했습니다: " + row.get("id_cust_erp") + "|" + row.get("nm_cust_erp"));
						getProgressInfo().put("error", getErrors());
					}
					iInsCnt++;
				}
				else{
					if (iErpCustInfoCount > 1){
						
					}
					row.put("id_update", sIdUser);
					
					if (model.updateERPCustInfo(row) == false){
						getErrors().add("거래처 변경에 실패 했습니다: " + row.get("id_cust_erp") + "|" + row.get("nm_cust_erp"));
						getProgressInfo().put("error", getErrors());
					}
					iUpdCnt++;
				}
				
				getProgressInfo().put("curr", iTotal);
			}
			
			model.commit();
			
			getProgressInfo().put("curr", iTotal);
			
			mInfo.put("upd_cnt", iUpdCnt);
			mInfo.put("ins_cnt", iInsCnt);
			mInfo.put("error", getErrors());
		}
		catch(RuntimeException ex){
			model.rollback();
			toInvalid(ex.getMessage());
		}
		
		setInfo(mInfo);
	}
	
	public void custErpUploadProgress(){
		setInfo(getProgressInfo());
	}
	
	public void custErpUploadStop(){
		getProgressInfo().put("stop", "yes");
	}
	
	protected LinkedHashMap<String, String> getCustInfoDiffFieldInfo(){
		return new LinkedHashMap<String, String>(){
			private static final long serialVersionUID = 1L;

		{
			this.put("id_cust", "가맹점ID");
			this.put("nm_cust_erp", "가맹점명(전)");
			this.put("nm_ceo_erp", "대표자명(전)");
			this.put("ssn_erp", "사업자번호(전)");
			this.put("email_erp", "이메일(전)");
			this.put("nm_cust", "가맹점명(후)");
			this.put("nm_ceo", "대표자명(후)");
			this.put("ssn", "사업자번호(후)");
			this.put("email", "이메일(후)");
		}};
	}
	protected List<Map<String,String>> emptyDuplicateFieldValues(List<Map<String,String>> data){
		String[] saField = new String[]{
			"nm_cust",
			"nm_ceo",
			"ssn",
			"email"
		};
		
		for(Map<String,String> item : data){
			for(String sField : saField){
				if (item.get(sField).equals(item.get(sField + "_erp"))){
					item.put(sField, "");
					item.put(sField + "_erp", "");
				}
			}
		}
		
		return data;
	}
	public void custDiffCheck(){
		String sDate = tryParamStr(param, "yyyymm_use", "");
		List<Map<String,String>> diffList = null;
		File fileExcel = null;
		String sFileName = "";
		int iSize = 0;
		
		try{
			if (isEmpty(sDate)){
				param.put("yyyymm_use", getFormatedNowDate("yyyyMM"));
			}
			else{
				param.put("yyyymm_use", dateFormat(sDate, "yyyyMM"));
			}
			
			diffList = model.selectERPCustInfoDiff(param);
			
			if (diffList != null && diffList.size() > 0){
				iSize = diffList.size();
				sFileName = "cust_diff_" + this.getFormatedNowDate() + ".xls";
				fileExcel = new File(EXCEL_PATH + sFileName);
				diffList = emptyDuplicateFieldValues(diffList);
				
				excel.setData(diffList, getCustInfoDiffFieldInfo());
				
				if (excel.write(fileExcel) == false){
					throw new RuntimeException("검색된 자료는 " + iSize + "개 이나 엑셀 저장에는 실패 했습니다.");
				}
			}
			else{
				getResult().setMsg("라인넷과 ERP의 거래처 자료간에 차이점을 확인 했으나 발견되지 않았습니다.");
			}
			
			param.put("file_name", sFileName);
			param.put("cust_count", iSize + "");
			param.put("id_insert", sessionInfo.getIdUser());
			
			if (model.insertCustDiffXLS(param) == false){
				throw new RuntimeException("검색된 자료는 " + iSize + "개 이고 엑셀 저장에는 성공 했으나\nDB에 기록하는 것엔 실패 하였습니다.");
			}
			
			getResult().setMsg("검색된 자료는 " + iSize + "개 이고 엑셀 저장에 성공 하였습니다. (" + sFileName + ")");
		}
		catch(RuntimeException ex){
			ex.printStackTrace();
			toInvalid(ex.getMessage());
			
			return;
		}
	}
	
	public void custDiffList(){
		setData(model.selectCustDiffXLS(param));
		setCount(model.selectCustDiffXLSCount(param));
	}
	
	public void excelDownload(){
		String sFileName = param.get("file");
		
		if (isEmpty(sFileName)){
			toInvalid("File does not exists.");
			
			return;
		}
		
		this.attachment(sFileName);
		
		try {
			service.writeFileToStream(response.getOutputStream(), EXCEL_PATH + sFileName);
		} catch (IOException e) {
			e.printStackTrace();
			
			toInvalid("File download fail.");
		}
	}
	
	public void custNewToTmp(){
		Map<String, Object> mInfo = new HashMap<String, Object>();
		String sDate = tryValueStr(param, "yyyymm_use");
		boolean bDelSucc = false;
		boolean bInsSucc = false;
		
		if (isEmpty(sDate)){
			param.put("yyyymm_use", getFormatedNowDate("yyyyMM"));
		}
		else{
			param.put("yyyymm_use", dateFormat(sDate, "yyyyMM"));
		}
		
		bDelSucc = model.deleteNewCustId();
		bInsSucc = model.insertAllNewCustId(param); 
		
		mInfo.put("del_succ", bDelSucc);
		mInfo.put("ins_succ", bInsSucc);
		
		setInfo(mInfo);
	}
	
	public void custNewNoEmailList(){
		List<Map<String,String>> list = model.selectNewCustIdNoEmail();
		
		setData(list);
		
		if (list == null){
			setCount(0);
		}
		else{
			setCount(list.size());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void custEmailSync(){
		CustManageModel model = new CustManageModel(db);
		
		param.put("xsa", XSA_CODE);
		
		try {
			StringBuilder sb = service.remoteHttp("POST", ONS_CUST_EMAIL_REQUEST_URL, param, 1000);
			JSONResponse jsonRes = JSONResponse.parse(sb.toString());
			String[] saCust = tryValueStr(param, "id_custs").split(",");
			LinkedList<String> listCustId = new LinkedList<String>();
			List<Map> listRes = (List<Map>)jsonRes.getData();
			HashMap<String,String> mParam = new HashMap<String, String>();
			HashMap<String,Object> mInfo = null;
			String sEmail = "";
			int iTotal = 0;
			int iUpdCnt = 0;
			int iDefCnt = 0;
			
			if (saCust == null || saCust.length == 0){
				throw new RuntimeException("요청된 가맹점ID 내역이 없습니다.");
			}
			
			// 1: 이메일이 없는 모든 가맹점 대상을 수집한다.
			for(String idCust : saCust){
				listCustId.add(idCust);
			}
			
			iTotal = saCust.length;
			
			model.startTransaction();
			
			// 2: ONS 본섭에서 응답받은 각 내용들을 실제 DB에 update 한다.
			for(Map mItem : listRes){
//				System.out.println("custEmailSync-----");
//				System.out.println(mItem);
				// 2.1: Email 존재 여부를 확인하고 update 한다.
				if (mItem.containsKey("email") == true && !isEmpty(mItem.get("email")) ){
					if (model.updateCustEmail(mItem)){
						listCustId.remove(mItem.get("id_cust"));
						iUpdCnt++;
					}
					else{
						throw new RuntimeException("이메일을 업데이트 시도 하였으나 실패 했습니다.\n가맹점ID=" + mItem.get("id_cust"));
					}
				}
				// 2.2: 만약 ONS 에서 온 것 조차도 Email이 없다면
				// 기본 이메일 입력을 위해 남겨둔다.
			}
			
			// 3: ONS에도 없는 것들은 기본 이메일 설정을 한다
			for(String idCust : listCustId){
				mParam.put("id_cust", idCust);
				
				try{
					sEmail = model.getCustEmail(mParam);
					
					if (isEmpty(sEmail) == false){
						throw new RuntimeException("가맹점의 이메일이 비어있다 하여 기본 이메일로 변경하려 하였으나 이미 이메일이 존재 하였습니다.\n가맹점ID=" + idCust);
					}
				}
				catch(NullPointerException ex){
					throw new RuntimeException("가맹점에서 이메일 존재 여부를 확인하려 하였으나 관련 내역이 없었습니다.\n가맹점ID=" + idCust);
				}
				
				if (model.getTpStore(mParam) == 5){
					mParam.put("email", "wowcine@jway.kr");
				}
				else{
					mParam.put("email", "cinehotel@jway.kr");
				}
				
				if (model.updateCustEmail(mParam) == false){
					throw new RuntimeException("기본 이메일 적용 시도를 하였으나 실패 했습니다.\n가맹점ID=" + idCust);
				}
				
				iDefCnt++;
			}
			
			model.commit();
			
			mInfo = new HashMap<String, Object>();
			mInfo.put("total", iTotal);
			mInfo.put("upd_cnt", iUpdCnt);
			mInfo.put("def_cnt", iDefCnt);
			
			setInfo(mInfo);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			model.rollback();
			toInvalid("동기화 서버에 접속 시도 하였으나 잘못된 문자열이 섞여 있었습니다.");
		}
		catch(RuntimeException ex){
			model.rollback();
			toInvalid(ex.getMessage());
		}
		catch(Exception ex){
			model.rollback();
			toInvalid("동기화 서버 접근에 실패 하였습니다.");
		}
		
		model.empty();
	}
	
	protected String[] getFieldNames(){
		return new String[] {
				"cust_code",
				"nm_cust",
				"ssn",
				"jumin",
				"jumin_memo",
				"nm_ceo",
				"tp_busi",
				"nm_store",
				"no_post",
				"addr",
				"addr2",
				"tel_un1",
				"tel_un2",
				"tel_un3",
				"tel_in",
				"fax1",
				"fax2",
				"fax3",
				"nm_cust_print",
				"trade_start",
				"trade_end",
				"use_type",
				"id_cust",
				"mng_name",
				"mng_pos",
				"tel1",
				"tel2",
				"tel3",
				"tel_in2",
				"hp1",
				"hp2",
				"hp3",
				"email",
				"homepage",
				"credit_limit",
				"set_amt"
		};
	}
	
	public void custNewCreate(){
		File fileTemplate = null;
		File fileSaved = null;
		String sDate;
		String sYearMonth;
		String sMonth;
		String sFileName;
		String sIdUser;
		List<Map<String,String>> listData = null;
		
		try{
			clearProgressInfo();
			
			try{
				fileTemplate = new File(EXCEL_PATH + TEMPLATE_CUST_NEW);
				
				getProgressInfo().put("curr", 1);
			}
			catch(Exception ex){
				throw new RuntimeException("신규 거래처 템플릿 엑셀 파일이 존재하지 않습니다.\n필요 파일명=" + TEMPLATE_CUST_NEW);
			}
			
			sDate = tryValueStr(param, "date");
			
			if (isEmpty(sDate)){
				sYearMonth = getFormatedNowDate("yyyyMM");
				sMonth = getFormatedNowDate("MM");
			}
			else{
				sYearMonth = dateFormat(sDate, "yyyyMM");
				sMonth = dateFormat(sDate, "MM");
			}
			
			sFileName = TEMPLATE_CUST_NEW.replaceAll("template", sYearMonth + "-" + getFormatedNowDate());
			listData = model.selectNewCust();
			
			if (listData == null || listData.size() == 0){
				throw new RuntimeException("신규 거래처 파일을 생성 하려 하였으나 조회된 내역이 없었습니다.");
			}
			
			getProgressInfo().put("curr", 2);
			
			try{
				excel.load(fileTemplate);
				excel.getSheet(0)
				.setName(sMonth + "월 신규")
				.cell("A3")
				.set(sMonth + "월")
				.setField(getFieldNames())
				.setData(listData, 3);
				
				getProgressInfo().put("curr", 3);
			}
			catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException("템플릿 엑셀 파일에 자료를 삽입하던 중 오류가 발생 했습니다.");
			}
			
			try{
				fileSaved = new File(EXCEL_PATH + sFileName);
				excel.write(fileSaved);
				getProgressInfo().put("curr", 4);
			}
			catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException("엑셀 파일을 " + sFileName + " 이름으로 저장 하려 했으나 실패 했습니다.");
			}
			
			sIdUser = sessionInfo.getIdUser();
			
			param.put("file_name", sFileName);
			param.put("cust_count", listData.size() + "");
			param.put("id_insert", sIdUser);
			
			if (model.insertCustNewXLS(param) == false){
				throw new RuntimeException("엑셀 파일을 생성 했으나 관련 내용을 기록 하는데 실패 했습니다.");
			}
			
			getProgressInfo().put("curr", 5);
			
			listData.clear();
			listData = null;
			fileSaved = null;
			
			setInfo(true);
		}
		catch(RuntimeException ex){
			if (fileSaved != null){
				fileSaved.deleteOnExit();
			}
			if (listData != null){
				listData.clear();
			}
			toInvalid(ex.getMessage());
			
			return;
		}
	}
	
	public void custNewList(){
		setData(model.selectCustNewXLS(param));
		setCount(model.selectCustNewXLSCount());
	}
	
	/* ************************************************
	매출내역 부분
	***************************************************/

	
	
	public void salesXls() throws IOException{
		LinenetERPSalesXlsService service = new LinenetERPSalesXlsService();
		
		try{
			service
			.setResources(excel, model, EXCEL_PATH, TEMPLATE_SALES, param)
			.action();
			
			service.empty();
			
			attachment("sales", "xls");
			excel.write(response.getOutputStream());
			
		}
		catch(RuntimeException ex){
			toInvalid(ex.getMessage());
		}
	}
	
	/* ************************************************
	매출세금 계산서 부분
	***************************************************/
	
	// 매출세금계산서 부분
	protected String[] getCustCloseFieldNames(){
		return new String[]{
			"" // 순번
			,"ssn" // 사업자번호
			,"nm_cust" // 거래처명
			,"" // 유형
			,"status" // 상태
			,"close_date" // 날짜
			,"" // 유형
			,"" // 과세
			,"" // 코드
			,"id_cust" // 가맹점ID
		};
	}
	
	/**
	 * 
	 * <pre>
	 * 입력된 자료를 기반으로 발행/미발행 여부를 알려준다.
	 * </pre>
	 * @param dateUse 대조하는 기준 날짜
	 * @param row 자료가 든 collection.
	 * @return 발행이면 true, 아니면 false.
	 */
//	protected boolean isTaxInv(Date dateUse, Map<String,String> row, LinenetERPModel model){
//		String ssn = row.get("ssn");
//		Date dateClose;
//		
//		// 사업자등록번호가 없다면 미발행
//		if (isEmpty(ssn) || "0".equals(ssn)){
//			return false;
//		}
//		
//		// 신규폐업 자료에는 있지만 라인넷엔 없다면 미발행
//		if (model.hasSSN(row) == false){
//			return false;
//		}
//		
//		dateClose = dateParse( param.get("close_date"), "." );
//		
//		
//	}
	
	public void taxbillCloseUpload(){
		HashMap<String,Object> mInfo = new HashMap<String, Object>();
		List<Map<String,String>> retList = null;
		Map<String,String> mCustInfo;
		String sIdUser = sessionInfo.getIdUser();
		int iSucc = 0;
		int iCurr = 0;
		String sStatus = ""; // 신규 폐업 자료에 있던 상태
		int iCloseStatus = 0; // 기록될 신규 폐업 코드
		int iCustCloseCount = 0;
		String sCloseDate = "";
		String sDateUse = "";
		
		try{
			//applyParamFromFile("yyyymm_use");
			clearProgressInfo();
			
			//System.out.println("yyyymm_use=" + param.get("yyyymm_use"));
			//System.out.println("yyyymm_use file=" + getFileString("yyyymm_use"));
			sDateUse = getFileString("yyyymm_use");
			
			if (isEmpty(sDateUse)){
				throw new RuntimeException("입력 날짜가 비었거나 파일을 입력하지 않았습니다.");
			}
			
			retList = getDataFromExcelFile(
					getFile("tax_bill_close_excel"), 
					"신규 폐업", 1, 
					getCustCloseFieldNames());

			model.startTransaction();
			
			getProgressInfo().put("total", retList.size());
			
			for(Map<String,String> row : retList){
				if (getProgressInfo().containsKey("stop") && "yes".equals(getProgressInfo().get("stop").toString()) ){
					throw new RuntimeException("작업이 중지 되었습니다.");
				}
				
				iCurr++;
				iCloseStatus = 0;
				
				getProgressInfo().put("curr", iCurr);
				
				if (isEmpty(row, "close_date") || isEmpty(row, "ssn")){
					continue;
				}
				
				sCloseDate = row.get("close_date").replaceAll("\\.", "-");
				
				// 1. 위 정보를 바탕으로 실제 가맹점 정보가 존재 하는지 확인 한다.
				mCustInfo = model.selectOneCustInfo(row);
				
				if (mCustInfo == null || mCustInfo.isEmpty()){
					getErrors().add("엑셀에 사업자번호 존재, 라인넷 없음: " + row.get("ssn") + "|" + row.get("nm_cust") + "|" + row.get("id_cust"));
					getProgressInfo().put("error", getErrors());
					
					continue;
				}
				
				// 2. 키워드에 따른 폐업 상태 코드를 구한다.
				sStatus = row.get("status");
				
				// 2.1. 상태의 키워드에 따라 나눔
				if (sStatus.contains("휴업")){
					iCloseStatus = 1;
				}
				else if (sStatus.contains("폐업")){
					iCloseStatus = 2;
				}
				else if (sStatus.contains("사업")){
					iCloseStatus = 3;
				}
				else{
					iCloseStatus = 0;
				}
				
				row.put("close_date", sCloseDate);
				row.put("close_status", iCloseStatus + "");
				row.put("id_insert", sIdUser);
				
				// 3. 폐업 거래처가 존재하면 지워준다.
				iCustCloseCount = model.getCustCloseCount(row);
				
				if (iCustCloseCount == 1){
					model.deleteCustClose(row);
				}
				// 3.1. 만약 이미 존재하는게 2개 이상이라면 오류 뿜뿜..
				else if (iCustCloseCount > 1){
					getErrors().add("폐업 거래처가 이미 존재하여 삭제 하려 했으나 같은 사업자번호로 " + iCustCloseCount + "개 존재 합니다: " +
							//"\n신규폐업 자료의 사업자번호가 중복되는 거래처에 '가맹점ID'를 함께 기재하여 다시 시도하여 주십시오.\n" + 
							//"관련 거래처: " + 
							row.get("ssn") + "|" + row.get("nm_cust") + "|" + row.get("id_cust"));
					getProgressInfo().put("error", getErrors());
					
					continue;
				}
				
				// 4. 폐업 거래처를 추가 한다.
				if (model.insertCustClose(row) == false){
					getErrors().add("폐업 거래처 추가에 실패 했습니다: " + row.get("ssn") + "|"+ row.get("nm_cust") + "|" + row.get("id_cust"));
					getProgressInfo().put("error", getErrors());
					
					continue;
				}
				
				iSucc++;
			}
			
			model.commit();
			
			getProgressInfo().put("curr", iCurr);
			
			mInfo.put("succ_cnt", iSucc);
		}
		catch(RuntimeException ex){
			model.rollback();
			toInvalid(ex.getMessage());
		}
		
		setInfo(mInfo);
		
		if(retList != null){
			retList.clear();
			retList = null;
		}
	}
	
	@SuppressWarnings("static-access")
	protected LinkedHashMap<String, Integer> getSalesTaxbillFields(){
		return new LinkedHashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;
		{
			this.put("wr_date", excel.TYPE_STRING);
			this.put("nm_cust", excel.TYPE_STRING);
			this.put("ssn", excel.TYPE_STRING);
			this.put("nm_ceo", excel.TYPE_STRING);
			this.put("buy_cnt", excel.TYPE_NUMERIC);
			this.put("amt_supply", excel.TYPE_NUMERIC);
			this.put("amt_vat", excel.TYPE_NUMERIC);
			this.put("sales_subj", excel.TYPE_STRING);
			this.put("rel_subj", excel.TYPE_STRING);
			this.put("prod_name", excel.TYPE_STRING);
			this.put("buis_type1", excel.TYPE_STRING);
			this.put("buis_type2", excel.TYPE_STRING);
			this.put("addr1", excel.TYPE_STRING);
			this.put("addr2", excel.TYPE_STRING);
		}};
	}
	
	public void taxbillCreate(){
		String sDate = tryParamStr(param, "yyyymm_use", "");
		String[] saDate = null;
		String sYear = "";
		String sMonth = "";
		List<Map<String,String>> list = null;
		File fileExcel = null;
		File fileTemplateWow = null;
		File fileTemplatePtv = null;
		String sFileNamePrefix = "sales_tax_bill_";
		String sFileNameWow = "";
		String sFileNamePtv = "";
		String sFileDate = this.getFormatedNowDate();
		int iSizeWowInv = 0;
		int iSizeWowInvNo = 0;
		int iSizePtvInv = 0;
		int iSizePtvInvNo = 0;
		String sCustCount = "";
		
		try{
			clearProgressInfo();
			
			if (isEmpty(sDate) || sDate.length() != 10){
				throw new RuntimeException("사용연월을 입력 하세요.");
			}
			
			getProgressInfo().put("total", 9);
			getProgressInfo().put("curr", 0);
			
			saDate = sDate.split("-");
			sYear = saDate[0];
			sMonth = saDate[1];
			sDate = sYear + sMonth;
			
			// 와우시네 작업
			fileTemplateWow = new File(EXCEL_PATH + TEMPLATE_SALES_TAX_BILL_CINEHOTEL);
			if (excel.load(fileTemplateWow) == false){
				throw new RuntimeException(TEMPLATE_SALES_TAX_BILL_CINEHOTEL + " 엑셀 템플릿 파일이 존재하지 않습니다.\n관리자에 문의 하세요.");
			}
			
			getProgressInfo().put("curr", 1);
			
			list = model.selectSalesTaxbill(sDate, true, true);
			iSizeWowInv = (list != null) ? list.size() : 0;
			excel.getSheet(0)
			.cell("B3")
			.set(sYear + "년")
			.setFieldType(getSalesTaxbillFields())
			.setData(list, 11)
			;
			
			getProgressInfo().put("curr", 2);
			
			list = model.selectSalesTaxbill(sDate, true, false);
			iSizeWowInvNo = (list != null) ? list.size() : 0;
			excel.getSheet(1)
			.cell("B3")
			.set(sYear + "년")
			.setFieldType(getSalesTaxbillFields())
			.setData(list, 11)
			;
			
			getProgressInfo().put("curr", 3);
			
			excel.getSheet(2)
			.cell("A1")
			.set(sYear.substring(2) + "년 " + sMonth + "월 라인넷 와우시네 매출");
			
			sFileNameWow = sFileNamePrefix + sFileDate + "(wowcine).xls";
			fileExcel = new File(EXCEL_PATH + sFileNameWow);
			
			if (excel.write(fileExcel) == false){
				throw new RuntimeException("와우시네 파일 생성에 실패 했습니다.");
			}
			
			getProgressInfo().put("curr", 4);
			
			// 시네호텔 작업
			fileTemplatePtv = new File(EXCEL_PATH + TEMPLATE_SALES_TAX_BILL_WOWCINE);
			if (excel.load(fileTemplatePtv) == false){
				throw new RuntimeException(TEMPLATE_SALES_TAX_BILL_WOWCINE + " 엑셀 템플릿 파일이 존재하지 않습니다.\n관리자에 문의 하세요.");
			}
			
			getProgressInfo().put("curr", 5);
			
			list = model.selectSalesTaxbill(sDate, false, true);
			iSizePtvInv = (list != null) ? list.size() : 0;
			excel.getSheet(0)
			.cell("B3")
			.set(sYear + "년")
			.setFieldType(getSalesTaxbillFields())
			.setData(list, 11)
			;
			
			getProgressInfo().put("curr", 6);
			
			list = model.selectSalesTaxbill(sDate, false, false);
			iSizePtvInvNo = (list != null) ? list.size() : 0;
			excel.getSheet(1)
			.cell("B3")
			.set(sYear + "년")
			.setFieldType(getSalesTaxbillFields())
			.setData(list, 11)
			;
			
			getProgressInfo().put("curr", 7);
			
			excel.getSheet(2)
			.cell("A1")
			.set(sYear.substring(2) + "년 " + sMonth + "월 라인넷 씨네호텔 매출");
			
			sFileNamePtv = sFileNamePrefix + sFileDate + "(cinehotel).xls";
			fileExcel = new File(EXCEL_PATH + sFileNamePtv);
			
			if (excel.write(fileExcel) == false){
				throw new RuntimeException("씨네호텔 파일 생성에 실패 했습니다.");
			}
			
			getProgressInfo().put("curr", 8);
			
			sCustCount = iSizeWowInv + "," + iSizeWowInvNo + "," + iSizePtvInv + "," + iSizePtvInvNo;
			
			param.put("file_name_wow", sFileNameWow);
			param.put("file_name_ptv", sFileNamePtv);
			param.put("wow_inv_cnt", iSizeWowInv + "");
			param.put("wow_invno_cnt", iSizeWowInvNo + "");
			param.put("ptv_inv_cnt", iSizePtvInv + "");
			param.put("ptv_invno_cnt", iSizePtvInvNo + "");
			param.put("id_insert", sessionInfo.getIdUser());
			
			if (model.insertSalesTaxbillXLS(param) == false){
				throw new RuntimeException("검색된 자료는 각각 " + sCustCount + "개 이고 엑셀 저장에는 성공 했으나\nDB에 기록하는 것엔 실패 하였습니다.");
			}
			
			getProgressInfo().put("curr", 9);
			
			getResult().setMsg("검색된 자료는 각각 " + sCustCount + "개 이고 엑셀 저장에 성공 하였습니다. (" + sFileNameWow + "," + sFileNamePtv + ")");
		}
		catch(RuntimeException ex){
			ex.printStackTrace();
			toInvalid(ex.getMessage());
			
			return;
		}
	}
	
	public void taxbillList(){
		setData(model.selectSalesTaxbillXLS(param));
		setCount(model.selectSalesTaxbillXLSCount(param));
	}
	
	
	@Override
	public void empty() {
		if (errors != null){
			errors.clear();
			errors = null;
		}
		
		super.empty();
	}
}
