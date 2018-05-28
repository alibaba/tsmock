/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MockServerHandler.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.core
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 * @version: v1.0
 */

package com.alibaba.tsmock.core.http;

import java.io.IOException;

import com.alibaba.tsmock.core.router.http.HttpRouter;
import com.alibaba.tsmock.log.TransactionLogger;
import com.alibaba.tsmock.po.http.HttpRequest;
import com.alibaba.tsmock.po.http.HttpRoute;
import com.alibaba.tsmock.po.http.HttpRouteRequest;
import com.alibaba.tsmock.scriptengine.ScriptEngine;
import com.alibaba.tsmock.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * @ClassName: HttpMockServerHandler
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 */
public class HttpMockRequestHandler implements HttpHandler {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(HttpMockRequestHandler.class);

	/** The response handler. */
	private final HttpMockResponseHandler responseHandler = new HttpMockResponseHandler();

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {

		if (exchange.isInIoThread()) {
			exchange.dispatch(this);
			return;
		}

		processRequest(exchange);
	}

	/**
	 * Process request.
	 *
	 * @param exchange
	 *            the exchange
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void processRequest(HttpServerExchange exchange) throws IOException {

		try {
			logger.info("------------------------------");
			logger.info("[HTTP] Match the requst with route:");
			logger.info("[HTTP] Method  :[{}]", exchange.getRequestMethod());
			logger.info("[HTTP] URI     :[{}]", exchange.getRequestURI());
			logger.info("[HTTP] QueryStr:[{}]", exchange.getQueryString());
			final HttpRequest httpRequest = HttpRequest.getInstance(exchange, false);
			TransactionLogger
					.logString("==============================================================================");
			TransactionLogger.logHttpRequest(exchange, httpRequest);
			final HttpRoute httpRoute = HttpRouter.findMatchedRoute(exchange, httpRequest, false);
			if (httpRoute != null) {
				logger.info("[HTTP] Find matched route:" + httpRoute);
				final HttpRouteRequest routeRequest = httpRoute.getRequest();

				final String requestScriptFile = routeRequest.getScript();
				if (!StringUtils.isEmpty(requestScriptFile)) {
					String requestBody = httpRequest.getBody();
					requestBody = StringUtil.escapeStr(requestBody);
					String script = ScriptEngine.loadScriptFromFile(requestScriptFile);
					script = ScriptEngine.preProcessHttpRequest(script, requestBody, exchange.getQueryString());
					final boolean verifyResult = ScriptEngine.executeWithBooleanResult(script);
					if (verifyResult == false) {
						logger.error("Failed to process the request with script, will not do any further process");
						return;
					}
				}

				responseHandler.process(exchange, httpRequest, httpRoute.getRequest(), httpRoute.getResponse(),
						httpRoute.getCallbacks());
			}
		} catch (final RuntimeException ex) {
			logger.error("Get exception:" + ex.getMessage());
			throw ex;
		} finally {

		}

	}

}