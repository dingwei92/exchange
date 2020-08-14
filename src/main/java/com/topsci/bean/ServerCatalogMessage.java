package com.topsci.bean;

import java.io.Serializable;

/**
 * 
 * @author yanhan
 * 业务系统发布数据记录bean
 */
public class ServerCatalogMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8835299964630661736L;

	/**
	 * 主键，自增
	 */
	private int id;
	
	/**
	 * 对应的发布的业务系统ID
	 */
	private int business_system_id;
	
	/**
	 * 对应的发布的服务ID
	 */
	private int server_catalog_id;
	
	/**
	 * 关联数据详细表id
	 */
	private String server_message_detail_id;
	
	/**
	 * 处理状态：Y：已处理    N：未处理   E：处理失败
	 */
	private String status;
	
	/**
	 * 数据更新时间：yyyy-MM-dd hh:mm:ss
	 */
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
