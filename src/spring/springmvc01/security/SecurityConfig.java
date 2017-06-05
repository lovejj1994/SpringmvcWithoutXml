package spring.springmvc01.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import spring.springmvc01.service.UserService;


//spring-security配置
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JndiObjectFactoryBean jndiObjectFactoryBean;
	@Autowired
	private UserService userService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*auth.inMemoryAuthentication()
			.withUser("admin").password("admin").roles("admin");*/
		/*MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/springinaction4?characterEncoding=utf-8");
		dataSource.setUser("root");
		dataSource.setPassword("1234");*/
//		auth.jdbcAuthentication().dataSource((DataSource) jndiObjectFactoryBean.getObject())
//		.usersByUsernameQuery("select username,password,true from users where username=?")
//		.authoritiesByUsernameQuery("select username,authority from authorities where username=?");
		auth.userDetailsService(userService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//   http  mapsto  https
		http/*.portMapper().http(8080).mapsTo(8444)
		.and()*/
		.csrf().disable()
		.formLogin()
		.loginPage("/login")
		.and()
		.authorizeRequests()
		.mvcMatchers(HttpMethod.GET,"/").authenticated()
		.anyRequest().permitAll()
		.and()
		/*.requiresChannel()
		.antMatchers("/*")
		.requiresSecure()
		.and()*/
		//谷歌游览器测试中，如果不关闭游览器，到了过期时间也不会重新登录，关闭后有效。
		.rememberMe().key("lovejj1994").tokenValiditySeconds(60)
		.and()
		.logout().logoutSuccessUrl("/").and()
		.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
		/*.and()
		.requiresChannel()
		.antMatchers("/redirect*","/")
		.requiresInsecure();*/
	}
	
	@Bean    
	public SessionRegistry sessionRegistry(){    
	    return new SessionRegistryImpl();    
	}    
	
	

	
	
}
