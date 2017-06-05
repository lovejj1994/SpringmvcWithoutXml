package spring.springmvc01.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;

import spring.springmvc01.bean.RedisPan;

@Service
public class AlertServiceImpl implements AlertService {

	private static final Log logger = LogFactory.getLog(AlertServiceImpl.class);
	
	@Autowired
	@Qualifier("jmsTemplate")
	private JmsOperations jmsOperations;
	
	@Override
	public void sendRedisPanAlert(final RedisPan redisPan) {
		jmsOperations.convertAndSend(redisPan);
		logger.info("jms--一个redispan被发送，redis的name是"+redisPan.getName());
	}

}
