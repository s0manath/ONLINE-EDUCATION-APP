package in.somanath.service;

import in.somanath.binding.Loginform;
import in.somanath.binding.Signupform;
import in.somanath.binding.Unlockform;

public interface UserService {
	
	public String login(Loginform form);
	public boolean signup(Signupform form);
	public boolean unlockAccount(Unlockform form);
	public boolean forgotPwd(String email);

}
