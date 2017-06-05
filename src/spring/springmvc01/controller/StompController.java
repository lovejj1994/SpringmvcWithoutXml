package spring.springmvc01.controller;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import spring.springmvc01.bean.RedisPan;

@Controller
public class StompController {

	private static final Log logger = LogFactory.getLog(StompController.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	// 隐藏了前戳 /app
	@MessageMapping("/demo01")
	@SendTo("/queue/test.rabbit.queue")
	public RedisPan demo01(RedisPan pan) {
		logger.info("接受了一个redispan name为:  " + pan.getName());
		pan.setName(UUID.randomUUID().toString());
		logger.info("发送了一个redispan name为:  " + pan.getName());
		return pan;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/sendtouser")
	public Object demo02(@RequestParam(name = "username") String username) {
		JSONObject jsonObject = new JSONObject();
		simpMessagingTemplate.convertAndSend("/queue/touser-" + username, "欢迎" + username);
		logger.info("发送信息到指定的user,username为:  " + username);
		return jsonObject;
	}

}
