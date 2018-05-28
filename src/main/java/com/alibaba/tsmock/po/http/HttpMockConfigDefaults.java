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
public class HttpMockConfigDefaults {
	Integer statusCode;
	String  body;
	String  contentType;

	public HttpMockConfigDefaults() {

	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "statusCode:" + statusCode + ",body:" + body +",contentType:"+contentType;
	}
}
