package com.dfs.agentapp;

import com.dfs.agentapp.config.AESDecryptionFilter;
import com.dfs.agentapp.util.AESencryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgentappApplication {
	@Autowired
	private AESencryption aeSencryption;

	public static void main(String[] args) {
		SpringApplication.run(AgentappApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AESDecryptionFilter> aesDecryptionFilter() {
		FilterRegistrationBean<AESDecryptionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new AESDecryptionFilter());
		registrationBean.addUrlPatterns("/*"); // Endpoint to apply decryption
		return registrationBean;
	}


}
