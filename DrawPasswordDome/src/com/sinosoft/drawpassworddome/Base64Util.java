package com.sinosoft.drawpassworddome;

import android.util.Base64;

public class Base64Util {
	  // 加密  
	/**
	 * 加密  
	 * @param str
	 * @return
	 */
	public static String Base64Code(String str){
    	byte[] buffer = str.getBytes();
    	Base64.encodeToString(buffer, Base64.DEFAULT).replaceAll("\n", "");
//		return Base64.encodeToString(buffer, Base64.DEFAULT);
    	return Base64.encodeToString(buffer, Base64.DEFAULT).replaceAll("\n", "");//去掉加密是\n转义字符
    }  
  
    // 解密  
	/**
	 *  解密  
	 * @param strBase64
	 * @return
	 */
    public static String getFromBase64(String strBase64) { 
    	byte[] b64=strBase64.getBytes();
    	String result= new String(Base64.decode(b64, Base64.DEFAULT));
        return result;  
    }  
}
