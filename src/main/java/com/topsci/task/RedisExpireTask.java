package com.topsci.task;

import com.topsci.common.CatchAbout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisExpireTask extends BaseTask {
    private static Logger logger = LoggerFactory.getLogger(RedisExpireTask.class);
    private CatchAbout catchAbout = CatchAbout.getInstance();

    @Override
    public void run() {
        logger.info("redis的msgsn缓存清理开始");
        try{
            catchAbout.disableInvalidMsgSN();
            logger.info("redis的msgsn缓存清理结束");
        }
        catch (Exception ex)
        {
            logger.info("redis的msgsn缓存清理错误",ex);
        }
    }
}
