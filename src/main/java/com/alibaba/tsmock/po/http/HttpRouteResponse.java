/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: Response.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:24
 * @version: v1.0
 */

package com.alibaba.tsmock.po.http;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Class that will hold all the response data.
 *
 * @ClassName: Response
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:24
 */
public final class HttpRouteResponse {

	/** The status code. */
	private Integer statusCode;

	/** The body pointing to file. */
	private boolean bodyPointingToFile = false;

	/** The body. */
	private String body;


	/** The content type. */
	private String contentType;

	/** The header list. */
	private List<HttpRouteHeader> headers;

	/** The script. */
	private String script;

	private Integer sleep;

	private String compress;

	/**
	 * Instantiates a new response.
	 */
	public HttpRouteResponse() {
	}

	/**
	 * Instantiates a new response.
	 *
	 * @param statusCode
	 *            the status code
	 * @param body
	 *            the body
	 * @param contentType
	 *            the content type
	 * @param headers
	 *            the header list
	 * @param bodyPointingToFile
	 *            the body pointing to file
	 * @param script
	 *            the script
	 */
	public HttpRouteResponse(final Integer statusCode, final String body, final String contentType,
							 final List<HttpRouteHeader> headers, final boolean bodyPointingToFile,
							 String script) {
		this.statusCode = statusCode;
		this.body = body;
		this.contentType = contentType;
		this.headers = headers;
		this.bodyPointingToFile = bodyPointingToFile;
		this.script = script;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HTTP Route Response{" + "statusCode=" + statusCode + ", body='" + body + '\'' + ", contentType='"
				+ contentType + '\'' + ", headers=" + headers + ", script=" + script + '}';
	}

	/**
	 * Gets the status code.
	 *
	 * @return the status code
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBody() {
		return body;
	}



	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Checks if is body pointing to file.
	 *
	 * @return true, if is body pointing to file
	 */
	public boolean isBodyPointingToFile() {
		return bodyPointingToFile;
	}

	/**
	 * Gets the header list.
	 *
	 * @return the header list
	 */
	public List<HttpRouteHeader> getHeaders() {
		if (headers == null) {
			headers = Collections.emptyList();
		}

		return headers;
	}

	/**
	 * Configure content type.
	 *
	 * @param defaultContentType
	 *            the default content type
	 */
	public void configureContentType(final String defaultContentType) {
		if (StringUtils.isEmpty(contentType)) {
			contentType = defaultContentType;
		}
	}


	public void configureStatusCode(final Integer defaultStatusCode) {
		if (statusCode==0) {
			statusCode = defaultStatusCode;
		}
	}


	public void configureBody(final String defaultBody) {
		if (StringUtils.isEmpty(body)) {
			body = defaultBody;
		}
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

	/**
	 * Sets the status code.
	 *
	 * @param statusCode
	 *            the new status code
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Sets the body pointing to file.
	 *
	 * @param bodyPointingToFile
	 *            the new body pointing to file
	 */
	public void setBodyPointingToFile(boolean bodyPointingToFile) {
		this.bodyPointingToFile = bodyPointingToFile;
	}

	/**
	 * Sets the body.
	 *
	 * @param body
	 *            the new body
	 */
	public void setBody(String body) {
		this.body = body;
	}



	/**
	 * Sets the content type.
	 *
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Sets the header list.
	 *
	 * @param headers
	 *            the new header list
	 */
	public void setHeaders(List<HttpRouteHeader> headers) {
		this.headers = headers;
	}

	public Integer getSleep() {
		return sleep;
	}

	public void setSleep(Integer sleep) {
		this.sleep = sleep;
	}

	public String getCompress() {
		return compress;
	}

	public void setCompress(String compress) {
		this.compress = compress;
	}

}