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

package com.alibaba.tsmock.po.mq;

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
public final class MqMockConfig extends MockConfig {

	private MqMockConfigLog log;



	/** The route list. */
	private List<MqRouteInfo> routeTable;

	/**
	 * Instantiates a new mock core config.
	 */
	public MqMockConfig() {
	}

	public MqMockConfigLog getLog() {
		return log;
	}

	public void setLog(MqMockConfigLog log) {
		this.log = log;
	}

	/**
	 * Gets the route table.
	 *
	 * @return the route table
	 */
	public List<MqRouteInfo> getRouteTable() {
		if (routeTable == null) {
			routeTable = Collections.emptyList();
		}

		return routeTable;
	}

	/**
	 * Sets the route list.
	 *
	 * @param routeTable
	 *            the new route list
	 */
	public void setRouteTable(List<MqRouteInfo> routeTable) {
		this.routeTable = routeTable;
	}

}