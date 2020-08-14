package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 订阅明细表
 * @author yanhan
 *
 */
@Document
public class ServerSubscribesList{


	@Id
	private int id;
	
	/**
	 * 关联订阅表id
	 */
	private int serverSubscribesId;
	
	/**
	 * 关联分发服务详细表id
	 */
	private int serverCatalogListId;
	
	/**
	 * 数据更新时间：yyyy-MM-dd hh:mm:ss
	 */
	private String lastdate;

	/**
	 * 筛选条件类型，1为等于，2为范围，3为多个
	 */
	private int conType;

	/**
	 * 筛选条件值 逗号分隔的形式
	 */
	private int conValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerSubscribesId() {
		return serverSubscribesId;
	}

	public void setServerSubscribesId(int serverSubscribesId) {
		this.serverSubscribesId = serverSubscribesId;
	}

	public int getServerCatalogListId() {
		return serverCatalogListId;
	}

	public void setServerCatalogListId(int serverCatalogListId) {
		this.serverCatalogListId = serverCatalogListId;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	public int getConType() {
		return conType;
	}

	public void setConType(int conType) {
		this.conType = conType;
	}

	public int getConValue() {
		return conValue;
	}

	public void setConValue(int conValue) {
		this.conValue = conValue;
	}
}
