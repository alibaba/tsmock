/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: ResponseHandler.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.core
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 17:06:15
 * @version: v1.0
 */
package com.alibaba.tsmock.core.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Scanner;
import java.util.zip.Deflater;

import com.alibaba.tsmock.config.MockServerConfig;
import com.alibaba.tsmock.constants.Encode;
import com.alibaba.tsmock.log.TransactionLogger;
import com.alibaba.tsmock.scriptengine.ScriptEngine;
import com.alibaba.tsmock.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.tsmock.po.http.HttpRequest;
import com.alibaba.tsmock.po.http.HttpRouteCallback;
import com.alibaba.tsmock.po.http.HttpRouteHeader;
import com.alibaba.tsmock.po.http.HttpRouteRequest;
import com.alibaba.tsmock.po.http.HttpRouteResponse;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

/**
 * The Class ResponseHandler.
 *
 * @ClassName: ResponseHandler
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 */
class HttpMockResponseHandler {

	/** The logger. */
	private final static Logger logger = LoggerFactory.getLogger(HttpMockResponseHandler.class);

	/**
	 * Process.
	 *
	 * @param exchange
	 *            the exchange
	 * @param routeRequest
	 *            the request
	 * @param routeResponse
	 *            the response
	 */
	public void process(final HttpServerExchange exchange, HttpRequest httpRequest, HttpRouteRequest routeRequest,
						final HttpRouteResponse routeResponse, final List<HttpRouteCallback> httpRouteCallbacks) {
		String routeResponseBody = null;
		String escapedRouteResponseBody = null;
		String escapedRequestBody = null;

		if (routeResponse.isBodyPointingToFile() == true) {
			try {
				routeResponseBody = FileUtils.readFileToString(new File(routeResponse.getBody()));
			} catch (final IOException ioe) {
				logger.error("[HTTP] Get exception:" + ioe.getMessage());
			}
		} else {
			routeResponseBody = routeResponse.getBody();
		}

		if (routeResponseBody==null){
			routeResponseBody = MockServerConfig.httpMockConfig.getDefaults().getBody();
			if (routeResponseBody==null) {
				routeResponseBody="";
			}
		}


		final String requestBody = httpRequest.getBody();
		logger.debug("[HTTP] Request body:" + requestBody);
		logger.debug("[HTTP] Route Response body:" + routeResponseBody);

		final String responseScriptFile = routeResponse.getScript();
		if (!StringUtils.isEmpty(responseScriptFile)) {
			escapedRequestBody = StringUtil.escapeStr(requestBody);
			escapedRouteResponseBody = StringUtil.escapeStr(routeResponseBody);
			String script = ScriptEngine.loadScriptFromFile(responseScriptFile);
			script = ScriptEngine.preProcessHttpResponse(script, escapedRequestBody, escapedRouteResponseBody,
					exchange.getQueryString());
			escapedRouteResponseBody = ScriptEngine.executeWithStringResult(script);
			logger.debug("[HTTP] Response body after script process:" + escapedRouteResponseBody);
			routeResponse.setBody(escapedRouteResponseBody);
		} else {
			routeResponse.setBody(routeResponseBody);
		}

		if (StringUtils.isEmpty(routeResponse.getContentType())) {
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MockServerConfig.httpMockConfig.getDefaults().getContentType());
		}
		else {
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, routeResponse.getContentType());
		}

		if (routeResponse.getStatusCode()==null) {
			exchange.setResponseCode(MockServerConfig.httpMockConfig.getDefaults().getStatusCode());
		}
		else{
			exchange.setResponseCode(routeResponse.getStatusCode());
		}

		setResponseHeaders(routeResponse, exchange);
		final Integer responseSleep = routeResponse.getSleep();
		if (responseSleep !=null) {
			logger.info("[HTTP] Sleep {} seconds before send response", responseSleep / 1000);
			SleepUtil.sleep(responseSleep);

		}
		final String responseBody = routeResponse.getBody();
		logger.info("[HTTP] Send response:" + responseBody);
		TransactionLogger.logString("------------------------------------------------------------------------------");
		TransactionLogger.logHttpResponse(exchange, responseBody);
		sendResponseBody(exchange, routeResponse);

		if (httpRouteCallbacks != null) {
			for (final HttpRouteCallback httpRouteCallback : httpRouteCallbacks) {
				if ((httpRouteCallback != null) && (httpRouteCallback.getType() != null)) {
					logger.info("[HTTP] Callback that will be process:{}", httpRouteCallback);
					if (httpRouteCallback.getType().equalsIgnoreCase("mq")) {
						processMqCallback(httpRouteCallback, exchange, escapedRequestBody, escapedRouteResponseBody);
					} else if (httpRouteCallback.getType().equalsIgnoreCase("http")) {
						processHttpCallback(httpRouteCallback, exchange, escapedRequestBody, escapedRouteResponseBody);
					} else if (httpRouteCallback.getType().equalsIgnoreCase("websocket")) {
						processWebSocketCallback(httpRouteCallback, exchange, escapedRequestBody,
								escapedRouteResponseBody);
					}
				}
			}
		}
		routeResponse.setBody(routeResponseBody);
	}

	/**
	 * Define response body.
	 *
	 * @param exchange
	 *            the exchange
	 * @param response
	 *            the response
	 */
	private void sendResponseBody(final HttpServerExchange exchange, final HttpRouteResponse response) {
		if (response.getBody() != null) {
			String compress = response.getCompress();
			if (!StringUtils.isEmpty(compress) && compress.equalsIgnoreCase("gzip")) {
				final byte[] gZipBody = CompressUtil.compress(response.getBody().getBytes());
				final ByteBuffer buffer = ByteBuffer.wrap(gZipBody);
				exchange.getResponseSender().send(buffer);
			} else {
				exchange.getResponseSender().send(response.getBody());
			}
			return;
		}
	}

	/**
	 * Sets the response headers.
	 *
	 * @param response
	 *            the response
	 * @param exchange
	 *            the exchange
	 */
	private void setResponseHeaders(final HttpRouteResponse response, final HttpServerExchange exchange) {
		for (final HttpRouteHeader header : response.getHeaders()) {
			for (final String value : header.getValueList()) {
				exchange.getResponseHeaders().add(new HttpString(header.getName()), value);
			}
		}
	}



	/**
	 * Extract body.
	 *
	 * @param exchange
	 *            the exchange
	 * @return the string
	 */
	private String extractBody(final HttpServerExchange exchange) {
		if (exchange == null) {
			return null;
		}

		exchange.startBlocking();

		final Scanner scanner = new Scanner(exchange.getInputStream(), Encode.ENCODING.value).useDelimiter("\\A");

		if (!scanner.hasNext()) {
			return null;
		}

		return scanner.next();
	}

	private boolean processMqCallback(HttpRouteCallback httpRouteCallback, HttpServerExchange exchange,
			String escapedRequestBody, String escapedRouteResponseBody) {
		boolean result = true;
		try {
			String info = httpRouteCallback.getInfo();
			logger.debug("[HTTP] Callback info:[{}]", info);
			String value = httpRouteCallback.getValue();
			logger.debug("[HTTP] Callback value:[{}]", value);
			final Integer sleep = httpRouteCallback.getSleep();
			logger.debug("[HTTP] Callback sleep:[{}]", sleep);

			final String callbackScriptFile = httpRouteCallback.getScript();
			if (!StringUtils.isEmpty(callbackScriptFile)) {
				final String escapedInfo = StringUtil.escapeStr(info);
				final String escapedValue = StringUtil.escapeStr(value);
				String script = ScriptEngine.loadScriptFromFile(callbackScriptFile);
				script = ScriptEngine.preProcessHttpCallback(script, escapedRequestBody, escapedRouteResponseBody,
						escapedInfo, escapedValue, exchange.getQueryString());
				final Object obj = ScriptEngine.executeWithObjectResult(script);

				final Class<?> cls = obj.getClass();

				final Method getInfoMethod = cls.getDeclaredMethod("getInfo");
				final Method getValueMethod = cls.getDeclaredMethod("getValue");
				final Object infoObj = getInfoMethod.invoke(obj);
				final Object valueObj = getValueMethod.invoke(obj);

				info = infoObj.toString();
				value = valueObj.toString();
			}

			String producer = null;
			String topic = null;
			String tag = null;
			String key = null;

			final String[] infos = info.split(",");
			for (final String mqInfo : infos) {
				if (mqInfo.startsWith("producer=")) {
					producer = mqInfo.substring(9);
				} else if (mqInfo.startsWith("topic=")) {
					topic = mqInfo.substring(6);
				} else if (mqInfo.startsWith("tag=")) {
					tag = mqInfo.substring(4);
				} else if (mqInfo.startsWith("key=")) {
					key = mqInfo.substring(4);
				}
			}

			if (sleep!=null)
				Thread.sleep(sleep);

			final StringBuffer sb = new StringBuffer();
			sb.append("Topic:" + topic + "\n");
			sb.append("Tag:" + tag + "\n");
			sb.append("Key:" + key + "\n");
			sb.append("Value:" + value + "\n");
			TransactionLogger
					.logString("------------------------------------------------------------------------------");
			TransactionLogger.logHttpCallbackMq(topic, tag, key, value);
			final MqSender mqSender = MqSender.getMqSender();
			mqSender.send(topic, tag, key, value);

		} catch (final Exception e) {
			logger.error("Get exception:" + e.getMessage());
			result = false;
		}
		return result;
	}

	private boolean processHttpCallback(HttpRouteCallback httpRouteCallback, HttpServerExchange exchange,
			String escapedRequestBody, String escapedRouteResponseBody) {
		boolean result = true;
		try {
			String info = httpRouteCallback.getInfo();
			logger.debug("[HTTP] Callback info:[{}]", info);
			String value = httpRouteCallback.getValue();
			logger.debug("[HTTP] Callback value:[{}]", value);
			final Integer callbackSleep = httpRouteCallback.getSleep();

			final String callbackScriptFile = httpRouteCallback.getScript();

			String method = null;
			String url = null;

			if (!StringUtils.isEmpty(callbackScriptFile)) {
				logger.debug("[HTTP] Callback script:[{}]", callbackScriptFile);
				final String escapedInfo = StringUtil.escapeStr(info);
				final String escapedValue = StringUtil.escapeStr(value);
				escapedRouteResponseBody = StringUtils.replace(escapedRouteResponseBody, "\"", "\\\"");
				String script = ScriptEngine.loadScriptFromFile(callbackScriptFile);
				script = ScriptEngine.preProcessHttpCallback(script, escapedRequestBody, escapedRouteResponseBody,
						escapedInfo, escapedValue, exchange.getQueryString());
				final Object obj = ScriptEngine.executeWithObjectResult(script);

				final Class<?> cls = obj.getClass();

				final Method getInfoMethod = cls.getDeclaredMethod("getInfo");
				final Method getValueMethod = cls.getDeclaredMethod("getValue");
				final Object infoObj = getInfoMethod.invoke(obj);
				final Object valueObj = getValueMethod.invoke(obj);

				info = infoObj.toString();
				value = valueObj.toString();
			}

			final String[] infos = info.split(",");
			for (final String httpInfo : infos) {
				if (httpInfo.startsWith("method=")) {
					method = httpInfo.substring(7);
				} else if (httpInfo.startsWith("url=")) {
					url = httpInfo.substring(4);
				}
			}
			if (callbackSleep!=null) {
				logger.debug("[HTTP] Callback sleep:[{}]", callbackSleep);
				SleepUtil.sleep(callbackSleep);
			}


			final StringBuffer sb = new StringBuffer();
			sb.append("Method:" + method + "\n");
			sb.append("Url:" + url + "\n");
			if (method.equalsIgnoreCase("get")) {
				HttpUtil.sendGet(url, null);
			} else if (method.equalsIgnoreCase("post")) {
				sb.append("Body:" + value + "\n");
				HttpUtil.sendPost(url, null, value, ContentType.APPLICATION_JSON);
			}
			TransactionLogger
					.logString("------------------------------------------------------------------------------");
			TransactionLogger.logHttpCallbackHttp(method, url, value);

		} catch (final Exception e) {
			logger.error("Get exception:" + e.getMessage());
			result = false;
		}
		return result;
	}

	private boolean processWebSocketCallback(HttpRouteCallback httpRouteCallback, HttpServerExchange exchange,
			String escapedRequestBody, String escapedRouteResponseBody) {
		boolean result = true;
		try {
			String info = httpRouteCallback.getInfo();
			logger.debug("[HTTP] Callback info:[{}]", info);
			String value = httpRouteCallback.getValue();
			logger.debug("[HTTP] Callback value:[{}]", value);
			final Integer sleep = httpRouteCallback.getSleep();
			logger.debug("[HTTP] Callback sleep:[{}]", sleep);

			final String callbackScriptFile = httpRouteCallback.getScript();
			if (!StringUtils.isEmpty(callbackScriptFile)) {
				final String escapedInfo = StringUtil.escapeStr(info);
				final String escapedValue = StringUtil.escapeStr(value);
				String script = ScriptEngine.loadScriptFromFile(callbackScriptFile);
				script = ScriptEngine.preProcessHttpCallback(script, escapedRequestBody, escapedRouteResponseBody,
						escapedInfo, escapedValue, exchange.getQueryString());
				final Object obj = ScriptEngine.executeWithObjectResult(script);

				final Class<?> cls = obj.getClass();

				final Method getInfoMethod = cls.getDeclaredMethod("getInfo");
				final Method getValueMethod = cls.getDeclaredMethod("getValue");
				final Object infoObj = getInfoMethod.invoke(obj);
				final Object valueObj = getValueMethod.invoke(obj);

				info = infoObj.toString();
				value = valueObj.toString();
			}

			String url = null;

			final String[] infos = info.split(",");
			for (final String webSocketInfo : infos) {
				if (webSocketInfo.startsWith("url=")) {
					url = webSocketInfo.substring(4);
				}
			}

			if (sleep!=null)
				Thread.sleep(sleep);

			final WebSocketUtil webSocketUtil = new WebSocketUtil(new URI(url));
			final StringBuffer sb = new StringBuffer();
			sb.append("Body:" + value + "\n");
			TransactionLogger
					.logString("------------------------------------------------------------------------------");
			TransactionLogger.logHttpCallbackWebsocket(url, value);
			webSocketUtil.sendMessage(value);

		} catch (final Exception e) {
			logger.error("Get exception:" + e.getMessage());
			result = false;
		}
		return result;
	}

}