package in.somanath.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.somanath.binding.Loginform;
import in.somanath.binding.Signupform;
import in.somanath.binding.Unlockform;
import in.somanath.entity.UserDtlsEntity;
import in.somanath.repo.UserDtlsRepo;
import in.somanath.util.EmailUtils;
import in.somanath.util.PwdUtils;
import jakarta.servlet.http.HttpSession;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDtlsRepo userdtlsrepo;
	
	@Autowired
	private EmailUtils emailutils;
	
	@Autowired
	private HttpSession session;

	@Override
	public String login(Loginform form) {
		
		UserDtlsEntity entity = userdtlsrepo.findByEmailAndPwd(form.getEmail(), form.getPwd());
		
		if(entity==null) {
			return "Invalid Credential";
		}
		if(entity.getAccStatus().equals("Locked")) {
			
			return"Account is Locked";
		}
		
		session.setAttribute("userId", entity.getUserId());
		
		return "Success";
	}

	@Override
	public boolean signup(Signupform form) {
		
		UserDtlsEntity user = userdtlsrepo.findByEmail(form.getEmail());
		
		if(user!=null) {
			return false;
		}
		
		
		//TODO: copy the data from binding object to entity object
		
		UserDtlsEntity entity=new UserDtlsEntity();
		BeanUtils.copyProperties(form, entity);
		
		//TODO: generate random password and set to object
		
		String generateRendomPwd = PwdUtils.generateRendomPwd();
		entity.setPwd(generateRendomPwd);
		
		//TODO: set the account status as locked
	
		entity.setAccStatus("Locked");
		
		//TODO: insert the record
		
		userdtlsrepo.save(entity);
		
		//TODO: send email to unlock the account
		
		String to = entity.getEmail();
		String subject="Unlock Your Account | AshokIT";
		
		StringBuffer body= new StringBuffer("");
		body.append("<h1>use below temporary password to unlock your account</h1>");
		
		body.append("Temporary Password :"+generateRendomPwd);
		
		body.append("<br/>");
		
		body.append("<a href=\"http://localhost:8080/unlock?email=" +to+ "\">unlock your account</a>");
		
		emailutils.sendEmail(to,subject,body.toString());
		
		
		
		return true;
	}

	@Override
	public boolean unlockAccount(Unlockform form) {
		
		UserDtlsEntity entity = userdtlsrepo.findByEmail(form.getEmail());
		
		if(entity.getPwd().equals(form.getTempPwd())) {
			
			entity.setPwd(form.getConfirmPwd());
			entity.setAccStatus("Unlocked");
			userdtlsrepo.save(entity);
			
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean forgotPwd(String email) {
		
		//Check the EmailId is Present in DB Or Not
		UserDtlsEntity entity = userdtlsrepo.findByEmail(email);
		
		if(entity==null) {
			return false;
			
		}
		//TODO: if record is available then send the password to email
		String subject="Recover Password";
		String body="your pwd ::"+entity.getPwd();
		
		emailutils.sendEmail(email,subject,body);
		
		return true;
	}

}
