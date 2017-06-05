package spring.springmvc01.service;

import spring.springmvc01.bean.RedisPan;

public interface HttpRedisService extends BaseService{
	void redissave(RedisPan pan);
	RedisPan redisget(int id);
}
