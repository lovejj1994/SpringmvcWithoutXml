package spring.springmvc01.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.springmvc01.bean.RedisPan;

@Service
public class RabbitAlertServiceImpl implements AlertService {

	private static final Log logger = LogFactory.getLog(RabbitAlertServiceImpl.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public void sendRedisPanAlert(final RedisPan redisPan) {
		rabbitTemplate.convertAndSend("test.rabbit.direct","test.rabbit.binding",redisPan);
		logger.info("rabbit--一个redispan被发送，redis的name是"+redisPan.getName());
	}

}
