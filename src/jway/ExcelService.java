package com.jway;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.CellReference;





@SuppressWarnings("deprecation")
public class ExcelService implements IEmptiable{
	public final static int TYPE_NUMERIC = HSSFCell.CELL_TYPE_NUMERIC;
	public final static int TYPE_STRING = HSSFCell.CELL_TYPE_STRING;
	public final static int TYPE_FORMULA = HSSFCell.CELL_TYPE_FORMULA;
//	public class ExcelStyle{
//		public ExcelStyle(){
//			style = wb.createCellStyle();
//		}
//		
//		public ExcelStyle bold(boolean bold){
//			HSSFFont font = null;
//			
//			if (wb != null && style != null){
//				font = wb.createFont();
//				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//				style.setFont(font);
//			}
//			
//			return this;
//		}
//		
//		public ExcelStyle bold(){
//			return bold(true);
//		}
//		
//		public ExcelStyle bgGray25(){
//			if (style != null){
//				style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
//			}
//			
//			return this;
//		}
//		public ExcelStyle bgGray40(){
//			if (style != null){
//				style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
//			}
//			
//			return this;
//		}
//	}
	
	public class ExcelSheet{
		private int index = 0;
		private int row;
		private short col;
		private String[] fields;
		private Map<String, Integer> fieldType;
		private HSSFCellStyle cellStyle;
		private HSSFFont cellFont;
		
		public ExcelSheet(){
			//setIndex(0);
		}
		
		public ExcelSheet(int index){
			setIndex(index);
		}
		
		public ExcelSheet setIndex(int index){
			sheet = wb.getSheetAt(index);
			this.index = index;
			
			return this;
		}
		
		public ExcelSheet setName(String name){
			wb.setSheetName(index, name);
			
			return this;
		}
		
		public ExcelSheet cell(String cellRef){
			CellReference cr = new CellReference(cellRef);
			row = cr.getRow();
			col = cr.getCol();
			cellStyle = null;
			cellFont = null;
			
			return this;
		}
		public ExcelSheet cell(int row, int col){
			this.row = row;
			this.col = (short)col;
			cellStyle = null;
			cellFont = null;
			
			return this;
		}

		
		public ExcelSheet set(String value){
			setCellValue(sheet, row, col, value);
			
			return this;
		}
		public ExcelSheet set(double value){
			setCellValue(sheet, row, col, value);
			
			return this;
		}
		public ExcelSheet setFormula(String formula){
			setCellFormula(sheet, row, col, formula);
			
			return this;
		}
		
		public int getInt(){
			return (int)getCell(sheet, row, col).getNumericCellValue();
		}
		public String getStr(){
			return getCell(sheet, row, col).getRichStringCellValue().getString();
		}
		public String getFormula(){
			return getCell(sheet, row, col).getCellFormula();
		}
		
		public ExcelSheet cellMergeH(int colCount){
			CellRangeAddress cra = new CellRangeAddress(row, row, col, col + colCount - 1);
			sheet.addMergedRegion(cra);
			
			return this;
		}
		
		protected HSSFCellStyle getStyle(){
			if (cellStyle == null){
				cellStyle = wb.createCellStyle();
				getCell(sheet, row, col).setCellStyle(cellStyle);
			}
			
			return cellStyle;
		}
		
		public ExcelSheet alignRight(){
			getStyle().setAlignment( HSSFCellStyle.ALIGN_RIGHT );
			
			return this;
		}
		public ExcelSheet alignLeft(){
			getStyle().setAlignment( HSSFCellStyle.ALIGN_LEFT );
			
			return this;
		}
		public ExcelSheet alignCenter(){
			getStyle().setAlignment( HSSFCellStyle.ALIGN_CENTER );
			
			return this;
		}
		
		protected HSSFFont getFont(){
			if (cellFont == null){
				cellFont = wb.createFont();
				getStyle().setFont(cellFont);
			}
			
			return cellFont;
		}
		
		public ExcelSheet bold(boolean bold){
			getFont().setBoldweight( (bold)? HSSFFont.BOLDWEIGHT_BOLD : HSSFFont.BOLDWEIGHT_NORMAL );
			
			return this;
		}
		public ExcelSheet bold(){
			return bold(true);
		}

		public ExcelSheet border(){
			return border(true);
		}
		public ExcelSheet border(boolean border){
			HSSFCellStyle style = getStyle();
			if (border){
				style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
				style.setBorderRight( HSSFCellStyle.BORDER_THIN );
				style.setBorderTop( HSSFCellStyle.BORDER_THIN );
				style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
			}
			else{
				style.setBorderLeft( HSSFCellStyle.BORDER_NONE );
				style.setBorderRight( HSSFCellStyle.BORDER_NONE );
				style.setBorderTop( HSSFCellStyle.BORDER_NONE );
				style.setBorderBottom( HSSFCellStyle.BORDER_NONE );
			}
			
			return this;
		}
		
		public ExcelSheet setField(String... field){
			fields = field;
			
			return this;
		}
		public ExcelSheet setFieldType(LinkedHashMap<String, Integer> types){
			fields = types.keySet().toArray(new String[]{});
			fieldType = types;
			
			return this;
		}
		public Map<String, Integer> getFieldType(){
			if (fieldType == null){
				fieldType = new HashMap<String, Integer>();
			}
			
			return fieldType;
		}
		public ExcelSheet clearFieldType(){
			fieldType.clear();
			fieldType = null;
			
			return this;
		}
		public ExcelSheet putFieldTypeString(String... keys){
			Map<String, Integer> mFt = getFieldType();
			
			for(String key: keys){
				mFt.put(key, HSSFCell.CELL_TYPE_STRING);
			}
			
			return this;
		}
		public ExcelSheet putFieldTypeNumber(String... keys){
			Map<String, Integer> mFt = getFieldType();
			
			for(String key: keys){
				mFt.put(key, HSSFCell.CELL_TYPE_NUMERIC);
			}
			
			return this;
		}
		public ExcelSheet putFieldTypeFormula(String... keys){
			Map<String, Integer> mFt = getFieldType();
			
			for(String key: keys){
				mFt.put(key, HSSFCell.CELL_TYPE_FORMULA);
			}
			
			return this;
		}
		@SuppressWarnings("rawtypes")
		public ExcelSheet setData(List list, int startIndex){
			setDataToSheet(sheet, list, startIndex, fields, fieldType);
			
			return this;
		}
	}


	
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private HSSFCellStyle borderStyle;
	private ExcelSheet _sheet;
	
	public HSSFWorkbook getWorkbook(){
		return wb;
	}
	
	public HSSFCellStyle getBorderStyle(){
		if (borderStyle == null){
			if (wb == null){
				return null;
			}
			borderStyle = wb.createCellStyle();
			borderStyle.setBorderLeft( HSSFCellStyle.BORDER_THIN );
			borderStyle.setBorderRight( HSSFCellStyle.BORDER_THIN );
			borderStyle.setBorderTop( HSSFCellStyle.BORDER_THIN );
			borderStyle.setBorderBottom( HSSFCellStyle.BORDER_THIN );
			borderStyle.getFont(wb).setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		}
		
		return borderStyle;
	}

	
	public ExcelSheet getSheet(int index){
		if (_sheet == null){
			_sheet = new ExcelSheet(index);
		}
		else{
			_sheet.setIndex(index);
		}
		
		return _sheet; 
	}
	
	public String calcCellRangeV(String cellRef, int rowCount){
		CellReference cr = new CellReference(cellRef);
		
		CellRangeAddress cra = new CellRangeAddress(cr.getRow(), cr.getRow() + rowCount, cr.getCol(), cr.getCol());
		
		return cra.formatAsString();
	}
	
	
	
	//TODO: 암호 1111 들어오는거 수행할 수 있음 할 것
	public boolean load(File file, String pass){
		FileInputStream fis = null;
		HSSFWorkbook wb;
		//POIFSFileSystem fs = new POIFSFileSystem();
		
		try {				
			if (pass == null){
				fis = new FileInputStream(file);
				wb = new HSSFWorkbook(fis);
				this.wb = wb;
				
				fis.close();
				
				return true;
			}
			else{
				//Biff8EncryptionKey.setCurrentUserPassword(pass);
				//fs = new POIFSFileSystem();
			}
			
			return true;
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				fis.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return false;
		}
	}
	
	public boolean load(File file){
		return load(file, null);
	}
	
	/**
	 * 
	 * <pre>
	 * 지정된 엑셀파일(xls)로부터 자료를 읽어와 List 로 바꿔준다.
	 * 엑셀 내 첫번째 시트에서 실제 입력된 자료 범위 내에서 작업하며,
	 * 그 범위의 기준은 가장 좌측 셀의 값이 비어 있지 않은 것 까지를 기준으로 한다.
	 * </pre>
	 * @param file 자료를 읽어올 엑셀 파일
	 * @param start 자료를 읽어올 때 시작될 행 번호. 1부터 시작된다.
	 * @param keys 엑셀 자료의 각 열(column)별 키(key) 이름. 좌측부터 순서대로 쓰이며 중간에 null 이나 빈 문자열이면 해당 열은 무시한다.
	 * @return 각 행(Row)이 Map 으로 변환되어진 List.
	 */
	public List<Map<String, String>> readExcelToList(File file, int start, String... keys){
		FileInputStream fis = null;
		HSSFWorkbook wb;
		HSSFSheet s;
		List<Map<String, String>> list = null;
		
		try {
			fis = new FileInputStream(file);
			wb = new HSSFWorkbook(fis);
			s = wb.getSheetAt(0);
			
			list = readSheet(s, start, keys);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		try {
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (list == null){
			return new ArrayList<Map<String, String>>();
		}

		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Map<String, String>> readSheet(HSSFSheet s, int start, String... keys){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map mRow = null;
		HSSFRow r = null;
		HSSFCell c = null;
		int iRow = start;
		int iCol = 0;
		boolean finished = false;
		String sVal = null;
		int iMaxRow = s.getPhysicalNumberOfRows();
		
		//System.out.println("readSheet ::: ");
		//System.out.println("maxRow = " + iMaxRow);
		
		while((iRow < iMaxRow) && (finished == false)){
			r = s.getRow(iRow);
			mRow = new HashMap();
			iCol = -1;
			
			//System.out.println("row num = " + iRow);
			//System.out.println("col count = " + r.getPhysicalNumberOfCells());
			
			for(String key : keys){
				iCol++;
				c = r.getCell(iCol);
				
				if (key == null || "".equals(key)){
					continue;
				}
				
				if (c != null){
					sVal = extCellValue(c);
					if (sVal != null){
						sVal = sVal.trim();
					}
				}
				else{
					sVal = "";
				}
				
				//System.out.println("key = " + key + ", " + iCol);
			
				
				//System.out.println("row=" + iRow + ", col=" + iCol + ", key= " + key + ", val=" + sVal);
				
				if ((iCol == 0) && (sVal == null || "".equals(sVal))){
					finished = true;
					break;
				}
				
				mRow.put(key, sVal);
			}
			
			if (finished){
				break;
			}
			
			list.add(mRow);
			iRow++;
		}
		
		return list;
	}
	
	protected String extCellValue(HSSFCell c){
		double dVal = 0.0;
		long lVal = 0L;
		double dValPointUnder = 0.0;
		
		switch(c.getCellType()){
			case HSSFCell.CELL_TYPE_BOOLEAN:
				return c.getBooleanCellValue() + "";
			case HSSFCell.CELL_TYPE_ERROR:
				return c.getErrorCellValue() + "";
			case HSSFCell.CELL_TYPE_FORMULA:
				return c.getCellFormula();
			case HSSFCell.CELL_TYPE_NUMERIC:
				dVal = c.getNumericCellValue();
				lVal = (long)dVal;
				dValPointUnder = dVal - lVal;
				
				if (dValPointUnder > 0.000000001){
					return Double.toString(dVal);
				}
				else{
					return lVal + "";
				}
			case HSSFCell.CELL_TYPE_STRING:
				return c.getRichStringCellValue().getString();
			case HSSFCell.CELL_TYPE_BLANK:
			default:
				return "";
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public ExcelService setData(List list, LinkedHashMap<String, String> fieldInfo){
		wb = new HSSFWorkbook();
		HSSFSheet s = wb.createSheet();
		setDataToSheet(s, list, fieldInfo);
		
		return this;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public ExcelService setData(List list, String... fieldInfo) throws RuntimeException{
		LinkedHashMap<String, String> mInfo = new LinkedHashMap<String, String>();
		
		if ((fieldInfo == null) || ((fieldInfo.length % 2) != 0)){
			throw new RuntimeException("ExcelService.writeToExcel: fieldInfo length is not even.");
		}
		
		for(int i = 0; i < fieldInfo.length; i++){
			mInfo.put(fieldInfo[i], fieldInfo[++i]);
		}
		
		return setData(list, mInfo);
	}
	
	public boolean write(File file){
		FileOutputStream fos = null;
		boolean bRet = false;
		
		try {
			fos = new FileOutputStream(file);
			bRet = write(fos);
			
			if (bRet){
				fos.flush();
			}
			
			fos.close();
			
			return bRet;
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			return false;
		}
	}
	
	public boolean write(OutputStream out){
		try {
			wb.write(out);
			
			empty();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			empty();
			
			return false;
		}
	}
	
	protected HSSFCell getCell(HSSFSheet s, int cellRow, int cellCol){
		HSSFRow row = s.getRow(cellRow);
		HSSFCell cell = null;
		
		if (row == null){
			row = s.createRow(cellRow);
			cell = row.createCell(cellCol);
		}
		else{
			cell = row.getCell(cellCol);
			
			if (cell == null){
				cell = row.createCell(cellCol);
			}
		}

		return cell;
	}
	
	protected HSSFCell setCellValue(HSSFSheet s, String cellRef, double value){
		CellReference cr = new CellReference(cellRef);
		
		return setCellValue(s, cr.getRow(), cr.getCol(), value);
	}
	protected HSSFCell setCellValue(HSSFSheet s, String cellRef, String value){
		CellReference cr = new CellReference(cellRef);
		
		return setCellValue(s, cr.getRow(), cr.getCol(), value);
	}
	protected HSSFCell setCellFormula(HSSFSheet s, String cellRef, String formula){
		CellReference cr = new CellReference(cellRef);
		
		return setCellFormula(s, cr.getRow(), cr.getCol(), formula);
	}
	
	protected HSSFCell setCellValue(HSSFSheet s, int cellRow, short cellCol, Object value){
		if (value == null){
			return null;
		}
		
		if (value.getClass() == String.class){
			return setCellValue(getCell(s, cellRow, cellCol), (String)value);
		}
		if (value.getClass() == Double.class){
			return setCellValue(getCell(s, cellRow, cellCol), (Double)value);
		}
		if (value.getClass() == Integer.class){
			return setCellValue(getCell(s, cellRow, cellCol), (Integer)value);
		}
		
		return null;
	}
	protected HSSFCell setCellValue(HSSFCell c, int cellType, Object value){
		if (value == null){
			return null;
		}
		
		c.setCellType(cellType);
		
		switch(cellType){
			case HSSFCell.CELL_TYPE_FORMULA:
				return setCellFormula(c, value.toString());
			case HSSFCell.CELL_TYPE_NUMERIC:
				return setCellValue(c, Double.parseDouble(value.toString()));
			default:
				return setCellValue(c, value.toString());
		}
	}
	
	protected HSSFCell setCellValue(HSSFSheet s, int cellRow, short cellCol, double value){
		return setCellValue(getCell(s, cellRow, cellCol), value);
	}
	protected HSSFCell setCellValue(HSSFSheet s, int cellRow, short cellCol, String value){
		return setCellValue(getCell(s, cellRow, cellCol), value);
	}
	protected HSSFCell setCellFormula(HSSFSheet s, int cellRow, short cellCol, String formula){
		return setCellFormula(getCell(s, cellRow, cellCol), formula);
	}
	
	protected HSSFCell setCellValue(HSSFCell cell, double value){
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		
		return cell;
	}
	protected HSSFCell setCellValue(HSSFCell cell, String value){
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		//System.out.println("setCellValue=" + value);
		cell.setCellValue( new HSSFRichTextString( value.toString() ) );
		
		return cell;
	}
	protected HSSFCell setCellFormula(HSSFCell cell, String fomula){
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula( fomula );
		
		return cell;
	}

	
	@SuppressWarnings("rawtypes")
	protected ArrayList<String> addHeader(HSSFSheet s, List list, LinkedHashMap<String, String> fieldInfo){
		HSSFRow r = null;
		HSSFCell c = null;
		Map mRow = null;
		Set keySet = null;
		String sKey = "";
		String sLabel = "";
		ArrayList<String> keyList = null;
		int i = 0;
		boolean useFieldInfo = fieldInfo != null;
		HSSFCellStyle style = null;
		
		if (list == null || list.size() < 1){
			return null;
		}
		
		style = getBorderStyle();
		
		keyList = new ArrayList<String>();
		mRow = (useFieldInfo)? fieldInfo : (Map)list.get(0);
		keySet = mRow.keySet();
		r = s.createRow(0);
		
		for(Object oKey : keySet){
			sKey = oKey.toString();
			c = r.createCell(i);
			sLabel = (useFieldInfo) ? mRow.get(sKey).toString() : sKey;
			
			c.setCellStyle(style);
			c.setCellValue( new HSSFRichTextString( sLabel ) );
			keyList.add( sKey );
			i++;
		}
		
		return keyList;
	}
	@SuppressWarnings("rawtypes")
	protected void setDataToSheet(HSSFSheet s, List list, LinkedHashMap<String, String> fieldInfo){
		setDataToSheet(s, list, 1, fieldInfo);
	}
	@SuppressWarnings("rawtypes")
	protected void setDataToSheet(HSSFSheet s, List list, int start, LinkedHashMap<String, String> fieldNames){
		setDataToSheet(s, list, start, fieldNames, null);
	}
	//
	@SuppressWarnings("rawtypes")
	protected void setDataToSheet(HSSFSheet s, List list, int start, ArrayList<String> keyList, Map<String, Integer> fieldTypes){
		setDataToSheet(s, list, start, keyList.toArray(new String[]{}), fieldTypes);
	}
	@SuppressWarnings("rawtypes")
	protected void setDataToSheet(HSSFSheet s, List list, int start, String[] keyList, Map<String, Integer> fieldTypes){
		HSSFRow r = null;
		HSSFCell c = null;
		Map mRow = null;
		int iRow = start;
		int iCol = 0;
		int iCellType = HSSFCell.CELL_TYPE_STRING;
		HSSFCellStyle style = null;
		
		if (keyList == null){
			return;
		}
		
		style = getBorderStyle();
		
		System.out.println("setDataToSheet=" + list.size() + ", " + keyList);
				
		for(Object oRow : list){
			r = s.createRow(iRow);
			mRow = (Map)oRow;
			iCol = 0;
			
			for(String key : keyList){
				c = r.createCell(iCol);
				c.setCellStyle(style);
				
				if (fieldTypes != null){
					iCellType = fieldTypes.get(key);
				}
				else{
					iCellType = HSSFCell.CELL_TYPE_STRING;
				}
				
				try{
					setCellValue(c, iCellType, mRow.get( key ));
				}
				catch(Exception ex){
					System.out.println(ex);
					
					c.setCellValue( new HSSFRichTextString( "" ) );
				}
				
				iCol++;
			}
			
			iRow++;
		}
	}
	@SuppressWarnings("rawtypes")
	protected void setDataToSheet(HSSFSheet s, List list, int start, LinkedHashMap<String, String> fieldNames, Map<String, Integer> fieldTypes){
		ArrayList<String> keyList = null;
		
		if (fieldNames != null){
			keyList = addHeader(s, list, fieldNames);
		}
		else{
			keyList = extKeyList(list);
		}
		
		setDataToSheet(s, list, start, keyList, fieldTypes);
	}
	
	@SuppressWarnings("rawtypes")
	protected ArrayList<String> extKeyList(List list){
		ArrayList<String> keyList = new ArrayList<String>();
		Set keySet = null;
		
		try{
			keySet = ((Map)list.get(0)).keySet();
			
			for(Object oKey : keySet){
				keyList.add(oKey.toString());
			}
			
			return keyList;
		}
		catch(Exception ex){
			return null;
		}
	}

	public void empty() {
		wb = null;
		borderStyle = null;
	}
}
