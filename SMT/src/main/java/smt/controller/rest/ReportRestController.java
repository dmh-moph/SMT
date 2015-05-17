package smt.controller.rest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;














import smt.model.OrganizationNetwork;
import smt.model.PsychoSocialReport;
import smt.model.glb.HealthZone;
import smt.model.glb.Province;
import smt.service.EntityService;
import smt.view.M08ExcelReport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/Report")
public class ReportRestController {

	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value="/m08Report", method={RequestMethod.POST})
	public ModelAndView m08ReportHandle(Model model, 
			@RequestParam(required=false) String beginDate, 
			@RequestParam(required=false) String endDate,
			@RequestParam(required=false) Long orgId,
			@RequestParam(required=false) Long provinceId,
			@RequestParam(required=false) Long zoneId,
			HttpServletRequest request) throws JsonProcessingException, IOException, ParseException {
		
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode node =  mapper.readTree(beginDate);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		
		
		PsychoSocialReport exampleReport = new PsychoSocialReport();
		exampleReport.setBeginReportDate(sdf.parse(beginDate));
		exampleReport.setEndReportDate(sdf.parse(endDate));
		OrganizationNetwork org = new OrganizationNetwork();
		if(orgId != null) {
			org.setId(orgId);
		} else if(provinceId != null) {
			Province province = new Province();
			province.setId(provinceId);
			org.setProvince(province);
		} else if(zoneId != null) {
			HealthZone zone = new HealthZone();
			zone.setId(zoneId);
			org.setZone(zone);
		}
		 
		
		Iterable<PsychoSocialReport> reports = entityService.findPsychoSocialReportByExample(exampleReport);
		
		ModelAndView m = new ModelAndView(new M08ExcelReport());
		m.addObject("reports", reports);
		
		m.addObject("beginReportDate", beginDate );
		
		m.addObject("endReportDate", endDate );
		
		return m;
	}
	
}
