package in.somanath.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.somanath.entity.CourseEntity;

public interface CourseRepo extends JpaRepository<CourseEntity, Integer> {
	
	

}
