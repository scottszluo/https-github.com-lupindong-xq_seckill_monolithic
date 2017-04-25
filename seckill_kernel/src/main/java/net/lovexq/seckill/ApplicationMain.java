package net.lovexq.seckill;

import net.lovexq.seckill.core.repository.impl.BasicRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 程序入口
 *
 * @author LuPindong
 * @time 2017-04-19 06:53
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BasicRepositoryImpl.class)
public class ApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
