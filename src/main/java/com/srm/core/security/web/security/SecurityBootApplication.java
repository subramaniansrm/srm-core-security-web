package com.srm.core.security.web.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class SecurityBootApplication extends WebMvcConfigurerAdapter {

	/*@Autowired
	private UserServiceCheckInterceptor userServiceIn;*/

	public static void main(String[] args) {
		SpringApplication.run(SecurityBootApplication.class, args);
	}

	/*@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userServiceIn);
	}*/

}
