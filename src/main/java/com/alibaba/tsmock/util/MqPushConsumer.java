package com.alibaba.tsmock.util;

import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.tsmock.core.mq.MqMockResponseHandler;
import com.alibaba.tsmock.po.mq.MqInfo;
import com.alibaba.tsmock.po.mq.MqRouteInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.metaq.client.MetaPushConsumer;

import java.util.UUID;

public class MqPushConsumer {
    private static Logger logger = LoggerFactory.getLogger(MqPushConsumer.class);
    private static MqPushConsumer mqPushConsumer;
    private MetaPushConsumer _consumer;
    private String recvMsg;






    public MqPushConsumer(String consumerName,String topic, String tag, MessageListenerConcurrently pushListener) {
        if (StringUtils.isEmpty(consumerName)) {
            consumerName = UUID.randomUUID().toString();
        }
        try {
            _consumer = new MetaPushConsumer(consumerName);

            _consumer.subscribe(topic, tag);

            _consumer.setConsumeMessageBatchMaxSize(1);

            _consumer.registerMessageListener(pushListener);

            _consumer.start();
        } catch (final Exception e) {
            logger.error("Get Exception:" + e);
        }

        logger.info("Consumer Started,topic:[{}],tag:[{}]", topic, tag);
    }


    public String getRecvMsg() {
        return recvMsg;
    }

    public void setRecvMsg(String recvMsg) {
        this.recvMsg = recvMsg;
    }

    public static void setMqPushConsumer(MqPushConsumer mqPushConsumer) {
        MqPushConsumer.mqPushConsumer = mqPushConsumer;
    }

}
