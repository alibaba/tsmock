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

import java.io.File;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.tsmock.main.TSMockMain;
import com.alibaba.tsmock.util.JSONUtil;
import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.tsmock.api.rest.po.TSMockResponse;
import com.alibaba.tsmock.po.http.HttpMockConfig;

/**
 * @ClassName: Config
 * @Description: REST API--config http
 * @author: qinjun.qj
 * @date: 2017年5月17日下午3:14:13
 */
@Path("config/http")
public class HttpConfig {

	public HttpConfig() {

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HttpMockConfig loadHttpConfig() {
		HttpMockConfig httpMockConfig = null;
		try {
			final String configStr = FileUtils.readFileToString(new File(TSMockMain.httpOptionStr));
			String minifyConfigStr = JSONUtil.minify(configStr);
			httpMockConfig = JSON.parseObject(minifyConfigStr, HttpMockConfig.class);
		} catch (final IOException ioe) {

		}
		return httpMockConfig;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TSMockResponse saveHttpConfig(String msg) {
		TSMockResponse tsMockResponse = null;
		try {
			FileUtils.writeStringToFile(new File(TSMockMain.httpOptionStr), msg);
			tsMockResponse = new TSMockResponse();
			tsMockResponse.setReturn_code("0000");
			tsMockResponse.setReturn_msg("Success");
		} catch (final IOException ioe) {
			tsMockResponse = new TSMockResponse();
			tsMockResponse.setReturn_code("2001");
			tsMockResponse.setReturn_msg("Fail");
		}
		return tsMockResponse;

	}
}
