package spring.springmvc01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spring.springmvc01.bean.RedisPan;

@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/rest/")
public class RestController {
	
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;
	
	
	// 供SpringmvcWithoutXmlClient客户端调用
	@RequestMapping(method = RequestMethod.GET, value = "restredisFindOne/{id}")
	public RedisPan restRedisFindOne(@PathVariable(value = "id") int id,
			RedirectAttributes redirectAttributes) {
		RedisPan pan = (RedisPan) redisTemplate.opsForValue().get(String.valueOf(id));
		return pan;
	}
}
