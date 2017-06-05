package spring.springmvc01.config;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.type.TypeFactory;

import spring.springmvc01.bean.RedisPan;
import spring.springmvc01.repository.JpaSpringDataHibernatePanRepository;
import spring.springmvc01.service.HttpRedisService;
import spring.springmvc01.service.HttpRedisServiceImpl;
import spring.springmvc01.service.RmiRedisService;
import spring.springmvc01.service.RmiRedisServiceImpl;

@Configuration
@ComponentScan(basePackages = { "spring.springmvc01" }, excludeFilters = {
		// 不扫描@EnableWebMvc注解的组件
		@Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) 
})
@EnableTransactionManagement
// 扫描 它的 基础 包 来 查找 扩展 自 Spring Data JPA Repository 接口 的 所有 接口。
@EnableJpaRepositories(basePackageClasses = { JpaSpringDataHibernatePanRepository.class }
// transactionManagerRef="jpaTransactionManager",
		, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "jpaTransactionManager")
@EnableCaching
public class SpringConfig {

	@Autowired
	private JndiObjectFactoryBean jndiObjectFactoryBean;

	// spring-jdbc
	// 获取jndi数据库源，jndi由中间件配置
	@Bean
	public JndiObjectFactoryBean dataResourse() {
		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
		jndiObjectFactoryBean.setJndiName("jdbc/TestDB");
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
		return jndiObjectFactoryBean;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate((DataSource) jndiObjectFactoryBean.getObject());
	}

	// hibernate
	/**
	 * 声明hibernate的session工厂
	 * 
	 * @return
	 */
	@Bean
	public LocalSessionFactoryBean localSessionFactoryBean() {
		LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
		sfb.setDataSource((DataSource) dataResourse().getObject());
		sfb.setPackagesToScan(new String[] { "spring.springmvc01.bean" });
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.format_sql", "true");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		sfb.setHibernateProperties(properties);
		return sfb;
	}

	/**
	 * 给 不使 用 模板 的 Hibernate Repository 添加 异常 转换 功能
	 * 
	 * @return
	 */
	@Bean
	public BeanPostProcessor persistenceTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	// jpa-hibernate

	// Hibernate对Jpa的实现
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		return hibernateJpaVendorAdapter;
	}

	@Bean
	// 定义实体管理器工厂
	// Jpa配置 扮演了容器的角色。完全掌管JPA -->
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lcmf = new LocalContainerEntityManagerFactoryBean();
		lcmf.setDataSource((DataSource) dataResourse().getObject());
		lcmf.setJpaVendorAdapter(jpaVendorAdapter());
		lcmf.setPackagesToScan("spring.springmvc01.jpahibernate", "spring.springmvc01.jpaspringdata");
		return lcmf;
	}

	// jpa要用JpaTransactionManager创建事务，不能跟前面的DataSourceTransactionManager冲突
	// 实验证明JpaTransactionManager支持hibernate 和 jpa-hibernate
	// DataSourceTransactionManager不支持jpa-hibernate
	@Bean
	public PlatformTransactionManager jpaTransactionManager() {
		JpaTransactionManager jtm = new JpaTransactionManager();
		jtm.setEntityManagerFactory(entityManagerFactory().getObject());
		return jtm;
	}

	// spring-jpa-redis
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory jcf = new JedisConnectionFactory();
		jcf.setHostName("localhost");
		return jcf;
	}

	@Bean
	public RedisTemplate redisTemplate() {
		RedisTemplate rt = new RedisTemplate();
		rt.setConnectionFactory(redisConnectionFactory());
		rt.setKeySerializer(new StringRedisSerializer());
		return rt;
	}

	// spring-cache-redis for jpa-hibernate
	@Bean
	public CacheManager cacheManager() {
		return new RedisCacheManager(redisTemplate());
	}

	// @Bean
	/*
	 * public RedisTemplate<Integer, JpaSpringDataHibernatePan>
	 * redisspringdatajpaTemplate() { RedisTemplate<Integer,
	 * JpaSpringDataHibernatePan> rt = new RedisTemplate<Integer,
	 * JpaSpringDataHibernatePan>();
	 * rt.setConnectionFactory(redisConnectionFactory());
	 * rt.setKeySerializer(new StringRedisSerializer()); return rt; }
	 */

	@Bean
	public KeyGenerator mykeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuffer sb = new StringBuffer();
				sb.append(target.getClass().getSimpleName());
				sb.append(",");
				sb.append(method.getName() + ",");

				int i = 0;
				for (Class c : method.getParameterTypes()) {
					sb.append(c.getSimpleName() + ":");
					sb.append(params[i].toString() + "?");
					i++;
				}
				return sb.toString();
			}

		};
	}

	// spring-rmi

	// rmi服务端
	@Bean
	public RmiServiceExporter rmiServiceExporter(RmiRedisServiceImpl rmiRedisServiceImpl) {
		RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
		rmiServiceExporter.setService(rmiRedisServiceImpl);
		rmiServiceExporter.setServiceName("RmiRedisService");
		rmiServiceExporter.setServiceInterface(RmiRedisService.class);
		rmiServiceExporter.setRegistryPort(1099);
		return rmiServiceExporter;
	}

	// http-rmi

	// http服务端
	@Bean(name = "httpredisservice")
	public HttpInvokerServiceExporter httpInvokerServiceExporter(HttpRedisServiceImpl httpRedisServiceImpl) {
		HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
		httpInvokerServiceExporter.setService(httpRedisServiceImpl);
		httpInvokerServiceExporter.setServiceInterface(HttpRedisService.class);
		return httpInvokerServiceExporter;
	}

	// spring-activemq

	// activemq连接工厂
	@Bean
	public ActiveMQConnectionFactory mqConnectionFactory() {
		ActiveMQConnectionFactory mqConnectionFactory = new ActiveMQConnectionFactory();
		mqConnectionFactory.setBrokerURL("tcp://pan:61616");
		return mqConnectionFactory;
	}
	
	
	@Bean
	public MessageConverter messageConverter(){
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTypeIdPropertyName(TypeFactory.defaultInstance().constructType(RedisPan.class).toString());
		return converter;
	}
	
	//信息到达的队列
	@Bean
	public ActiveMQQueue queue() {
		ActiveMQQueue queue = new ActiveMQQueue("test.queue");
		return queue;
	}
	
	@Bean
	public ActiveMQTopic topic() {
		ActiveMQTopic topic = new ActiveMQTopic("test.topic");
		return topic;
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(mqConnectionFactory());
		jmsTemplate.setDefaultDestinationName("test.queue");
		jmsTemplate.setMessageConverter(messageConverter());
		return jmsTemplate;
	}
	
	// spring-amqp
	
	@Bean
	public CachingConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost",5672);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}
	@Bean
	public org.springframework.amqp.support.converter.MessageConverter rabbitMessageConverter(){
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		return converter;
	}
	@Bean
	public Queue rabbitQueue(){
		return new Queue("test.rabbit.queue");
	}
	@Bean
	public DirectExchange directExchange(){
		return new DirectExchange("test.rabbit.direct");
	}
	
	@Bean
	public Binding binding(){
		return BindingBuilder.bind(rabbitQueue()).to(directExchange()).with("test.rabbit.binding");
	}
	
	
	@Bean
	public RabbitAdmin admin(){
		RabbitAdmin admin = new RabbitAdmin(connectionFactory());
		admin.declareQueue(rabbitQueue());
		admin.declareExchange(directExchange());
		admin.declareBinding(binding());
		return admin;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(){
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(rabbitMessageConverter());
		return rabbitTemplate;
	}
	
	
}
