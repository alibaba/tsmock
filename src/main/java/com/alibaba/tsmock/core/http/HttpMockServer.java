/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpMockServer.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.core
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:40
 * @version: v1.0
 */

package com.alibaba.tsmock.core.http;

import io.undertow.Undertow;

/**
 * Class that will start/shutdown ServletServer implementation.
 *
 * @ClassName: HttpMockServer
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:40
 */
public final class HttpMockServer {

	/** The http core. */
	private final Undertow httpServer;

	/**
	 * Instantiates a new http mock core.
	 *
	 * @param httpServer
	 *            the http core
	 */
	private HttpMockServer(final Undertow httpServer) {
		this.httpServer = httpServer;
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		httpServer.stop();
	}

	/**
	 * Start.
	 *
	 */
	public static void start() {
		createServer();
	}

	/**
	 * Creates the core.
	 *
	 * @return the http mock core
	 */
	private static HttpMockServer createServer() {
		final Undertow undertow = HttpMockServerHelper.startHttpServer();

		return new HttpMockServer(undertow);
	}
}