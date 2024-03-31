package in.somanath.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.somanath.binding.DashbordResponse;
import in.somanath.binding.EnqSearchCriteria;
import in.somanath.binding.EnquiryForm;
import in.somanath.entity.StudentEnqEntity;
import in.somanath.service.EnquireService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {
	
	@Autowired
	private EnquireService enqservice;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/logout")
	public String logoutPage() {
		session.invalidate();
		return"index";
	}

	@GetMapping("/dashboard")
	public String dashbordpage( Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");
		
		DashbordResponse dashbordData = enqservice.getDashbordData(userId);
		
		model.addAttribute("dashboardData", dashbordData);
		return "dashboard";
	}
	
	@GetMapping("/enquiry")
	public String enquiryPage(Model model) {
		formInit(model);
		
		return "add-enquiry";
	}

	private void formInit(Model model) {
		//get course for dropdown
		List<String> courseName = enqservice.getCourseName();
		//get status for dropdown
		List<String> enqStatus = enqservice.getEnqStatus();
		
		EnquiryForm formObj = new EnquiryForm();
		
		model.addAttribute("addEnqPage", formObj);
		
		model.addAttribute("coursename", courseName);
		
		model.addAttribute("enqstatus", enqStatus);
	}
	
	@PostMapping("/addenq")
	public String addEnqPage(@ModelAttribute("addEnqPage") EnquiryForm addEnqPage,Model model) {
		
		System.out.println(addEnqPage);
		
		boolean upsertEnquiry = enqservice.upsertEnquiry(addEnqPage);
		
		if(upsertEnquiry) {
			model.addAttribute("succMsg", "Enquiry Added");
		}else {
			model.addAttribute("errMsg", "Problem Occured");
		}
		return "add-enquiry";
		
	}
	
	@GetMapping("/enquiries")
	public String viewEnquiryPage( Model model) {
		formInit(model);
		List<StudentEnqEntity> enquiries = enqservice.getEnquiries();
		model.addAttribute("enquiries", enquiries);
		return "view-enquiries";
	}
	@GetMapping("/filter-enquiries")
	public String getFilteredEnqs(@RequestParam String cname,@RequestParam String mode,
			                      @RequestParam String status,Model model ) {
		
		EnqSearchCriteria searchCriteria = new EnqSearchCriteria();
		searchCriteria.setClassMode(mode);
		searchCriteria.setCourseName(cname);
		searchCriteria.setEnqStatus(status);
		
		Integer userId = (Integer) session.getAttribute("userId");
		List<StudentEnqEntity> filteredEnqs = enqservice.getFilteredEnqs(searchCriteria,userId);
		model.addAttribute("enquiries", filteredEnqs);
		
		return "filter-enquiries-page";
		
		 
	}
	
	
}
