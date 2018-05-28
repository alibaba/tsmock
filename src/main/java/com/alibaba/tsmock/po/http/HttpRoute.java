/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: Route.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:28:34
 * @version: v1.0
 */

package com.alibaba.tsmock.po.http;

import java.util.List;

/**
 * Class that will contain a Request Route. A route is composed of a request and
 * response
 *
 * @ClassName: Route
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:28:34
 */
public class HttpRoute {

	String name;

	/** The request. */
	private HttpRouteRequest request;

	/** The response. */
	private HttpRouteResponse response;

	/** The callback. */
	private List<HttpRouteCallback> callbacks;

	private HttpRouteProxy proxy;

	/**
	 * Instantiates a new route.
	 */
	public HttpRoute() {

	}

	/**
	 * Instantiates a new route.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param callback
	 *            the callback
	 * @param project
	 *            the project
	 */
	public HttpRoute(final String name, final HttpRouteRequest request, final HttpRouteResponse response,
			final List<HttpRouteCallback> callbacks) {
		this.name = name;
		this.request = request;
		this.response = response;
		this.callbacks = callbacks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the request.
	 *
	 * @return the request
	 */
	public HttpRouteRequest getRequest() {
		return request;
	}

	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public HttpRouteResponse getResponse() {
		return response;
	}

	/**
	 * Sets the request.
	 *
	 * @param request
	 *            the new request
	 */
	public void setRequest(final HttpRouteRequest request) {
		this.request = request;
	}

	/**
	 * Sets the response.
	 *
	 * @param response
	 *            the new response
	 */
	public void setResponse(final HttpRouteResponse response) {
		this.response = response;
	}

	public HttpRouteProxy getProxy() {
		return proxy;
	}

	public void setProxy(HttpRouteProxy proxy) {
		this.proxy = proxy;
	}

	public List<HttpRouteCallback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<HttpRouteCallback> callbacks) {
		this.callbacks = callbacks;
	}

	@Override
	public String toString() {
		return "HTTP Route {" + "request=" + request + ", respone='" + response + '\'' + ",callbacks='" + callbacks
				+ ",proxy=" + proxy + '}';
	}

}