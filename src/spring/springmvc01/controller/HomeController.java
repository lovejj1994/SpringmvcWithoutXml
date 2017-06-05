package spring.springmvc01.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Maps;

import spring.springmvc01.bean.HibernatePan;
import spring.springmvc01.bean.Pan;
import spring.springmvc01.bean.RedisPan;
import spring.springmvc01.jpahibernate.JpaHibernatePan;
import spring.springmvc01.jpaspringdata.JpaSpringDataHibernatePan;
import spring.springmvc01.repository.HibernatePanRepository;
import spring.springmvc01.repository.JpaHibernatePanRepository;
import spring.springmvc01.repository.PanRepository;
import spring.springmvc01.service.AlertService;
import spring.springmvc01.service.SpringDataJpaService;

@Controller
@RequestMapping(value = "/")
public class HomeController implements BaseController {
	
	private static final Log logger = LogFactory.getLog(HomeController.class);


	// 自动注入时最好用接口+@Qualifier 的形式
	@Autowired
	@Qualifier("jdbcRepository")
	private PanRepository jdbcRepository;

	@Autowired
	@Qualifier("hibernateRepository")
	private HibernatePanRepository hibernatePanRepository;

	@Autowired
	@Qualifier("jpaHibernateRepository")
	private JpaHibernatePanRepository jpaHibernateRepository;

	@Autowired
	private SpringDataJpaService springDataJpaService;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate redisTemplate;

	@Autowired
	@Qualifier("alertServiceImpl")
	private AlertService alertService;
	
	@Autowired
	@Qualifier("rabbitAlertServiceImpl")
	private AlertService rabbitAlertService;
	
	@Autowired
	@Qualifier("mailSender")
	private JavaMailSender javaMailSender;
	
	@Autowired(required=false)
	@Qualifier("templateEngine")
	private TemplateEngine templateEngine;
	
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.GET, value = "login")
	public String login(HttpServletRequest request) {
		WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(request);
		webApplicationContext.getBean(TemplateEngine.class,"templateEngine");
		return "login";
	}

	@RequestMapping(method = RequestMethod.POST, value = "file")
	public String postFile(@RequestPart MultipartFile file) {
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.GET, value = "file")
	public String getFile(Model model) {
		model.addAttribute("welcome", "welcome");
		return "home";
	}

	// 如果使用redirect跳转,放进model的属性会自动接在跳转路径的后面
	// 如 下面的跳转url会变成 /redirectPlace?one=aaa,如果用了RedirectAttributes则前面就不会起作用
	@RequestMapping(method = RequestMethod.GET, value = "redirect")
	public String redirect(Model method, RedirectAttributes redirectAttributes) {
		Pan pan = new Pan();
		pan.setName("pan");
		method.addAttribute("one", "aaa");
		method.addAttribute("two", pan); // 对于对象不适用
		redirectAttributes.addFlashAttribute("pan", pan);
		return "redirect:/redirectPlace";
	}

	@RequestMapping(method = RequestMethod.GET, value = "redirectPlace")
	public String redirectPlace(Model method) {
		if (method.containsAttribute("pan")) {
			System.out.println(((Pan) method.asMap().get("pan")).getName());
		}
		return "redirect:/file";
	}

	// spring jdbc

	@RequestMapping(method = RequestMethod.POST, value = "findOne")
	public String findOne(@RequestParam(name = "name") String name, RedirectAttributes redirectAttributes) {
		Pan pan = null;
		try {
			pan = jdbcRepository.findHomeByUserName(name);
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			pan = null;
		}
		// java.util.Objects.requireNonNull(null, "没有找到该用户");
		redirectAttributes.addFlashAttribute("pan", pan);
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.GET, value = "findOne")
	public String findOne() {
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.POST, value = "save")
	public String save(@RequestParam(name = "name") String name, @RequestParam(name = "password") String password,
			@RequestParam(name = "enabled") int enabled) {
		Pan pan = new Pan(name, password, enabled);
		jdbcRepository.save(pan);
		return "redirect:/file";
	}

	// hibernate

	@RequestMapping(method = RequestMethod.POST, value = "hibernatesave")
	public String hibernatesave(@RequestParam(name = "id") int id, @RequestParam(name = "name") String name,
			@RequestParam(name = "password") String password, @RequestParam(name = "enabled") int enabled) {
		HibernatePan pan = new HibernatePan(id, name, password, enabled);
		hibernatePanRepository.save(pan);
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.POST, value = "hibernatefindOne")
	public String hibernatefindOne(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes) {
		HibernatePan pan = hibernatePanRepository.findHomeByUserName(id);
		// java.util.Objects.requireNonNull(null, "没有找到该用户");
		redirectAttributes.addFlashAttribute("hibernatepan", pan);
		return "redirect:/file";
	}

	// jpa-hibernate

	@RequestMapping(method = RequestMethod.POST, value = "jpahibernatesave")
	public String jpahibernatesave(@RequestParam(name = "id") int id, @RequestParam(name = "name") String name,
			@RequestParam(name = "password") String password, @RequestParam(name = "enabled") int enabled) {
		JpaHibernatePan pan = new JpaHibernatePan(id, name, password, enabled);
		jpaHibernateRepository.save(pan);
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.POST, value = "jpahibernatefindOne")
	public String jpaHibernateFindOne(@RequestParam(name = "id") int id, RedirectAttributes redirectAttributes) {
		JpaHibernatePan pan = jpaHibernateRepository.findHomeByUserName(id);
		// java.util.Objects.requireNonNull(null, "没有找到该用户");
		redirectAttributes.addFlashAttribute("jpahibernatepan", pan);
		return "redirect:/file";
	}

	// spring-data-jpa-hibernate

	@RequestMapping(method = RequestMethod.POST, value = "springdatajpahibernatesave")
	public String springdatajpahibernatesave(@RequestParam(name = "id") int id,
			@RequestParam(name = "name") String name, @RequestParam(name = "password") String password,
			@RequestParam(name = "enabled") int enabled) {
		JpaSpringDataHibernatePan pan = new JpaSpringDataHibernatePan(id, name, password, enabled);
		springDataJpaService.save(pan);
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.POST, value = "springdatajpahibernatefindOne")
	public String springdatajpaHibernateFindOne(@RequestParam(name = "id", defaultValue = "-1") int id,
			@RequestParam(name = "name") String name, RedirectAttributes redirectAttributes) {
		List<JpaSpringDataHibernatePan> pan = springDataJpaService.findOne(name, id);
		// java.util.Objects.requireNonNull(null, "没有找到该用户");
		redirectAttributes.addFlashAttribute("springdatajpahibernatepan", pan);
		return "redirect:/file";
	}

	// spring-data-redis

	@RequestMapping(method = RequestMethod.POST, value = "springdataredissave")
	public String springdataredissave(@RequestParam(name = "id") int id, @RequestParam(name = "name") String name,
			@RequestParam(name = "password") String password, @RequestParam(name = "enabled") int enabled) {
		RedisPan pan = new RedisPan(id, name, password, enabled);
		redisTemplate.opsForValue().set(String.valueOf(pan.getId()), pan);
		alertService.sendRedisPanAlert(pan);
		rabbitAlertService.sendRedisPanAlert(pan);
		sendmail(pan);
		return "redirect:/file";
	}

	@RequestMapping(method = RequestMethod.POST, value = "springdataredisFindOne")
	public String springdataredisFindOne(@RequestParam(name = "id", defaultValue = "-1") int id,
			RedirectAttributes redirectAttributes) {
		RedisPan pan = (RedisPan) redisTemplate.opsForValue().get(String.valueOf(id));
		// java.util.Objects.requireNonNull(null, "没有找到该用户");
		redirectAttributes.addFlashAttribute("springdataredispan", pan);
		return "redirect:/file";
	}

	// @返回json需要导入jackson包
	@RequestMapping(method = RequestMethod.GET, value = "jsonrestredisFindOne", produces = { "application/json" })
	public @ResponseBody RedisPan jsonrestredisFindOne(@RequestParam(name = "id", defaultValue = "-1") int id) {
		RedisPan pan = (RedisPan) redisTemplate.opsForValue().get(String.valueOf(id));
		return pan;
	}

	// 返回xml需要类名上注解@XmlRootElement
	@RequestMapping(method = RequestMethod.GET, value = "xmlrestredisFindOne", produces = { "application/xml" })
	public @ResponseBody RedisPan xmlrestredisFindOne(@RequestParam(name = "id", defaultValue = "-1") int id) {
		RedisPan pan = (RedisPan) redisTemplate.opsForValue().get(String.valueOf(id));
		return pan;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "restredissave", consumes = {
			"application/json" }, produces = { "application/json" })
	public Object restredissave(@RequestBody RedisPan pan,UriComponentsBuilder ui) throws JsonGenerationException, JsonMappingException, IOException {
		Map msg = Maps.newHashMap();
		redisTemplate.opsForValue().set(String.valueOf(pan.getId()), pan);
		msg.put("msg", "保存成功");
		return msg;
	}
	
	public void sendmail(RedisPan pan) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,true);
			helper.setTo("994971838@qq.com");
			helper.setSubject("new redis : "+ pan.getName());
			//helper.setText("有一个新的redis被保存");
			helper.setFrom("455234037@qq.com");
			//添加附件
			Path path = Paths.get(HomeController.class.getResource("/").getPath().substring(1, HomeController.class.getResource("/").getPath().length()));
			path = path.getParent().getParent().resolve("image").resolve("QQ图片20160808104058.jpg");
			helper.addAttachment("image.jpg", path.toFile());
			
			path = path.getParent().resolve("QQ截图20160815141018.png");
			
			Context context = new Context();
			context.setVariable("spittleText", pan.getName());
			//context.setVariable("spitterLogo", path.toString());
			String html = templateEngine.process("mail", context);
			
			helper.setText(html, true);
			helper.addInline("spitterLogo", path.toFile());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		javaMailSender.send(message);
		logger.info("邮件发送成功。。。。。。");
	}
}
