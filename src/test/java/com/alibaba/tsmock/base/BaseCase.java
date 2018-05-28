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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


/**
 * @ClassName: BaseTest
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-02-16 10:15:20
 */
public class BaseCase extends BaseSuite {
	final protected static Logger logger = LoggerFactory.getLogger(BaseCase.class);

	public BaseCase() {

	}

	@BeforeClass
	public void setup() {
		logger.info("==========================[Start Cases]===================");
	}



	@AfterClass
	public void cleanUp() {
		logger.info("==========================[End Cases]===================");
	}

}
