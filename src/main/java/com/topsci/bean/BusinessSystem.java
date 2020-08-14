package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 
 * @author yanhan
 * 业务系统表
 */
@Document
public class BusinessSystem{

	/**
	 * 主键，自增
	 */
	@Id
	private int id;

	/**
	 * 业务系统名称
	 */
	private String systemName;
	
	/**
	 * 业务简称，字母
	 */
	private String systemShort;
	
	/**
	 *  该服务对应的kafka主题id，关联kafka记录表id
	 */
	private int kafkaTopicId;
	private String deleted;
	private LocalDateTime lastConnect;
	/**
	 * 服务对应的topic对象
	 */
	private KafkaTopic kafkaTopicBean;

	/**
	 * 备注
	 */
	private String remark;

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public LocalDateTime getLastConnect() {
		return lastConnect;
	}

	public void setLastConnect(LocalDateTime lastConnect) {
		this.lastConnect = lastConnect;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSystemName() {
		return systemName;
	}


	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}


	public String getSystemShort() {
		return systemShort;
	}


	public void setSystemShort(String systemShort) {
		this.systemShort = systemShort;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public int getKafkaTopicId() {
		return kafkaTopicId;
	}


	public void setKafkaTopicId(int kafkaTopicId) {
		this.kafkaTopicId = kafkaTopicId;
	}


	public KafkaTopic getKafkaTopicBean() {
		return kafkaTopicBean;
	}


	public void setKafkaTopicBean(KafkaTopic kafkaTopicBean) {
		this.kafkaTopicBean = kafkaTopicBean;
	}
	
	
}
