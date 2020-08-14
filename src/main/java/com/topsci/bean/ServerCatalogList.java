package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class ServerCatalogList {
	@Id
	private int id;
	private String serverElementName;
	private String serverElementDescribe;
	private String elementDescribe;
	private String upddatetime;
	private int serverCatalogId;
	private int keycol;
	private int dbkey;

	public int getDbkey() {
		return dbkey;
	}

	public void setDbkey(int dbkey) {
		this.dbkey = dbkey;
	}

	public int getKeycol() {
		return keycol;
	}

	public void setKeycol(int keycol) {
		this.keycol = keycol;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerCatalogId() {
		return serverCatalogId;
	}

	public void setServerCatalogId(int serverCatalogId) {
		this.serverCatalogId = serverCatalogId;
	}

	public String getServerElementName() {
		return serverElementName;
	}

	public void setServerElementName(String serverElementName) {
		this.serverElementName = serverElementName;
	}

	public String getServerElementDescribe() {
		return serverElementDescribe;
	}

	public void setServerElementDescribe(String serverElementDescribe) {
		this.serverElementDescribe = serverElementDescribe;
	}

	public String getElementDescribe() {
		return elementDescribe;
	}

	public void setElementDescribe(String elementDescribe) {
		this.elementDescribe = elementDescribe;
	}

	public String getUpddatetime() {
		return upddatetime;
	}

	public void setUpddatetime(String upddatetime) {
		this.upddatetime = upddatetime;
	}

	
	
	
}
