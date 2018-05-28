/**
 * Copyright © 2017阿里巴巴. All rights reserved.
 *
 * @Title: ARTestData.java
 * @Prject: arauto
 * @Package: com.alibaba.arauto.base
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-23 19:42:30
 * @version: v1.0
 */
package com.alibaba.tsmock.base;


import com.alibaba.fastjson.JSON;
import com.alibaba.tsmock.constants.Config;
import com.alibaba.tsmock.main.TSMockMain;
import com.alibaba.tsmock.po.http.HttpMockConfig;
import com.alibaba.tsmock.po.mq.MqMockConfig;
import com.alibaba.tsmock.util.SleepUtil;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: BaseTest
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-02-16 10:15:20
 */
public class BaseSuite {
	protected final static Logger logger = LoggerFactory.getLogger(BaseSuite.class);
	protected static HttpMockConfig httpMockConfig=null;
	protected static MqMockConfig mqMockConfig=null;

	@BeforeSuite
	public void beforeSuite() {
		logger.info("==========================[Start Suite]===================");
		try {
			Map<String, String> logProps = new HashMap<String, String>();

			logProps.put("log4j.appender.file.File", Config.LOG_PATH + File.separator + Config.TSMOCK_LOG);
			TSMockMain.setLogOptionStr(Config.LOG_PATH);
			TSMockMain.start(Config.DEFAULT_HTTP_MOCK_FILE, Config.DEFAULT_MQ_MOCK_FILE, logProps,true);
			SleepUtil.sleep(5000);
		}
		catch (InterruptedException ie) {
			Assert.assertTrue("Failed to start http mock server:" + ie,false);
		}

		try {
			String httpMockConfigStr = FileUtils.readFileToString(new File(Config.DEFAULT_HTTP_MOCK_FILE));

			httpMockConfig = JSON.parseObject(httpMockConfigStr, HttpMockConfig.class);
		}
		catch (IOException ioe) {
			Assert.assertTrue("Failed to read the http configuration file:"+ioe,false);
		}

		try {
			String mqMockConfigStr = FileUtils.readFileToString(new File(Config.DEFAULT_MQ_MOCK_FILE));
			mqMockConfig = JSON.parseObject(mqMockConfigStr, MqMockConfig.class);
		}
		catch (IOException ioe) {
			Assert.assertTrue("Failed to read the http configuration file:"+ioe,false);
		}
	}

	@AfterSuite
	public void afterSuite() {
		logger.info("==========================[End Suite]===================");
	}

}
