package com.dfs.app;

import com.dfs.app.config.AESDecryptionFilter;
import com.dfs.app.util.AESencryption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppApplication {

	public static void main(String[] args) {

		SpringApplication.run(AppApplication.class, args);


	}
    @Bean
    public FilterRegistrationBean<AESDecryptionFilter> aesDecryptionFilter() {
        FilterRegistrationBean<AESDecryptionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AESDecryptionFilter());
        registrationBean.addUrlPatterns("/*"); // Endpoint to apply decryption
        return registrationBean;
    }

}
