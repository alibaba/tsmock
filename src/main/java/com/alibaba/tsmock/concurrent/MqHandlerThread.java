/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MqHandler.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.cocurrent
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月9日下午9:30:10
 * @version: v1.0
 */
package com.alibaba.tsmock.concurrent;

import com.alibaba.tsmock.core.mq.MqMockRequestHandler;
import com.alibaba.tsmock.po.mq.MqRouteInfo;

/**
 * @ClassName: MqHandler
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月9日下午9:30:10
 */
public class MqHandlerThread implements Runnable {

	MqRouteInfo mqRouteInfo;

	public MqHandlerThread(MqRouteInfo mqRouteInfo) {
		this.mqRouteInfo = mqRouteInfo;
	}

	@Override
	public void run() {

		final MqMockRequestHandler mqHandler = new MqMockRequestHandler();
		mqHandler.handleRequest(mqRouteInfo);

	}

}
