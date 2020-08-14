package com.topsci.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: dingwei
 * @Date: 2020/06/24/13:46
 * @Description:
 **/
public interface Source {
    String OUTPUT = "output";

    @Output(Source.OUTPUT)
    MessageChannel output();

}
