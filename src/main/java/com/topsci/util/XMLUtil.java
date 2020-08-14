package com.topsci.util;

import net.sf.json.JSONArray;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

/**
 * Created by lzw on 2016/12/14.
 */
public class XMLUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

    public static String formatXml(String str)  {
        try {
            Document document = null;
            document = DocumentHelper.parseText(str);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf8");
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        }
        catch (Exception ex)
        {
            logger.error("格式化xml消息出错",ex);
        }
        return "";
    }


    /**
     * 单个bean转换发送
     * @param msgType
     * @param masNum
     * @param masCount
     * @param fromId
     * @param toId
     * @param bean
     * @return
     */
    public static <T> String createXml(String msgType, int masNum, int masCount, String fromId, String toId,String sessionID, String msgCode
            ,String msgSentTime, String msgsn,String msgOTime,T bean){
        String resmsg = "";
        try {
            if (bean != null) {
                msgType = msgType.toUpperCase();
                StringBuilder sb = new StringBuilder("<Body><");
                String bodyXml = JaxbUtil.jsonArrayToXml((JSONArray) bean).replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                if (bodyXml != null && !"".equals(bodyXml)) {
                    sb.append(msgType).append(">");
                    sb.append(bodyXml);
                    sb.append("</").append(msgType).append(">");
                    sb.append("</Body>");
                    resmsg = addHead(msgType,masNum, masCount, fromId, toId,sessionID,msgCode,msgSentTime, msgsn,msgOTime, sb.toString());
                    logger.info("生成xml："+resmsg);
                    return resmsg;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加消息头
     * @param msgType
     * @param masNum
     * @param masCount
     * @param fromId
     * @param toId
     * @param sessionID
     * @param msgCode
     * @param body
     * @return
     */
    private static String addHead(String msgType, int masNum, int masCount, String fromId, String toId,String sessionID,String msgCode
            ,String MsgSentTime, String msgsn,String MsgOTime,String body){
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        xml.append("<Envelope><Header><MsgSentTime>");
        xml.append(MsgSentTime);
        xml.append("</MsgSentTime><MsgSN>");
        xml.append(msgsn);
        xml.append("</MsgSN><FormSysID>");
        xml.append(fromId);
        xml.append("</FormSysID><ToSysID>");
        xml.append(toId);
        xml.append("</ToSysID><MsgOID>");
        xml.append(fromId);
        xml.append("</MsgOID><MsgOTime>");
        xml.append(MsgOTime);
        xml.append("</MsgOTime><SessionID>");
        xml.append(sessionID);
        xml.append("</SessionID><MsgType>");
        xml.append(msgType);
        xml.append("</MsgType><MsgCode>");
        xml.append(msgCode);
        xml.append("</MsgCode><MsgNum>");
        xml.append(masNum);
        xml.append("</MsgNum><MsgCount>");
        xml.append(masCount);
        xml.append("</MsgCount></Header>");
        xml.append(body);
        xml.append("</Envelope>");
        return xml.toString();
    }
}
