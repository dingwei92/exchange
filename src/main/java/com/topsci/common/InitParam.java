package com.topsci.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author yanhan
 * 初始化类，包含所有初始化方法,静态变量
 */
public class InitParam {
	
	
	private final Logger logger = LoggerFactory.getLogger(InitParam.class);

	/**
	 * 初始化配置文件，并验证必要参数
	 * @throws Exception 
	 */
	public boolean initConfig(){
		return true;

	}
	
	/**
	  * @author yanhan hyan@cm-topsci.com
	  * 验证数据库是否正常，如果正确的返回true
	 */
	public boolean checkDB(){
		/*boolean flag = false;
		try {
			String str = DBTools.getString("select 1 from dual");
			if(str!=null&&"1".equals(str)){
				flag = true;
			}
		} catch (Exception e) {
			logger.error("数据库连接失败！请检查配置信息或者DB服务器！",e);
		}*/
		return true;
	}
	
	/**
	 * 因为schema文件使用频繁(接受到的xml都需要验证)，所有需要缓存
	 */
//	public boolean initSchema(){
//		boolean flag = true;
//		byte[] str = null;
//		StringBuffer propertiesFile = new StringBuffer();
//		propertiesFile.append(this.getClass().getClassLoader().getResource("ReceiveSchema.xsd").getPath());
//		
//		try {
//			str = FileUtil.getByteByFile(propertiesFile.toString());
//			if(str==null){
//				flag = false;
//			}else{
//				CatchAbout.schemaCache.put(Common.SCHEMA_RECEIVE,str);//放入map缓存
//			}
//		} catch (Exception e) {
//			flag = false;
//			logger.error("缓存Schema文件时出错！",e);
//			e.printStackTrace();
//		}
//		return flag;
//	}
	
    
 
    
}
