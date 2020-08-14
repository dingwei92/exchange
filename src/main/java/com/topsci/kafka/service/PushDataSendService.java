package com.topsci.kafka.service;

import com.topsci.bean.*;
import com.topsci.common.CatchAbout;
import com.topsci.common.Common;
import com.topsci.config.PropertiesBean;
import com.topsci.db.service.YwDBService;
import com.topsci.kafka.producer.KafkaSendSevice;
import com.topsci.util.DateUtil;
import com.topsci.util.SpringUtil;
import com.topsci.util.StringUtil;
import com.topsci.util.ZipUtil;
import com.topsci.web.log.WebLogService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author yanhan
 *         对分发数据进行推送
 */
public class PushDataSendService{

    private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
    private final Logger logger = LoggerFactory.getLogger(PushDataSendService.class);
    private static YwDBService ywDBService = YwDBService.getInstance();
    private static CatchAbout catchAbout = CatchAbout.getInstance();
    private static KafkaSendSevice kafkaSendSevice = KafkaSendSevice.getInstance();
    private ServerMessageDetail serverMessageDetailBean;

    public boolean execute(ServerMessageDetail SMDBean) {
        serverMessageDetailBean = SMDBean;
        boolean flag = true; //分发结果状态,默认成功

        //查询需要订阅该服务的集合，如果没有订阅者，则修改状态为‘已处理’
        List<ServerSubscribes> ssbs = null;
//        long starttime = System.currentTimeMillis();
        /**
         * 判断是数据快照还是增量发布
         */
        ServerSubscribes sessionidBean = catchAbout.existsSessionID(serverMessageDetailBean.getSessionid());
//        long end1 = System.currentTimeMillis();
        if (sessionidBean != null) {
            ssbs = new ArrayList<ServerSubscribes>();
            ssbs.add(sessionidBean);

            /**
             * 如果是快照，则需要重新更新sessionid失效时间
             */
            catchAbout.saveOrUpdateSessionid(serverMessageDetailBean.getSessionid(), sessionidBean.getId());

        } else {
            if(serverMessageDetailBean.getMsgcode().equals(Common.XML_DATATYPE_THREEERROR))
            {
                logger.warn("主动请求"+serverMessageDetailBean.getSessionid()+"的回复包含第三方错误!");
                return flag;
            }
            else {
//                long starttime1 = System.currentTimeMillis();
                ssbs = catchAbout.getServerSubscribesList(serverMessageDetailBean);
//                long endtime1 = System.currentTimeMillis();
                //System.err.println("查询订阅者耗时： "+(endtime1-starttime1)+" 毫秒");
            }
        }
//        long end2 = System.currentTimeMillis();

        List<ServerSubscribesMessage> sub_history_message = null; //分发消息记录集合
        if (ssbs != null && ssbs.size() > 0) {
            ServerSubscribesMessage messageBean = null;
            sub_history_message = new ArrayList<ServerSubscribesMessage>();
            Map<String, List<String>> map = new HashMap<>(); //存储需要发送的topic和xml集合
            List<String> webmap = new ArrayList<>(); //存储需要发送给日志的xml集合
            List<String> xmls = null;
            for (ServerSubscribes ssb : ssbs) {
                if("Y".equals(ssb.getEnables())) {
                    xmls = getNewXML(ssb);
                    if (xmls.size() > 0) {
                        for (String xml : xmls) {
                            if (xml != null) {
                                messageBean = new ServerSubscribesMessage();
                                messageBean.setBusiness_system_id(ssb.getBusinessSystemId());
                                messageBean.setServer_subscribes_id(ssb.getId());
                                messageBean.setServer_message_detail_id(serverMessageDetailBean.getId());
                                messageBean.setLastdate(DateUtil.getNow());
                                sub_history_message.add(messageBean);
                                List<String> xmllist = map.get(ssb.getBusinessSystemBean().getKafkaTopicBean().getTopicName());
                                if (xmllist == null) {
                                    xmllist = new ArrayList<>();
                                }
                                xmllist.add(xml);
                                map.put(ssb.getBusinessSystemBean().getKafkaTopicBean().getTopicName(), xmllist);
                                if (Common.sendWebLog) {
                                    webmap.add(xml);
                                }
                            }
                        }
                    }
                }
            }

            if (map != null && map.size() > 0) {
                /**
                 * 分发采用同步发送，目的是能获取发送是否成功，如果不成功，会再次发送
                 */
//                long starttime2 = System.currentTimeMillis();
                if (!kafkaSendSevice.sendMultiple(map)) {
                    flag = false; //发送失败
                }
                if(Common.sendWebLog){
                    WebLogService.sendXmlLog(webmap);
                }
//                long endtime2 = System.currentTimeMillis();
                //System.err.println("发送耗时： "+(endtime2-starttime2)+" 毫秒");
            }
        }

//        long end3 = System.currentTimeMillis();

        /**
         * 发送成功
         */
        if (flag) {
            /**
             * 保存分发记录
             */
            //判断是否存储xml源数据
            if (!propertiesBean.isSaveXml()) {
                serverMessageDetailBean.setMsgdata("");
            }
            catchAbout.recordMsgSN(serverMessageDetailBean.getMsgsn());
//            long starttime3 = System.currentTimeMillis();
            //ServerCatalog serverCatalogBean = catchAbout.getServerCatalogBean(serverMessageDetailBean.getMsgtype());
//            long endtime3 = System.currentTimeMillis();
//            logger.error("查询发布者耗时： "+(endtime3-starttime3)+" 毫秒");
//            long starttime4 = System.currentTimeMillis();
            //ywDBService.saveServerCatalogMessage(serverMessageDetailBean, sub_history_message, serverCatalogBean);
//            long endtime4 = System.currentTimeMillis();
//            logger.error("数据库存储耗时： "+(endtime4-starttime4)+" 毫秒");
        } else {
//            long starttime5 = System.currentTimeMillis();
            try {
                catchAbout.saveMessageToCatch(serverMessageDetailBean);//失败时，重新放入发布队列
            }
            catch (Exception ex)
            {
                logger.error("重新加入队列错误！ msgType:"+serverMessageDetailBean.getMsgtype());
            }
            if(sessionidBean == null) {
                logger.error("增量消息发送失败(重新加入队列): \nTo:" + serverMessageDetailBean.getTosysid() + " From: " + serverMessageDetailBean.getFormsysid() + " MsgType: "
                        + serverMessageDetailBean.getMsgtype());
            }
            else
            {
                logger.error("快照发送失败(重新加入队列): \nTo:" + serverMessageDetailBean.getTosysid() + " From: " + serverMessageDetailBean.getFormsysid() + " MsgType: "
                        + serverMessageDetailBean.getMsgtype());
            }
//            long endtime5 = System.currentTimeMillis();
//            logger.error("重新加入队列耗时： "+(endtime5-starttime5)+" 毫秒");
        }
//        long end4 = System.currentTimeMillis();
//        logger.error("单个推送耗时: "+(end1 - starttime)+"ms   "+(end2 - starttime)+"ms   "+(end3 - starttime)+"ms   "+(end4 - starttime)+"ms   ");
        return flag;
    }

    /**
     * 如果是正常的发布数据：根据订阅详细字段，与原始xml数据，构建新的xml
     * 如果是第三方发送给第三方的错误消息，则无需过滤
     */
    @SuppressWarnings("unchecked")
    private List<String> getNewXML(ServerSubscribes serverSubscribesBean) {

        List<String> xmls = new ArrayList<>();
        String xml = null;
        Document document = null;

        try {
            document = DocumentHelper.parseText(ZipUtil.unZip(serverMessageDetailBean.getMsgdata()));
            Element envelope = document.getRootElement(); //得到 Envelope
            Element header = envelope.element("Header");
            Element body = envelope.element("Body");

            /**
             * 修改发送者，接受者的信息
             */
            if (header != null) {
                /**
                 * 消息创建者，发送者修改为server
                 */
                header.element(Common.HEAD_FORMSYSID).setText(Common.SERVER_NAME);
//                header.element(Common.HEAD_MSGOID).setText(Common.SERVER_NAME);

                /**
                 * 发送目的修改为订阅者
                 */
                header.element(Common.HEAD_TOSYSID).setText(serverSubscribesBean.getBusinessSystemBean().getSystemShort());

            }

            List<Element> listbody = body.elements();
            if (!Common.XML_DATATYPE_THREEERROR.equals(serverMessageDetailBean.getMsgcode())) {
                /**
                 * 删除未订阅的字段
                 */
                for (Element element : listbody) {
                    Element tmpelement = (Element) element.clone();
                    List<Element> list2 = tmpelement.elements();
                    if(checkSatisfied(tmpelement,serverSubscribesBean)) {
                        for (Element e : list2) { //字段级别~~
                            //验证字段是否被订阅，如果没有被订阅则删除
                            if (!checkElement(e.getName(), serverSubscribesBean.getServerCatalogList())) {
                                tmpelement.remove(e);
                            }
                        }
                        //封装一条消息
                        Document tmpdocument = (Document) document.clone();
                        Element tmpenvelope = tmpdocument.getRootElement(); //得到 Envelope
                        Element tmpheader = tmpenvelope.element("Header");
                        tmpheader.element(Common.HEAD_MSGCOUNT).setText("1");
                        tmpheader.element(Common.HEAD_MSGSN).setText(UUID.randomUUID().toString().replaceAll("-",""));
                        tmpenvelope.remove(tmpenvelope.element("Body"));
                        Element tmpbody=tmpenvelope.addElement("Body");
                        tmpbody.add(tmpelement);
                        xmls.add(tmpdocument.asXML());
                    }
                }
            }
        } catch (DocumentException e) {
            logger.error("分发时解析XML失败！ ", e);
        }
        return xmls;

    }


    /**
     * 验证字段名，是否被订阅
     */
    private boolean checkElement(String elementName, List<ServerCatalogList> elements) {
        boolean flag = false;
        if (elements != null && elements.size() > 0) {
            for (ServerCatalogList bean : elements) {
                if (elementName.trim().equals(bean.getServerElementName().trim())) {
                    return true;
                }
            }
        }
        return flag;
    }


    /**
     * 验证发布数据的字段值是否符合订阅时的条件
     */
    private boolean checkSatisfied(Element elements, ServerSubscribes subbean)
    {
        boolean validated = true;
        try {
            List<String[]> beans = ywDBService.getConditionedSubscribeListBean(subbean.getId());
            if (beans.size() == 0) {
                return validated;
            }
            for (String[] args : beans) {
                boolean exit = false;
                String colname = args[0];
                String coltype = args[1];
                String coltypevalue = args[2];
                Element col = elements.element(colname);
                if(col == null)
                {
                    continue;
                }
                String coltext = col.getTextTrim();
                if(StringUtils.isEmpty(coltext))
                {
                    coltext = "";
                }
                List<String> coltvlist = Arrays.asList(coltypevalue.split(","));
                if(Common.SUBSCRIBE_CON_EQUAL.equals(coltype))
                {
                    if(!coltypevalue.equals(coltext))
                    {
                        validated = false;
                        break;
                    }
                }
                else if(Common.SUBSCRIBE_CON_RANGE.equals(coltype))
                {

                    if(coltvlist.size()>0 && StringUtils.isNotEmpty(coltvlist.get(0)) && StringUtil.compareNumberString(coltvlist.get(0),coltext) > 0)
                    {
                        validated = false;
                        break;
                    }
                    if(coltvlist.size()>1 && StringUtils.isNotEmpty(coltvlist.get(1)) && StringUtil.compareNumberString(coltvlist.get(1),coltext) < 0)
                    {
                        validated = false;
                        break;
                    }
                }
                else if(Common.SUBSCRIBE_CON_MULTIPLE.equals(coltype))
                {
                    if(!coltvlist.contains(coltext)) {
                        validated = false;
                        break;
                    }
                }
                else if(Common.SUBSCRIBE_CON_LIKE.equals(coltype)) {
                    final String coltextfinal = coltext;
                    if(coltvlist.stream().noneMatch(str->{
                        if(str.endsWith("%")) {
                            return coltextfinal.startsWith(str.replaceAll("%",""));
                        } else if(str.startsWith("%")){
                            return coltextfinal.endsWith(str.replaceAll("%",""));
                        } else{
                            return coltextfinal.startsWith(str);
                        }
                    })) {
                        validated = false;
                        break;
                    }
                }
                if(exit)
                {
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("处理订阅{}字段条件筛选时错误！",subbean.getId(),ex);
        }
        return validated;
    }




}
