package com.topsci.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
  * @Description 自定义配置熟悉bean
 */
@Component
public class PropertiesBean {

	/**
	 * 监听服务上传topic开启的线程数
	 */
	@Value("${receivePoolSize}")
	private int receivePoolSize;
	
	/**
	 * 发送服务订阅者topic开启的线程数
	 */
	@Value("${sendPoolSize}")
	private int sendPoolSize;

	/**
	 * 推送队列最大长度
	 */
	@Value("${maxPushQueueSize}")
	private int maxPushQueueSize;

	/**
	 * 发送服务订阅者topic连接url
	 */
	//@Value("${kafkaBrokers}")
	private String kafkaBrokers;
	
	/**
	 * 监听服务上传时，topic名称
	 */
	//@Value("${receiveTopic}")
	private String receiveTopic;

	/**
	 * 客户端状态上传topic
	 */
	//@Value("${clientAliveTopic}")
	private String clientAliveTopic;


	/**
	 * 监听日志请求的topic名称
	 */
	@Value("${web.log.request.topic}")
	private String logRequestTopic;

	/**
	 * 发送日志的topic名称
	 */
	@Value("${web.log.send.topic}")
	private String logSendTopic;

	/**
	 * 日志请求的最大延迟时间
	 */
	@Value("${web.log.max.delay.second}")
	private int logMaxDelay;
	
	
	/**
	 * 是否保存xml消息体 
	 */
	@Value("${isSaveXml}")
	private boolean isSaveXml;
	
	/**
	 * 是否保存错误的xml消息体 
	 */
	@Value("${isSaveErrorXml}")
	private boolean isSaveErrorXml;
	
	/**
	 * redis url  格式： ip:端口,ip:端口 
	 */
	@Value("${redisUrl}")
	private String redisUrl;

	/**
	 * redis master名称
	 */
	@Value("${redis.master.name}")
	private String redisMasterName;

	/**
	 * redis master密码
	 */
	@Value("${redis.master.pwd}")
	private String redisMasterPwd;

	/**
	 * redis服务器类型
	 */
	@Value("${redis.type}")
	private String redisType;

	/**
	 * redis使用的数据库
	 */
	@Value("${redis.db}")
	private int redisDB;

	/**
	 * ssl证书路径
	 */
	//@Value("${SSL.client.truststore.location}")
	private String sslCertLocation;

	/**
	 * 自动发布程序的系统名称
	 */
	private String autoPublisherSysname;
	/**
	 * zeromq端口
	 */
	@Value("${zeromq.port}")
	private String zeromqAddressPort;
	/**
	 * zeromq 地址
	 */
	@Value("${zeromq.host}")
	private String zeromqAddress;
	@Value("${dataStaticTaskTime}")
	private String dataStaticTaskTime;
	@Value("${distributeThread}")
	private Integer distributeThread;
	@Value("${distributeThreadDelay}")
	private Integer distributeThreadDelay; //ms

	public String getDataStaticTaskTime() {
		return dataStaticTaskTime;
	}

	public void setDataStaticTaskTime(String dataStaticTaskTime) {
		this.dataStaticTaskTime = dataStaticTaskTime;
	}

	public String getZeromqAddressPort() {
		return zeromqAddressPort;
	}

	public void setZeromqAddressPort(String zeromqAddressPort) {
		this.zeromqAddressPort = zeromqAddressPort;
	}

	public String getZeromqAddress() {
		return zeromqAddress;
	}

	public void setZeromqAddress(String zeromqAddress) {
		this.zeromqAddress = zeromqAddress;
	}

	public int getRedisDB() {
		return redisDB;
	}

	public void setRedisDB(int redisDB) {
		this.redisDB = redisDB;
	}

	public Integer getDistributeThread() {
		return distributeThread;
	}

	public void setDistributeThread(Integer distributeThread) {
		this.distributeThread = distributeThread;
	}

	public Integer getDistributeThreadDelay() {
		return distributeThreadDelay;
	}

	public void setDistributeThreadDelay(Integer distributeThreadDelay) {
		this.distributeThreadDelay = distributeThreadDelay;
	}

	public String getAutoPublisherSysname() {
		return autoPublisherSysname;
	}

	public void setAutoPublisherSysname(String autoPublisherSysname) {
		this.autoPublisherSysname = autoPublisherSysname;
	}

	public String getRedisType() {
		return redisType;
	}

	public void setRedisType(String redisType) {
		this.redisType = redisType;
	}

	public String getRedisMasterName() {
		return redisMasterName;
	}

	public void setRedisMasterName(String redisMasterName) {
		this.redisMasterName = redisMasterName;
	}

	public String getRedisMasterPwd() {
		return redisMasterPwd;
	}

	public void setRedisMasterPwd(String redisMasterPwd) {
		this.redisMasterPwd = redisMasterPwd;
	}

	public int getReceivePoolSize() {
		return receivePoolSize;
	}

	public void setReceivePoolSize(int receivePoolSize) {
		this.receivePoolSize = receivePoolSize;
	}

	public int getSendPoolSize() {
		return sendPoolSize;
	}

	public void setSendPoolSize(int sendPoolSize) {
		this.sendPoolSize = sendPoolSize;
	}

	public String getKafkaBrokers() {
		return kafkaBrokers;
	}

	public void setKafkaBrokers(String kafkaBrokers) {
		this.kafkaBrokers = kafkaBrokers;
	}

	public String getReceiveTopic() {
		return receiveTopic;
	}

	public void setReceiveTopic(String receiveTopic) {
		this.receiveTopic = receiveTopic;
	}

	public boolean isSaveXml() {
		return isSaveXml;
	}

	public void setSaveXml(boolean isSaveXml) {
		this.isSaveXml = isSaveXml;
	}

	public boolean isSaveErrorXml() {
		return isSaveErrorXml;
	}

	public void setSaveErrorXml(boolean isSaveErrorXml) {
		this.isSaveErrorXml = isSaveErrorXml;
	}

	public String getRedisUrl() {
		return redisUrl;
	}

	public void setRedisUrl(String redisUrl) {
		this.redisUrl = redisUrl;
	}

	public String getSslCertLocation() {
		return sslCertLocation;
	}

	public void setSslCertLocation(String sslCertLocation) {
		this.sslCertLocation = sslCertLocation;
	}

	public String getLogRequestTopic() {
		return logRequestTopic;
	}

	public void setLogRequestTopic(String logRequestTopic) {
		this.logRequestTopic = logRequestTopic;
	}

	public String getLogSendTopic() {
		return logSendTopic;
	}

	public void setLogSendTopic(String logSendTopic) {
		this.logSendTopic = logSendTopic;
	}

	public int getLogMaxDelay() {
		return logMaxDelay;
	}

	public void setLogMaxDelay(int logMaxDelay) {
		this.logMaxDelay = logMaxDelay;
	}

	public void setIsSaveXml(boolean isSaveXml) {
		this.isSaveXml = isSaveXml;
	}

	public void setIsSaveErrorXml(boolean isSaveErrorXml) {
		this.isSaveErrorXml = isSaveErrorXml;
	}

	public String getClientAliveTopic() {
		return clientAliveTopic;
	}

	public void setClientAliveTopic(String clientAliveTopic) {
		this.clientAliveTopic = clientAliveTopic;
	}

	public int getMaxPushQueueSize() {
		return maxPushQueueSize;
	}

	public void setMaxPushQueueSize(int maxPushQueueSize) {
		this.maxPushQueueSize = maxPushQueueSize;
	}


	public boolean isIsSaveXml() {
		return isSaveXml;
	}

	public boolean isIsSaveErrorXml() {
		return isSaveErrorXml;
	}
}