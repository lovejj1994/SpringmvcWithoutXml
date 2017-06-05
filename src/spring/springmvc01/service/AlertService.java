package spring.springmvc01.service;

import spring.springmvc01.bean.RedisPan;

public interface AlertService {
	void sendRedisPanAlert(RedisPan redisPan); 
}
