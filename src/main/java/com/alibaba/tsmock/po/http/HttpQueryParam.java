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
public class HttpQueryParam {

	/** The name. */
	private String name;

	/** The value list. */
	private List<String> values;

	public HttpQueryParam() {

	}

	public HttpQueryParam(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("QueryParam{name:");
		sb.append(name);
		sb.append(",");
		if (values != null) {
			for (final String value : values) {
				sb.append("value:");
				sb.append(value);
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}

}