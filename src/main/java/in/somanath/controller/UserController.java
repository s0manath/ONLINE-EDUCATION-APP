package in.somanath.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.somanath.binding.Loginform;
import in.somanath.binding.Signupform;
import in.somanath.binding.Unlockform;
import in.somanath.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	
	//SignUp Controller Method
	
	@GetMapping("/signup")
	public String signuppage(Model model) {
		model.addAttribute("user", new Signupform());
		return "signup";
	}
	@PostMapping("/signup")
	public String handelSignUp(@ModelAttribute("user") Signupform form,Model model) {
		
		boolean status = userservice.signup(form);
		
		if(status) {
			model.addAttribute("succmsg","Account Created,Check Your Email");
		}else {
			model.addAttribute("errmsg","Choose Unique Email");
		}
		
		return"signup";
	}
	
	//Unlock Controller Method
	
	@GetMapping("/unlock")
	public String unlockpage(@RequestParam String email,Model model) {
		
		Unlockform unlockform = new Unlockform();
		unlockform.setEmail(email);
		model.addAttribute("unlock", unlockform);
		return "unlock";
	}
	@PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute ("unlock")Unlockform unlock,Model model) {
		
		System.out.println(unlock);
		
		if(unlock.getNewPwd().equals(unlock.getConfirmPwd())) {
			boolean unlockAccount = userservice.unlockAccount(unlock);
			if(unlockAccount) {
				model.addAttribute("succmsg","Account Unlock Successfully");
			}
			else {
				model.addAttribute("errmsg","Check Your Temparary Password In Your Email,It Should Match With Your Password");
			}
		}
		else {
			model.addAttribute("errmsg","New Password And Confirm Password Should Be Same");
		}
		return "unlock";
	}
	
	//LogIn Controller Method
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		
		model.addAttribute("login",new Loginform());
		
		return "login";
	}
	@PostMapping("/login")
	public String validateLoginPage(@ModelAttribute("login") Loginform loginform,Model model) {
		
		System.out.println(loginform);
		String status = userservice.login(loginform);
		if(status.contains("Success")) {
			//redirect request to Dashboard method
			//return Dashboard
			return"redirect:/dashboard";
		}
		else {
			model.addAttribute("errMsg",status);
		}
		return "login";
	}

	@GetMapping("/forgotpwd")
	public String forgotPwdPage() {
		
		return "forgotPwd";
	}
	
	@PostMapping("/forgotpwd")
	public String forgotPwdPage(@RequestParam ("email") String email,Model model ) {
		
		System.out.println(email);
		boolean status = userservice.forgotPwd(email);
		
		if(status) {
			model.addAttribute("succmsg","Password Sent To Your Email");
		}else {
			model.addAttribute("errmsg","Invalid Email");
		}
		
		return"forgotPwd";
		
	}
}
