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

package com.alibaba.tsmock.core.mq;

import com.alibaba.tsmock.po.mq.MqInfo;
import com.alibaba.tsmock.po.mq.MqRouteInfo;
import com.alibaba.tsmock.util.MqPushConsumer;
import com.alibaba.tsmock.util.MqPushConsumerFactory;

/**
 * This class is the responsible for handling a incoming request. The request
 * will be validated and if it is correctly configured its response will be
 * sent. If there request is not found or have any kind of error, an
 * InternalServerError will be sent
 *
 * @ClassName: MqMockRequestHandler
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 */
public class MqMockRequestHandler {

	public MqMockRequestHandler() {

	}

	public void handleRequest(final MqRouteInfo mqRouteInfo) {
		final MqInfo mq = mqRouteInfo.getMq();
		final String consumerName = mq.getConsumer();
		final String topic = mq.getTopic();
		final String tag = mq.getTag();
		MqPushConsumer mqPushConsumer = MqPushConsumerFactory.getInstance(consumerName,topic,tag,new MqMockResponseHandler(mqRouteInfo));
	}

}