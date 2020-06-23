package com.keith;

import com.keith.config.shiro.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class GylInterfaceApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(GylInterfaceApplication.class, args);
        System.out.println("------ApiApplication start success ! ------");
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GylInterfaceApplication.class);
    }
}
