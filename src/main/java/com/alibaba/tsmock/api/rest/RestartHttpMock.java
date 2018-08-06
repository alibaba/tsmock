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

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.tsmock.main.TSMockMain;
import com.alibaba.tsmock.api.rest.po.TSMockResponse;
import com.alibaba.tsmock.constants.Config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: StartSrv
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月24日上午10:02:00
 */
@Path("restart/http")
public class RestartHttpMock {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public TSMockResponse startService() {
		TSMockResponse tsMockResponse = null;
		try {
			Map<String, String> logProps = new HashMap<String, String>();
			logProps.put("log4j.appender.file.File", TSMockMain.logOptionStr + File.separator + Config.TSMOCK_LOG);
			if (TSMockMain.reStart(TSMockMain.httpOptionStr, null,logProps,true) == false) {
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
