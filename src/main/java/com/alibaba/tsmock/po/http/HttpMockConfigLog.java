/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: ServerOption.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.po.http
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月9日上午11:15:50
 * @version: v1.0
 */
package com.alibaba.tsmock.po.http;

/**
 * @ClassName: HttpMockConfigDefaults
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 22017-1-7 16:46:24
 */
public class HttpMockConfigLog {
	String 		logLevel;
	Boolean  	logBody;

	public HttpMockConfigLog() {

	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public Boolean getLogBody() {
		return logBody;
	}

	public void setLogBody(Boolean logBody) {
		this.logBody = logBody;
	}

	@Override
	public String toString() {
		return "logLevel:" + logLevel + ",logBody:" + logBody ;
	}
}
