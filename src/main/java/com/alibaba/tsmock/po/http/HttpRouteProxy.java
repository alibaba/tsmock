/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpRouteProxy.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.po.http
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月16日下午8:10:54
 * @version: v1.0
 */
package com.alibaba.tsmock.po.http;

/**
 * @ClassName: HttpRouteProxy
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月16日下午8:10:54
 */
public class HttpRouteProxy {
	boolean enabled;
	String targetUrl;

	public HttpRouteProxy() {

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	@Override
	public String toString() {
		return "Proxy{" + "enabled=" + enabled + ", targetUrl='" + targetUrl + '}';
	}

}
