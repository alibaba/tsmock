/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpMockServerThread.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.cocurrent
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月9日上午9:56:31
 * @version: v1.0
 */
package com.alibaba.tsmock.concurrent;

import com.alibaba.tsmock.core.http.HttpMockServer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: HttpMockServerThread
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月9日上午9:56:31
 */
public class HttpMockServerThread implements Runnable {

	public HttpMockServerThread() {

	}

	@Override
	public void run() {
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String formatDate = sdf.format(date);
		System.out.println("["+formatDate+"] [HTTP] Start the mock server");
		HttpMockServer.start();
	}
}
