package com.topsci.web.log;

import com.topsci.config.PropertiesBean;
import com.topsci.kafka.producer.KafkaSendSevice;
import com.topsci.util.SpringUtil;
import com.topsci.util.StringUtil;
import com.topsci.util.XMLUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lzw on 2016/12/14.
 */
public class WebLogService {
    private static PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
    public static void sendXmlLog(final String log)
    {
        KafkaSendSevice sendSevice = KafkaSendSevice.getInstance();
        final String formattedlog = XMLUtil.formatXml(log);
        if(StringUtils.isNotEmpty(formattedlog)) {
            sendSevice.send(new HashMap<String, String>() {{
                put(propertiesBean.getLogSendTopic(), formattedlog);
            }});
        }
    }

    public static void sendXmlLog(List<String> logs)
    {
        for(String log : logs){
            sendXmlLog(log);
        }
    }
}
