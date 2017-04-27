package net.lovexq.seckill;

import net.lovexq.seckill.core.repository.impl.BasicRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 程序入口
 *
 * @author LuPindong
 * @time 2017-04-19 06:53
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BasicRepositoryImpl.class)
public class ApplicationMain extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[]{ApplicationMain.class});
    }
}