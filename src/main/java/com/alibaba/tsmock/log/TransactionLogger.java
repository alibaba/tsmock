/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: TrasactionLogger.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.log
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月9日下午12:09:57
 * @version: v1.0
 */
package com.alibaba.tsmock.log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.tsmock.config.EnumConfigType;
import com.alibaba.tsmock.config.MockServerConfig;
import com.alibaba.tsmock.constants.Config;
import com.alibaba.tsmock.main.TSMockMain;
import com.alibaba.tsmock.po.http.HttpMockConfig;
import com.alibaba.tsmock.po.http.HttpRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HttpServerExchange;

/**
 * @ClassName: TrasactionLogger
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月9日下午12:09:57
 */
public class TransactionLogger {
	private static final Logger logger = LoggerFactory.getLogger(TransactionLogger.class);

	public static void logHttpRequest(HttpServerExchange exchange, HttpRequest httpRequest) {
		logString("HTTP REQUEST:");
		final String method = httpRequest.getHttpMethod();
		logString("HTTP REQUEST Method:" + method);
		final String path = httpRequest.getPath();
		logString("HTTP REQUEST Path:" + path);
		final Map<String, List<String>> queryParams = httpRequest.getQueryParams();
		if ((queryParams != null) && (queryParams.size() > 0)) {
			for (final String name : queryParams.keySet()) {
				logString("HTTP REQUEST QueryParam Name:" + name);
				final List<String> values = queryParams.get(name);
				if ((values != null) && (values.size() > 0)
						&& (!((values.size() == 1) && StringUtils.isEmpty(values.get(0))))) {
					for (final String value : values) {
						logString("HTTP REQUEST QueryParam Value:" + value);
					}
				}
			}
		}

		logString("HTTP REQUEST Header:" + exchange.getRequestHeaders());

		final String body = httpRequest.getBody();
		if (!StringUtils.isEmpty(body)) {
			HttpMockConfig httpMockConfig = (HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP);
			if (httpMockConfig.getLog().getLogBody()) {
				logString("HTTP REQUEST Body:" + body);
			} else {
				logString("HTTP REQUEST Body:[disabled recording in configuration file]");
			}
		}
	}

	public static void logHttpResponse(HttpServerExchange exchange, String body) {
		logString("HTTP RESPONSE:");
		logString("HTTP RESPONSE Header:" + exchange.getResponseHeaders());
		HttpMockConfig httpMockConfig = (HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP);
		if (httpMockConfig.getLog().getLogBody()) {
			logString("HTTP RESPONSE Body:" + body);
		} else {
			logString("HTTP RESPONSE Body:[disabled recording in configuration file]");
		}
	}

	public static void logHttpCallbackHttp(String method, String url, String body) {
		logString("HTTP CALLBACK:");
		logString("HTTP CALLBACK Method:" + method);
		logString("HTTP CALLBACK Url:" + url);
		HttpMockConfig httpMockConfig = (HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP);
		if (httpMockConfig.getLog().getLogBody()) {
			logString("HTTP CALLBACK Body:" + body);
		} else {
			logString("HTTP CALLBACK Body:[disabled recording in configuration file]");
		}
	}

	public static void logHttpCallbackMq(String topic, String tag, String key, String value) {
		logString("MQ CALLBACK:");
		logString("MQ CALLBACK Topic:" + topic);
		logString("MQ CALLBACK Tag:" + tag);
		logString("MQ CALLBACK Key:" + key);
		HttpMockConfig httpMockConfig = (HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP);
		if (httpMockConfig.getLog().getLogBody()) {
			logString("MQ CALLBACK Value:" + value);
		} else {
			logString("MQ CALLBACK Value:[disabled recording in configuration file]");
		}
	}

	public static void logHttpCallbackWebsocket(String url, String body) {
		logString("WEBSOCKET CALLBACK:");
		logString("WEBSOCKET CALLBACK url:" + url);
		HttpMockConfig httpMockConfig = (HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP);
		if (httpMockConfig.getLog().getLogBody()) {
			logString("WEBSOCKET CALLBACK Body:" + body);
		} else {
			logString("WEBSOCKET CALLBACK Body:[disabled recording in configuration file]");
		}
	}

	public static void logString(String msg) {
		final String transLogFile = TSMockMain.logOptionStr + File.separator + Config.TRANS_LOG;
		final String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
		final String threadName = Thread.currentThread().getName();
		final String finalStr = "[" + dateStr + "] [" + threadName + "] " + msg + "\n";
		try {
			FileUtils.writeStringToFile(new File(transLogFile), finalStr, true);
		} catch (final IOException ioe) {
			logger.warn("Get exception when save transaction:" + ioe);
		}
	}
}
