package com.alibaba.tsmock.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

public class MqSender {
	private final Logger logger = LoggerFactory.getLogger(MqSender.class);
	private static MqSender mqSender = null;
	private final DefaultMQProducer _producer;
	private String metaqProducerGroup;

	private MqSender() {
		metaqProducerGroup = UUID.randomUUID().toString();
		logger.debug("[MQ] Use random uuid as producer group:" + metaqProducerGroup);
		_producer = new DefaultMQProducer(metaqProducerGroup);
		try {
			_producer.start();
		} catch (final MQClientException e) {
			logger.error("[MQ] start metaq producer error: " + e.getMessage(), e);
		}
	}

	private MqSender(String producer) {
		logger.debug("[MQ] Producer group:" + producer);
		_producer = new DefaultMQProducer(producer);
		try {
			_producer.start();
		} catch (final MQClientException e) {
			logger.error("[MQ] start metaq producer error: " + e.getMessage(), e);
		}
	}


	public static MqSender getMqSender() {
		if (mqSender == null) {
			mqSender = new MqSender();
		}
		return mqSender;
	}


	public static MqSender getMqSender(String metaqProducerGroup) {
		if (mqSender == null) {
			mqSender = new MqSender( metaqProducerGroup);
		}
		return mqSender;
	}

	public void send(String topic, String tag, String keys, String body) {
		final Message message = new Message(topic, tag, keys, body.getBytes());
		this.send(message);
	}

	public void send(Message message) {
		final String topic = message.getTopic();
		if (StringUtils.isEmpty(topic)) {
			throw new IllegalArgumentException("topic must not be empty");
		}

		final String body = new String(message.getBody());
		if (StringUtils.isEmpty(body)) {
			throw new IllegalArgumentException("message body must not be empty");
		}

		final int sleepSeconds = 5;
		int retryTimes = 3;
		int sendTimes = 1;
		SendStatus sendStatus;
		while (retryTimes > 0) {
			try {
				final SendResult sendResult = _producer.send(message);
				sendStatus = sendResult.getSendStatus();
				if (!SendStatus.SEND_OK.equals(sendStatus)) {
					throw new RuntimeException("send status was wrong: " + sendStatus);
				}
				logger.info("[MQ] send message to topic [{}] success, ", topic);
				logger.debug("[MQ] body is: \n{}", body);
				return;
			} catch (final Exception e) {
				logger.error("[MQ] send message to topic {} error at send times {}: ", topic, sendTimes, e);
				if (retryTimes > 1) {
					logger.debug("[MQ] will retry at {} seconds later", sleepSeconds);
				}
				retryTimes--;
				sendTimes++;
				try {
					Thread.sleep(1000 * sleepSeconds);
				} catch (final InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		logger.warn("send message to topic {} failed, body is: \n{}",  topic, body);
	}
}
