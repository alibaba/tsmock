/**
 * Copyright © 2016 Alibaba Inc . All rights reserved.
 *
 * @Title: Callback.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2016年12月27日下午1:58:26
 * @version: v1.0
 */
package com.alibaba.tsmock.po.mq;

/**
 * The Class Callback.
 *
 * @ClassName: Callback
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2016年12月27日下午1:58:26
 */
public class MqRouteCallback {

	/** The type. */
	private String type;

	/** The info. */
	private String info;

	/** The value. */
	private String value;

	/** The sleep. */
	private String sleep;

	/** The script. */
	private String script;

	/**
	 * Instantiates a new callback.
	 */
	public MqRouteCallback() {
	}

	/**
	 * Instantiates a new callback.
	 *
	 * @param type
	 *            the type
	 * @param info
	 *            the info
	 * @param value
	 *            the value
	 * @param sleep
	 *            the sleep
	 * @param script
	 *            the script
	 * @Title:Callback
	 * @Description:TODO
	 */
	public MqRouteCallback(final String type, final String info, final String value, String sleep, String script) {
		super();
		this.type = type;
		this.info = info;
		this.value = value;
		this.sleep = sleep;
		this.script = script;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Gets the info.
	 *
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Sets the info.
	 *
	 * @param info
	 *            the new info
	 */
	public void setInfo(final String info) {
		this.info = info;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Gets the sleep.
	 *
	 * @return the sleep
	 */
	public String getSleep() {
		return sleep;
	}

	/**
	 * Sets the sleep.
	 *
	 * @param sleep
	 *            the new sleep
	 */
	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	/**
	 * Gets the script.
	 *
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Sets the script.
	 *
	 * @param script
	 *            the new script
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MQ Route Callback {" + "type=" + type + ", info='" + info + '\'' + ", value='" + value + '\''
				+ ", sleep='" + sleep + ", script='" + script + '}';
	}

}
