package com.topsci.db.dao;

import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.KafkaTopic;

public class KafKaTopicDao {
	
	private static final Logger logger = LoggerFactory.getLogger(KafKaTopicDao.class);
	
	/**
	 * 根据kafka_topic的id获取 kafkatopicBean对象
	 */
	public KafkaTopic getKafkaTopicBeanByID(int id){
		KafkaTopic kafkaTopicBean = null;
		try {
			String[] keys = {"id"};
			Object[] vals = {id};
			kafkaTopicBean = (KafkaTopic) MongodbUtils.findOne(KafkaTopic.class,keys,vals);
		} catch (Exception e) {
			logger.error("根据kafka_topic的id，查询kafkatopicBean对象出错！",e);
			e.printStackTrace();
		}
		return kafkaTopicBean;
	}
}
