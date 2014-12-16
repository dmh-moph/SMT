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
	
	@RequestMapping("/m03")
	public String m03Handle(Model model) {
		model.addAttribute("behaviorType", "B");
		model.addAttribute("title","m03: ข้อมูลด้านพฤติกรรมปัญหาของวัยรุ่น");
		model.addAttribute("behaviorTypeTxt", "พฤติกรรมปัญหา");
		return "m03";
	}
	
	@RequestMapping("/m04")
	public String m04Handle(Model model) {
		model.addAttribute("behaviorType", "R");
		model.addAttribute("title","m04: ข้อมูลด้านพฤติกรรมเสี่ยงของวัยรุ่น");
		model.addAttribute("behaviorTypeTxt", "พฤติกรรมเสี่ยง");
		return "m03";
	}
}
