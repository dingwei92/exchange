package com.topsci.kafka.consume;

import java.util.*;

import com.topsci.common.Common;
import com.topsci.tools.kafka.consume.ConsumeThread;
import com.topsci.util.SpringUtil;
import com.topsci.web.log.WebLogService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.topsci.bean.ServerCatalog;
import com.topsci.bean.ServerCatalogList;
import com.topsci.bean.ServerMessageDetail;
import com.topsci.bean.ServerMessageError;
import com.topsci.bean.ServerSubscribes;
import com.topsci.common.CatchAbout;
import com.topsci.config.PropertiesBean;
import com.topsci.db.service.YwDBService;
import com.topsci.kafka.producer.KafkaSendSevice;
import com.topsci.util.DateUtil;
import com.topsci.util.StringUtil;
import com.topsci.util.ZipUtil;
import com.topsci.xml.XmlFactory;


@SuppressWarnings("unchecked")
public class ConsumerThread extends ConsumeThread {

	private static final Logger logger = LoggerFactory.getLogger(ConsumerThread.class);
	private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
	/**
	 * 协议头
	 */
	private String msgType;
	private String msgSentTime;
	private String msgSN;
	private String formSysID;
	private String toSysID;
	private String msgOID;
	private String msgOTime;

	private String msgCount;
	private String sessionID;
	private String msgcode;
	private String msgNum;


	/**
	 * 消息发送者对应的topci
	 */
	private String topic;

	/**
	 * 缓存操作类
	 */
	private CatchAbout catchAbout = CatchAbout.getInstance();

	/**
	 * 数据库操作类
	 */
	private YwDBService ywDBService = YwDBService.getInstance();

	private ServerCatalog serverCatalogBean;
	private ServerSubscribes serverSubscribesBean;


	private String headerCode; //头信息错误码
	private String bodyCode;   //数据body错误码

	public void run() {
		/**
		 * 格式验证
		 */
		xmlParser();
		logger.debug("接受到消息: " + catchAbout.getQueueSize() + "   " + getMessage());

		/**
		 * 解析正确
		 */
		if (headerCode == null) {
			if (Common.sendWebLog) {
				WebLogService.sendXmlLog(getMessage());
			}
			/**
			 * 如果解析正确，则把xml信息set到数据bean
			 */
			ServerMessageDetail serverMessageDetailBean = new ServerMessageDetail();
			serverMessageDetailBean.setFormsysid(formSysID);
			serverMessageDetailBean.setMsgcount(Integer.parseInt(msgCount));
			serverMessageDetailBean.setMsgoid(msgOID);
			serverMessageDetailBean.setMsgotime(msgOTime);
			serverMessageDetailBean.setMsgsenttime(msgSentTime);
			serverMessageDetailBean.setMsgsn(msgSN);
			serverMessageDetailBean.setMsgtype(msgType);
			serverMessageDetailBean.setLastdate(DateUtil.getNow());
			serverMessageDetailBean.setSessionid(sessionID);
			serverMessageDetailBean.setMsgcode(msgcode);
			serverMessageDetailBean.setTosysid(toSysID);
			serverMessageDetailBean.setMsgnum(Integer.parseInt(msgNum));

			/**
			 * 对存储的xml进行zip压缩
			 */
			serverMessageDetailBean.setMsgdata(ZipUtil.zip(getMessage()));
			/**
			 * 保存到第三方缓存队列
			 */
			catchAbout.saveMessageToCatch(serverMessageDetailBean);

		} else {
			/**
			 * 解析错误
			 */

			String status = "N";    //错误消息是否应答给对方，默认没有应答
			/**
			 * 返回通知,只发送一次，如果失败则不发送了，注意
			 */
			if (!StringUtil.isNull(topic)) {
				/**
				 * 采用直接调用了发送的api，没有采用异步发送，异步发送不能确定是否发送成功
				 */
				Map<String, String> map = new HashMap<String, String>();
				map.put(topic, XmlFactory.getInstance().createErrorXmlResponse(headerCode, bodyCode,
						msgType, formSysID, sessionID, getMessage()));
				if (KafkaSendSevice.getInstance().send(map)) {
					status = "Y";
				}
			}

			ServerMessageError errorBean = new ServerMessageError();
			/**
			 * 错误信息相关
			 */
			errorBean.setError_code(headerCode);
			errorBean.setLastdate(DateUtil.getNow());
			errorBean.setStatus(status);

			/**
			 * 消息相关
			 */
			errorBean.setFormsysid(formSysID);
			errorBean.setMsgcount(Integer.parseInt(msgCount));
			errorBean.setMsgoid(msgOID);
			errorBean.setMsgotime(msgOTime);
			errorBean.setMsgsenttime(msgSentTime);
			errorBean.setMsgsn(msgSN);
			errorBean.setMsgtype(msgType);
			errorBean.setTosysid(toSysID);
			errorBean.setSessionid(sessionID);
			errorBean.setMsgcode(msgcode);

			/**
			 * 判断是否存储xml
			 */
			if (propertiesBean.isSaveErrorXml()) {
				/**
				 * 注意： 错误的xml不进行压缩，方便查看错误
				 */
				errorBean.setMsgdata(getMessage());
			} else {
				errorBean.setMsgdata("");
			}
			ywDBService.saveMessageError(errorBean);
		}
	}


	/**
	 * 解析方法，返回错误码，如果正确返回null
	 */
	private void xmlParser(){

		/**
		 * 日志输出
		 */
		try {
			Document document = DocumentHelper.parseText(getMessage());
			Element envelope = document.getRootElement(); // 得到 Envelope
			Element header = envelope.element("Header");
			Element body = envelope.element("Body");


			/**
			 * 逻辑说明： 根据msgcode区别是发布数据还是主动订阅数据， 根据sessionID判断发布数据中那些是增量，那些是数据快照
			 */
			msgType = header.elementText(Common.HEAD_MSGTYPE);
			msgSentTime = header.elementText(Common.HEAD_MSGSENTTIME);
			msgSN = header.elementText(Common.HEAD_MSGSN);
			formSysID = header.elementText(Common.HEAD_FORMSYSID);
			toSysID = header.elementText(Common.HEAD_TOSYSID);
			msgOID = header.elementText(Common.HEAD_MSGOID);
			msgOTime = header.elementText(Common.HEAD_MSGOTIME);
			msgNum = header.elementText(Common.HEAD_MSGNUM);
			msgCount = header.elementText(Common.HEAD_MSGCOUNT);
			sessionID = header.elementText(Common.HEAD_SESSIONID);
			msgcode = header.elementText(Common.HEAD_MSGCODE);

			/**
			 * 根据fromSysID和msgcode获取消息发送者的topic
			 */
//			 long startime1 = new Date().getTime();
			if(!StringUtil.isNull(formSysID)&&!StringUtil.isNull(msgcode)){
				topic = catchAbout.getTopic(msgcode, formSysID);
				if(StringUtil.isNull(topic)){
					headerCode = Common.XML_ERROR_CODE_NO_SENDERTOPIC;
					logger.error("接受消息拦截：获取不到发送者的topic,请检查发送方的formSysID与msgCode！\n消息：{}",getMessage());
					return;
				}
			}

//			long startime2 = new Date().getTime();

			/**
			 * 消息头都不能为空
			 */
			if (StringUtil.isNull(msgType) || StringUtil.isNull(msgSentTime)
					|| StringUtil.isNull(msgSN) || StringUtil.isNull(formSysID)
					|| StringUtil.isNull(toSysID) || StringUtil.isNull(msgOID)
					|| StringUtil.isNull(msgOTime)
					|| StringUtil.isNull(msgNum)
					|| StringUtil.isNull(msgCount)
					|| StringUtil.isNull(sessionID)
					|| StringUtil.isNull(msgcode)
			) {
				headerCode = Common.XML_ERROR_CODE_HEAD_MISS_HEAD;
				logger.error("接受消息拦截：头信息缺少,检查是否拼写错误！\n消息：{}", getMessage());
				return;
			}

			if (StringUtil.isNull(msgCount)||StringUtil.isNull(msgNum)) {
				headerCode = Common.XML_ERROR_CODE_HEAD_MISS_NUMCOUNT;
				logger.error("接受消息拦截：头信息缺少，MsgNum或者MsgCount！ \n消息：{}", getMessage());
				return;
			}

//			long startime3 = new Date().getTime();
			/**
			 * 验证msgSN是否已经存在，数据库操作，可能比较慢
			 */
			if(catchAbout.checkMsgSN(msgSN)){
				headerCode = Common.XML_ERROR_CODE_DATA_MSGSN;
				logger.error("接受消息拦截：头信息msgSN已经存在！ \n消息：{}", getMessage());
				return;
			}
			long startime4 = new Date().getTime();
			/**
			 * 下面对发布和订阅不同的消息类型进行区别和验证
			 */

//			long startime5 = new Date().getTime();
//			long startime6 = new Date().getTime();
//			long startime7 = new Date().getTime();

			/**
			 * 发布数据类型验证
			 */
			if(Common.XML_DATATYPE_FB.equals(msgcode) || Common.XML_DATATYPE_THREEERROR.equals(msgcode)){

				boolean flag = false;
				List<Element> list = body.elements();

				/**
				 * 判断msgNum，msgCount是不是数字
				 */
				if(!StringUtil.isNumber(msgNum)||!StringUtil.isNumber(msgCount)){
					headerCode = Common.XML_ERROR_CODE_HEAD_NOTNUM_NUMCOUNT;
					logger.error("接受消息拦截：msgNum或者msgCount不是数字！\n消息：{}", getMessage());
					return;
				}

				/**
				 * 数据集大小与头信息描述是否一致判断
				 */
				if(list.size()!=Integer.parseInt(msgCount)){
					headerCode = Common.XML_ERROR_CODE_NUM_MATCH;
					logger.error("接受消息拦截：数据集大小与头信息msgCount描述不一致！ \n消息：{}", getMessage());
					return;
				}

				/**
				 * 数据集大小是否超过设置的上限判断
				 */
				if(Integer.parseInt(msgCount)>Common.HEAD_MSGCOUNT_SIZE){
					headerCode = Common.XML_ERROR_CODE_NUM_MAX;
					logger.error("接受消息拦截：数据集大小超过设置的上限！ \n消息：{}", getMessage());
					return;
				}

//				startime5 = new Date().getTime();
				/**
				 * 验证发布数据时的msgtype是否正确
				 */
				serverCatalogBean = catchAbout.getServerCatalogBean(msgType);
				if(serverCatalogBean==null){
					headerCode = Common.XML_ERROR_CODE_CATALOGILL;
					logger.error("接受消息拦截：发布的formSysID,msgType不合法，请检查web端是否设置！ \n消息：{}", getMessage());
					return;
				}
//				startime6 = new Date().getTime();
				//检查接口使用的模板是否在有效期内
//				if(ywDBService.checkCatalogTemplateValid(serverCatalogBean.getTemplateId()))
//				{
//					headerCode = Common.XML_ERROR_CODE_TEMPLATE_INVALID;
//					logger.error("接受消息拦截：发布的接口模板已失效！ \n消息：{}", getMessage());
//					return;
//				}

				if(Common.XML_DATATYPE_FB.equals(msgcode)) {

					/**
					 * 验证数据元素是否包含了主键标识
					 */
					for (Element e : list) {
						for (ServerCatalogList b : serverCatalogBean.getServerCatalogList()) {
							if (e.getName().equals(b.getServerElementName())) {
								if(b.getDbkey() == 1) {
									if (!"TRUE".equals(e.attributeValue("INDEX"))) {
										headerCode = Common.XML_ERROR_CODE_INDEX_MISS;
										return;
									}
								}
								else
								{
									if(StringUtils.isNotEmpty(e.attributeValue("INDEX")))
									{
										headerCode = Common.XML_ERROR_CODE_INDEX_MISS;
										return;
									}
								}
							}
						}
					}


					/**
					 * 验证web端定义的服务发布字段，在上传的数据中是否都包含，如果没有则判断为格式错误
					 */
//				 long starttime4 = System.currentTimeMillis();
					StringBuffer errorElement = new StringBuffer("缺少的字段名：");
					StringBuffer errEle2 = new StringBuffer("类型不正确的字段名：");
					flag = true;
					//OK:
					for (Element e : list) {
//						for(ServerCatalogListBean b : serverCatalogBean.getServerCatalogList())
//						{
//							if(e.getName().equals(b.getServer_element_name()) && b.getDb_col() == 1)
//							{
//
//							}
//						}

						for (ServerCatalogList b : serverCatalogBean.getServerCatalogList()) {
							if (e.elementText(b.getServerElementName()) == null) {
								errorElement.append(b.getServerElementName());
								errorElement.append(",");
//							logger.error("接受发布拦截："+errorElement);
								flag = false;
								//break OK;  //为了获取所有缺少的字段，暂时注释
							}
							else {
								String coltype = e.element(b.getServerElementName()).attributeValue(Common.ColumnType);
								if (StringUtils.isEmpty(coltype) || !b.getElementDescribe().equals(coltype)) {
									flag = false;
									errEle2.append(b.getServerElementName());
									errEle2.append(",");
								}
							}
						}

					}
					if(errEle2.length()>10)
					{
						errorElement.append(errEle2);
					}
					if (!flag) {
						headerCode = Common.XML_ERROR_CODE_DATA_FORMAT;
						bodyCode = Common.XML_ERROR_CODE_BODY_DATAERROR;
						errorElement.append("需要总字段数：" + serverCatalogBean.getServerCatalogList().size());
						logger.error("接受消息拦截：" + errorElement.toString()+"\n消息：{}", getMessage());
						return;
					}
				}
//				startime7 = new Date().getTime();

			}

			/**
			 * 主动订阅数据类型
			 */
			if (Common.XML_DATATYPE_REQUEST.equals(msgcode)) {
				//Todo: 以后的修改：主动请求时要检查过滤参数是否合法
				/**
				 * 说明:请求数据时，body必须有子节点Parameter
				 */

//				/**
//				 * 判断发送toSysID 是否合法， 发送给发布订阅模块为ALL
//				 */
//				if(!toSysID.equals(Common.SERVER_NAME)){
//					logger.error("接受消息拦截：头信息toSysID非正确值！ ");
//					headerCode = Common.XML_ERROR_CODE_HEAD_FORMAT;
//					return;
//				}

				/**
				 * 验证formSysID,msgType是否合法，筛选条件是否合法（不合法会重置为订阅时设置的条件） 说明
				 */
				serverSubscribesBean = catchAbout.getServerSubscribesBean(formSysID, msgType);
				if (serverSubscribesBean == null) {
					headerCode = Common.XML_ERROR_CODE_SUBILL;
					logger.error("接受消息拦截：订阅的msgType不合法，请检查web端是否设置！  \n消息：{}", getMessage());
					return;
				}
				else
				{
					List<String[]> cons = ywDBService.getConditionedSubscribeListBean(serverSubscribesBean.getId());
					for(String[] con : cons)
					{
						String ename = con[0];
						String type = con[1];
						String value = con[2];
						Element tmpcon = body.element(ename);
						if(tmpcon == null)
						{
							resetCondition(tmpcon,ename,type,value,body);
						}
						else
						{
							validateCondition(tmpcon,ename,type,value,body);
						}
					}
				}

				/**
				 * 需要对主动请求回话sessionID进行限制， 不能有相同的sessionID
				 */
				if(ywDBService.haveSessionID(sessionID)){
					headerCode = Common.XML_ERROR_CODE_SESSIONID;
					logger.error("接受消息拦截：主动订阅时SessinID不能重复！  \n消息：{}", getMessage());
					return;
				}
			}
//			System.out.println("一条消息的校验时间：" +
//					((startime2-startime1)+"ms，")+
//					((startime3-startime2)+"ms，")+
//					((startime4-startime3)+"ms，")+
//					((startime5-startime4)+"ms，")+
//					((startime6-startime5)+"ms，")+
//					((startime7-startime6)+"ms"));
		} catch (Exception e) {
			headerCode = Common.XML_ERROR_CODE_HEAD_FORMAT;
			logger.error("总线=》xml数据解析错误！\n消息：{}", getMessage(),e);
		}
	}

	/**
	 * 检查筛选条件是否合法，不合法的重置为订阅时的条件
	 * 筛选的类型不可改变
	 */
	private void validateCondition(Element tmpcon,String ename,String type,String value,Element body)
	{
		String querytype = tmpcon.element(Common.SUBSCRIBE_CON_QUERYTYPE).getText();
		if(!type.equals(querytype))
		{
			resetCondition(tmpcon,ename,type,value,body);
			return;
		}
		if(Common.SUBSCRIBE_CON_EQUAL.equals(querytype))
		{
			List<Element> values = tmpcon.elements(Common.SUBSCRIBE_CON_VALUE);
			if(values != null && values.size() == 1)
			{
				String v = values.get(0).getText();
				if(!value.equals(v))
				{
					resetCondition(tmpcon,ename,type,value,body);
				}
			}
			else
			{
				resetCondition(tmpcon,ename,type,value,body);
			}
		}
		else if(Common.SUBSCRIBE_CON_RANGE.equals(querytype))
		{
			Element from = tmpcon.element(Common.SUBSCRIBE_CON_FROM);
			Element to = tmpcon.element(Common.SUBSCRIBE_CON_TO);
			String[] strs = value.split(",");
			if(strs.length>0)
			{
				if(StringUtil.compareNumberString(from.getText(),strs[0])<0)
				{
					resetCondition(tmpcon,ename,type,value,body);
					return;
				}
			}
			if(strs.length>1)
			{
				if(StringUtil.compareNumberString(to.getText(),strs[1])>0)
				{
					resetCondition(tmpcon,ename,type,value,body);
					return;
				}
			}
		}
		else if(Common.SUBSCRIBE_CON_MULTIPLE.equals(querytype))
		{
			List<Element> values = tmpcon.elements(Common.SUBSCRIBE_CON_VALUE);
			if(values != null && values.size() >0)
			{
				String[] strs = value.split(",");
				List<String> strlist = Arrays.asList(strs);
				for(Element e : values)
				{
					if(strlist.stream().noneMatch(str->{
						String s = str.replaceAll("%","");
						return s.equals(e.getText());
					})){
						resetCondition(tmpcon,ename,type,value,body);
						return;
					}
				}
			}
			else
			{
				resetCondition(tmpcon,ename,type,value,body);
			}
		}
		else if(Common.SUBSCRIBE_CON_LIKE.equals(querytype))
		{
			List<Element> values = tmpcon.elements(Common.SUBSCRIBE_CON_VALUE);
			if(values != null && values.size() == 1)
			{
				String v = StringUtils.isEmpty(values.get(0).getText())?"":values.get(0).getText();
				if(!v.startsWith(value))
				{
					resetCondition(tmpcon,ename,type,value,body);
				}
			}
			else
			{
				resetCondition(tmpcon,ename,type,value,body);
			}
		}
	}

	/**
	 * 重置为订阅时的条件
	 */
	private void resetCondition(Element tmpcon,String ename,String type,String value,Element body)
	{
		tmpcon = body.addElement(ename);
		Element tmpele = tmpcon.addElement(Common.SUBSCRIBE_CON_QUERYTYPE);
		tmpele.setText(type);
		if(Common.SUBSCRIBE_CON_EQUAL.equals(type))
		{
			tmpele = tmpcon.addElement(Common.SUBSCRIBE_CON_VALUE);
			tmpele.setText(value);
		}
		else if(Common.SUBSCRIBE_CON_RANGE.equals(type))
		{
			String[] strs = value.split(",");
			if(strs.length > 0) {
				tmpele = tmpcon.addElement(Common.SUBSCRIBE_CON_FROM);
				tmpele.setText(strs[0]);
			}
			if(strs.length > 1)
			{
				tmpele = tmpcon.addElement(Common.SUBSCRIBE_CON_TO);
				tmpele.setText(strs[0]);
			}
		}
		else if(Common.SUBSCRIBE_CON_MULTIPLE.equals(type))
		{
			String[] strs = value.split(",");
			for(String str : strs) {
				tmpele = tmpcon.addElement(Common.SUBSCRIBE_CON_VALUE);
				tmpele.setText(str);
			}
		}
		else if(Common.SUBSCRIBE_CON_LIKE.equals(type))
		{
			tmpele = tmpcon.addElement(Common.SUBSCRIBE_CON_VALUE);
			tmpele.setText(value);
		}
	}
}
