/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: Request.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.model
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:23
 * @version: v1.0
 */

package com.alibaba.tsmock.po.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that will hold all the request data.
 *
 * @ClassName: Request
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:23
 */
public final class HttpRouteRequest {

	/** The query param list. */
	public List<HttpQueryParam> queryParams;

	public List<HttpFormParam> formParams;


	/** The path. */
	public String path;

	/** The body. */
	public String bodyRegex;

	/** The method. */
	public String method;

	/** The description. */
	public String description;

	private String script;

	public HttpRouteRequest() {

	}

	/**
	 * Instantiates a new request.
	 *
	 * @param builder
	 *            the builder
	 */
	private HttpRouteRequest(final RequestBuilder builder) {
		path = builder.path;
		bodyRegex = builder.bodyRegex;
		method = builder.method;
		description = builder.description;
		queryParams = builder.queryParams;
	}


	/**
	 * Sets the path.
	 *
	 * @param path
	 *            the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Sets the body.
	 *
	 *            the new body
	 */
	public void setBodyRegex(String bodyRegex) {
		this.bodyRegex = bodyRegex;
	}

	/**
	 * Sets the method.
	 *
	 * @param method
	 *            the new method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public String getBodyRegex() {
		return bodyRegex;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	public List<HttpQueryParam> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(List<HttpQueryParam> queryParams) {
		this.queryParams = queryParams;
	}

	public List<HttpFormParam> getFormParams() {
		return formParams;
	}

	public void setFormParams(List<HttpFormParam> formParams) {
		this.formParams = formParams;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Map<String, List<String>> queryParam2Map() {

		final Map<String, List<String>> queryParamMap = new HashMap<String, List<String>>();
		if (queryParams != null) {
			for (final HttpQueryParam httpQueryParam : queryParams) {
				final String name = httpQueryParam.getName();
				if (!queryParamMap.containsKey(name)) {
					queryParamMap.put(name, httpQueryParam.getValues());
				}
			}
		}
		return queryParamMap;
	}


	public Map<String, String> formParam2Map() {

		final Map<String, String> formParamMap = new HashMap<String, String>();
		if (formParams != null) {
			for (final HttpFormParam httpformParam : formParams) {
				final String name = httpformParam.getName();
				if (!formParamMap.containsKey(name)) {
					formParamMap.put(name, httpformParam.getValue());
				}
			}
		}
		return formParamMap;
	}

	@Override
	public String toString() {
		return "HTTP Route Request{" + "method=" + method + ", path='" + path + '\'' + ", body='" + bodyRegex + '\''
				+ ", query param=" + queryParams + ", form param=" + formParams + "}";

	}

	/**
	 * Request Builder The only way to create an object of the Request type.
	 *
	 * @ClassName: Request
	 * @Description:
	 * @author: qinjun.qj
	 * @date: 2017-1-7 16:46:23
	 */
	public static class RequestBuilder {

		/** The name. */
		private String name;

		/** The path. */
		private String path;

		/** The body. */
		private String bodyRegex;

		/** The method. */
		private String method;

		/** The description. */
		private String description;

		/** The optional query param list. */
		private List<HttpQueryParam> queryParams;

		private String script;

		/**
		 * Name.
		 *
		 * @param name
		 *            the name
		 * @return the request builder
		 */
		public RequestBuilder name(final String name) {
			this.name = name;
			return this;
		}

		/**
		 * Body.
		 *
		 *            the body
		 * @return the request builder
		 */
		public RequestBuilder bodyRegex(final String bodyRegex) {
			this.bodyRegex = bodyRegex;
			return this;
		}

		/**
		 * Path.
		 *
		 * @param path
		 *            the path
		 * @return the request builder
		 */
		public RequestBuilder path(final String path) {
			this.path = path;
			return this;
		}

		/**
		 * Method.
		 *
		 * @param method
		 *            the method
		 * @return the request builder
		 */
		public RequestBuilder method(final String method) {
			this.method = method;
			return this;
		}

		/**
		 * Description.
		 *
		 * @param description
		 *            the description
		 * @return the request builder
		 */
		public RequestBuilder description(final String description) {
			this.description = description;
			return this;
		}

		public RequestBuilder script(final String script) {
			this.script = script;
			return this;
		}

		public RequestBuilder queryParams(List<HttpQueryParam> queryParams) {
			this.queryParams = queryParams;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the request
		 */
		public HttpRouteRequest build() {
			return new HttpRouteRequest(this);
		}

	}
}