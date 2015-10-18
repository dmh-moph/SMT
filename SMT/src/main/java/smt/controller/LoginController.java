package smt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	public static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping("/login")
	public String login(
			@RequestParam(required=false) String error ,
			Model model) {
		
		if(error != null ) {
			model.addAttribute("error", error);
		}
		
		return "auth/login";
	}
	
	@RequestMapping(value="/Register", method={RequestMethod.GET})
	public String register() {
		
		logger.debug("/Register Entering..");
		
		
		return "auth/register";
		
	}
}
