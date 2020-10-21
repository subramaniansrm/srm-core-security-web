package com.srm.core.security.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.srm.coreframework.security.HttpAuthenticationEntryPoint;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		 http.requestMatchers().and().authorizeRequests()
		  .antMatchers("/rta/**").authenticated()
		  .antMatchers("/rta/**").permitAll()
		 	.antMatchers("/rtaapp/**").authenticated()
		  .antMatchers("/rtaapp/**").permitAll()
		  .antMatchers("/rtaEmail/**").authenticated()
		  .antMatchers("/rtaEmail/**").permitAll();
				 
		/*http
       
        .authorizeRequests()
            .antMatchers("static/assets/**","static/**","rta/**","/")
                .permitAll()
            .anyRequest()
                .authenticated();*/

				
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.authenticationEntryPoint(customAuthEntryPoint());
	}

	@Bean
	public AuthenticationEntryPoint customAuthEntryPoint() {
		return new HttpAuthenticationEntryPoint();
	}

}