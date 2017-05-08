package net.lovexq.background.core.support.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

/**
 * Created by LuPindong on 2017-2-15.
 */
@Component
public class MqProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqProducer.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendQueueMessage(byte[] dataArray, Queue queue) {
        try {
            LOGGER.info("【发送消息】>>>队列目的地：{}", queue.getQueueName());
            jmsMessagingTemplate.convertAndSend(queue, dataArray);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}