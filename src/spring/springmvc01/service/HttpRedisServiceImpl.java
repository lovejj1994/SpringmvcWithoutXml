package spring.springmvc01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import spring.springmvc01.bean.RedisPan;

@Service
public class HttpRedisServiceImpl implements HttpRedisService{

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;
	
	@Override
	public RedisPan redisget(int id) {
		return (RedisPan) redisTemplate.opsForValue().get(String.valueOf(id));
	}

	@Override
	public void redissave(RedisPan pan) {
		redisTemplate.opsForValue().set(String.valueOf(pan.getId()), pan);
	}

}
