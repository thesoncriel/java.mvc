package com.jway.lnerp;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jway.*;

public class LinenetERPSalesXlsService extends Util implements IEmptiable{
	private ExcelService excel;
	private LinenetERPModel model;
	private String EXCEL_PATH;
	private String TEMPLATE_SALES;
	private Map<String,String> param;
	
	public LinenetERPSalesXlsService setResources(
			ExcelService excel, 
			LinenetERPModel model,
			String excelPath,
			String templateSales,
			Map<String,String> param
			){
		this.excel = excel;
		this.model = model;
		this.EXCEL_PATH = excelPath;
		this.TEMPLATE_SALES = templateSales;
		this.param = param;
		
		return this;
	}
	
	@SuppressWarnings("static-access")
	protected LinkedHashMap<String, Integer> getSalesXlsFieldTypes(){
		return new LinkedHashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;
		{
			this.put("rownum", excel.TYPE_NUMERIC);
			this.put("id_cust", excel.TYPE_STRING);
			this.put("nm_cust", excel.TYPE_STRING);
			this.put("ssn", excel.TYPE_STRING);
			this.put("nm_ceo", excel.TYPE_STRING);
			this.put("nm_tp_store", excel.TYPE_STRING);
			this.put("amt_supply", excel.TYPE_NUMERIC);
			this.put("amt_vat", excel.TYPE_NUMERIC);
			this.put("amt_sum", excel.TYPE_NUMERIC);
			this.put("etc_note", excel.TYPE_STRING);
		}};
	}
	
	public void action(){
		String sDate = tryValueStr(param, "yyyymm_use");
		String sYear = "";
		String sMonth = "";
		File fileExcel = new File(EXCEL_PATH + TEMPLATE_SALES);
		List<Map<String,String>> data;
		int iRowCurr = 0;
		int iRowTotalCinehotel = 0;
		int iRowTotalWowcine = 0;
	
		if (isEmpty(sDate) || (sDate.length() != 10)){
			throw new RuntimeException("날짜 형식이 잘못 되었습니다.");
		}
		
		sDate = dateFormat(sDate, "yyyyMM");
		sYear = sDate.substring(0, 4);
		sMonth = sDate.substring(4, 6);
		param.put("yyyymm_use", sDate);
		
		if (fileExcel == null || fileExcel.exists() == false){
			throw new RuntimeException("엑셀 템플릿 파일이 존재하지 않습니다.\n위치=" + EXCEL_PATH + TEMPLATE_SALES);
		}
		
		if (excel.load(fileExcel) == false){
			throw new RuntimeException("엑셀 템플릿 파일을 읽어오는데 실패 했습니다.");
		}
		
		excel.getSheet(0)
		.cell("A1").set(sYear + "년 " + sMonth + "월 라인넷 매출")
		.setName("라인넷 " + sMonth + "월 매출")
		;
		
		data = model.selectSalesHistoryCinehotel(param);
		
		if (data == null || data.size() == 0){
			throw new RuntimeException("씨네호텔에 검색된 자료가 없습니다.");
		}
		
		iRowCurr = 3;
		
		excel.getSheet(0)
		.setFieldType(getSalesXlsFieldTypes())
		.setData(data, iRowCurr);
		
		// 씨네호텔 계
		iRowCurr += data.size();
		iRowTotalCinehotel = iRowCurr + 1;
		
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("씨네호텔 계")
		.bold()
		.cellMergeH(6)
		.alignCenter()
		.border()
		.cell(iRowCurr, 6)
		.setFormula("SUM(G4:G" + iRowCurr + ")")
		.alignRight()
		.border()
		.cell(iRowCurr, 7)
		.setFormula("SUM(H4:H" + iRowCurr + ")")
		.alignRight()
		.border()
		.cell(iRowCurr, 8)
		.setFormula("SUM(I4:I" + iRowCurr + ")")
		.alignRight()
		.border()
		;
		
		// 와우시네 자료
		data = model.selectSalesHistoryWowcine(param);
		
		if (data == null || data.size() == 0){
			throw new RuntimeException("와우시네에 검색된 자료가 없습니다.");
		}
		
		iRowCurr++;
		
		excel.getSheet(0)
		.setFieldType(getSalesXlsFieldTypes())
		.setData(data, iRowCurr);
		
		// 와우시네 계
		iRowCurr += data.size();
		iRowTotalWowcine = iRowCurr + 1;
		
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("와우시네 계")
		.bold()
		.cellMergeH(6)
		.alignCenter()
		.border()
		.cell(iRowCurr, 6)
		.setFormula("SUM(G" + (iRowTotalCinehotel + 1) + ":G" + iRowCurr + ")")
		.alignRight()
		.border()
		.cell(iRowCurr, 7)
		.setFormula("SUM(H" + (iRowTotalCinehotel + 1) +":H" + iRowCurr + ")")
		.alignRight()
		.border()
		.cell(iRowCurr, 8)
		.setFormula("SUM(I" + (iRowTotalCinehotel + 1) + ":I" + iRowCurr + ")")
		.alignRight()
		.border()
		;
		
		// 총계
		iRowCurr++;
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("총계")
		.bold()
		.cellMergeH(6)
		.alignCenter()
		.border()
		.cell(iRowCurr, 6)
		.setFormula("G" + iRowTotalCinehotel + " + G" + iRowTotalWowcine)
		.alignRight()
		.border()
		.cell(iRowCurr, 7)
		.setFormula("H" + iRowTotalCinehotel + " + H" + iRowTotalWowcine)
		.alignRight()
		.border()
		.cell(iRowCurr, 8)
		.setFormula("I" + iRowTotalCinehotel + " + I" + iRowTotalWowcine)
		.alignRight()
		.border()
		;
		
		// 요약 타이틀
		iRowCurr += 2;
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("[" + sYear + "년 " + sMonth + "월 라인넷 매출]")
		.bold()
		.cellMergeH(6)
		;
		
		// 요약 필드명
		iRowCurr++;
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("구분")
		.cellMergeH(2)
		.alignCenter()
		.border()
		.cell(iRowCurr, 2)
		.set("공급가액")
		.alignCenter()
		.border()
		.cell(iRowCurr, 3)
		.set("부가세")
		.alignCenter()
		.border()
		.cell(iRowCurr, 4)
		.set("합계")
		.alignCenter()
		.border()
		.cell(iRowCurr, 5)
		.set("건수")
		.alignCenter()
		.border()
		;
		
		// 요약 씨네호텔
		iRowCurr++;
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("씨네호텔")
		.cellMergeH(2)
		.alignCenter()
		.border()
		.cell(iRowCurr, 2)
		.setFormula("G" + iRowTotalCinehotel)
		.alignCenter()
		.border()
		.cell(iRowCurr, 3)
		.setFormula("H" + iRowTotalCinehotel)
		.alignCenter()
		.border()
		.cell(iRowCurr, 4)
		.setFormula("I" + iRowTotalCinehotel)
		.alignCenter()
		.border()
		.cell(iRowCurr, 5)
		.setFormula("A" + (iRowTotalCinehotel - 1))
		.alignCenter()
		.border()
		;
		
		// 요약 와우시네
		iRowCurr++;
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("와우시네")
		.cellMergeH(2)
		.alignCenter()
		.border()
		.cell(iRowCurr, 2)
		.setFormula("G" + iRowTotalWowcine)
		.alignCenter()
		.border()
		.cell(iRowCurr, 3)
		.setFormula("H" + iRowTotalWowcine)
		.alignCenter()
		.border()
		.cell(iRowCurr, 4)
		.setFormula("I" + iRowTotalWowcine)
		.alignCenter()
		.border()
		.cell(iRowCurr, 5)
		.setFormula("A" + (iRowTotalWowcine - 1))
		.alignCenter()
		.border()
		;
		
		// 요약 총계
		iRowCurr++;
		excel.getSheet(0)
		.cell(iRowCurr, 0)
		.set("총계")
		.cellMergeH(2)
		.alignCenter()
		.bold()
		.border()
		.cell(iRowCurr, 2)
		.setFormula("C" + (iRowCurr - 1) + " + C" + (iRowCurr))
		.alignCenter()
		.bold()
		.border()
		.cell(iRowCurr, 3)
		.setFormula("D" + (iRowCurr - 1) + " + D" + (iRowCurr))
		.alignCenter()
		.bold()
		.border()
		.cell(iRowCurr, 4)
		.setFormula("E" + (iRowCurr - 1) + " + E" + (iRowCurr))
		.alignCenter()
		.bold()
		.border()
		.cell(iRowCurr, 5)
		.setFormula("F" + (iRowCurr - 1) + " + F" + (iRowCurr))
		.alignCenter()
		.bold()
		.border()
		;
	}

	public void empty() {
		this.excel = null;
		this.model = null;
		this.EXCEL_PATH = null;
		this.TEMPLATE_SALES = null;
		this.param = null;
	}
}
