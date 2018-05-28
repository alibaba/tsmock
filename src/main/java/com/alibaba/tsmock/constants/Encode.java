/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: ProjectConfiguration.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.configuration
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:40:37
 * @version: v1.0
 */
package com.alibaba.tsmock.constants;

/**
 * The Enum ProjectConfiguration.
 *
 * @ClassName: ProjectConfiguration
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:26:12
 */
public enum Encode {

	/** The encoding. */
	ENCODING("UTF-8");

	/** The value. */
	public final String value;

	/**
	 * Instantiates a new project configuration.
	 *
	 * @param value
	 *            the value
	 */
	Encode(final String value) {
		this.value = value;
	}
}
