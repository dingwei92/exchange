package com.topsci.service;

import com.topsci.messaging.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: dingwei
 * @Date: 2020/06/24/14:27
 * @Description: Processor Service
 **/
@Service
@EnableBinding(Processor.class)
public class SendService {
    private final Logger logger = LoggerFactory.getLogger(SendService.class);

    @Autowired
    private BinderAwareChannelResolver resolver;
    @Autowired
    private Processor processor;

    public boolean send(String data) {
        logger.info("*******************springcloudstream*****************");
        logger.info("send :{}", data);
        return processor.output().send(MessageBuilder.withPayload(data).build());
    }

    /*动态获取topic*/
    public boolean dynamicSend(String target, String body, Object contentType) {
        logger.info("*******************springcloudstream*****************");
        logger.info("send body:{},target:{}", body, target);
        return resolver.resolveDestination(target).send(MessageBuilder.createMessage(body,
                new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, contentType))));
    }
}