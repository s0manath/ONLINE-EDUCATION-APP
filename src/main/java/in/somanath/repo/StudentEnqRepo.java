package in.somanath.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.somanath.entity.StudentEnqEntity;

public interface StudentEnqRepo extends JpaRepository<StudentEnqEntity, Integer> {

}
