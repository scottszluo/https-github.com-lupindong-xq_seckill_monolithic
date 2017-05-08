package net.lovexq.background;

import net.lovexq.background.core.repository.impl.BasicRepositoryImpl;
import net.lovexq.background.core.support.dynamicds.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 程序入口
 *
 * @author LuPindong
 * @time 2017-04-19 06:53
 */
@SpringBootApplication
@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源
@EnableJpaRepositories(repositoryBaseClass = BasicRepositoryImpl.class) // 启用Jpa基类
public class ApplicationMain extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[]{ApplicationMain.class});
    }
}