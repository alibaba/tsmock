/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: Response.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:24
 * @version: v1.0
 */

package com.alibaba.tsmock.po.mq;

/**
 * Class that will hold all the response data.
 *
 * @ClassName: Response
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:24
 */
public final class MqRouteResponse {

	private String producer;
	private String topic;
	private String tag;
	private String key;
	private String body;
	private int sleep;
	private String script;

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public String toString() {
		return "MQ Route Response {" + "producer=" + producer + ", topic='" + topic + '\'' + ",tag='" + tag + ",key='"
				+ key + ",body='" + body + ",sleep='" + sleep + ",script='" + script + '}';
	}
}