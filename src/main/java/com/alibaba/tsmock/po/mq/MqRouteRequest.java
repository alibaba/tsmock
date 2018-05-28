/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: Request.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:23
 * @version: v1.0
 */

package com.alibaba.tsmock.po.mq;

/**
 * Class that will hold all the request data.
 *
 * @ClassName: Request
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:23
 */
public final class MqRouteRequest {

	private String key;
	private String bodyRegex;
	private String script;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBodyRegex() {
		return bodyRegex;
	}

	public void setBodyRegex(String bodyRegex) {
		this.bodyRegex = bodyRegex;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public String toString() {
		return "MQ Route Request {" + "key=" + key + ", bodyRegex='" + bodyRegex + '\'' + ", script='" + script + '}';

	}

}