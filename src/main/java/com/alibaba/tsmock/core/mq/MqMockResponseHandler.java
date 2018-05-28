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
package com.alibaba.tsmock.core.mq;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.tsmock.po.mq.MqRoute;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.tsmock.po.mq.MqRouteCallback;
import com.alibaba.tsmock.po.mq.MqRouteInfo;
import com.alibaba.tsmock.po.mq.MqRouteRequest;
import com.alibaba.tsmock.po.mq.MqRouteResponse;
import com.alibaba.tsmock.scriptengine.ScriptEngine;
import com.alibaba.tsmock.util.HttpUtil;
import com.alibaba.tsmock.util.MqSender;
import com.alibaba.tsmock.util.SleepUtil;
import com.alibaba.tsmock.util.WebSocketUtil;

/**
 * The Class ResponseHandler.
 *
 * @ClassName: ResponseHandler
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 */
public class MqMockResponseHandler implements MessageListenerConcurrently {
	MqRouteInfo mqRouteInfo = null;

	public MqMockResponseHandler(MqRouteInfo mqRouteInfo) {
		this.mqRouteInfo = mqRouteInfo;
	}

	/** The logger. */
	private final static Logger logger = LoggerFactory.getLogger(MqMockResponseHandler.class);

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		logger.info("===============Receive one MQ request===============");
		logger.info("[MQ] Match the requst with route:");
		final MessageExt msg = msgs.get(0);
		final String requestKey = msg.getKeys();
		logger.info("[MQ] Request key          : " + requestKey);
		final String requestBody = new String(msg.getBody());
		logger.info("[MQ] Request body         : " + requestBody);

		final List<MqRoute> mqRouteList = mqRouteInfo.getRoute();

		for (final MqRoute mqRoute : mqRouteList) {

			final MqRouteRequest mqRouteRequest = mqRoute.getRequest();
			final String routeKey = mqRouteRequest.getKey();
			final String routeBodyRegex = mqRouteRequest.getBodyRegex();
			boolean matchFlag = false;
			if (StringUtils.isEmpty(routeKey)) {
				if (StringUtils.isEmpty(routeBodyRegex)) {
					matchFlag = true;
				} else {
					final Pattern pattern = Pattern.compile(routeBodyRegex);
					final Matcher matcher = pattern.matcher(requestBody);
					if (matcher.find()) {
						matchFlag = true;
					}
				}
			} else {
				if(!StringUtils.isEmpty(requestKey)) {
					if (requestKey.equalsIgnoreCase(routeKey)) {
						if (StringUtils.isEmpty(routeBodyRegex)) {
							matchFlag = true;
						} else {
							final Pattern pattern = Pattern.compile(routeBodyRegex);
							final Matcher matcher = pattern.matcher(requestBody);
							if (matcher.find()) {
								matchFlag = true;
							}
						}
					}
				}
			}
			if (matchFlag == true) {
				logger.info("[MQ] Find matched route:" + mqRoute);

				final String requestScriptFile = mqRouteRequest.getScript();
				if (!StringUtils.isEmpty(requestScriptFile)) {
					final String escapedRequestBody = StringUtils.replace(requestBody, "\"", "\\\"");
					String script = ScriptEngine.loadScriptFromFile(requestScriptFile);
					script = ScriptEngine.preProcessMqRequest(script, requestKey, escapedRequestBody);
					final boolean verifyResult = ScriptEngine.executeWithBooleanResult(script);
					if (verifyResult == false) {
						logger.error("[MQ] Failed to verify the request with script, will not do any further process");
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}
				}

				final MqRouteResponse mqResponse = mqRoute.getResponse();
				final String responseProducer = mqResponse.getProducer();
				final String responseTopic = mqResponse.getTopic();
				final String responseTag = mqResponse.getTag();
				final String responseKey = mqResponse.getKey();
				String responseBody = mqResponse.getBody();
				final String responseScriptFile = mqResponse.getScript();
				final int responseSleep = mqResponse.getSleep();

				if (!StringUtils.isEmpty(responseScriptFile)) {
					final String escapedRequestBody = StringUtils.replace(requestBody, "\"", "\\\"");
					final String escapedResponseBody = StringUtils.replace(responseBody, "\"", "\\\"");
					String script = ScriptEngine.loadScriptFromFile(responseScriptFile);
					script = ScriptEngine.preProcessMqResponse(script, requestKey, escapedRequestBody, responseKey,
							escapedResponseBody);
					responseBody = ScriptEngine.executeWithStringResult(script);

				}

				SleepUtil.sleep(responseSleep);

				final MqSender mqSender = MqSender.getMqSender(responseProducer);
				mqSender.send(responseTopic, responseTag, responseKey, responseBody);

				final List<MqRouteCallback> mqRouteCallbacks = mqRoute.getCallbacks();
				if (mqRouteCallbacks != null) {
					for (final MqRouteCallback mqRouteCallback : mqRouteCallbacks) {

						if ((mqRouteCallback != null) && (mqRouteCallback.getType() != null)) {
							logger.info("[MQ] Callback that will be process:", mqRouteCallback);
							if (mqRouteCallback.getType().equalsIgnoreCase("mq")) {
								processMqRouteCallback(mqSender, mqRouteCallback, requestKey, requestBody, responseKey,
										responseBody);
							} else if (mqRouteCallback.getType().equalsIgnoreCase("http")) {
								processHttpRouteCallback(mqRouteCallback, requestKey, requestBody, responseKey,
										responseBody);
							} else if (mqRouteCallback.getType().equalsIgnoreCase("websocket")) {
								processWebsocketRouteCallback(mqRouteCallback, requestKey, requestBody, responseKey,
										responseBody);
							}
						}

					}

				}
			}
		}

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

	private boolean processMqRouteCallback(MqSender mqSender, MqRouteCallback mqRouteCallback, String requestKey,
			String requestBody, String responseKey, String responseBody) {
		boolean result = true;
		try {
			String info = mqRouteCallback.getInfo();
			logger.info("[MQ] Callback info:[{}]", info);
			String value = mqRouteCallback.getValue();
			logger.info("[MQ] Callback value:[{}]", value);
			final String callbackSleep = mqRouteCallback.getSleep();
			logger.info("[MQ] Callback sleep:[{}]", callbackSleep);

			final String callbackScriptFile = mqRouteCallback.getScript();
			if (!StringUtils.isEmpty(callbackScriptFile)) {
				final String escapedInfo = StringUtils.replace(info, "\"", "\\\"");
				final String escapedValue = StringUtils.replace(value, "\"", "\\\"");
				String script = ScriptEngine.loadScriptFromFile(callbackScriptFile);
				script = ScriptEngine.preProcessMqCallback(script, requestKey, requestBody, responseKey, responseBody,
						escapedInfo, escapedValue);
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

			final int sleepTime = Integer.valueOf(callbackSleep);
			SleepUtil.sleep(sleepTime);

			mqSender.send(topic, tag, key, value);

		} catch (final Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		return result;
	}

	private boolean processHttpRouteCallback(MqRouteCallback mqRouteCallback, String requestKey, String requestBody,
			String responseKey, String responseBody) {
		boolean result = true;
		try {
			String info = mqRouteCallback.getInfo();
			logger.info("[MQ] Callback info:[{}]", info);
			String value = mqRouteCallback.getValue();
			logger.info("[MQ] Callback value:[{}]", value);
			final String sleep = mqRouteCallback.getSleep();
			logger.info("[MQ] Callback sleep:[{}]", sleep);
			String method = null;
			String url = null;

			final String callbackScriptFile = mqRouteCallback.getScript();
			if (!StringUtils.isEmpty(callbackScriptFile)) {
				final String escapedInfo = StringUtils.replace(info, "\"", "\\\"");
				final String escapedValue = StringUtils.replace(value, "\"", "\\\"");
				responseBody = StringUtils.replace(responseBody, "\"", "\\\"");
				String script = ScriptEngine.loadScriptFromFile(callbackScriptFile);
				script = ScriptEngine.preProcessMqCallback(script, requestKey, requestBody, responseKey, responseBody,
						escapedInfo, escapedValue);
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

			final int sleepTime = Integer.valueOf(sleep);
			Thread.sleep(sleepTime);

			if (method.equalsIgnoreCase("get")) {
				HttpUtil.sendGet(url, null);
			} else if (method.equalsIgnoreCase("post")) {
				HttpUtil.sendPost(url, null, value, ContentType.APPLICATION_JSON);
			}

		} catch (final Exception e) {
			logger.error(e.getMessage());
			result = false;
		}

		return result;
	}

	private boolean processWebsocketRouteCallback(MqRouteCallback mqRouteCallback, String requestKey,
			String requestBody, String responseKey, String responseBody) {
		boolean result = true;
		try {
			String info = mqRouteCallback.getInfo();
			logger.info("[MQ] Callback info:[{}]", info);
			String value = mqRouteCallback.getValue();
			logger.info("[MQ] Callback value:[{}]", value);
			final String sleep = mqRouteCallback.getSleep();
			logger.info("[MQ] Callback sleep:[{}]", sleep);

			final String callbackScriptFile = mqRouteCallback.getScript();
			if (!StringUtils.isEmpty(callbackScriptFile)) {
				final String escapedInfo = StringUtils.replace(info, "\"", "\\\"");
				final String escapedValue = StringUtils.replace(value, "\"", "\\\"");
				String script = ScriptEngine.loadScriptFromFile(callbackScriptFile);
				script = ScriptEngine.preProcessMqCallback(script, requestKey, requestBody, responseKey, responseBody,
						escapedInfo, escapedValue);
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

			final int sleepTime = Integer.valueOf(sleep);
			Thread.sleep(sleepTime);

			final WebSocketUtil webSocketUtil = new WebSocketUtil(new URI(url));

			webSocketUtil.sendMessage(value);

		} catch (final Exception e) {
			logger.error(e.getMessage());
			result = false;
		}
		return result;
	}

}