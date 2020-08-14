package com.topsci.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author yanhan
 * 对数据压缩工具类
 */
public class ZipUtil {
	private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

	// 压缩
	  public static String zip(String str){
	     String res = null;
		 if (str == null || str.length() == 0) {
	      return str;
	    }
	    try {
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		    GZIPOutputStream gzip = new GZIPOutputStream(out);
		    gzip.write(str.getBytes());
		    gzip.close();
		    res = out.toString("ISO-8859-1");
		    out.close();
		} catch (Exception e) {
			logger.error("zip压缩失败！",e);
			e.printStackTrace();
		}
	    return res;
	  }
	 
	  // 解压缩
	  public static String unZip(String str) {
		 String res = null;
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    try {
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		    ByteArrayInputStream in = new ByteArrayInputStream(str
		        .getBytes("ISO-8859-1"));
		    GZIPInputStream gunzip = new GZIPInputStream(in);
		    byte[] buffer = new byte[256];
		    int n;
		    while ((n = gunzip.read(buffer)) >= 0) {
		      out.write(buffer, 0, n);
		    }
		    res = out.toString("UTF-8"); //需要指定编码，不然jar执行时乱码

		    out.close();
		} catch (Exception e) {
			logger.error("zip解压缩失败！",e);
			e.printStackTrace();
		}
	    return res;
	  }

}
