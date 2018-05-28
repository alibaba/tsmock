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

package com.alibaba.tsmock.po.mq;

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
public class MqRoute {

	String  name;

	/** The request. */
	private MqRouteRequest request;

	/** The response. */
	private MqRouteResponse response;

	/** The callback. */
	private List<MqRouteCallback> callbacks;

	/**
	 * Instantiates a new route.
	 */
	public MqRoute() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MqRouteRequest getRequest() {
		return request;
	}

	public void setRequest(MqRouteRequest request) {
		this.request = request;
	}

	public MqRouteResponse getResponse() {
		return response;
	}

	public void setResponse(MqRouteResponse response) {
		this.response = response;
	}

	public List<MqRouteCallback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<MqRouteCallback> callbacks) {
		this.callbacks = callbacks;
	}

	@Override
	public String toString() {
		return "MQ Route {" + "request=" + request + ", respone='" + response + '\'' + ",callbacks='" + callbacks + '}';
	}
}