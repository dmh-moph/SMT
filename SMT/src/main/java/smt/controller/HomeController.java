package smt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import smt.auth.model.Activeuser;
import smt.auth.model.SecurityUser;
import smt.auth.service.SecUserLoginHistoryRepository;
import smt.service.EntityService;

@Controller
public class HomeController {
	public static Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private EntityService entityService;
	
	@Autowired
	private SecUserLoginHistoryRepository secUserLoginHistoryRepository;
	
	@RequestMapping("/")
	public String home(Model model, @Activeuser SecurityUser user) {
		logger.debug("username: " + user.toString());
		
		addVisitorCounter(model);
		
		return "home";
	}
	
	private void addVisitorCounter(Model model) {
		// TODO Auto-generated method stub
		Integer totalVisitor = secUserLoginHistoryRepository.countAllUser();
		model.addAttribute("totalVisitor", totalVisitor);
	}

	@RequestMapping("/a01")
	public String a01Handle(Model model) {
		model.addAttribute("title","a01: ข้อมูลผู้ใช้งาน");
		model.addAttribute("typeTxt", "ข้อมูลผู้ใช้งาน");
		
		addVisitorCounter(model);
		return "a01";
	}
	
	@RequestMapping("/a02")
	public String a02Handle(Model model) {
		model.addAttribute("title","a02: สถิติการ download ข้อมูล");
		model.addAttribute("typeTxt", "สถิติการ download ข้อมูล");
		
		addVisitorCounter(model);
		return "a02";
	}
	
	@RequestMapping("/m08")
	public String m08Handle(Model model) {
		model.addAttribute("title","m08: ข้อมูลรายงานผลการดำเนินงานศูนย์ให้คำปรึกษาคุณภาพ (Psychosocial Clinic)");
		
		addVisitorCounter(model);
		return "m08";
	}
	
	@RequestMapping("/m06")
	public String m06Handle(Model model, @Activeuser SecurityUser user) {
		model.addAttribute("roles", user.getRoles().toString() );
		model.addAttribute("title","m06: : ข้อมูลเครือข่ายที่ทำงานเกี่ยวกับเด็กและวัยรุ่น");
		
		addVisitorCounter(model);
		return "m06";
	}
	
	@RequestMapping("/m03")
	public String m03Handle(Model model) {
		model.addAttribute("behaviorType", "B");
		model.addAttribute("title","m03: ข้อมูลด้านพฤติกรรมปัญหาวัยรุ่น");
		model.addAttribute("behaviorTypeTxt", "พฤติกรรมปัญหา");
		
		addVisitorCounter(model);
		return "m0304";
	}
	
	@RequestMapping("/m04")
	public String m04Handle(Model model) {
		model.addAttribute("behaviorType", "R");
		model.addAttribute("title","m04: ข้อมูลด้านพฤติกรรมเสี่ยงวัยรุ่น");
		model.addAttribute("behaviorTypeTxt", "พฤติกรรมเสี่ยง");
		
		addVisitorCounter(model);
		return "m0304";
	}

	@RequestMapping("/m01")
	public String m01Handle(Model model) {
		
		model.addAttribute("title","m01: ข้อมูลสถานการณ์ปัญหาสุขภาพจิตวัยรุ่น");
		model.addAttribute("typeTxt", "ข้อมูลสถานการณ์ปัญหาสุขภาพจิตวัยรุ่น");
		
		addVisitorCounter(model);
		return "m01";
	}
	
	@RequestMapping("/m07")
	public String m07Handle(Model model) {
		
		model.addAttribute("title","m07: ข้อมูลงานวิจัยสถานการณ์ปัญหาวัยรุ่น");
		model.addAttribute("typeTxt", "ข้อมูลงานวิจัยสถานการณ์ปัญหาวัยรุ่น");
		
		addVisitorCounter(model);
		return "m07";
	}
	
	@RequestMapping("/m02")
	public String m02Handle(Model model, @Activeuser SecurityUser user) {
		model.addAttribute("roles", user.getRoles().toString() );
		model.addAttribute("title","m02: ข้อมูลทางวิชาการ");
		model.addAttribute("typeTxt", "ข้อมูลวิชาการ");
		
		addVisitorCounter(model);
		return "m02";
	}
	
	@RequestMapping("/m05")
	public String m05Handle(Model model) {
		
		model.addAttribute("title","m05: ข้อมูลงานวิจัยและการสำรวจเฝ้าระวังปัญหาวัยรุ่น");
		model.addAttribute("typeTxt", "ข้อมูลงานวิจัยและการสำรวจเฝ้าระวังปัญหาวัยรุ่น");
		
		addVisitorCounter(model);
		return "m05";
	}
	
	@RequestMapping("/m09")
	public String m09Handle(Model model) {
		
		model.addAttribute("title","m09: ข้อมูสถานการณ์ปัญหา");
		model.addAttribute("typeTxt", "ข้อมูสถานการณ์ปัญหา");
		
		addVisitorCounter(model);
		return "m09";
	}
	
}
