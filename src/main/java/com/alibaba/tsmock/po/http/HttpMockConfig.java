/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MockServerConfig.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:24
 * @version: v1.0
 */

package com.alibaba.tsmock.po.http;

import java.util.Collections;
import java.util.List;

import com.alibaba.tsmock.po.MockConfig;

/**
 * Class that will hold all the project configurations.
 *
 * @ClassName: MockServerConfig
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:24
 */
public final class HttpMockConfig extends MockConfig {

	/** The port. */
	private Integer port;

	/** The host. */
	private String host;

	/** The baseUrl. */
	private String baseUrl;

	private List<HttpServerOption> serverOptions;

	private HttpMockConfigDefaults defaults;

	private HttpMockConfigLog  log;


	/** The route list. */
	private List<HttpRoute> routeTable;

	/** The mapping routes file list. */
	private List<String> mappingRoutesFileList;

	/**
	 * Instantiates a new mock core config.
	 */
	public HttpMockConfig() {
	}


	/**
	 * Post construct.
	 */
	public void setDefaultResponseContentType(String contentType) {
		for (final HttpRoute httpRoute : getRouteTable()) {
			httpRoute.getResponse().configureContentType(contentType);
		}
	}


	public void setDefaultResponseStatusCode(Integer statusCode) {
		for (final HttpRoute httpRoute : getRouteTable()) {
			httpRoute.getResponse().configureStatusCode(statusCode);
		}
	}


	public void setDefaultResponseBody(String body) {
		for (final HttpRoute httpRoute : getRouteTable()) {
			httpRoute.getResponse().configureBody(body);
		}
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the baseUrl.
	 *
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}



	/**
	 * Sets the port.
	 *
	 * @param port
	 *            the new port
	 */
	public void setPort(final Integer port) {
		this.port = port;
	}

	/**
	 * Sets the host.
	 *
	 * @param host
	 *            the new host
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * Sets the baseUrl.
	 *
	 * @param baseUrl
	 *            the new baseUrl
	 */
	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public List<HttpServerOption> getServerOptions() {
		return serverOptions;
	}

	public void setServerOptions(List<HttpServerOption> serverOptions) {
		this.serverOptions = serverOptions;
	}

	public HttpMockConfigDefaults getDefaults() {
		return defaults;
	}

	public void setDefaults(HttpMockConfigDefaults defaults) {
		this.defaults = defaults;
	}

	public HttpMockConfigLog getLog() {
		return log;
	}

	public void setLog(HttpMockConfigLog log) {
		this.log = log;
	}

	/**
	 * Gets the route table.
	 *
	 * @return the route table
	 */
	public List<HttpRoute> getRouteTable() {
		if (routeTable == null) {
			routeTable = Collections.emptyList();
		}

		return routeTable;
	}

	/**
	 * Gets the mapping routes file list.
	 *
	 * @return the mapping routes file list
	 */
	public List<String> getMappingRoutesFileList() {
		if (mappingRoutesFileList == null) {
			mappingRoutesFileList = Collections.emptyList();
		}

		return mappingRoutesFileList;
	}


	/**
	 * Sets the route table.
	 *
	 * @param routeTable
	 *            the new route tabale
	 */
	public void setRouteTable(List<HttpRoute> routeTable) {
		this.routeTable = routeTable;
	}

	/**
	 * Sets the mapping routes file list.
	 *
	 * @param mappingRoutesFileList
	 *            the new mapping routes file list
	 */
	public void setMappingRoutesFileList(List<String> mappingRoutesFileList) {
		this.mappingRoutesFileList = mappingRoutesFileList;
	}

}