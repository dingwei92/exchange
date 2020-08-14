package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 订阅表
 * @author yanhan
 *
 */
@Document
public class ServerSubscribes{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2047347190289157376L;
	@Id
	private int id;
	
	/**
	 * 订阅服务的业务系统id
	 */
	private int businessSystemId;
	
	/**
	 * 订阅服务对应 的id
	 */
	private int serverCatalogId;
	
	/**
	 * 出事订阅是否需要全量下发数据： Y：是   N：否   默认：N
	 */
	private String isFull;
	
	/**
	 * 当is_full 为Y时，全量获取， 数据起始时间及其他参数
	 */
	private String startTime;
	private String endTime;

	/**
	 * 数据更新时间：yyyy-MM-dd hh:mm:ss
	 */
	private String lastdate;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 删除标志  'Y'删除 ， 'N' 正常
	 */
	private String deleted;

	private String parameter;

	/**
	 * 启用(Y)、禁用(N)标志
	 */
	private String enables;
	
	
	
	/**
	 * 该订阅对应的订阅字段详细集合
	 */
	private List<ServerCatalogList> serverCatalogList;
	
	
	
	/**
	 * 订阅服务的业务系统bean 
	 */
	private BusinessSystem businessSystemBean;
	
	/**
	 * 
	 * 订阅分发服务bean
	 */
	private ServerCatalog serverCatalogBean;

	public String getEnables() {
		return enables;
	}

	public void setEnables(String enables) {
		this.enables = enables;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBusinessSystemId() {
		return businessSystemId;
	}

	public void setBusinessSystemId(int businessSystemId) {
		this.businessSystemId = businessSystemId;
	}

	public int getServerCatalogId() {
		return serverCatalogId;
	}

	public void setServerCatalogId(int serverCatalogId) {
		this.serverCatalogId = serverCatalogId;
	}

	public String getIsFull() {
		return isFull;
	}

	public void setIsFull(String isFull) {
		this.isFull = isFull;
	}

	

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ServerCatalogList> getServerCatalogList() {
		return serverCatalogList;
	}

	public void setServerCatalogList(List<ServerCatalogList> serverCatalogList) {
		this.serverCatalogList = serverCatalogList;
	}


	public BusinessSystem getBusinessSystemBean() {
		return businessSystemBean;
	}

	public void setBusinessSystemBean(BusinessSystem businessSystemBean) {
		this.businessSystemBean = businessSystemBean;
	}

	public ServerCatalog getServerCatalogBean() {
		return serverCatalogBean;
	}

	public void setServerCatalogBean(ServerCatalog serverCatalogBean) {
		this.serverCatalogBean = serverCatalogBean;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
