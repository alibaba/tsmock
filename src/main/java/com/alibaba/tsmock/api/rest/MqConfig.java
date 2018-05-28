/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: Config.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.api.rest
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月17日下午3:14:13
 * @version: v1.0
 */
package com.alibaba.tsmock.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.tsmock.config.MockServerConfig;
import com.alibaba.tsmock.po.mq.MqMockConfig;

/**
 * @ClassName: Config
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月17日下午3:14:13
 */
@Path("config/mq")
public class MqConfig {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public MqMockConfig getHttpConfig() {
		return MockServerConfig.mqMockConfig;
	}

}
