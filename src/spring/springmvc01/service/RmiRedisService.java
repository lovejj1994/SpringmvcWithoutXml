package spring.springmvc01.service;

import spring.springmvc01.bean.RedisPan;

public interface RmiRedisService extends BaseService{
	void redissave(RedisPan pan);
	RedisPan redisget(int id);
}
