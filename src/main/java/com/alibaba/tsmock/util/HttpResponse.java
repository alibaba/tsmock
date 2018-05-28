package com.alibaba.tsmock.util;

import org.apache.http.Header;

public class HttpResponse {
	int statusCode;
	String body;
	Header[] headers;
	String contentType;
	byte[] byteBody;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getByteBody() {
		return byteBody;
	}

	public void setByteBody(byte[] byteBody) {
		this.byteBody = byteBody;
	}
}
