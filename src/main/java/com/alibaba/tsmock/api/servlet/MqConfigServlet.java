/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpConfigServlet.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.servlet
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月17日下午10:49:21
 * @version: v1.0
 */
package com.alibaba.tsmock.api.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.tsmock.config.MockServerConfig;

/**
 * @ClassName: HttpConfigServlet
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月17日下午10:49:21
 */
public class MqConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print(MockServerConfig.mqMockConfigStr);
	}
}
