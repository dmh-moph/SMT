package smt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import smt.auth.model.Activeuser;
import smt.auth.model.SecurityUser;
import smt.auth.service.ActiveUserHandlerMethodArgumentResolver;

@Controller
public class HomeController {
	public static Logger logger = LoggerFactory.getLogger(HomeController.class);

	
	@RequestMapping("/")
	public String home(Model model, @Activeuser SecurityUser user) {
		logger.debug("username: " + user.getUsername());
		
		return "home";
	}
	
	@RequestMapping("/m01")
	public String m01Handle(Model model) {
		
		return "m01";
	}
}
