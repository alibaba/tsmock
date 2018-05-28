/**
 * Copyright © 2017阿里巴巴. All rights reserved.
 *
 * @Title: ExceptionUtils.java
 * @Prject: tsutils
 * @Package: com.alibaba.tsutils.exception
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年3月17日下午1:40:13
 * @version: v1.0
 */
package com.alibaba.tsmock.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @ClassName: ExceptionUtils
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年3月17日下午1:40:13
 */
public class ExceptionUtil {
	public static String getExceptionWithStack(Throwable e) {
		final String[] es = ExceptionUtils.getRootCauseStackTrace(e);
		return StringUtils.join(es);
	}
}
