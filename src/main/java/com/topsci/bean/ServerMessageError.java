package com.topsci.bean;

import java.io.Serializable;

/**
 * 
 * @author yanhan
 * 获取的错误xml数据bean
 */
public class ServerMessageError implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8605685108980444744L;
	
	private int id;
	/**
	 * 是否返回错误消息应答， Y：应答了， N ： 没有应答
	 */
	private String status;  
	
	/**
	 * 错误类型
	 */
	private String error_code;
	
	/**
	 * 接受xml的时间
	 */
	private String lastdate;
	
	/**
	 * 返回消息的 topic_name
	 */
	private String topic_name;

	private String msgdata;
	private String msgsn;
	private String msgsenttime;
	private String formsysid;
	private String tosysid;
	private String msgoid;
	private String msgotime;
	private String msgtype;
	private String msgnum;

	private int msgcount;
	
	private String sessionid;
	
	private String msgcode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getTopic_name() {
		return topic_name;
	}

	public void setTopic_name(String topic_name) {
		this.topic_name = topic_name;
	}

	public String getMsgdata() {
		return msgdata;
	}

	public void setMsgdata(String msgdata) {
		this.msgdata = msgdata;
	}

	public String getMsgsn() {
		return msgsn;
	}

	public void setMsgsn(String msgsn) {
		this.msgsn = msgsn;
	}

	public String getMsgsenttime() {
		return msgsenttime;
	}

	public void setMsgsenttime(String msgsenttime) {
		this.msgsenttime = msgsenttime;
	}

	public String getFormsysid() {
		return formsysid;
	}

	public void setFormsysid(String formsysid) {
		this.formsysid = formsysid;
	}

	public String getTosysid() {
		return tosysid;
	}

	public void setTosysid(String tosysid) {
		this.tosysid = tosysid;
	}

	public String getMsgoid() {
		return msgoid;
	}

	public void setMsgoid(String msgoid) {
		this.msgoid = msgoid;
	}

	public String getMsgotime() {
		return msgotime;
	}

	public void setMsgotime(String msgotime) {
		this.msgotime = msgotime;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public int getMsgcount() {
		return msgcount;
	}

	public void setMsgcount(int msgcount) {
		this.msgcount = msgcount;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getMsgcode() {
		return msgcode;
	}

	public void setMsgcode(String msgcode) {
		this.msgcode = msgcode;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getMsgnum() {
		return msgnum;
	}

	public void setMsgnum(String msgnum) {
		this.msgnum = msgnum;
	}
}
