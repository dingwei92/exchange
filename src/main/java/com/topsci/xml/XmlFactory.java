package com.topsci.xml;

import com.topsci.bean.BusinessSystem;
import com.topsci.bean.ServerCatalog;
import com.topsci.bean.ServerMessageDetail;
import com.topsci.bean.ServerSubscribes;
import com.topsci.common.Common;
import com.topsci.util.DateUtil;
import com.topsci.util.StringUtil;
import com.topsci.util.ZipUtil;

/**
 * @author yanhan
 *         xml构建
 */
public class XmlFactory {

    private static XmlFactory xmlFactory = null;

    private XmlFactory() {
    }

    public static XmlFactory getInstance() {
        if (xmlFactory == null) {
            synchronized (XmlFactory.class) {
                if (xmlFactory == null)
                    xmlFactory = new XmlFactory();
            }
        }
        return xmlFactory;
    }


    /**
     * 构建错误的xml消息返回
     */
    public String createErrorXmlResponse(String headerCode, String bodyCode, String msgType, String toSysID, String sessionID, String xml) {

        if (bodyCode == null) {
            bodyCode = Common.XML_ERROR_CODE_BODY_DEFAULT;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Envelope><Header>");
        sb.append("<MsgSentTime>");
        sb.append(DateUtil.getNow3());
        sb.append("</MsgSentTime><MsgSN>");
        sb.append(StringUtil.getUUID());
        sb.append("</MsgSN><FormSysID>");
        sb.append(Common.SERVER_NAME);
        sb.append("</FormSysID><ToSysID>");
        sb.append(toSysID);
        sb.append("</ToSysID><MsgOID>");
        sb.append(Common.SERVER_NAME);
        sb.append("</MsgOID><MsgOTime>");
        sb.append(DateUtil.getNow3());
        sb.append("</MsgOTime>");

        sb.append("<MsgNum>0</MsgNum>");

        sb.append("<MsgType>");
        sb.append(msgType);
        sb.append("</MsgType>");
        sb.append("<MsgCount>1</MsgCount><MsgCode>");
        sb.append(headerCode);
        sb.append("</MsgCode><SessionID>");
        sb.append(sessionID);
        sb.append("</SessionID></Header>");

        sb.append(xml.substring(xml.indexOf("<Body>")));
//        sb.append("</Body></Envelope>");
        return sb.toString();
    }

    /**
     * 构建主动全量请求xml
     */
    public ServerMessageDetail createXmlRequest(ServerSubscribes bean) {

        ServerMessageDetail message = null;
        if (bean != null) {
            BusinessSystem bs = bean.getBusinessSystemBean();
            ServerCatalog sc = bean.getServerCatalogBean();
            if (bs != null && sc != null) {
                message = new ServerMessageDetail();
                message.setLastdate(DateUtil.getNow());
                message.setMsgsenttime(DateUtil.getNow3());
                message.setFormsysid(bs.getSystemShort());
                message.setMsgnum(0);
                message.setMsgcount(1);
                message.setMsgoid(bs.getSystemShort());
                message.setMsgotime(DateUtil.getNow3());
                message.setMsgsn(StringUtil.getUUID());
                message.setMsgtype(sc.getServerShort());
                message.setTosysid(Common.SERVER_NAME);
                message.setSessionid(StringUtil.getUUID());
                message.setMsgcode(Common.XML_DATATYPE_REQUEST);
                /**
                 * 最后根据message构建xml
                 */
                message.setMsgdata(ZipUtil.zip(creatXmlRequest(message, bean)));
            }
        }
        return message;
    }

    /**
     * 构建主动请求xml
     */
    public String creatXmlRequest(ServerMessageDetail message, ServerSubscribes bean) {
        StringBuffer sb = new StringBuffer();
        if (message != null) {
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Envelope><Header><MsgSentTime>");
            sb.append(message.getMsgsenttime());
            sb.append("</MsgSentTime><MsgSN>");
            sb.append(message.getMsgsn());
            sb.append("</MsgSN><FormSysID>");
            sb.append(message.getFormsysid());
            sb.append("</FormSysID><ToSysID>");
            sb.append(message.getTosysid());
            sb.append("</ToSysID><MsgOID>");
            sb.append(message.getMsgoid());
            sb.append("</MsgOID><MsgOTime>");
            sb.append(message.getMsgotime());
            sb.append("</MsgOTime><MsgType>");
            sb.append(message.getMsgtype());
            sb.append("</MsgType><MsgNum>");
            sb.append(message.getMsgnum());
            sb.append("</MsgNum><MsgCount>");
            sb.append(message.getMsgcount());
            sb.append("</MsgCount><SessionID>");
            sb.append(message.getSessionid());
            sb.append("</SessionID><MsgCode>");
            sb.append(message.getMsgcode());
            sb.append("</MsgCode>");
            sb.append("</Header><Body>");

            /**
             * 主动请求数据，参数
             */
            if (!StringUtil.isNull(bean.getStartTime()) && !StringUtil.isNull(bean.getEndTime())) {
                sb.append("<Parameter>");
                sb.append("<StartDate>");
                sb.append(bean.getStartTime());
                sb.append("</StartDate>");
                sb.append("<EndDate>");
                sb.append(bean.getEndTime());
                sb.append("</EndDate>");
                sb.append(bean.getParameter());
                sb.append("</Parameter>");
            }

            sb.append("</Body></Envelope>");


        }
        return sb.toString();
    }


}
