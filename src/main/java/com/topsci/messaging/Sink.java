package com.topsci.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: dingwei
 * @Date: 2020/06/24/13:45
 * @Description:
 **/
public interface Sink {
    String INPUT_RECEIVE = "inputrec";
    String INPUT_RECEIVE_STATUS = "inputstatus";

    @Input(INPUT_RECEIVE)
    SubscribableChannel input1();

    @Input(INPUT_RECEIVE_STATUS)
    SubscribableChannel input2();
}
