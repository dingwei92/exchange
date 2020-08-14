package com.topsci.db.dao;


import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.BusinessSystem;

import java.util.List;

@SuppressWarnings("unchecked")
public class BusinessSystemDao {
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessSystemDao.class);

	
	/**
	 * 根据业务系统id查询bean
	 */
	public BusinessSystem getBusinessSystemBeanByID(int id){
		BusinessSystem businessSystemBean = null;
		
		try {
			String[] params = {"id"};
			Object[] val = {id};
			businessSystemBean = (BusinessSystem)MongodbUtils.findOne(BusinessSystem.class,params,val);
		} catch (Exception e) {
			logger.error("根据业务系统id查询bean出错！",e);
			e.printStackTrace();
		}
		return businessSystemBean;
	}

	public List<BusinessSystem> getBusinessSystemBeans(){
		List<BusinessSystem> businessSystemBean = null;
		try {
			businessSystemBean = (List<BusinessSystem>)MongodbUtils.findAll(BusinessSystem.class);
		} catch (Exception e) {
			logger.error("根据业务系统id查询bean出错！",e);
			e.printStackTrace();
		}
		return businessSystemBean;
	}
}
