/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MockHeader.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:28:30
 * @version: v1.0
 */

package com.alibaba.tsmock.po.http;

import java.util.Collections;
import java.util.List;

/**
 * Class that will hold all the header data.
 *
 * @ClassName: MockHeader
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:28:30
 */
public class HttpRouteHeader {

	/** The name. */
	private String name;

	/** The using wild card. */
	private boolean usingWildCard;

	/** The value list. */
	private List<String> valueList;

	public HttpRouteHeader() {

	}

	/**
	 * Instantiates a new mock header.
	 *
	 * @param name
	 *            the name
	 * @param usingWildCard
	 *            the using wild card
	 * @param valueList
	 *            the value list
	 */
	public HttpRouteHeader(final String name, final boolean usingWildCard, final List<String> valueList) {
		this.name = name;
		this.usingWildCard = usingWildCard;
		this.valueList = valueList;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if is using wild card.
	 *
	 * @return true, if is using wild card
	 */
	public boolean isUsingWildCard() {
		return usingWildCard;
	}

	/**
	 * Sets the using wild card.
	 *
	 * @param usingWildCard
	 *            the new using wild card
	 */
	public void setUsingWildCard(final boolean usingWildCard) {
		this.usingWildCard = usingWildCard;
	}

	/**
	 * Gets the value list.
	 *
	 * @return the value list
	 */
	public List<String> getValueList() {
		if (valueList == null) {
			valueList = Collections.emptyList();
		}

		return valueList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Header{" + "name='" + name + '\'' + ", usingWildCard=" + usingWildCard + ", valueList=" + valueList
				+ '}';
	}
}