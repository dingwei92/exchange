package com.topsci.edgexfoundry;

import com.topsci.config.PropertiesBean;
import com.topsci.tools.kafka.consume.ConsumeThread;
import com.topsci.tools.kafka.consume.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yanhan
 * 订阅线程
 */
public class ProducerDataThread implements Runnable{

	private ZeroMQEventSubscriber zeroMQEventSubscriber;

	public ProducerDataThread(ZeroMQEventSubscriber zeroMQEventSubscriber){
		this.zeroMQEventSubscriber = zeroMQEventSubscriber;
	}
	@Override
	public void run() {
        zeroMQEventSubscriber.receive();
	}

}
