/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: HttpMockProxyClient.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.factory.handler
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-16 15:27:36
 * @version: v1.0
 */
package com.alibaba.tsmock.proxy;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.Channel;
import java.util.concurrent.TimeUnit;

import com.alibaba.tsmock.path.PathHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.ChannelListener;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import com.alibaba.tsmock.po.http.HttpRequest;
import com.alibaba.tsmock.po.http.HttpRoute;
import com.alibaba.tsmock.po.http.HttpRouteProxy;
import com.alibaba.tsmock.core.router.http.HttpRouter;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientConnection;
import io.undertow.client.UndertowClient;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyClient;
import io.undertow.server.handlers.proxy.ProxyConnection;

/**
 * @ClassName: HttpMockProxyClient
 * @Description: TODO
 * @author: qinjun.qj
 * @date: 2017-01-16 15:27:36
 */
public class HttpMockProxyClient implements ProxyClient {
	private static Logger logger = LoggerFactory.getLogger(HttpMockProxyClient.class);
	private URI targetUri;

	private final UndertowClient client;

	private static final ProxyTarget TARGET = new ProxyTarget() {
	};

	public HttpMockProxyClient() {
		client = UndertowClient.getInstance();
	}

	@Override
	public ProxyTarget findTarget(HttpServerExchange exchange) {
		return TARGET;
	}

	@Override
	public void getConnection(ProxyTarget target, HttpServerExchange exchange, ProxyCallback<ProxyConnection> callback,
			long timeout, TimeUnit timeUnit) {
		logger.info("===============Receive one HTTP request===============");
		logger.debug("Exchange:" + exchange);
		final HttpRequest httpRequest = HttpRequest.newInstanceForProxy(exchange);
		final HttpRoute httpRoute = HttpRouter.findMatchedRoute(exchange, httpRequest, true);

		boolean proxyEnabled = false;
		if (httpRoute != null) {
			logger.debug("[HTTP] [Proxy] Find matched route:" + httpRoute);

			final HttpRouteProxy httpRouteProxy = httpRoute.getProxy();
			if (httpRouteProxy != null) {
				proxyEnabled = httpRouteProxy.isEnabled();
				if (proxyEnabled) {
					logger.info("[HTTP] [Proxy] Proxy enabled:" + proxyEnabled);
					final String targetUrl = httpRouteProxy.getTargetUrl();
					try {
						targetUri = new URI(targetUrl);

						logger.info("[HTTP] [Proxy] Target uri:" + targetUri);

						exchange.setRequestURI("");

						client.connect(new ConnectNotifier(callback, exchange), targetUri, exchange.getIoThread(),
								exchange.getConnection().getByteBufferPool(), OptionMap.EMPTY);
					} catch (final Exception e) {
						logger.error("[HTTP] [Proxy] Connect to targetUri fail:" + e);
						return;
					}
				}
			}

		}
		if (!proxyEnabled) {
			logger.info("[HTTP] [Proxy] Proxy enabled:" + proxyEnabled);
			try {
				final PathHandler pathHandler = PathHandlerFactory.create();
				pathHandler.handleRequest(exchange);
			} catch (final Exception e) {
				logger.error("[HTTP] [Proxy] Get exception:" + e);
				return;
			}
		}

	}

	private final class ConnectNotifier implements ClientCallback<ClientConnection> {
		private final ProxyCallback<ProxyConnection> callback;
		private final HttpServerExchange exchange;

		private ConnectNotifier(ProxyCallback<ProxyConnection> callback, HttpServerExchange exchange) {
			this.callback = callback;
			this.exchange = exchange;
		}

		@Override
		public void completed(final ClientConnection connection) {
			final ServerConnection serverConnection = exchange.getConnection();
			// we attach to the connection so it can be re-used
			serverConnection.addCloseListener(new ServerConnection.CloseListener() {
				@Override
				public void closed(ServerConnection serverConnection) {
					IoUtils.safeClose(connection);
				}
			});
			connection.getCloseSetter().set(new ChannelListener<Channel>() {
				@Override
				public void handleEvent(Channel channel) {
				}
			});
			callback.completed(exchange,
					new ProxyConnection(connection, targetUri.getPath() == null ? "/" : targetUri.getPath()));
		}

		@Override
		public void failed(IOException e) {
			callback.failed(exchange);
		}
	}

}
