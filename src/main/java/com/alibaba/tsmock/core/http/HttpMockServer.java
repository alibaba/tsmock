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

import com.alibaba.tsmock.path.PathHandlerFactory;
import io.undertow.Undertow;
import io.undertow.servlet.Servlets;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that will start/shutdown ServletServer implementation.
 *
 * @ClassName: HttpMockServer
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:40
 */
public final class HttpMockServer {

	private static HttpMockServer httpMockServer;
	/** The http core. */
	private  Undertow httpServer;

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
	public boolean shutdown() {
		try {
			PathHandlerFactory.getManager().undeploy();
			Servlets.defaultContainer().removeDeployment(PathHandlerFactory.getDeploymentInfo());
			PathHandlerFactory.setDeploymentInfo(null);
			PathHandlerFactory.setManager(null);
			httpMockServer = null;
			httpServer = null;
		}
		catch (Exception e) {
			Date dateStart=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			String formatDateSstart = sdf.format(dateStart);
			System.out.println("["+formatDateSstart+"] [HTTP] Get exception when stop the server:"+e);
		}
		return true;
	}



	/**
	 * Start.
	 *
	 */
	public static void start() {
		getServer();
	}

	/**
	 * Creates the core.
	 *
	 * @return the http mock core
	 */
	public static HttpMockServer getServer() {
		if (httpMockServer ==null) {
			final Undertow undertow = HttpMockServerHelper.startHttpServer();
			httpMockServer =  new HttpMockServer(undertow);

		}
		return httpMockServer;
	}
}