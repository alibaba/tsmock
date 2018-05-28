package com.alibaba.tsmock.config;
/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MockServerConfig.java
 * @Prject: tsmock
 * @Package:
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月6日下午12:05:05
 * @version: v1.0
 */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.tsmock.core.router.http.HttpRouter;
import com.alibaba.tsmock.log.Log4jInitor;
import com.alibaba.tsmock.po.MockConfig;
import com.alibaba.tsmock.po.http.HttpMockConfigDefaults;
import com.alibaba.tsmock.po.mq.MqMockConfig;
import com.alibaba.tsmock.util.JSONUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.tsmock.po.http.HttpMockConfig;

/**
 * @ClassName: MockServerConfig
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月6日下午12:05:05
 */
public class MockServerConfig {
	public static String httpMockConfigStr;
	public static HttpMockConfig httpMockConfig;
	public static String mqMockConfigStr;
	public static MqMockConfig mqMockConfig;

	public static boolean init(EnumConfigType configType, final String fileName,Map<String,String> logProps) {
		boolean result = true;
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String formatDate = sdf.format(date);
		System.out.println("["+formatDate+"] [" + configType + "] Start to init mock with configuration file:[" + fileName + "]");
		if (configType == EnumConfigType.HTTP) {
			if (httpMockConfig == null) {
				try {
					httpMockConfigStr = FileUtils.readFileToString(new File(fileName));
					String httpMockConfigMinifyStr = JSONUtil.minify(httpMockConfigStr);

					httpMockConfig = JSON.parseObject(httpMockConfigMinifyStr, HttpMockConfig.class);
					logProps.put("log4j.rootLogger", httpMockConfig.getLog().getLogLevel().toUpperCase() + ", file, stdout");
					HttpMockConfigDefaults httpMockConfigDefaults = httpMockConfig.getDefaults();
					if (httpMockConfigDefaults!=null) {
						Integer defaultStatusCode = httpMockConfigDefaults.getStatusCode();
						String defaultContentType = httpMockConfigDefaults.getContentType();
						String defaultBody = httpMockConfigDefaults.getBody();

						httpMockConfig.setDefaultResponseContentType(defaultContentType);

					}
					HttpRouter.initRouter(httpMockConfig.getRouteTable());
				} catch (final IOException ioe) {
					System.err.println("[HTTP] Get exception:" + ioe);
					result = false;
				}
			}
		} else if (configType == EnumConfigType.MQ) {
			if (mqMockConfig == null) {
				try {
					mqMockConfigStr = FileUtils.readFileToString(new File(fileName));
					String mqMockConfigMinifyStr = JSONUtil.minify(mqMockConfigStr);
					mqMockConfig = JSON.parseObject(mqMockConfigMinifyStr, MqMockConfig.class);
					logProps.put("log4j.rootLogger", httpMockConfig.getLog().getLogLevel().toUpperCase() + ", file, stdout");
				} catch (final Exception e) {
					result = false;
					System.err.println("[MQ] Get exception:" + e);
				}
			}
		} else {
			result = false;
			System.err.println("[" + configType + "] Invalid Type:" + configType);
		}
		Log4jInitor.setup(logProps);
		final Logger logger = LoggerFactory.getLogger(MockServerConfig.class);
		logger.info("[" + configType + "] Init mock with configuration file:[" + fileName + "] successful");
		return result;
	}

	public static MockConfig getConfig(EnumConfigType configType) {
		if (configType == EnumConfigType.HTTP) {
			return httpMockConfig;
		} else if (configType == EnumConfigType.MQ) {
			return mqMockConfig;
		}
		return null;
	}
}
