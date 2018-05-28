/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: QueryParam.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:28:32
 * @version: v1.0
 */

package com.alibaba.tsmock.po.http;

import java.util.List;

/**
 * Class that will hold all the query param data.
 *
 * @ClassName: QueryParam
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:28:32
 */
public class HttpFormParam {

	/** The name. */
	private String name;

	/** The value list. */
	private String value;

	public HttpFormParam() {

	}

	public HttpFormParam(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("FormParam{name:");
		sb.append(name);
		sb.append(",value:"+value);
		sb.append("}");
		return sb.toString();
	}

}