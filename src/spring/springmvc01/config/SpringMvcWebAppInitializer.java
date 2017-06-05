package spring.springmvc01.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * spring通用配置文件
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { SpringConfig.class};
	}

	/**
	 * 主要是springmvc的配置文件
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { SpringMvcConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" }; // 将DispatcherServlet(springmvc所有的请求都会通过DispatcherServlet分发到具体的controller)映射到“/”
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/");
		registration.setMultipartConfig(multipartConfigElement);
	}

	//添加自定义servlet，拦截springInvoker请求
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		ServletRegistration.Dynamic servlet=servletContext.addServlet("httpredisservice", new HttpRequestHandlerServlet());
		servlet.addMapping("/redisservice");
	}
	
	
	

}
