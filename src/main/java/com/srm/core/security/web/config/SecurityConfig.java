package com.srm.core.security.web.config;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.srm.coreframework.response.CustomUserDetails;
import com.srm.coreframework.security.OAuth2AuthenticationUser;


/**
 */
@Configuration
@EnableWebSecurity
@PropertySource(value = { "classpath:configuration.properties" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${security.signing-key}")
	private String signingKey;

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);
		converter.setAccessTokenConverter(getAuthenticationAccessTokenConverter());
		return converter;
	}

	private DefaultAccessTokenConverter getAuthenticationAccessTokenConverter() {
		return new DefaultAccessTokenConverter() {
			@Override
			public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
				OAuth2Authentication authentication = (OAuth2Authentication) super.extractAuthentication(map);

				OAuth2AuthenticationUser authenticationUser = new OAuth2AuthenticationUser(
						authentication.getOAuth2Request(), authentication.getUserAuthentication());
				LinkedHashMap linkedHashMap = (LinkedHashMap) map.get("userDetails");
				CustomUserDetails customUserDetails = new CustomUserDetails((String) linkedHashMap.get("username"));
				customUserDetails.setAuthentication((Boolean) linkedHashMap.get("enabled"));
				customUserDetails.setAuthorities((ArrayList<GrantedAuthority>) linkedHashMap.get("authorities"));
				customUserDetails.setFirstName((String) linkedHashMap.get("firstName"));
				customUserDetails.setLastName((String) linkedHashMap.get("lastName"));
				customUserDetails.setPassword((String) linkedHashMap.get("password"));
				customUserDetails.setEntityId((Integer)linkedHashMap.get("entityId"));
				customUserDetails.setRoleId((Integer)linkedHashMap.get("roleId"));
				customUserDetails.setLangCode((String)linkedHashMap.get("langCode"));
				
				Integer userId = new Integer(0);
				if(linkedHashMap.get("userId")!=null) {
					userId = new Integer(linkedHashMap.get("userId").toString());
				}
				
				customUserDetails.setUserId(userId);
				
				/*customUserDetails.setEmployeeId(new Long(linkedHashMap.get("employeeId").toString()));
				customUserDetails.setEmployeeName((String) linkedHashMap.get("employeeName"));
				customUserDetails.setEmployeeCode((String) linkedHashMap.get("employeeCode"));*/
				authenticationUser.setCustomUserDetails(customUserDetails);
			
				return authenticationUser;
			}
		};
	}

	@Bean
	public ResourceServerTokenServices tokenService() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		OAuth2AuthenticationManager authManager = new OAuth2AuthenticationManager();
		authManager.setTokenServices(tokenService());
		return authManager;
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**" , "/swagger-ui.html", "/jars/**");
    }

}