/*******************************************************************************
 * Copyright 2017 Dell Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @microservice:  export-distro
 * @author: Jim White, Dell
 * @version: 1.0.0
 *******************************************************************************/
package com.topsci.edgexfoundry;

import com.topsci.bean.ServerMessageDetail;
import com.topsci.common.CatchAbout;
import com.topsci.common.Common;
import com.topsci.util.*;
import com.topsci.web.log.WebLogService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import java.util.Base64;

/**
 * Core Data message ingestion bean - gets messages out of ZeroMQ. Export
 * service message origination point
 * 
 * @author jim_white
 *
 */
@Component
public class ZeroMQEventSubscriber {

	private final Logger logger = LoggerFactory.getLogger(ZeroMQEventSubscriber.class);

	@Value("${zeromq.port}")
	private String zeromqAddressPort;
	@Value("${zeromq.host}")
	private String zeromqAddress;

	private ZMQ.Socket subscriber;
	private ZMQ.Context context;

	{
		context = ZMQ.context(1);
		// super.setOutputChannelName("export-inboud-events");
	}

	public void receive() {
		getSubscriber();
		byte[] raw;
		// long rawTime;
		logger.info("Watching for new Event messages...");
		try {
			while (!Thread.currentThread().isInterrupted()) {
				raw = subscriber.recv();
				String ss = new String(raw);
				if(ss.equals("events")){
					continue;
				}
				String payload = JsonUtils.getString(JSONObject.fromObject(ss),"Payload");
				payload = new String(Base64.getDecoder().decode(payload));

				JSONArray readingArray = JSONObject.fromObject(payload).getJSONArray("readings");
				String deviceName = JSONObject.fromObject(payload).getString("device");
				/*
				<Header>
					<MsgSentTime>20200408144511</MsgSentTime>
					<MsgSN>15c49635c46b49ca8afe0e4ef5007aca</MsgSN>
					<FormSysID>ALL</FormSysID>
					<ToSysID>AUTO_PUBLISHER</ToSysID>
					<MsgOID>ALL</MsgOID>
					<MsgOTime>20200408144511</MsgOTime>
					<MsgNum>0</MsgNum>
					<MsgType>ZX_S_EXAM_RECORD</MsgType>
					<MsgCount>1</MsgCount>
					<MsgCode>1007</MsgCode>
					<SessionID>78ebfd9d4a10454298038a320138deb8</SessionID>
				  </Header>
				 */
				if (Common.sendWebLog) {
					WebLogService.sendXmlLog(payload);
				}
				if(!DeviceCache.getSingleton().cache().containsKey(deviceName)){
					logger.warn("有新DS服务添加，设备信息同步中！");
					continue;
				}
				String dsName = DeviceCache.getSingleton().cache().get(deviceName);
				String msgType = dsName;
				int masNum = 0;
				int masCount = 1;
				String fromId = "ALL";
				String toId = "AUTO_PUBLISHER";
				String sessionID =  UUIDGenerator.getNoCrossUUID();
				String msgCode = "0001";
				String msgSentTime = DateUtil.getNow3();
				String msgsn = UUIDGenerator.getNoCrossUUID().toUpperCase();
				String msgOTime = DateUtil.getNow3();
				String xml = XMLUtil.createXml(msgType, masNum, masCount, fromId, toId,sessionID, msgCode,msgSentTime,msgsn,msgOTime,readingArray);

				/**
				 * 如果解析正确，则把xml信息set到数据bean
				 */
				ServerMessageDetail serverMessageDetailBean = new ServerMessageDetail();
				serverMessageDetailBean.setFormsysid(fromId);
				serverMessageDetailBean.setMsgcount(masCount);
				serverMessageDetailBean.setMsgoid("ALL");
				serverMessageDetailBean.setMsgotime(msgOTime);
				serverMessageDetailBean.setMsgsenttime(msgSentTime);
				serverMessageDetailBean.setMsgsn(msgsn);
				serverMessageDetailBean.setMsgtype(msgType);
				serverMessageDetailBean.setLastdate(DateUtil.getNow());
				serverMessageDetailBean.setSessionid(sessionID);
				serverMessageDetailBean.setMsgcode(msgCode);
				serverMessageDetailBean.setTosysid(toId);
				serverMessageDetailBean.setMsgnum(masNum);

				/**
				 * 对存储的xml进行zip压缩
				 */
				serverMessageDetailBean.setMsgdata(ZipUtil.zip(xml));
				/**
				 * 保存到第三方缓存队列
				 */
				CatchAbout.getInstance().saveMessageToCatch(serverMessageDetailBean);
			}
		} catch (Exception e) {
			logger.error("Unable to receive messages via ZMQ: " + e.getMessage());
		}
		logger.error("Shutting off Event message watch due to error!");
		if (subscriber != null) {
			subscriber.close();
		}
		subscriber = null;
		// try to restart
		logger.debug("Attempting restart of Event message watch.");
		receive();
		// context.term();
	}

	public String getZeromqAddress() {
		return zeromqAddress;
	}

	public void setZeromqAddress(String zeromqAddress) {
		this.zeromqAddress = zeromqAddress;
	}

	public String getZeromqAddressPort() {
		return zeromqAddressPort;
	}

	public void setZeromqAddressPort(String zeromqAddressPort) {
		this.zeromqAddressPort = zeromqAddressPort;
	}

	// @Override
	// public void setOutputChannelName(String outputChannelName) {
	// super.setOutputChannelName(outputChannelName);
	// }

	private ZMQ.Socket getSubscriber() {
		if (subscriber == null) {
			try {
				subscriber = context.socket(ZMQ.SUB);
				subscriber.connect(zeromqAddress + ":" + zeromqAddressPort);
				subscriber.subscribe("".getBytes());
			} catch (Exception e) {
				logger.error("Unable to get a ZMQ subscriber.  Error:  " + e);
				subscriber = null;
			}
		}
		return subscriber;
	}


}
