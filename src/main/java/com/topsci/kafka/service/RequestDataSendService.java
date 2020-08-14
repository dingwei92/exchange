package com.topsci.kafka.service;

import com.topsci.bean.ServerCatalog;
import com.topsci.bean.ServerMessageDetail;
import com.topsci.bean.ServerSubscribes;
import com.topsci.common.CatchAbout;
import com.topsci.common.Common;
import com.topsci.config.PropertiesBean;
import com.topsci.db.service.YwDBService;
import com.topsci.kafka.producer.KafkaSendSevice;
import com.topsci.util.SpringUtil;
import com.topsci.util.ZipUtil;
import com.topsci.web.log.WebLogService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yanhan
 * 对主动请求订阅进行推送
 */
public class RequestDataSendService{
	
	private final Logger logger = LoggerFactory.getLogger(RequestDataSendService.class);
	private static YwDBService ywDBService = YwDBService.getInstance();
	private static CatchAbout catchAbout = CatchAbout.getInstance();
	private static KafkaSendSevice kafkaSendSevice = KafkaSendSevice.getInstance();
	private ServerMessageDetail serverMessageDetailBean;
	private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
	public boolean execute(ServerMessageDetail smb) {
		serverMessageDetailBean = smb;
		boolean flag = true; //设置状态（发送结果状态）
		String topic = null; //发送topic名称
		String message = null;
		ServerSubscribes serverSubscribesBean = catchAbout.getServerSubscribesBean(serverMessageDetailBean.getFormsysid(),serverMessageDetailBean.getMsgtype());
		ServerCatalog serverCatalogBean = serverSubscribesBean.getServerCatalogBean(); //订阅对应的发布服务bean
		
		if(serverCatalogBean!=null){
			topic = serverCatalogBean.getBusinessSystemBean().getKafkaTopicBean().getTopicName();
			message = getNewXML(serverCatalogBean.getBusinessSystemBean().getSystemShort(), serverMessageDetailBean.getMsgtype());
			Map<String, String> map = new HashMap<String, String>(); //存储需要发送的topic和xml集合
			map.put(topic, message);
			/**
			 * 分发采用同步发送，目的是能获取发送是否成功，如果不成功，会再次发送
			 */
			if (!kafkaSendSevice.send(map)) {
				flag = false; //发送失败
			}
			if (Common.sendWebLog) {
				WebLogService.sendXmlLog(message);
			}
			/**
			 * 发送成功
			 */
			if (flag) {
				/**
				 * 保存发送记录
				 */
				//判断是否存储xml源数据
				if (!propertiesBean.isSaveXml()) {
					serverMessageDetailBean.setMsgdata("");
				}
				//ywDBService.saveSubscribesCataLogMessage(serverSubscribesBean, serverMessageDetailBean);

				/**
				 * 存储后增加sessionid缓存
				 */
				catchAbout.saveOrUpdateSessionid(serverMessageDetailBean.getSessionid(), serverSubscribesBean.getId());
			} else {
				/**
				 * 发送失败,重新放入队列
				 */
				catchAbout.saveMessageToCatch(serverMessageDetailBean);
				logger.error("主动请求发送失败(重新加入队列): \nTo:" + message);
			}
			 
		}else{
			logger.error("路由主动请求xml没有找到对应的topic，请悉知！ 具体信息:"+message);
		}
		
	   return flag;
	}

	private String getNewXML(String toSys,String msgType){
		String xml = null;

		Document document = null;
		try {
			document = DocumentHelper.parseText(ZipUtil.unZip(serverMessageDetailBean.getMsgdata()));
			Element envelope = document.getRootElement(); //得到 Envelope
			Element header = envelope.element("Header");
			
			if(header!=null){
				/**
				 * 消息创建者，发送者修改为server
				 */
				header.element(Common.HEAD_FORMSYSID).setText(Common.SERVER_NAME);
//				header.element(Common.HEAD_MSGOID).setText(Common.SERVER_NAME);
				/**
				 * 发送目的修改为消息发布者
				 */
				header.element(Common.HEAD_TOSYSID).setText(toSys);
			}

			xml = document.asXML();
			
		} catch (DocumentException e) {
			logger.error("路由时解析XML失败！ ", e);
		}
		return xml;
	}
}
