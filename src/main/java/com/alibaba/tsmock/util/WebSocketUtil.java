/**  
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: WebSocketUtil.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.util
 * @Description: 
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:53
 * @version: v1.0
 */
package com.alibaba.tsmock.util;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * The Class WebSocketUtil.
 *
 * @ClassName: WebSocketUtil
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:53
 */
@ClientEndpoint
public class WebSocketUtil {

	/** The user session. */
	Session userSession = null;
	
	/** The message handler. */
	private MessageHandler messageHandler;

	/**
	 * Instantiates a new web socket util.
	 *
	 * @param endpointURI
	 *            the endpoint URI
	 */
	public WebSocketUtil(URI endpointURI) {
		try {
			final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Callback hook for Connection open events.
	 *
	 * @param userSession
	 *            the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("opening websocket");
		this.userSession = userSession;
	}

	/**
	 * Callback hook for Connection close events.
	 *
	 * @param userSession
	 *            the userSession which is getting closed.
	 * @param reason
	 *            the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a
	 * client send a message.
	 *
	 * @param message
	 *            The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		if (messageHandler != null) {
			messageHandler.handleMessage(message);
		}
	}

	/**
	 * register message handler.
	 *
	 * @param msgHandler
	 *            the msg handler
	 */
	public void addMessageHandler(MessageHandler msgHandler) {
		messageHandler = msgHandler;
	}

	/**
	 * Send a message.
	 *
	 * @param message
	 *            the message
	 */
	public void sendMessage(String message) {
		userSession.getAsyncRemote().sendText(message);
	}

	/**
	 * The Interface MessageHandler.
	 *
	 * @ClassName: WebSocketUtil
	 * @Description:
	 * @author: qinjun.qj
	 * @date: 2017-1-7 16:46:53
	 */
	public static interface MessageHandler {

		/**
		 * Handle message.
		 *
		 * @param message
		 *            the message
		 */
		public void handleMessage(String message);
	}
}