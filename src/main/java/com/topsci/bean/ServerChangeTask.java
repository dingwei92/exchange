package com.topsci.bean;

import java.io.Serializable;

/**
 * 服务变更日志表，增量记录，一条变更记录一条日志
 * @author yanhan
 *
 */
public class ServerChangeTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791578045434979392L;

	private int id;
	
	/**
	 * 变更类型： C：发布  S：订阅
	 */
	private String change_type;
	
	/**
	 *  变更对应的数据ID
		若CHANGE_TYPE='C' 则对应SERVER_CATALOG表的ID
		若CHANGE_TYPE='S' 则对应SERVER_SUBSCRIBES表的ID
	 */
	private int server_change_id;
	
	/**
	 *  修改动作：
		A：新增  
		U：修改
		D：删除
	 */
	private String deal_state;
	
	/**
	 * 数据更新时间，格式：yyyy-HH-mm hh:mm:ss
	 */
	private String lastdate;
	
	/**
	 * N：没有处理，  Y：已经处理
	 */
	private String status;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}

	public int getServer_change_id() {
		return server_change_id;
	}

	public void setServer_change_id(int server_change_id) {
		this.server_change_id = server_change_id;
	}

	public String getDeal_state() {
		return deal_state;
	}

	public void setDeal_state(String deal_state) {
		this.deal_state = deal_state;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
