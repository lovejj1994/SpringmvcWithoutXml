package spring.springmvc01.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import spring.springmvc01.service.UserService;

//方法访问控制
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled=true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration{
	
	@Autowired
	private JndiObjectFactoryBean jndiObjectFactoryBean;
	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}

	
}
