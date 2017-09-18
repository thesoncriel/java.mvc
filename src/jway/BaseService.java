package com.jway;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseService extends Util implements IEmptiable{
	/**
	 * SMS 전달 시 보내는 기본 번호로 쓰인다. 
	 */
	public static String BASE_SENDER = "15661505";
	public static String SMS_SERVICE_URL = "http://61.105.116.40:8080/sms/sms_exec.jsp";
	private List<String> err;
	
	public boolean moveParameterFile(Map<String, File> param, String key, String path){
		return moveFile(param.get(key), path);
	}
	
	public boolean moveFile(String src, String dest){
		return moveFile(new File(src), dest);
	}
	
	public boolean moveFile(File src, String dest){
		return moveFile(src, new File(dest));
	}
		
	public boolean moveFile(File src, File dest){
		File fileDest = dest;
		
		return src.renameTo(fileDest);
	}
	
	public String appendRandomSequenceToFileName(String name){
		long lTimeStamp = System.currentTimeMillis();
		String sRename = name.replace(".", "." + lTimeStamp + ".");
		
		return sRename;
	}
	
	public boolean copyFile(String src, String dest){
		return copyFile(new File(src), dest);
	}
	public boolean copyFile(File src, String dest){
		return copyFile(src, new File(dest));
	}
	// TODO: 언젠간 기존 이미지 있으면 다른 이미지 이름으로 대체할 수 있는 걸로 바꾸기...
	public boolean copyFile(File src, File dest){
		try{
			FileInputStream fis = new FileInputStream(src);
			FileOutputStream fos = new FileOutputStream(dest);
			int data = 0;
			
			if (dest.length() > 1024){
				//System.out.println("copyFile: exists - " + dest.getName());
				moveFile(dest, appendRandomSequenceToFileName(dest.getAbsolutePath()));
			}
			
			while((data = fis.read()) != -1){
				fos.write(data);
			}
			fis.close();
			fos.close();
			
			return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean writeFileToStream(OutputStream outs, String filepath){
		try {
			File file = new File(filepath);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int read = 0;
			
			while((read = bis.read()) != -1){
				outs.write(read);
			}
			
			outs.close();
			bis.close();
			
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
//	public boolean resizeJpgImage(File file, int width, int height){
//		try{
//			BufferedImage imgSrc = ImageIO.read(file);
//			BufferedImage imgDest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//			Graphics2D g = imgDest.createGraphics();
//			AffineTransform at = AffineTransform.getScaleInstance((double), sy)
//		}
//		catch(Exception e){
//			
//		}
//		
//		return false;
//	}
	
	public List<String> getErr(){
		if (this.err == null){
			this.err = new ArrayList<String>();
		}
		
		return this.err;
	}
	
	public void addErr(String msg){
		getErr().add(msg);
	}
	public void addErr(List<String> list){
		getErr().addAll(list);
	}
	
	public boolean hasErr(){
		return getErr().isEmpty() == false;
	}
	
	public String serializeParameter(Map<String, String> param, String... keys){
		StringBuilder sb = new StringBuilder();
		
		for(String key : keys){
			if (sb.length() > 0){
				sb.append("&");
			}
			sb.append( param.get(key) );
		}
		
		return sb.toString();
	}
	
	public int remoteHttpCall(String url){
		return remoteHttpCall(url, 10);
	}
	
	public int remoteHttpCall(Map<String, String> param, String... keys){
		int iResCode = 0;
		
		try{
		    int iTimeout = -1;
		    StringBuilder sbUrl = null;
		    String sUrl = "";
		    String sParam = "";
		    StringBuilder sbMsg = new StringBuilder();
		    
		    if (param.containsKey("url") == false){
		    	sbMsg.append("BaseService.remoteHttpCall====>");
		    	sbMsg.append("parameter: [url] is required. ");
		    	
		    	throw new Exception(sbMsg.toString());
		    }
		    
		    sbUrl = new StringBuilder();
		    sbUrl.append( param.get("url") );
		    sParam = serializeParameter(param, keys);
		    
		    if (sParam.length() > 0){
		    	sbUrl.append("?").append(sParam);
		    }
		    
		    sUrl = sbUrl.toString();
		    iTimeout = tryParamInt(param, "timeout", -1);
		    
		    if (iTimeout < 0){
		    	iResCode = remoteHttpCall(sUrl);
		    }
		    else{
		    	iResCode = remoteHttpCall(sUrl, iTimeout);
		    }
		    
		    param.put("__req_url", sUrl);
		    param.put("__res_code", iResCode + "");
		}
		catch(Exception ex){
			addErr(ex.getMessage());
			ex.printStackTrace();
		}
		
		return iResCode;
	}
	
	public int remoteHttpCall(String url, Object timeoutSec){
		try{
			return remoteHttpCall(url, Integer.parseInt( timeoutSec.toString() ));
		}
		catch(Exception ex){}
		
		return remoteHttpCall(url);
	}
	
	public int remoteHttpCall(String sUrl, int timeoutSec){
	    URL url = null;
	    int iResCode = 0;
	    int iTimeout = timeoutSec;
	    HttpURLConnection connection = null;
	    
	    try{
	        url = new URL(sUrl);
	        connection = (HttpURLConnection)url.openConnection();
	        connection.setConnectTimeout( iTimeout );
	        
	        connection.setRequestMethod("GET");
	        connection.connect();
	        iResCode = connection.getResponseCode();
	        /*
	        InputStream in = url.openStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder result = new StringBuilder();
	        String line;
	        
	        while((line = reader.readLine()) != null) {
	            result.append(line);
	            break;
	        }
	        
	        reader.close();
	        in.close();
	        */
	    }
	    catch(Exception ex){
	    	addErr(ex.toString());
	    }
	    
	    return iResCode;
	}
	public StringBuilder remoteHttpGet(String sUrl, int timeoutSec){
		return remoteHttp("GET", sUrl, "", timeoutSec);
	}
	
	public StringBuilder remoteHttp(String method, String sUrl, Map<String,String> params, int timeoutSec) throws UnsupportedEncodingException{
		StringBuilder sbParams = new StringBuilder();
		//Iterator<String> keys = params.keySet().iterator();
		//String key = "";
		
		//System.out.println("remoteHttp ::: params serialize - " + params.size());
		
		if (params != null){
			//System.out.println("params is not null");
			
//			while(keys.hasNext()){
//				if (sbParams.length() > 0){
//					sbParams.append('&');
//				}
//				
//				key = keys.next();
//				
//				//System.out.println("param key = " + key);
//				
//				sbParams.append(key).append('=')
//				.append( params.get(key) );
//			}
			
			for(String key : params.keySet() ){
				if (sbParams.length() > 0){
					sbParams.append('&');
				}
				
				System.out.println("param key = " + key);
				
				sbParams.append(key).append('=')
				.append( URLEncoder.encode(params.get(key), "UTF-8") );
			}
			
			
			//System.out.println(sbParams);
		}
		
		return remoteHttp(method, sUrl, sbParams.toString(), timeoutSec);
	}
	
	public StringBuilder remoteHttp(String method, String sUrl, String params, int timeoutSec){
		return remoteHttp(method, sUrl, params, timeoutSec, true);
	}
	
	public StringBuilder remoteHttp(String method, String sUrl, String params, int timeoutSec, boolean useUTF8){
		StringBuilder sb = new StringBuilder();
		URL url = null;
	    int iTimeout = timeoutSec;
	    HttpURLConnection connection = null;
	    int iResCode = 0;
	    String sUrlAll = sUrl;
	    DataOutputStream dos = null;
	    
	    System.out.println("BaseService.remoteHttp [begin]");
	    
	    try{
	    	System.out.println("Connect to " + sUrl);
	    	System.out.println("Parameters: " + params);
	    	
	    	if ("GET".equals(method)){
	    		sUrlAll += (params != null && params.length() >= 3)? "?" + params : "";
	    	}
	    	
	        url = new URL(sUrlAll);
	        connection = (HttpURLConnection)url.openConnection();
	        connection.setConnectTimeout( iTimeout );
	        
	        connection.setRequestMethod(method);
	        
	        if ("POST".equals(method)){
	        	connection.setDoOutput(true);
	        	dos = new DataOutputStream(connection.getOutputStream());
	        	
	        	if (useUTF8){
	        		dos.writeUTF(params);
	        	}else{
	        		dos.writeBytes(params);
	        	}
	        	
	        	dos.flush();
	        	dos.close();
	        }
	        else{
	        	connection.connect();
	        }
	        
	        iResCode = connection.getResponseCode();
	        
	        System.out.println("Connection finish. res.code: " + iResCode);
	        
	        InputStream in = connection.getInputStream();
	        BufferedReader reader = null;
	        String line;
	        
//	        if (useUTF8){
//	        	reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//	        }
//	        else{
//	        	reader = new BufferedReader(new InputStreamReader(in));
//	        }
	        
	        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        
	        while((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        
	        System.out.println("Data Append finished. length=" + sb.length());
	        
	        reader.close();
	        in.close();
	        
	        System.out.println("Stream closed.");
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    	addErr(ex.toString());
	    }
	    
	    System.out.println("BaseService.remoteHttp [end]");
		
		return sb;
	}
	
	/**
	 * 
	 * <pre>
	 * SMS 메시지를 보낸다.
	 * 내부적으로 URLEncoder 를 통해 메시지를 URL 인코딩 해 준다.
	 * (즉 그냥 스트링 넣어주면 알아서 해 줍니당~)
	 * </pre>
	 * @param sender 보내는 사람 전화번호
	 * @param receiver 받는 사람 전화번호
	 * @param msg 보낼 메시지 내용
	 * @return 성공하면 true, 아니면 false.
	 */
	public boolean sendSMS(String sender, String receiver, String msg){
		StringBuilder sbRet = null;
		StringBuilder sbParams = new StringBuilder();
		
		//sParams += "&sender=15661505";
		//sParams += "&receiver=" + receiver;
		try {
			sbParams
			.append("mode=Send")
			.append("&sender=").append(sender)
			.append("&receiver=").append(receiver)
			//.append("&msg=").append( msg )
			.append("&msg=").append( URLEncoder.encode(msg, "UTF8") )
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		sbRet = remoteHttp("POST", SMS_SERVICE_URL, sbParams.toString(), 1000, false);
		
		return sbRet.indexOf("정상적으로 발송되었습니다") >= 0;
	}
	/**
	 * 
	 * <pre>
	 * SMS 메시지를 보낸다.
	 * 내부적으로 URLEncoder 를 통해 메시지를 URL 인코딩 해 준다.
	 * (즉 그냥 스트링 넣어주면 알아서 해 줍니당~)
	 * 보내는 번호를 {@link com.jway.BaseService.BASE_SENDER}로 이용한다
	 * </pre>
	 * @param receiver 받는 사람 전화번호
	 * @param msg 보낼 메시지 내용
	 * @return 성공하면 true, 아니면 false.
	 */
	public boolean sendSMS(String receiver, String msg){
		return sendSMS(BASE_SENDER, receiver, msg);
	}
	
	public void empty(){
		if (err != null){
			err.clear();
			err = null;
		}
	}
}
