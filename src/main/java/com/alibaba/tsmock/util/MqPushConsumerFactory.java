package com.alibaba.tsmock.util;

import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qinjun.qj on 2018/5/26.
 */
public class MqPushConsumerFactory {
    private static Map<String, MqPushConsumer> pool = new ConcurrentHashMap<String, MqPushConsumer>();

    public static MqPushConsumer getInstance(String consumerName,String topic, String tag, MessageListenerConcurrently pushListener) {
        MqPushConsumer mqPushConsumer = null;

        final String key = ((consumerName==null?"":consumerName) + "_" + (topic==null?"":topic) +"_"+(tag==null?"":tag));
        if (!pool.containsKey(key)) {
            mqPushConsumer = new MqPushConsumer(consumerName, topic, tag, pushListener);
            pool.put(key, mqPushConsumer);
        } else {
            mqPushConsumer = pool.get(key);
        }
        return mqPushConsumer;
    }
}
