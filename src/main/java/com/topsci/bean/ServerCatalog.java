package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author yanhan
 * 数据服务目录表，记录对外提供的数据服务目录
 */
@Document
public class ServerCatalog {
	@Id
	private int id;
	//("服务名称")
	private String serverName;
	//("例子")
	private String examples;
	//("是否指定系统 N  Y")
	private String specifySystem;
	//("指定系统ID")
	private String specifySystemIdList;
	//("更新时间")
	private String upddatetime;
	//("备注")
	private String remark;
	//("服务简称")
	private String serverShort;
	//("删除 N 否 Y是")
	private String deleted;
	//("业务系统ID")
	private int businessSystemId;
	private String docPath;
	//("模板id")
	private String templateId;
	//("推送类型 接口 1 ")
	private String publishType;
	//(hidden = false)
	private String dbId;
	
	/**
	 * 发布服务对应的字段集合
	 */
	@Transient
	private List<ServerCatalogList> serverCatalogList;
	
	/**
	 * 服务对应的业务系统对象 
	 */
	@Transient
	private BusinessSystem businessSystemBean;

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getBusinessSystemId() {
		return businessSystemId;
	}

	public void setBusinessSystemId(int businessSystemId) {
		this.businessSystemId = businessSystemId;
	}

	public String getExamples() {
		return examples;
	}

	public void setExamples(String examples) {
		this.examples = examples;
	}

	public String getSpecifySystem() {
		return specifySystem;
	}

	public void setSpecifySystem(String specifySystem) {
		this.specifySystem = specifySystem;
	}

	public String getSpecifySystemIdList() {
		return specifySystemIdList;
	}

	public void setSpecifySystemIdList(String specifySystemIdList) {
		this.specifySystemIdList = specifySystemIdList;
	}

	public String getUpddatetime() {
		return upddatetime;
	}

	public void setUpddatetime(String upddatetime) {
		this.upddatetime = upddatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getServerShort() {
		return serverShort;
	}

	public void setServerShort(String serverShort) {
		this.serverShort = serverShort;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getPublishType() {
		return publishType;
	}

	public void setPublishType(String publishType) {
		this.publishType = publishType;
	}


}
