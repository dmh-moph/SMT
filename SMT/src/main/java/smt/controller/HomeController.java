package smt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@RequestMapping("/")
	public String home(Model model) {
		
		return "home";
	}
	
	@RequestMapping("/m01")
	public String m01Handle(Model model) {
		
		return "m01";
	}
}
