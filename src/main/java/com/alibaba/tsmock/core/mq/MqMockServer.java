/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MqMockServer.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.core
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 * @version: v1.0
 */
package com.alibaba.tsmock.core.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.alibaba.tsmock.concurrent.MqHandlerThread;
import com.alibaba.tsmock.config.EnumConfigType;
import com.alibaba.tsmock.config.MockServerConfig;
import com.alibaba.tsmock.po.mq.MqMockConfig;
import com.alibaba.tsmock.po.mq.MqRouteInfo;

/**
 * The Class MqMockServer.
 *
 * @ClassName: MqMockServer
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:39
 */
public final class MqMockServer {

	public MqMockServer() {

	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {

	}

	/**
	 * Start.
	 *
	 *
	 * @return the mq mock core
	 */
	public static void start() {
		createServer();
	}

	/**
	 * Creates the core.
	 *
	 * @return the mq mock core
	 */
	private static void createServer() {
		final List<MqRouteInfo> mqRouteInfoList = ((MqMockConfig) MockServerConfig.getConfig(EnumConfigType.MQ))
				.getRouteTable();
		final ExecutorService executors = Executors.newFixedThreadPool(mqRouteInfoList.size());
		final List<Future> futures = new ArrayList<Future>();
		for (final MqRouteInfo mqRouteInfo : mqRouteInfoList) {
			//one thread for 1 mq
			final MqHandlerThread mqHandlerThread = new MqHandlerThread(mqRouteInfo);
			final Future future = executors.submit(mqHandlerThread);
			futures.add(future);
		}
	}
}