/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpRouterMathedLevel.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.core.http
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-1-14 10:55:49
 * @version: v1.0
 */
package com.alibaba.tsmock.core.router.http;

/**
 * @ClassName: HttpRouterMathedLevel
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-1-14 10:55:49
 */
public enum HttpRouterMatchedLevel {
	METHOD_PATH,
	METHOD_PATH_QUERYPARAMNAME,
	METHOD_PATH_QUERYPARAMNAME_QUERYPARAMVALUE,
	METHOD_PATH_QUERYPARAM_FORMPARAMNAME_OR_BODY,
	METHOD_PATH_QUERYPARAM_FORMPARAMNAME_FORMPARAMVALUE_OR_BODY
}
