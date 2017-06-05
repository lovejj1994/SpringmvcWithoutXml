package spring.springmvc01.config;

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.google.common.collect.Sets;

@Configuration
@EnableWebMvc
@ComponentScan("spring.springmvc01.controller")
public class SpringMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 普通配置视图解析器
	 * 
	 * @return
	 */
	// @Bean
	// public ViewResolver viewResolver(){
	// InternalResourceViewResolver resolver = new
	// InternalResourceViewResolver();
	// resolver.setPrefix("/WEB-INF/views/");
	// resolver.setSuffix(".html");
	// resolver.setExposeContextBeansAsAttributes(true);
	// return resolver;
	// }
	private static final String UTF8 = "UTF-8";

	// classpath模板引擎
	@Bean(name="classLoaderTemplateResolver")
	public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
		ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
		classLoaderTemplateResolver.setPrefix("/WEB-INF/views/template");
		classLoaderTemplateResolver.setSuffix(".html");
		classLoaderTemplateResolver.setCharacterEncoding(UTF8);
		classLoaderTemplateResolver.setTemplateMode(TemplateMode.HTML);
		classLoaderTemplateResolver.setOrder(2);
		return classLoaderTemplateResolver;
	}

	// 模板解析器
	@Bean(name="templateResolver")
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(UTF8);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setOrder(1);
		return templateResolver;
	}

	// 模板引擎
	@Bean(name="templateEngine")
	public TemplateEngine templateEngine(ClassLoaderTemplateResolver classLoaderTemplateResolver,
			SpringResourceTemplateResolver templateResolver) {
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		// springTemplateEngine.setTemplateResolver(templateResolver());
		Set<ITemplateResolver> iTemplateResolvers = Sets.newHashSet();
		iTemplateResolvers.add(templateResolver);
		iTemplateResolvers.add(classLoaderTemplateResolver);
		springTemplateEngine.setTemplateResolvers(iTemplateResolvers);
		springTemplateEngine.addDialect(new SpringSecurityDialect());
		return springTemplateEngine;
	}

	// Thymeleaf视图解析器
	@Bean
	public ViewResolver viewResolver(TemplateEngine templateEngine) {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		viewResolver.setCharacterEncoding(UTF8);
		return viewResolver;
	}

	/**
	 * 配置标准文件上传（servlet3.0）
	 * 
	 * @return
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	/**
	 * 配置静态资源处理
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
