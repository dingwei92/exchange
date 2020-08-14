package com.topsci.kafka.consume;

import com.topsci.config.PropertiesBean;
import com.topsci.tools.kafka.consume.ConsumeThread;
import com.topsci.tools.kafka.consume.KafkaConsumer;
import com.topsci.util.SpringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yanhan
 * 订阅线程
 */
public class ConsumerDataThread implements Runnable{

	/**
	 * 订阅的topic名称
	 */
	private String topic;
	private Class<? extends ConsumeThread> className;
	private PropertiesBean propertiesBean = SpringUtil.getBean(PropertiesBean.class);

	public ConsumerDataThread(String topic,Class<? extends ConsumeThread> className){
		this.topic = topic;
		this.className = className;
	}
	@Override
	public void run() {
		try {
            Map<String,Object> props = new HashMap<>();
            props.put("nocmcheck","no");
            props.put("group.id","qhserver");
            props.put("max.poll.records",10);
			KafkaConsumer consumer = new KafkaConsumer(propertiesBean.getKafkaBrokers(),topic,propertiesBean.getSslCertLocation(),props);
			while(true) {
				consumer.consume(className,propertiesBean.getReceivePoolSize());
			}
		} catch (Exception e) {
			System.out.println("订阅的topic "+topic+" 异常！");
			e.printStackTrace();
		}
		
	}

}
