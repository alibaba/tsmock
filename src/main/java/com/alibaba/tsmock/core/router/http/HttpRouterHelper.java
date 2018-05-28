/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: RouteMapKeyUtil.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.util
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:54
 * @version: v1.0
 */

package com.alibaba.tsmock.core.router.http;

import io.undertow.server.HttpServerExchange;

/**
 *
 * @ClassName: RouteMapKeyUtil
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:54
 */
public final class HttpRouterHelper {

	/**
	 * Instantiates a new route map key util.
	 */
	private HttpRouterHelper() {
	}

	/**
	 * Creates the key from request.
	 *
	 * @param httpServerExchange
	 *            the http core exchange
	 * @return the string
	 */
	public static String createRequestKey(final HttpServerExchange httpServerExchange) {
		final String requestMethod = httpServerExchange.getRequestMethod().toString();
		final String requestURI = httpServerExchange.getRequestURI();

		return createRouterKey(requestMethod, requestURI, "");
	}

	/**
	 * Creates the key.
	 *
	 * @param method
	 *            the method
	 * @param path
	 *            the path
	 * @return the string
	 */
	public static String createRouterKey(final String method, final String path, String baseUrl) {
		return method + "_" + baseUrl + path;
	}
}