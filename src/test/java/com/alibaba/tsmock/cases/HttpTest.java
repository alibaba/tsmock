package com.alibaba.tsmock.cases;

import com.alibaba.tsmock.base.BaseCase;
import com.alibaba.tsmock.po.http.*;
import com.alibaba.tsmock.util.CompressUtil;
import com.alibaba.tsmock.util.HttpResponse;
import com.alibaba.tsmock.util.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by qinjun.qj on 2018/1/31.
 * Test based on the route table
 */
public class HttpTest extends BaseCase {
    private final Logger logger = LoggerFactory.getLogger(HttpTest.class);

    @Test
    public void test() {
        String host = httpMockConfig.getHost();
        Integer port = httpMockConfig.getPort();
        String baseUrl = httpMockConfig.getBaseUrl();
        HttpMockConfigDefaults httpMockConfigDefaults = httpMockConfig.getDefaults();
        List<HttpRoute> routeTable = httpMockConfig.getRouteTable();
        HttpResponse httpResponse = null;

        for (int routeIndex = 1; routeIndex <= routeTable.size(); routeIndex++) {
            HttpRoute httpRoute = routeTable.get(routeIndex - 1);
            String caseName = httpRoute.getName();
            logger.info("==========================CASE [" + routeIndex + "]==========================");
            logger.info("Case name : " + caseName);
            HttpRouteRequest httpRouteRequest = httpRoute.getRequest();
            HttpRouteResponse httpRouteResponse = httpRoute.getResponse();
            HttpRouteProxy httpRouteProxy = httpRoute.getProxy();
            List<HttpRouteCallback> httpRouteCallbacks = httpRoute.getCallbacks();
            String requestMethod = httpRouteRequest.getMethod();
            String requestPath = httpRouteRequest.getPath();
            List<HttpQueryParam> httpRouteRequestQueryParams = httpRouteRequest.getQueryParams();
            List<HttpFormParam> httpRouteRequestFormParams = httpRouteRequest.getFormParams();
            String httpRequestBodyRegex = httpRouteRequest.getBodyRegex();
            String httpRequestScript = httpRouteRequest.getScript();

            String httpRequestFullUrl = "http://" + host + ":" + port + baseUrl + requestPath;

            if (httpRouteRequestQueryParams != null && httpRouteRequestQueryParams.size() > 0) {
                httpRequestFullUrl = httpRequestFullUrl + "?";
                for (int i = 0; i < httpRouteRequestQueryParams.size(); i++) {
                    HttpQueryParam httpQueryParam = httpRouteRequestQueryParams.get(i);
                    String name = httpQueryParam.getName();
                    List<String> values = httpQueryParam.getValues();
                    if (values != null && values.size() > 0) {
                        for (String value : values) {
                            httpRequestFullUrl = httpRequestFullUrl + name + "=" + value + "&";
                        }
                    } else {
                        String value = UUID.randomUUID().toString();
                        httpRequestFullUrl = httpRequestFullUrl + name + "=" + value + "&";
                    }
                }
                if (httpRequestFullUrl.endsWith("&")) {
                    httpRequestFullUrl = httpRequestFullUrl.substring(0, httpRequestFullUrl.length() - 1);
                }
            }

            long timeBeforeSend = Calendar.getInstance().getTimeInMillis();


            if (requestMethod.equalsIgnoreCase("get")) {
                httpResponse = HttpUtil.sendGet(httpRequestFullUrl, null);
            } else if (requestMethod.equalsIgnoreCase("post")) {
                Map<String, String> formParams = new HashMap<String, String>();
                if (httpRouteRequestFormParams != null && httpRouteRequestFormParams.size() > 0) {
                    for (HttpFormParam httpFormParam : httpRouteRequestFormParams) {
                        String name = httpFormParam.getName();
                        String value = httpFormParam.getValue();
                        if (value != null) {
                            formParams.put(name, value);
                        } else {
                            String randomValue = UUID.randomUUID().toString();
                            formParams.put(name, randomValue);
                        }
                    }


                    httpResponse = HttpUtil.sendPostWithForm(httpRequestFullUrl, null, formParams, true);
                } else if (!StringUtils.isEmpty(httpRequestBodyRegex)) {
                    String body = "test:body";
                    httpResponse = HttpUtil.sendPost(httpRequestFullUrl, null, body, null);
                } else {
                    httpResponse = HttpUtil.sendPost(httpRequestFullUrl, null, null, null);
                }
            } else if (requestMethod.equalsIgnoreCase("put")) {
                Map<String, String> formParams = new HashMap<String, String>();
                if (httpRouteRequestFormParams != null && httpRouteRequestFormParams.size() > 0) {
                    for (HttpFormParam httpFormParam : httpRouteRequestFormParams) {
                        String name = httpFormParam.getName();
                        String value = httpFormParam.getValue();
                        if (value != null) {
                            formParams.put(name, value);
                        } else {
                            String randomValue = UUID.randomUUID().toString();
                            formParams.put(name, randomValue);
                        }
                    }

                    httpResponse = HttpUtil.sendPutWithForm(httpRequestFullUrl, null, formParams, true);
                } else if (!StringUtils.isEmpty(httpRequestBodyRegex)) {
                    String body = "test:body";
                    httpResponse = HttpUtil.sendPut(httpRequestFullUrl, null, body, null);
                } else {
                    httpResponse = HttpUtil.sendPut(httpRequestFullUrl, null, null, null);
                }
            } else if (requestMethod.equalsIgnoreCase("delete")) {
                httpResponse = HttpUtil.sendDelete(httpRequestFullUrl, null);
            }
            long timeAfterSend = Calendar.getInstance().getTimeInMillis();




            Integer expectSleep = httpRouteResponse.getSleep();
            if (expectSleep!=null) {
                Assert.assertTrue(timeAfterSend-timeBeforeSend-expectSleep<expectSleep*0.1);
            }


            if (!StringUtils.isEmpty(httpRequestScript)) {
                if (httpRequestScript.endsWith("save_request.bsh")) {
                    File saveRequestFile = new File("src/test/data/saveData.txt");
                    Assert.assertTrue(saveRequestFile.exists());
                }
                else if (httpRequestScript.endsWith("validate_request.bsh")) {
                    //Below assert response will check the validation script result, get expect response indicate validate pass
                }
            }


            String httpResponseScript = httpRouteResponse.getScript();


            int statusCode = httpResponse.getStatusCode();
            String body = httpResponse.getBody();
            if (!StringUtils.isEmpty(httpResponseScript)) {
                if (httpResponseScript.endsWith("transform_response.bsh")) {
                    body = URLDecoder.decode(body);
                    if(body.equals("{\"matched\":\"test:body\"}")) {
                        body="{\"matched\":\"<body>\"}";
                    }
                }
            }
            byte[] bodyBytes = httpResponse.getByteBody();
            String contentType = httpResponse.getContentType();
            int expectStatusCode;
            if (httpRouteResponse.getStatusCode() == null) {
                expectStatusCode = httpMockConfigDefaults.getStatusCode();
            } else {
                expectStatusCode = httpRouteResponse.getStatusCode();
            }

            String expectContentType = null;
            if (httpRouteResponse.getContentType() == null) {
                expectContentType = httpMockConfigDefaults.getContentType();
            } else {
                expectContentType = httpRouteResponse.getContentType();
            }
            String expectBody = null;
            if (httpRouteResponse.isBodyPointingToFile()) {
                try {
                    expectBody = FileUtils.readFileToString(new File(httpRouteResponse.getBody()));
                } catch (final IOException ioe) {
                    logger.error("[HTTP] Get exception:" + ioe.getMessage());
                }
            } else {
                expectBody = httpRouteResponse.getBody();
            }
            if (httpRouteProxy!=null) {
                if (httpRouteProxy.isEnabled()) {
                    expectBody = "{\"matched\":\"[GET]+[Path]+[1 query param name]+[1 query param value]\"}";
                }
            }


            if (expectBody == null) {
                expectBody = httpMockConfigDefaults.getBody();
            }

            if (httpRouteResponse.getCompress()!=null && httpRouteResponse.getCompress().equalsIgnoreCase("gzip")) {
                byte[] uncompressBytes =CompressUtil.uncompress(bodyBytes);
                body = new String(uncompressBytes);
            }
            Assert.assertEquals(statusCode,expectStatusCode, "HTTP status code not match");
            Assert.assertEquals(contentType,expectContentType, "HTTP Content-Type not match");
            Assert.assertEquals(body, expectBody, "HTTP body not match");

            if (httpRouteCallbacks!=null) {
                for (HttpRouteCallback httpRouteCallback : httpRouteCallbacks) {

                }
            }
        }
    }
}
