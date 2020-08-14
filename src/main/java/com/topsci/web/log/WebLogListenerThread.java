package com.topsci.web.log;

import com.topsci.common.Common;
import com.topsci.config.PropertiesBean;
import com.topsci.tools.kafka.consume.ConsumeThread;
import com.topsci.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lzw on 2016/12/14.
 */
public class WebLogListenerThread extends ConsumeThread {
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private static Logger logger = LoggerFactory.getLogger(WebLogListenerThread.class);
    private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
    static {
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Override
    public void run() {
        String request = getMessage();
        logger.debug("收到集中平台日志请求: "+request);
        try {
            Date date = formatter.parse(request);
            Date current = new Date();
            long last = current.getTime() - date.getTime();
            if(last <= propertiesBean.getLogMaxDelay())
            {
                Common.webRequestLastUpdate = new Date();
            }
        }
        catch (Exception ex)
        {
            logger.error("日志请求topic时间转换错误",ex);
        }
    }
}
