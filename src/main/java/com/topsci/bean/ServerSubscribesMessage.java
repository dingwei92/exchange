package com.topsci.bean;

import java.io.Serializable;

/**
 * 分发数据记录表，记录推送的数据明细，此表也要定时归档，又task模块来做
 * @author yanhan
 *
 */
public class ServerSubscribesMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2811290935167608194L;

	
	private int id;
	
	/**
	 * 系统ID，对应BUSINESS_SYSTEM的ID字段
	 */
	private int business_system_id;
	
	/**
	 * 关联订阅表id
	 */
	private int server_subscribes_id;
	
	/**
	 * 关联数据表id
	 */
	private String server_message_detail_id;
	
	/**
	 * 处理状态 Y：成功   N：失败
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

	public int getServer_subscribes_id() {
		return server_subscribes_id;
	}

	public void setServer_subscribes_id(int server_subscribes_id) {
		this.server_subscribes_id = server_subscribes_id;
	}

	public String getServer_message_detail_id() {
		return server_message_detail_id;
	}

	public void setServer_message_detail_id(String server_message_detail_id) {
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
