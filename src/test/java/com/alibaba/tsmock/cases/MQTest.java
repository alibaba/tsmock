package com.alibaba.tsmock.cases;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.tsmock.base.BaseCase;
import com.alibaba.tsmock.po.MqResponse;
import com.alibaba.tsmock.po.mq.*;
import com.alibaba.tsmock.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MQTest extends BaseCase {
	private final Logger logger = LoggerFactory.getLogger(MQTest.class);

	//MQ test case need  mq env, so disable it to make ut pass, if you need ,just enable it
	@Test(enabled = false)
	public void test() {
		Map<String,List<MqResponse>> mqResponseMap = new ConcurrentHashMap<String,List<MqResponse>>();

		List<MqRouteInfo> routeTable = mqMockConfig.getRouteTable();
		MqSender mqSender = MqSender.getMqSender();
		int caseNum = 1;

		//receive
		// The message is async and may out of order , so send all then verify all
		for (int routeIndex = 1; routeIndex <= routeTable.size(); routeIndex++) {
			MqRouteInfo mqRouteInfo = routeTable.get(routeIndex - 1);
			List<MqRoute> mqRoutes = mqRouteInfo.getRoute();

			for (MqRoute mqRoute : mqRoutes) {
				logger.info("[TEST-RECEIVE] ==========================CASE [" + caseNum++ + "]==========================");
				String caseName = mqRoute.getName();
				logger.info("[TEST-RECEIVE] Case name : " + caseName);
				MqRouteResponse mqRouteResponse = mqRoute.getResponse();
				String expectResponseTopic = mqRouteResponse.getTopic();
				String expectResponseTag = mqRouteResponse.getTag();
				logger.info("[TEST-RECEIVE] topic : " + expectResponseTopic);
				logger.info("[TEST-RECEIVE] tag : " + expectResponseTag);

				String key = expectResponseTopic+"_"+expectResponseTag;
				if (!mqResponseMap.containsKey(key)) {
					List<MqResponse> mqResponseList = Collections.synchronizedList(new ArrayList<MqResponse>());
					mqResponseMap.put(key, mqResponseList);
				}
				MqPushConsumerFactory.getInstance(null,expectResponseTopic,expectResponseTag,new MqVerifyHandler(mqResponseMap,expectResponseTopic,expectResponseTag));
			}
		}

		SleepUtil.sleep(60*1000);
		caseNum = 1;
		for (int routeIndex = 1; routeIndex <= routeTable.size(); routeIndex++) {
			MqRouteInfo mqRouteInfo = routeTable.get(routeIndex - 1);
			MqInfo mqInfo = mqRouteInfo.getMq();
			String consumer = mqInfo.getConsumer();
			String requestTopic = mqInfo.getTopic();
			String requestTag = mqInfo.getTag();
			List<MqRoute> mqRoutes = mqRouteInfo.getRoute();

			//send
			for (MqRoute mqRoute : mqRoutes) {
				logger.info("[TEST-SEND] ==========================CASE [" + caseNum++ + "]==========================");
				String caseName = mqRoute.getName();
				logger.info("[TEST-SEND] Case name : " + caseName);
				logger.info("[TEST-SEND] Request topic : " + requestTopic);
				logger.info("[TEST-SEND] Request tag : " + requestTag);
				MqRouteRequest mqRouteRequest = mqRoute.getRequest();
				MqRouteResponse mqRouteResponse = mqRoute.getResponse();
				List<MqRouteCallback> mqRouteCallbacks = mqRoute.getCallbacks();

				String requestKey = mqRouteRequest.getKey();
				logger.info("[TEST-SEND] Request key : " + requestKey);
				String bodyRegex = mqRouteRequest.getBodyRegex();
				String requestBody = null;
				if (!StringUtils.isEmpty(bodyRegex)) {
					if (bodyRegex.equals("body.*")) {
						requestBody = "bodyaaaaa";
					} else if (bodyRegex.equals("sleep.*")) {
						requestBody = "sleepbbbbb";

					} else if (bodyRegex.equals("test.*")) {
						requestBody = "testaaaaa";
					}
				}
				else {
					requestBody = "{\"matched\":\"[key]\"}";
				}

				logger.info("[TEST-SEND] Request body : " + requestBody);
				mqSender.send(requestTopic, requestTag, requestKey, requestBody);

			}
		}





		SleepUtil.sleep(120*1000);
		//verify, it'a hard to verify exactly as it's mq , so only verify to a certain degree
		caseNum = 1;
		for (int routeIndex = 1; routeIndex <= routeTable.size(); routeIndex++) {
			MqRouteInfo mqRouteInfo = routeTable.get(routeIndex - 1);
			List<MqRoute> mqRoutes = mqRouteInfo.getRoute();

			for (MqRoute mqRoute : mqRoutes) {
				logger.info("[TEST-VERIFY] ==========================CASE [" + caseNum++ + "]==========================");
				String caseName = mqRoute.getName();
				logger.info("[TEST-VERIFY] Case name : " + caseName);
				MqRouteRequest mqRouteRequest = mqRoute.getRequest();
				String bodyRegex = mqRouteRequest.getBodyRegex();
				MqRouteResponse mqRouteResponse = mqRoute.getResponse();
				List<MqRouteCallback> mqRouteCallbacks = mqRoute.getCallbacks();


				String responsProducer = mqRouteResponse.getProducer();
				String expectResponseTopic = mqRouteResponse.getTopic();
				String expectResponseTag = mqRouteResponse.getTag();
				String expectResponseKey = mqRouteResponse.getKey();
				String expectResponseBody = mqRouteResponse.getBody();
				String expectResponseScript = mqRouteResponse.getScript();
				logger.info("[TEST-VERIFY] expect topic : " + expectResponseTopic);
				logger.info("[TEST-VERIFY] expect tag : " + expectResponseTag);
				logger.info("[TEST-VERIFY] expect key : " + expectResponseKey);


				if (StringUtils.isEmpty(bodyRegex)) {
					expectResponseBody = "{\"matched\":\"[key]\"}";
				}
				if (!StringUtils.isEmpty(expectResponseScript)) {
					if (expectResponseScript.equals("src/test/script/mq/response/transform_response.bsh")) {
						expectResponseBody = "{\"matched\":\"script processed\"}";
					}
				}

				logger.info("[TEST-VERIFY] expect body : " + expectResponseBody);


				List<MqResponse> mqResponseList = mqResponseMap.get(expectResponseTopic + "_" + expectResponseTag);
				boolean matchFlag = false;
				for (MqResponse mqResponse : mqResponseList) {
					String key = mqResponse.getKey();
					String body = mqResponse.getBody();

					logger.info("[TEST-VERIFY] received key : " + key);
					logger.info("[TEST-VERIFY] received body : " + body);

					if (expectResponseKey != null) {
						if (expectResponseKey.equals(key)) {
							if (expectResponseBody != null) {
								if (expectResponseBody.equals(body)) {
									matchFlag = true;
								}
							} else {
								matchFlag = true;
							}
						}
					} else {
						if (expectResponseBody != null) {
							if (expectResponseBody.equals(body)) {
								matchFlag = true;
							}
						}
					}
				}
				Assert.assertTrue(matchFlag, "[TEST-VERIFY] Verify fail!!!");
			}
		}
	}


	public class MqVerifyHandler implements MessageListenerConcurrently {
		private final Logger logger = LoggerFactory.getLogger(MqVerifyHandler.class);
		Map<String,List<MqResponse>> mqResponseMap = null;
		List<MqResponse> mqResponseList = null;
		String topic  = null;
		String tag = null;

		public MqVerifyHandler(Map<String,List<MqResponse>> mqResponseMap, String topic ,  String tag) {
			this.mqResponseMap = mqResponseMap;
			this.topic = topic;
			this.tag = tag;
			this.mqResponseList = mqResponseMap.get(topic+"_"+tag);
		}


		@Override
		public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
			logger.info("[TEST-RECEIVE] Receive one msg");
			final MessageExt msg = msgs.get(0);
			final String key = msg.getKeys();
			logger.info("[TEST-RECEIVE] Received key : " + key);
			final String body = new String(msg.getBody());
			logger.info("[TEST-RECEIVE] Received body : " + body);

			MqResponse mqResponse = new MqResponse();
			mqResponse.setTopic(topic);
			mqResponse.setTag(tag);
			mqResponse.setKey(key);
			mqResponse.setBody(body);
			mqResponseList.add(mqResponse);
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
	}
}
