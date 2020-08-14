package com.topsci.web.log;

import com.topsci.common.Common;
import com.topsci.config.PropertiesBean;
import com.topsci.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lzw on 2016/12/14.
 */
public class WebLogChecker implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(WebLogChecker.class);
    private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);
    private boolean checkDate(Date d)
    {
        if(d == null)
        {
            return false;
        }
        Date current = new Date();
        long last = current.getTime() - d.getTime();
        if(last > propertiesBean.getLogMaxDelay())
        {
            return false;
        }
        else
        {
            logger.debug("获取页面日志的请求");
            return true;
        }
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                boolean result = checkDate(Common.webRequestLastUpdate);
                Common.sendWebLog = result;
                logger.debug("发送日志到集中平台: "+result);
                Thread.sleep(1000);
            }
            catch (Exception ex)
            {
                logger.error("集中平台日志发送检查线程错误",ex);
            }
        }
    }
}
