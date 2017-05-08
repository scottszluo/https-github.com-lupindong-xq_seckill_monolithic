package net.lovexq.background.core.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

/**
 * Created by LuPindong on 2017-2-15.
 */
@EnableJms
@Configuration
public class ActiveMQConfig {

    @Bean
    public Queue initializeQueue() {
        return new ActiveMQQueue("LianJiaCrawler.Initialize.Queue");
    }

    @Bean
    public Queue checkQueue() {
        return new ActiveMQQueue("LianJiaCrawler.Check.Queue");
    }
}