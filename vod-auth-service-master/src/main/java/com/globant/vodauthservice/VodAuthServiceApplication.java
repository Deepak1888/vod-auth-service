package com.globant.vodauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.globant.vodauthservice.config.OauthConfigProperties;
import com.globant.vodauthservice.filter.CustomErrorFilter;



/**
 * @author anuruddh.yadav
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableConfigurationProperties(OauthConfigProperties.class)
public class VodAuthServiceApplication /*extends SpringBootServletInitializer*/{

	public static void main(String[] args) {
		SpringApplication.run(VodAuthServiceApplication.class, args);
	}

	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VodAuthServiceApplication.class);
    }*/
	
	@Bean
	public CustomErrorFilter customFilter() {
	    return new CustomErrorFilter();
	}
	
}
