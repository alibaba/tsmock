package com.alibaba.tsmock.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static HttpResponse sendGet(String uri, Map<String, String> headerMap)  {
		final HttpResponse HttpResponse = new HttpResponse();
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpGet httpGet = new HttpGet(uri);
			logger.info("Send request " + httpGet.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpGet.addHeader(headerKey, headerValue);
					}
				}
			}

			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);

			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}

			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());

				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}


		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}



	public static HttpResponse sendGet(String uri, Map<String, String> headerMap,Map<String,String> queryParams, boolean encode)  {
		final HttpResponse HttpResponse = new HttpResponse();
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		String queryStr="";
		if (queryParams.size()>0) {
			uri = uri + "?";
			boolean firstQueryParam = true;
			for (String queryParamName : queryParams.keySet()) {
				String queryParamValue = queryParams.get(queryParamName);
				if (encode) {
					queryParamValue = URLEncoder.encode(queryParamValue);
				}
				if (firstQueryParam) {
					queryStr = queryStr + queryParamName+"="+queryParamValue;
					firstQueryParam=false;
				}
				else {
					queryStr = queryStr + "&"+queryParamName+"="+queryParamValue;
				}
			}

			uri = uri + queryStr;
		}


		try {
			final HttpGet httpGet = new HttpGet(uri);
			logger.info("Send request " + httpGet.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpGet.addHeader(headerKey, headerValue);
					}
				}
			}

			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());

				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}


		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;

	}

	public static HttpResponse sendPost(String uri, Map<String, String> headerMap, String requestBody,
									ContentType bodyContentType)  {
		logger.info("Start to send post request");
		final HttpResponse HttpResponse = new HttpResponse();

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPost httpPost = new HttpPost(uri);
			logger.info("Send request " + httpPost.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpPost.addHeader(headerKey, headerValue);
					}
				}
			}

			if (!StringUtils.isEmpty(requestBody)) {
				final StringEntity entity = new StringEntity(requestBody, bodyContentType);

				httpPost.setEntity(entity);
				logger.debug("Request content type:[{}]", bodyContentType);
				logger.debug("Request body:[{}]", requestBody);
			}
			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());
				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}




	public static HttpResponse sendPost(String uri, Map<String, String> headerMap, byte[] requestBody)  {
		logger.info("Start to send post request");
		final HttpResponse HttpResponse = new HttpResponse();

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPost httpPost = new HttpPost(uri);
			logger.info("Send request " + httpPost.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpPost.addHeader(headerKey, headerValue);
					}
				}
			}

			if (requestBody!=null) {
				HttpEntity entity = new ByteArrayEntity(requestBody);

				httpPost.setEntity(entity);
				logger.debug("Request body:[{}]", requestBody);
			}
			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());
				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}



	public static HttpResponse sendPostWithForm(String uri, Map<String, String> headerMap, Map<String, String> forms,
											boolean urlEncode)  {
		logger.info("Start to send post request with form params");
		final HttpResponse HttpResponse = new HttpResponse();

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPost httpPost = new HttpPost(uri);
			logger.info("Send request " + httpPost.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpPost.addHeader(headerKey, headerValue);
					}
				}
			}

			final List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();

			for (final String name : forms.keySet()) {
				final String value = forms.get(name);
				formParams.add(new BasicNameValuePair(name, value));
			}
			final HttpEntity httpEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			httpPost.setEntity(httpEntity);

			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());
				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}





	public static HttpResponse sendPut(String uri, Map<String, String> headerMap, String requestBody,
								   ContentType bodyContentType)  {
		logger.info("Start to send put request");
		final HttpResponse HttpResponse = new HttpResponse();

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPut httpPut = new HttpPut(uri);
			logger.info("Send request " + httpPut.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpPut.addHeader(headerKey, headerValue);
					}
				}
			}

			if (!StringUtils.isEmpty(requestBody)) {
				final StringEntity entity = new StringEntity(requestBody, bodyContentType);

				httpPut.setEntity(entity);
				logger.debug("Request content type:[{}]", bodyContentType);
				logger.debug("Request body:[{}]", requestBody);
			}
			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPut);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());
				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}



	public static HttpResponse sendPut(String uri, Map<String, String> headerMap, byte[] requestBody)  {
		logger.info("Start to send post request");
		final HttpResponse HttpResponse = new HttpResponse();

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPut httpPut = new HttpPut(uri);
			logger.info("Send request " + httpPut.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpPut.addHeader(headerKey, headerValue);
					}
				}
			}

			if (requestBody!=null) {
				HttpEntity entity = new ByteArrayEntity(requestBody);

				httpPut.setEntity(entity);
				logger.debug("Request body:[{}]", requestBody);
			}
			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPut);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());
				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}



	public static HttpResponse sendPutWithForm(String uri, Map<String, String> headerMap, Map<String, String> forms,
										   boolean urlEncode)  {
		logger.info("Start to send put request with form params");
		final HttpResponse HttpResponse = new HttpResponse();

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPut httpPut = new HttpPut(uri);
			logger.info("Send request " + httpPut.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpPut.addHeader(headerKey, headerValue);
					}
				}
			}

			final List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();

			for (final String name : forms.keySet()) {
				final String value = forms.get(name);
				formParams.add(new BasicNameValuePair(name, value));
			}
			final HttpEntity httpEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			httpPut.setEntity(httpEntity);

			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPut);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());
				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}




	public static HttpResponse sendDelete(String uri, Map<String, String> headerMap)  {
		final HttpResponse HttpResponse = new HttpResponse();
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpDelete httpDelete = new HttpDelete(uri);
			logger.info("Send request " + httpDelete.getRequestLine());
			if (headerMap != null) {
				for (final String headerKey : headerMap.keySet()) {
					final String headerValue = headerMap.get(headerKey);
					if (!StringUtils.isEmpty(headerValue)) {
						logger.debug("Request header name:[{}], value:[{}]", headerKey,headerValue);
						httpDelete.addHeader(headerKey, headerValue);
					}
				}
			}

			final CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpDelete);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for (Header header:headers) {
				logger.debug("HttpResponse header name:[{}], value:[{}]",header.getName(),header.getValue());
			}
			HttpResponse.setHeaders(headers);
			Header contentTypeHeader = closeableHttpResponse.getLastHeader("Content-Type");
			if (contentTypeHeader!=null) {
				String contentType = contentTypeHeader.getValue();
				if (contentType != null) {
					HttpResponse.setContentType(contentType);
				}
			}
			final HttpEntity entity = closeableHttpResponse.getEntity();
			final int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
			HttpResponse.setStatusCode(statusCode);
			logger.info("HttpResponse status code:[{}]", statusCode);
			if (entity != null) {
				logger.info("HttpResponse body length: " + entity.getContentLength());

				byte[] HttpResponseBodyByte = EntityUtils.toByteArray(entity);
				String HttpResponseBody = new String(HttpResponseBodyByte,"utf8");
				logger.info("HttpResponse body: " + HttpResponseBody);
				EntityUtils.consume(entity);
				HttpResponse.setBody(HttpResponseBody);
				HttpResponse.setByteBody(HttpResponseBodyByte);
			}

		} catch (final Exception e) {
			logger.error(ExceptionUtil.getExceptionWithStack(e));
		} finally {
			try {
				httpClient.close();
			} catch (final IOException e) {
				logger.error(ExceptionUtil.getExceptionWithStack(e));
			}
		}
		return HttpResponse;
	}

}
