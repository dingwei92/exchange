package com.topsci.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * String字符串处理工具
 * @author CM-TOSCI
 *
 */
public class StringUtil {
	
	/**
	 * 验证是否是空字符串
	 * @param str 要验证的字符串
	 * @return 如果该字符串为NULL则返回空字符串""，否则返回原字符串
	 */
	public static String formatNull(String str){
		return (str == null) ? "" : str;
	}
	
	/**
	 * 验证是否是为空
	 * @param str 要验证的字符串
	 * @return 如果是空，返回true
	 */
	public static boolean isNull(String str){
		return !(str != null && str.length() > 0 && !str.equals("null"));
	}
	
    /**
     * 根据正则表达式验证字符串是否合法
     * @param resEx
     * @param str
     * @return
     */
    public static boolean find(String resEx,String str){
    	return Pattern.compile(resEx).matcher(str).find();
    }
    
    /**
     * 处理手机号码，将手机号码变为没有符号的格式
     * @param phone
     * @return
     */
	public static String phoneUtl(String phone){
		String result = "";
		if (phone == null || phone.length()==0) {
			return result;
		}
		result = phone.replace("-", "");
		result = result.replace(" ", "");
		if (result.length()>11) {
			result = result.substring(result.length()-11);
		}
		return result;
	}

	
	/**
	 * 返回上传手机号、数组
	 * @param obj
	 * @param string
	 * @return
	 */
//	public static String[] accContactsParas(JSONObject info, String key) {
//		String value = "";
//		String values[] = null;
//		if (info.containsKey(key)) {
//			value =info.getString(key) == null?"":info.getString(key);
//			value = value.substring(value.indexOf("[")+1,value.lastIndexOf("]"));
//			String []values1 =value.split(",");
//			if (values1 != null && values1.length>0) {
//				values = new String [values1.length];
//				for (int i = 0; i < values1.length; i++) {
//					String val = values1[i];
//					if (val != null && val.length()>0 && (!"null".equalsIgnoreCase(val))) {
//						values[i]=val.substring(val.indexOf("\"")+1,val.lastIndexOf("\""));
//					}
//					
//				}
//				
//			}
//		}
//		return values;
//	}
	
	/**
	 * 把一个String数组用","连接成一个字符串，元素用‘’引起<br>
	 * 例如传入new String[] { "aaa", "bbb", "ccc" }，则返回'aaa','bbb','ccc'
	 * @param strings String数组
	 * @return 连接后的字符串
	 */
	public static String connect2(Object[] strings)
	{
		if (strings.length < 1)
		{
			return "";
		}
		
		StringBuffer str = new StringBuffer();
		str.append("'"+strings[0]+"'");

		for (int i = 1; i < strings.length; i++)
		{
			str.append(",").append("'"+strings[i]+"'");
		}

		return str.toString();
	}

	public static String connect3(Object[] strings)
	{
		if (strings.length < 1)
		{
			return "";
		}

		StringBuffer str = new StringBuffer();
		str.append(strings[0]);

		for (int i = 1; i < strings.length; i++)
		{
			str.append(",").append(strings[i]);
		}

		return str.toString();
	}


	/** 
     * 把中文转成Unicode码 
     * @param str 
     * @return 
     */  
    public static String chinaToUnicode(String str){  
        String result="";  
        for (int i = 0; i < str.length(); i++){  
            int chr1 = (char) str.charAt(i);  
            if(chr1 >= 19968 && chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
                result+="\\u" + Integer.toHexString(chr1);  
            }else{  
                result+=str.charAt(i);  
            }  
        }  
        return result;  
    }  
  
    /** 
     * 判断是否为中文字符 
     * @param c 
     * @return 
     */  
    public static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
    
    
    public static String string2Unicode(String s) {  
        try {  
          StringBuffer out = new StringBuffer("");  
          byte[] bytes = s.getBytes("unicode");  
          for (int i = 2; i < bytes.length - 1; i += 2) {  
            out.append("u");  
            String str = Integer.toHexString(bytes[i + 1] & 0xff);  
            for (int j = str.length(); j < 2; j++) {  
              out.append("0");  
            }  
            String str1 = Integer.toHexString(bytes[i] & 0xff);  
      
            out.append(str);  
            out.append(str1);  
            out.append(" ");  
          }  
          return out.toString().toUpperCase();  
        }  
        catch (UnsupportedEncodingException e) {  
          e.printStackTrace();  
          return null;  
        }  
      }   
      
    public static String unicode2String(String unicodeStr){  
        StringBuffer sb = new StringBuffer();  
        String str[] = unicodeStr.toUpperCase().split("U");  
        for(int i=0;i<str.length;i++){  
          if(str[i].equals("")) continue;  
          char c = (char)Integer.parseInt(str[i].trim(),16);  
          sb.append(c);  
        }  
        return sb.toString();  
      }  
    
    public static boolean isNumber(String str){
    	 boolean flag = false;
    	 try {
    		 Pattern pattern = Pattern.compile("[0-9]*");  
        	 flag = pattern.matcher(str).matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	  return flag;
    }

	public static int compareNumberString(String s1,String s2)
	{
		int t = 0;
		if(s1.length() != s2.length())
		{
			return s1.length() - s2.length();
		}
		else
		{
			for(int i = 0 ; i < s1.length();i++)
			{
				t = s1.charAt(i)-s2.charAt(i);
				if( t != 0)
				{
					return t;
				}
			}
		}
		return t;
	}
    
    /**
     * 获取uuid
     */
    public static String getUUID(){
    	return UUID.randomUUID().toString().replace("-","");
    }
}
