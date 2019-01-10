package com.neuq.question;

import com.mongodb.MongoClientOptions;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@Configuration
public class ApplicationStarter {


    public static void main(String[] args) {
        LocaleContextHolder.setDefaultLocale(Locale.CHINA);
        new SpringApplicationBuilder(ApplicationStarter.class).web(WebApplicationType.SERVLET).run(args);
    }


    @Bean
    public MongoClientOptions mongoOptions() {

        return MongoClientOptions.builder()
                .socketTimeout(60000)
                .connectionsPerHost(200)
                .threadsAllowedToBlockForConnectionMultiplier(5)
                .connectTimeout(3000)
                .maxWaitTime(10000)
                .build();

    }
}

