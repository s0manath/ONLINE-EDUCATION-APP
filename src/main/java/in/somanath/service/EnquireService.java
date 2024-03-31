package in.somanath.service;

import java.util.List;

import in.somanath.binding.DashbordResponse;
import in.somanath.binding.EnqSearchCriteria;
import in.somanath.binding.EnquiryForm;
import in.somanath.entity.StudentEnqEntity;

public interface EnquireService {
	
	public List<String> getCourseName();
	
	public List<String> getEnqStatus();
	
	public DashbordResponse getDashbordData(Integer userId);
	
	public boolean upsertEnquiry(EnquiryForm form);
	
	public List<StudentEnqEntity> getEnquiries();
	
	public List<StudentEnqEntity> getFilteredEnqs(EnqSearchCriteria criteria,Integer userId);


}
