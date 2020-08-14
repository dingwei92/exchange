package com.topsci.service;


import com.topsci.kafka.consume.ClientAliveThread;
import com.topsci.messaging.Processor;
import com.topsci.tools.kafka.consume.ConsumeThread;
import com.topsci.tools.thread.ThreadPoolManage;
import com.topsci.web.log.WebLogListenerThread;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;


/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: dingwei
 * @Date: 2020/06/24/15:13
 * @Description: Sink Service
 **/
@Service
@EnableBinding(Processor.class)
public class RecieveService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RecieveService.class);
    private  ThreadPoolManage threadPool=ThreadPoolManage.getInstance().getThreadPool();

    //使用inputrec通道
    @StreamListener(Processor.INPUT_RECEIVE)
    public void subscribeByRec(String message) {
        logger.info("*******************springcloudstream*****************");
        logger.info("StreamListener RECEIVE_TOPIC 收到消息：{}", message);
        ConsumeThread clientAliveThread = new ClientAliveThread();
        clientAliveThread.setMessage(message);
        threadPool.submit(clientAliveThread);
    }

    //使用inputstatus通道
    @StreamListener(Processor.INPUT_RECEIVE_STATUS)
    public void subscribeByRecStatus(String message) {
        logger.info("*******************springcloudstream*****************");
        logger.info("StreamListener RECEIVE_TOPIC_STATUS 收到消息：{}", message);
        ConsumeThread webLogListenerThread = new WebLogListenerThread();
        webLogListenerThread.setMessage(message);
        threadPool.submit(webLogListenerThread);
    }
}