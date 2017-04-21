package net.lovexq.seckill.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * Created by LuPindong on 2017-2-15.
 */
//@Configuration
public class JdbcConfiguration {

    @Autowired
    private DataSource masterDataSource;

    @Bean
    public JdbcTemplate masterJdbcTemplate() {
        return new JdbcTemplate(masterDataSource);
    }

}
