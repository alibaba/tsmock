/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: SleepUtil.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.util
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月10日下午4:27:42
 * @version: v1.0
 */
package com.alibaba.tsmock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: SleepUtil
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月10日下午4:27:42
 */
public class SleepUtil {
	private static Logger logger = LoggerFactory.getLogger(SleepUtil.class);

	public static void sleep(int minSecond) {
		try {
			Thread.sleep(minSecond);
		} catch (final Exception e) {
			logger.warn("Sleep get interupted");
		}
	}
}
