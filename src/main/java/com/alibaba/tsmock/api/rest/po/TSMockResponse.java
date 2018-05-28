/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: TSMockResponse.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.api.rest.po
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月22日下午4:44:20
 * @version: v1.0
 */
package com.alibaba.tsmock.api.rest.po;

/**
 * @ClassName: TSMockResponse
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年5月22日下午4:44:20
 */
public class TSMockResponse {
	String return_code;
	String return_msg;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

}
