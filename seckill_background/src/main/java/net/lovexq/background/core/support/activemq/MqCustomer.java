package net.lovexq.background.core.support.activemq;

import net.lovexq.background.crawler.service.CrawlerService;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

/**
 * Created by LuPindong on 2017-2-15.
 */
@Component
public class MqCustomer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqCustomer.class);

    @Autowired
    private CrawlerService crawlerService;

    /**
     * 处理创建记录消息
     */
    @JmsListener(destination = "LianJiaCrawler.Initialize.Queue")
    public void receiveInitializeQueueMessage(Message message) {
        if (message instanceof ActiveMQBytesMessage) {
            ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
            try {
                ActiveMQQueue queue = (ActiveMQQueue) bytesMessage.getJMSDestination();
                byte[] dataArray = bytesMessage.getContent().getData();
                LOGGER.info("【接收消息】>>>队列目的地：{}", queue.getQueueName());
                crawlerService.saveInitializeData(dataArray);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 处理更新记录消息
     */
    @JmsListener(destination = "LianJiaCrawler.Check.Queue")
    public void receiveCheckQueueMessage(Message message) {
        if (message instanceof ActiveMQBytesMessage) {
            ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
            try {
                ActiveMQQueue queue = (ActiveMQQueue) bytesMessage.getJMSDestination();
                byte[] dataArray = bytesMessage.getContent().getData();
                LOGGER.info("【接收消息】>>>队列目的地：{}", queue.getQueueName());
                crawlerService.saveCheckData(dataArray);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

}
