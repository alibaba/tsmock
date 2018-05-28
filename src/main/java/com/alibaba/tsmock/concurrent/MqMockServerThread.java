/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MqMockServerThread.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.cocurrent
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月9日上午9:56:40
 * @version: v1.0
 */
package com.alibaba.tsmock.concurrent;

import com.alibaba.tsmock.core.mq.MqMockServer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: MqMockServerThread
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月9日上午9:56:40
 */
public class MqMockServerThread implements Runnable {

	public MqMockServerThread() {

	}

	@Override
	public void run() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String formatDate = sdf.format(date);
		System.out.println("["+formatDate+"] [MQ] Start the mock server");
		MqMockServer.start();
	}

}
