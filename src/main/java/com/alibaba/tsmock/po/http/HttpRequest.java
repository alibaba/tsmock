/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpRequest.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.po.http
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月12日下午4:42:54
 * @version: v1.0
 */
package com.alibaba.tsmock.po.http;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.tsmock.core.http.HttpMockServerHelper;
import org.apache.commons.lang3.StringUtils;

import io.undertow.server.HttpServerExchange;

/**
 * @ClassName: HttpRequest
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017年1月12日下午4:42:54
 */
public class HttpRequest {
	private static volatile HttpRequest httpRequest;

	String httpMethod;
	String path;
	Map<String, List<String>> queryParams;
	Map<String,String> formParams;
	String body;

	public HttpRequest() {

	}

	private HttpRequest(HttpServerExchange exchange) {
		httpMethod = exchange.getRequestMethod().toString();
		path = exchange.getRequestPath();
		queryParams = new HashMap<String, List<String>>();
		final Map<String, Deque<String>> params = exchange.getQueryParameters();

		for (final String name : params.keySet()) {
			queryParams.put(name, new ArrayList<String>(params.get(name)));
		}
	}

	public static HttpRequest newInstanceForProxy(HttpServerExchange exchange) {
		httpRequest = new HttpRequest(exchange);
		return httpRequest;
	}

	public static HttpRequest getInstance(HttpServerExchange exchange, boolean isProxy) {

		final HttpRequest httpRequest = new HttpRequest(exchange);

		if (!isProxy) {
			HttpRequestFormOrBody httpRequestFormOrBody= HttpMockServerHelper.extractBody(exchange);
			httpRequest.body = httpRequestFormOrBody.getBody();
			httpRequest.formParams = httpRequestFormOrBody.getForm();
		}
		return httpRequest;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, List<String>> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, List<String>> queryParams) {
		this.queryParams = queryParams;
	}

	public Map<String, String> getFormParams() {
		return formParams;
	}

	public void setFormParams(Map<String, String> formParams) {
		this.formParams = formParams;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();

		sb.append("\nRequest:\n");
		sb.append("Method:" + httpMethod + "\n");
		sb.append("Path:" + path + "\n");
		if ((queryParams != null) && (queryParams.size() > 0)) {
			for (final String name : queryParams.keySet()) {
				sb.append("Query param name:" + name + "\n");
				final List<String> values = queryParams.get(name);
				if ((values != null) && (values.size() > 0)
						&& (!((values.size() == 1) && StringUtils.isEmpty(values.get(0))))) {
					for (final String value : values) {
						sb.append("          value:" + value + "\n");
					}
				}
			}
		}
		if ((formParams != null) && (formParams.size() > 0)) {
			for (final String formName : formParams.keySet()) {
				sb.append("Form param name:" + formName + "\n");
				String formValue = formParams.get(formName);
				if(!StringUtils.isEmpty(formValue)) {
					sb.append("          value:" + formValue + "\n");
				}
			}
		}
		if (!StringUtils.isEmpty(body)) {
			sb.append("Body:\n" + body);
		}
		return sb.toString();
	}

}
