/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MqRouteList.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model.mq
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-10 am 11:39:26
 * @version: v1.0
 */
package com.alibaba.tsmock.po.mq;

import java.util.List;

/**
 * @ClassName: MqRouteList
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-10 am 11:39:26
 */
public class MqRouteInfo {
	MqInfo mq;
	List<MqRoute> route;

	public MqInfo getMq() {
		return mq;
	}

	public void setMq(MqInfo mq) {
		this.mq = mq;
	}

	public List<MqRoute> getRoute() {
		return route;
	}

	public void setRoute(List<MqRoute> route) {
		this.route = route;
	}

}
