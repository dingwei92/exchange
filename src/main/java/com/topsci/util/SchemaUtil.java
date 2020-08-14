package com.topsci.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SchemaUtil {
	
	
	/**
	 * 根据xml，schema path 验证，验证失败，抛出异常
	 */
	public static void checkXml(String xmlpath,String schemapath) throws SAXException, IOException{
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			
			Schema schema = schemaFactory.newSchema(new File(schemapath));
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new ErrorHandler() {
	 
				public void warning(SAXParseException exception)
						throws SAXException {
					System.out.println("警告：" + exception);
					throw exception;
				}
				public void fatalError(SAXParseException exception)
						throws SAXException {
					System.out.println("致命：" + exception);
					throw exception;
				}
				public void error(SAXParseException exception) throws SAXException {
					System.out.println("错误：" + exception);
					throw exception;
				}
			});
			validator.validate(new StreamSource(new File(xmlpath)));
	}
	
	
	
	/**
	 * 根据xml 字节数组，schema 二进制  验证，验证失败，抛出异常
	 * schemabody ：schema文件字节数组
	 */
	public static void checkXml(byte[] xml,byte[] schemabody) throws SAXException, IOException{
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new StreamSource(new ByteArrayInputStream(schemabody)));
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new ErrorHandler() {
	 
				public void warning(SAXParseException exception)
						throws SAXException {
					System.out.println("警告：" + exception);
					throw exception;
				}
				public void fatalError(SAXParseException exception)
						throws SAXException {
					System.out.println("致命：" + exception);
					throw exception;
				}
				public void error(SAXParseException exception) throws SAXException {
					System.out.println("错误：" + exception);
					throw exception;
				}
			});
			
			validator.validate(new StreamSource(new ByteArrayInputStream(xml)));
	}
	
	
	/**
	 * 处理业务系统发布的xml
	 * 
	 * 暂时不用schema验证
	 */
//	private boolean checkXmlBySchema(){
//		
//		/**
//		 * schema 验证xml格式,如果有异常，说明验证失败
//		 */
//		try {
//			SchemaUtil.checkXml(getMessage().getBytes(), Common.SCHEMA_RECEIVE);
//		} catch (Exception e) {
//			logger.error("发布的xml验证格式失败！",e);
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}

}
