package net.lovexq.seckill.core.support.activemq;

import net.lovexq.seckill.kernel.service.EstateService;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by LuPindong on 2017-2-15.
 */
@Component
public class MqCustomer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqCustomer.class);

    @Autowired
    private EstateService estateService;

    /**
     * 处理消息
     */
    @JmsListener(destination = "LianJiaCrawler.Queue")
    public void receiveQueueMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                ActiveMQQueue queue = (ActiveMQQueue) textMessage.getJMSDestination();
                LOGGER.info("【接收消息】>>>队列目的地：{}，消息正文{}", queue.getQueueName(), textMessage.getText());
                estateService.saveCrawlerData(textMessage.getText());
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
