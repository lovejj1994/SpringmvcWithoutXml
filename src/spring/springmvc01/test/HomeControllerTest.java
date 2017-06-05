package spring.springmvc01.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import spring.springmvc01.controller.HomeController;

public class HomeControllerTest {
	@Test
	//测试基本的HomeController接受请求并返回请求
	public void test() throws Exception {
		HomeController home = new HomeController();
		MockMvc mockMvc = standaloneSetup(home).build();
		MvcResult andReturn = mockMvc.perform(get("/"))
				.andExpect(view().name("home"))
				.andReturn();
		andReturn.getModelAndView().getViewName();
		
	}


}
