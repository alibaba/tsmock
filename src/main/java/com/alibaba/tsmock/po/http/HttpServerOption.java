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
 * @ClassName: ServerOption
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月9日上午11:15:50
 */
public class HttpServerOption {
	String name;
	String value;

	public HttpServerOption() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "name:" + name + ",value:" + value;
	}
}
