package com.topsci.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: dingwei
 * @Date: 2020/06/24/13:51
 * @Description: 自己定义通道接口
 **/
public interface CustomizeChannel {
    /**
     * 发消息的通道名称
     */
    String OUTPUT = "output";

    /**
     * 消息的订阅通道名称
     */
    String INPUT = "input";

    /**
     * 发消息的通道
     *
     * @return MessageChannel
     */
    @Output(OUTPUT)
    MessageChannel sendMessage();

    /**
     * 收消息的通道
     *
     * @return SubscribableChannel
     */
    @Input(INPUT)
    SubscribableChannel recieveMessage();
}
