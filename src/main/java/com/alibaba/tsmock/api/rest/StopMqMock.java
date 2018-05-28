/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: StartSrv.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.api.rest
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月24日上午10:02:00
 * @version: v1.0
 */
package com.alibaba.tsmock.api.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.tsmock.api.rest.po.TSMockResponse;
import com.alibaba.tsmock.constants.Config;
import com.alibaba.tsmock.main.TSMockMain;

/**
 * @ClassName: StartSrv
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月24日上午10:02:00
 */
@Path("start/mq")
public class StopMqMock {

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public TSMockResponse startService() {
		TSMockResponse tsMockResponse = null;
		try {
			if (TSMockMain.start(null, Config.DEFAULT_MQ_MOCK_FILE,null,true) == false) {
				tsMockResponse = new TSMockResponse();
				tsMockResponse.setReturn_code("1001");
				tsMockResponse.setReturn_msg("Fail");
			} else {
				tsMockResponse = new TSMockResponse();
				tsMockResponse.setReturn_code("0000");
				tsMockResponse.setReturn_msg("Success");
			}

		} catch (final Exception e) {
			tsMockResponse.setReturn_code("1002");
			tsMockResponse.setReturn_msg("Fail");
		}
		return tsMockResponse;

	}
}
