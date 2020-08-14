package com.topsci.kafka.producer;

import com.topsci.service.SendService;
import com.topsci.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class KafkaSendSevice {

    private final Logger logger = LoggerFactory.getLogger(KafkaSendSevice.class);


    private static KafkaSendSevice sendSevice;
    public KafkaSendSevice(){
	}

    public static synchronized KafkaSendSevice getInstance() {
        if (sendSevice == null) {
            sendSevice = new KafkaSendSevice();
        }
        return sendSevice;
    }

    /**
     * 发送方法
     */
    public boolean send(Map<String, String> map) {
        boolean flag = true;
        try {
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    /*springcloudstream*/
                    SendService sendService = SpringUtil.getBean(SendService.class);
                    sendService.dynamicSend(entry.getKey(), entry.getValue(), null);
                    logger.debug("发送消息至Topic： " + entry.getKey() + "  消息：" + entry.getValue());
                }
            }
        } catch (Exception e) {
            flag = false;
            logger.error("批量发送数据失败！", e);
        }
        logger.info("KafkaSendSevice.send 发送成功标识{}", flag);
        return flag;
    }

    public void send(String topic, String message) {
        /*springcloudstream*/
        SendService sendService = SpringUtil.getBean(SendService.class);
        sendService.dynamicSend(topic, message, null);
    }

    /**
     * 发送方法
     */
    public boolean sendMultiple(Map<String, List<String>> map) {
        boolean flag = true;
        try {
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    for (String str : entry.getValue()) {
                        /*springcloudstream*/
                        SendService sendService = SpringUtil.getBean(SendService.class);
                        sendService.dynamicSend(entry.getKey(), str, null);
                        logger.debug("发送消息至Topic： " + entry.getKey() + "  消息：" + str);
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            logger.error("批量发送数据失败！", e);
        }
        return flag;
    }


}
