package com.topsci.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lzw on 2017/5/5.
 */
public class ClientAliveCheckTask implements Runnable {
    private Logger logger = LoggerFactory.getLogger(ClientAliveCheckTask.class);

    @Override
    public void run() {
       /* String sql = "update BUSINESS_SYSTEM system set system.LAST_CONNECT = null where system.LAST_CONNECT < ?";
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.set(Calendar.MINUTE, date.get(Calendar.MINUTE) - 3);
        try {
            DBTools.update(sql, new Object[]{new Timestamp(date.getTime().getTime())});
        }
        catch (Exception ex)
        {
            logger.error("更新业务系统客户端状态失败！",ex);
        }*/
    }
}
