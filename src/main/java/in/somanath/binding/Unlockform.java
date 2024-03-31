package in.somanath.binding;

import lombok.Data;

@Data
public class Unlockform {
	
	private String email;
	private String tempPwd;
	private String newPwd;
	private String confirmPwd;

}
