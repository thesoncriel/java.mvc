package com.jway;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.imnetpia.exbill.common.FileItem;
import com.imnetpia.exbill.common.FileUpload;
import com.imnetpia.exbill.common.FileUploadException;

public class ExserverRequestFile implements RequestFile {
	private HttpServletRequest request;
	private HashMap<String, File> data;

	public void empty() {
		request = null;
	}

	public RequestFile setRequest(HttpServletRequest request) {
		this.request = request;
		
		return this;
	}

	public Map<String, File> get(String... keys) {
		if (data == null){
			data = parse(request, keys);
		}
		
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected HashMap<String, File> parse(HttpServletRequest request, String... keys){
		FileUpload fu = new FileUpload();
		HashMap<String, File> mRet = new HashMap<String, File>();
		Hashtable mLegacy = null;
		Enumeration enumKeys = null;
		Object oKey = null;
		String sKey = "";
		FileItem item = null;
		File file = null;
		ArrayList listKey = null;
		
		try {
			fu.setSizeMax(1024 * 1024 * 50);
			mLegacy = fu.parseRequest(request);
			
			if (keys != null && keys.length > 0){
				Arrays.asList(keys);
				listKey = new ArrayList<String>( Arrays.asList(keys) );
				
				enumKeys = Collections.enumeration( listKey );
			}
			else{
				enumKeys = mLegacy.keys();
			}
			
			
			
			while(enumKeys.hasMoreElements()){
				oKey = enumKeys.nextElement();
				sKey = oKey.toString();
				System.out.println("parameterToFiles===>" + sKey);
				item = (FileItem)mLegacy.get(sKey);
				file = item.getStoreLocation();
				System.out.println("parameterToFiles - file? ===>" + file);
				mRet.put(sKey, file);
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mRet;
	}
}
