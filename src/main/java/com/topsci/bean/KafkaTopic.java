package com.topsci.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author yanhan
 * kafka topic记录表
 */
@Document
public class KafkaTopic{
	@Id
	private int id;

	private String topicName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}


	
	
}
