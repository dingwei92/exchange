package com.topsci.bean;

import java.io.Serializable;

/**
 * 订阅方主动向服务方获取消息记录表，发布和订阅一样，需要做归档，由task来做
 * @author yanhan
 *
 */
public class SubscribesCataLogMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1597133900861374656L;

	private int id;
	
	/**
	 * 订阅系统的ID，对应BUSINESS_SYSTEM的ID
	 */
	private int business_system_id;
	
	/**
	 * 需要索取的服务ID，对应SERVER_CATALOG的ID
	 */
	private int server_catalog_id;
	
	/**
	 * 对应的消息内容，对应SERVER_MESSAGE_DETAIL的ID字段
	 */
	private int server_message_detail_id;
	
	/**
	 * 处理状态：  Y：已处理  N：未处理
	 */
	private String status;
	
	private String lastdate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBusiness_system_id() {
		return business_system_id;
	}

	public void setBusiness_system_id(int business_system_id) {
		this.business_system_id = business_system_id;
	}

	public int getServer_catalog_id() {
		return server_catalog_id;
	}

	public void setServer_catalog_id(int server_catalog_id) {
		this.server_catalog_id = server_catalog_id;
	}

	public int getServer_message_detail_id() {
		return server_message_detail_id;
	}

	public void setServer_message_detail_id(int server_message_detail_id) {
		this.server_message_detail_id = server_message_detail_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	
	
	
}
