package com.topsci.kafka.consume;

import com.topsci.tools.kafka.consume.ConsumeThread;
import com.topsci.util.MongodbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Created by lzw on 2017/5/5.
 */
public class ClientAliveThread extends ConsumeThread {
    private static final Logger logger = LoggerFactory.getLogger(ClientAliveThread.class);

    @Override
    public void run() {
        String message = getMessage();
        /* String sql = "update BUSINESS_SYSTEM set LAST_CONNECT=? where SYSTEM_SHORT=?";
        Object[] params = new Object[]{new Timestamp(new Date().getTime()),message};*/
        String[] key = {"lastConnect"};
        Object[] val = {LocalDateTime.now()};
        MongodbUtils.updateFirst("systemShort",message,key,val,"businessSystem");
        logger.debug("更新系统" + message + "的客户端状态");
    }
}
