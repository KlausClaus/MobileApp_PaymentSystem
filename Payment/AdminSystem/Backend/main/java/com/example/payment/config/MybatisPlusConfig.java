package com.example.payment.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for MyBatis-Plus settings.
 * This class configures the MyBatis-Plus framework for the application, including
 * scanning of mapper interfaces and setting up necessary interceptors like pagination.
 */
@Configuration
@MapperScan("com.example.payment.mapper")
public class MybatisPlusConfig {

    /**
     * Configures the MyBatis-Plus interceptor with a pagination interceptor for MySQL.
     * The interceptor ensures proper handling of pagination queries for the MySQL database.
     *
     * @return A configured {@link MybatisPlusInterceptor} instance.
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // Adding pagination support for MySQL database
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
