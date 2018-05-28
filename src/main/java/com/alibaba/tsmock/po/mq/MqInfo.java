/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MqInfo.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model.mq
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-10 am 11:37:49
 * @version: v1.0
 */
package com.alibaba.tsmock.po.mq;

/**
 * @ClassName: MqInfo
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-10 am 11:37:49
 */
public class MqInfo {
	String consumer;
	String topic;
	String tag;

	public String getConsumer() {
		return consumer;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
