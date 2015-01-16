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
	
	@RequestMapping("/m06")
	public String m01Handle(Model model) {
		model.addAttribute("title","m06: : ข้อมูลเครือข่ายที่ทำงานเกี่ยวกับเด็กและวัยรุ่น");
		return "m06";
	}
	
	@RequestMapping("/m03")
	public String m03Handle(Model model) {
		model.addAttribute("behaviorType", "B");
		model.addAttribute("title","m03: ข้อมูลด้านพฤติกรรมปัญหาวัยรุ่น");
		model.addAttribute("behaviorTypeTxt", "พฤติกรรมปัญหา");
		return "m0304";
	}
	
	@RequestMapping("/m04")
	public String m04Handle(Model model) {
		model.addAttribute("behaviorType", "R");
		model.addAttribute("title","m04: ข้อมูลด้านพฤติกรรมเสี่ยงวัยรุ่น");
		model.addAttribute("behaviorTypeTxt", "พฤติกรรมเสี่ยง");
		return "m0304";
	}
	
	@RequestMapping("/m02")
	public String m05Handle(Model model) {
		
		model.addAttribute("title","m02: ข้อมูลทางวิชาการ");
		model.addAttribute("typeTxt", "ข้อมูลวิชาการ");
		return "m02";
	}
}
