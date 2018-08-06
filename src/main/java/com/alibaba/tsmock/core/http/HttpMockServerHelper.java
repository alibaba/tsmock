/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpMockServerHelper.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.util
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:33:22
 * @version: v1.0
 */

package com.alibaba.tsmock.core.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.alibaba.tsmock.po.http.HttpRequestFormOrBody;
import com.alibaba.tsmock.proxy.HttpMockProxyClient;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.Options;

import com.alibaba.tsmock.config.EnumConfigType;
import com.alibaba.tsmock.config.MockServerConfig;
import com.alibaba.tsmock.path.PathHandlerFactory;
import com.alibaba.tsmock.po.http.HttpMockConfig;
import com.alibaba.tsmock.po.http.HttpServerOption;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.ProxyHandler;

/**
 *
 * @ClassName: HttpMockServerHelper
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:33:22
 */
public final class HttpMockServerHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpMockServerHelper.class);

    /**
     * Instantiates a new http core util.
     */
    private HttpMockServerHelper() {
    }

    /**
     * Start http core.
     *
     * @return the undertow
     */
    public static Undertow startHttpServer() {
        Undertow httpServer = null;

        try {

            final HttpMockProxyClient mockProxyClient = new HttpMockProxyClient();
            final HttpHandler proxyHandler = new ProxyHandler(mockProxyClient, 300000000,
                    ResponseCodeHandler.HANDLE_404);
            final HttpMockConfig httpMockConfig = (HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP);
            final Integer port = httpMockConfig.getPort();
            final String host = httpMockConfig.getHost();
            final List<HttpServerOption> serverOptions = httpMockConfig.getServerOptions();
            final PathHandler pathHandler = PathHandlerFactory.create();

            Builder httpServerBuilder = Undertow.builder().addHttpListener(port, host).setHandler(proxyHandler);
            if ((serverOptions != null) && (serverOptions.size() > 0)) {
                for (final HttpServerOption serverOption : serverOptions) {
                    logger.debug("[HTTP] Set option:" + serverOption);
                    if (serverOption.getName().equalsIgnoreCase("REQUEST_PARSE_TIMEOUT")) {
                        final Integer requestParseTimeOut = Integer.valueOf(serverOption.getValue());
                        httpServerBuilder = httpServerBuilder.setServerOption(UndertowOptions.REQUEST_PARSE_TIMEOUT,
                                requestParseTimeOut);
                    }
                    if (serverOption.getName().equalsIgnoreCase("WORKER_IO_THREADS")) {
                        final Integer workIOThreads = Integer.valueOf(serverOption.getValue());
                        httpServerBuilder = httpServerBuilder.setIoThreads(workIOThreads);
                    }
                    if (serverOption.getName().equalsIgnoreCase("WORKER_TASK_CORE_THREADS")) {
                        final Integer workTaskCoreThreads = Integer.valueOf(serverOption.getValue());
                        httpServerBuilder = httpServerBuilder.setWorkerThreads(workTaskCoreThreads);
                        httpServerBuilder = httpServerBuilder.setWorkerOption(Options.WORKER_TASK_CORE_THREADS,
                                workTaskCoreThreads);
                        httpServerBuilder = httpServerBuilder.setWorkerOption(Options.WORKER_TASK_MAX_THREADS,
                                workTaskCoreThreads * 10);
                    }
                    if (serverOption.getName().equalsIgnoreCase("BACKLOG")) {
                        final Integer backlog = Integer.valueOf(serverOption.getValue());
                        httpServerBuilder = httpServerBuilder.setSocketOption(Options.BACKLOG, backlog);
                    }
                }

            }
            httpServer = httpServerBuilder.build();
            httpServer.start();

        } catch (final Exception ex) {
            throw new IllegalStateException("Could not start the TSMockMain.", ex);
        }

        return httpServer;
    }

    /**
     * Extract body.
     *
     * @param exchange the exchange
     * @return  HttpRequestFormOrBody
     */
    public static HttpRequestFormOrBody extractBody(final HttpServerExchange exchange) {
        HttpRequestFormOrBody httpRequestFormOrBody = new HttpRequestFormOrBody();
        String requestBody = null;
        if (exchange == null) {
            return null;
        }
        if (hasBody(exchange)) {
            HeaderMap headerMap = exchange.getRequestHeaders();
            HeaderValues headerValues = headerMap.get("Content-Type");
            if (headerValues != null && headerValues.contains("application/x-www-form-urlencoded; charset=UTF-8")) {
                Map<String, String> form = null;
                final FormParserFactory parserFactory = FormParserFactory.builder().build();
                final FormDataParser parser = parserFactory.createParser(exchange);
                if (parser != null) {
                    try {
                        FormData data = parser.parseBlocking();
                        Iterator<String> it = data.iterator();
                        if (it.hasNext()) {
                            form = new HashMap<String, String>();
                            while (it.hasNext()) {
                                String formName = it.next();
                                for (FormData.FormValue formValue : data.get(formName)) {
                                    form.put(formName, formValue.getValue());
                                }
                            }
                        }
                    } catch (IOException e) {
                        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
                    }
                }
                httpRequestFormOrBody.setForm(form);
            } else {
                exchange.startBlocking();
                final InputStream stream;
                try {
                    if (headerMap.contains(Headers.CONTENT_ENCODING)
                            && headerMap.get(Headers.CONTENT_ENCODING).get(0).equalsIgnoreCase("gzip")) {
                        stream = new GZIPInputStream(exchange.getInputStream());
                    } else {
                        stream = exchange.getInputStream();
                    }

                    final byte[] data = new byte[100];
                    int read;
                    final ByteArrayOutputStream out = new ByteArrayOutputStream();

                    while ((read = stream.read(data)) != -1) {
                        out.write(data, 0, read);
                    }
                    requestBody = new String(out.toByteArray(), StandardCharsets.UTF_8);
                } catch (final IOException ioe) {
                    logger.error("Get exception when read the requestBody:" + ioe);
                }
                httpRequestFormOrBody.setBody(requestBody);
            }

        }
        return httpRequestFormOrBody;
    }


    private static boolean hasBody(HttpServerExchange exchange) {
        int length = (int) exchange.getRequestContentLength();
        if (length == 0) return false;  // if body is empty, skip reading

        HttpString method = exchange.getRequestMethod();
        return Methods.POST.equals(method) || Methods.PUT.equals(method);
    }
}