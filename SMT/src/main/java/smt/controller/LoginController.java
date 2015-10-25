package smt.controller;

import java.awt.Color;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.crsh.console.jline.internal.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.backgrounds.SquigglesBackgroundProducer;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import nl.captcha.gimpy.ShearGimpyRenderer;
import nl.captcha.noise.StraightLineNoiseProducer;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
import smt.auth.model.SecurityUser;
import smt.auth.service.SecUserEntityService;
import smt.webUI.ResponseJSend;
import smt.webUI.ResponseStatus;

@Controller
public class LoginController {

	public static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private SecUserEntityService secUserEntityService;
	
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
	
	@RequestMapping(value="/Register", method={RequestMethod.POST})
	public @ResponseBody ResponseJSend<Long> registerUser(@RequestBody JsonNode node, HttpServletRequest request) {
		ResponseJSend<Long> response;
		logger.debug("/Register User.. with captcha: " + node.path("captcha").asText());
		// 
		if(!validateCaptcha(node.path("captcha").asText(), request)) {
			// 
			response = new ResponseJSend<Long>();
			response.status = ResponseStatus.FAIL;
			response.data = null;
			response.message = "CAPTCHA was not match";
		} else {
			SecurityUser systemUser = secUserEntityService.findSecurityUserByUsername("SYSTEM");
			response = this.secUserEntityService.saveSecurityUser(node, systemUser);
		}
		
		return response;
		
	}
	
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	
	public void  getImage(HttpServletRequest request, HttpServletResponse response) {

//	        Captcha captcha = new Captcha.Builder(200, 50)
//	                .addText()
//	                .addNoise()
//	                .addNoise()
//	                .addNoise()
//	                .addBackground()
//	                .build();
	    
		 Random rand = new Random();
		// Create random fish color1
        java.awt.Color fishColor1 = new Color(
                150 + rand.nextInt(55),
                150 + rand.nextInt(55),
                150 + rand.nextInt(55)
        );

        // Create random fish color1
        java.awt.Color fishColor2 = new Color(
                150 + rand.nextInt(55),
                150 + rand.nextInt(55),
                150 + rand.nextInt(55)
        );
		
		 Captcha captcha = new Captcha.Builder(150, 50).addText(new DefaultTextProducer(5), new DefaultWordRenderer())
				 //.addBackground(new GradiatedBackgroundProducer())
				 .addNoise(new StraightLineNoiseProducer(new Color(255, 0, 0), 2))
				// .gimp(new FishEyeGimpyRenderer(fishColor1, fishColor2))
		            .addBorder().build();
		
		 request.getSession().setAttribute(Captcha.NAME, captcha);
		 logger.debug("captcha answer: "+  captcha.getAnswer());
		 
            CaptchaServletUtil.writeImage(response, captcha.getImage());
            
            
           
	}
	
	 public static boolean validateCaptcha(String webCaptcha, HttpServletRequest request ) {
	        Captcha captcha = (Captcha) request.getSession().getAttribute( Captcha.NAME );
	        if( captcha.isCorrect( webCaptcha ) ) {
	            return true;
	        }
	        return false;
	    }

	
	
}
