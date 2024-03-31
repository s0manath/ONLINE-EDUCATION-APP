package in.somanath.runner;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.somanath.entity.CourseEntity;
import in.somanath.entity.EnqStatusEntity;
import in.somanath.repo.CourseRepo;
import in.somanath.repo.EnqStatusRepo;

@Component
public class DataLoader  implements ApplicationRunner{
	
	@Autowired
	private CourseRepo courserepo;
	
	@Autowired
	private EnqStatusRepo enqStatusrepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		courserepo.deleteAll();
		enqStatusrepo.deleteAll();
		
		CourseEntity c1 = new CourseEntity();
		c1.setCourseName("Java");
		
		CourseEntity c2 = new CourseEntity();
		c2.setCourseName("Python");
		
		CourseEntity c3 = new CourseEntity();
		c3.setCourseName("Aws");
		
		courserepo.saveAll(Arrays.asList(c1,c2,c3));
		
		EnqStatusEntity e1=new EnqStatusEntity();
		e1.setStatusName("New");
		
		EnqStatusEntity e2=new EnqStatusEntity();
		e2.setStatusName("Enroled");
		
		EnqStatusEntity e3=new EnqStatusEntity();
		e3.setStatusName("Lost");
		
		enqStatusrepo.saveAll(Arrays.asList(e1,e2,e3));
		
	}

}
