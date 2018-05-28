/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: StringUtil.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.util
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月13日下午4:14:13
 * @version: v1.0
 */
package com.alibaba.tsmock.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: StringUtil
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年2月13日下午4:14:13
 */
public class StringUtil {

	public static String escapeStr(String str) {
		String escapedStr = StringUtils.replace(str, "\\", "\\\\");
		escapedStr = StringUtils.replace(escapedStr, "\"", "\\\"");
		return escapedStr;
	}
}
