package in.somanath.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.somanath.binding.DashbordResponse;
import in.somanath.binding.EnqSearchCriteria;
import in.somanath.binding.EnquiryForm;
import in.somanath.entity.CourseEntity;
import in.somanath.entity.EnqStatusEntity;
import in.somanath.entity.StudentEnqEntity;
import in.somanath.entity.UserDtlsEntity;
import in.somanath.repo.CourseRepo;
import in.somanath.repo.EnqStatusRepo;
import in.somanath.repo.StudentEnqRepo;
import in.somanath.repo.UserDtlsRepo;
import jakarta.servlet.http.HttpSession;

@Service
public class EnquireServiceImpl implements EnquireService {
	
	@Autowired
	private UserDtlsRepo userDtlsRepo;
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;
	
	@Autowired
	private StudentEnqRepo studentEnqRepo;
	
	@Autowired
	private HttpSession session;

	@Override
	public List<String> getCourseName() {
		
		List<CourseEntity> findAll = courseRepo.findAll();
		
		List<String> names= new ArrayList<>();
		
		for(CourseEntity entity:findAll) {
			names.add(entity.getCourseName());
		}
		
		return names;
	}

	@Override
	public List<String> getEnqStatus() {
		
		List<EnqStatusEntity> findAll = enqStatusRepo.findAll();
		
		List<String> status= new ArrayList<>();
		
		for(EnqStatusEntity entity:findAll) {
			status.add(entity.getStatusName());
		}
		
		return status;
	}

	@Override
	public DashbordResponse getDashbordData(Integer userId) {
		
		DashbordResponse response = new DashbordResponse();
		
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		
		if(findById.isPresent()) {
			
			UserDtlsEntity userentity = findById.get();
			
			List<StudentEnqEntity> enquiries = userentity.getEnquiries();
			
			Integer totalcount = enquiries.size();
			
			Integer EnroledCount = enquiries.stream()
			.filter(e -> e.getEnqStatus().equals("Enroled"))
			.collect(Collectors.toList()).size();
			
			Integer LostCount = enquiries.stream()
			.filter(e-> e.getEnqStatus().equals("Lost"))
			.collect(Collectors.toList()).size();
			
			response.setEnrolledCnt(EnroledCount);
			response.setLostCnt(LostCount);
			response.setTotalEnquriesCnt(totalcount);
			
			
		}
		
		return response;
	}

	@Override
	public boolean upsertEnquiry(EnquiryForm form) {
		
		StudentEnqEntity studentEnqEntity = new StudentEnqEntity();
		BeanUtils.copyProperties(form, studentEnqEntity);
		
		Integer id = (Integer)session.getAttribute("userId");
		
		
		
		UserDtlsEntity userEntity = userDtlsRepo.findById(id).get();
		studentEnqEntity.setUser(userEntity);
		
		studentEnqRepo.save(studentEnqEntity);
		
		return true;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		if(findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			return enquiries;
		}
		
		return null;
	}
	@Override
	public List<StudentEnqEntity> getFilteredEnqs(EnqSearchCriteria criteria, Integer userId) {
		Optional<UserDtlsEntity> findById = userDtlsRepo.findById(userId);
		if(findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			
			//filter logic
			
			if(null!=criteria.getCourseName() & !"".equals(criteria.getCourseName())) {
				enquiries = enquiries.stream()
				.filter(e->e.getCourseName().equals(criteria.getCourseName()))
				.collect(Collectors.toList());
			}
			if(null!=criteria.getClassMode() & !"".equals(criteria.getClassMode())){
				enquiries=enquiries.stream()
				.filter(e->e.getClassMode().equals(criteria.getClassMode()))
				.collect(Collectors.toList());
			}
            if(null!=criteria.getEnqStatus() & !"".equals(criteria.getEnqStatus())){
				enquiries=enquiries.stream()
				.filter(e->e.getEnqStatus().equals(criteria.getEnqStatus()))
				.collect(Collectors.toList());
			}
			
			return enquiries;
		}
		
		return null;
	}


}
