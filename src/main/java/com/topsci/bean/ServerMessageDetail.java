package com.topsci.bean;

import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;

public class ServerMessageDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5915325065357538462L;

	/**
	 * 主键，自增
	 */
	private String id;
	
	/**
	 * 消息id
	 */
	private String msgsn;
	
	/**
	 * 消息发送时间
	 */
	private String msgsenttime;
	
	/**
	 * 发送系统编号，当平台主动推送的时候，此选项为定值：ALL
	 */
	private String formsysid;
	
	/**
	 * 接收系统编号，当发布者发布消息的时候，此选项为定值：ALL
	 */
	private String tosysid;
	
	/**
	 * 消息创建者,对应BUSINEDD_SYSTEM的SYSTEM_SHORT(系统简称，字母)
	 */
	private String msgoid;
	
	/**
	 * 消息创建者发送时间
	 */
	private String msgotime;
	
	
	/**
	 * 消息类型，取值为当前的服务目录编号
	 */
	private String msgtype;
	
	/**
	 * 消息编号，当一条消息过大需要分批发送的时候，用此值标记消息的连续性，从0开始，自增。默认0
	 */
	private int msgnum;

	
	/**
	 * 当前body的消息总数，每条消息最多100
	 */
	private int msgcount;
	
	
	/**
	 * 消息数据
	 */
	private String msgdata;
	
	/**
	 * 更新时间，默认为当前时间
	 */
	private String lastdate;

	
	/**
	 * 会话唯一标识 
	 */
	private String sessionid;
	
	/**
	 * 消息类型码 
	 */
	private String msgcode;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getMsgnum() {
		return msgnum;
	}

	public void setMsgnum(int msgnum) {
		this.msgnum = msgnum;
	}

	public int getMsgcount() {
		return msgcount;
	}

	public void setMsgcount(int msgcount) {
		this.msgcount = msgcount;
	}

	public String getMsgdata() {
		return msgdata;
	}

	public void setMsgdata(String msgdata) {
		this.msgdata = msgdata;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
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
	
	public static ServerMessageDetail getNewOne(ServerMessageDetail old)
	{
		byte[] srcByte = SerializationUtils.serialize(old);
		return (ServerMessageDetail) SerializationUtils.deserialize(srcByte);
	}

	
}
