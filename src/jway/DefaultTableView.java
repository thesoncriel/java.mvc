package com.jway;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class DefaultTableView extends BaseView implements IEmptiable {
	private List<Map<String,String>> data;
	private Map<String, String> param;
	private LinkedHashMap<String,String > fields;
	
	public DefaultTableView setParam(Map<String, String> param){
		this.param = param;
		
		return this;
	}
	
	public DefaultTableView setField(LinkedHashMap<String,String > fields){
		this.fields = fields;
		
		return this;
	}
	
	public DefaultTableView setData(List<Map<String,String>> data){
		this.data = data;
		
		return this;
	}
	
	public boolean render(OutputStream out){
		StringBuilder sb = new StringBuilder();
		Map<String, String> row;
		Set<String> keySet;
		Set<String> paramKeySet = (param != null)? param.keySet() : new LinkedHashSet<String>();
		
		if (data == null || data.size() == 0){
			if (fields == null || fields.size() == 0){
				return false;
			}
		}
		
		if (fields == null){
			row = data.get(0);
		}
		else{
			row = fields;
		}
		
		keySet = row.keySet();
		
		String sTmpl = "" +
				" <!DOCTYPE html> " +
				" <html lang=\"en\"> " +
				" <head> " +
				"     <meta charset=\"UTF-8\"> " +
				//"     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=euc-kr\"> " +
				"     <title>Default Table View</title> " +
				"     <style> " +
				" table,th,td{border: 1px solid #333;border-spacing:0;border-collapse:collapse;} th{font-weight:bold;background:#ff0;}" +
				"     </style> " +
				" </head> " +
				" <body> " +
				"     <h1>Parameters Info </h1> " +
				"     <form>" +
				"     <table> " +
				"         <tbody> ";
		sb.append(sTmpl);
		
				//"                 <th></th> "
				//"                 <td></td> " +
		
		for(String key : paramKeySet){
			sb.append("<tr>")
			.append("<th>")
			.append(key)
			.append("</th>")
			.append("<td>")
			.append("<input name=\"").append(key).append("\" value=\"").append(param.get(key)).append("\">")
			.append("</td>")
			.append("</tr>");
		}
		
		sTmpl =	"         </tbody> " +
				"     </table> " +
				"     <button type=\"submit\">Submit</button>" +
				"     </form>" +
				"     <h1>Result</h1> " +
				"     <table> " +
				"         <thead> " +
				"             <tr> ";
		sb.append(sTmpl);
				//"                 <th></th> " +
		
		for(String key : keySet){
			sb.append("<th>")
			.append(key)
			.append("</th>")
			;
		}
		
		sTmpl = "             </tr> " +
				"         </thead> " +
				"         <tbody> ";
		sb.append(sTmpl);
				//"                 <td></td> " +
		
		for(Map<String, String> mRow : data){
			sb.append("<tr>");
			
			for(String key : keySet){
				sb.append("<td>")
				.append(mRow.get(key))
				.append("</td>")
				;
			}
			
			sb.append("</tr>");
		}
		if (data == null || data.size() == 0){
			sb.append("<td colspan=\"").append(keySet.size()).append("\">")
			.append("해당 자료 없음.")
			.append("</td>")
			;
		}
		
		sTmpl = "         </tbody> " +
				"     </table> " +
				" </body> " +
				" </html> ";
		sb.append(sTmpl);
		
		try {
			out.write(sb.toString().getBytes("UTF-8"));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void empty() {
		data = null;
		param = null;
	}
}
