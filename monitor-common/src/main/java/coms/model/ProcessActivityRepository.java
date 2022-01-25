package coms.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProcessActivityRepository extends CrudRepository<ProcessActivity, Long> {

	ProcessActivity findById(long id);
	
	@Query(value = "select * from process_actvity where EMAIL_ADDRESS = ?1", nativeQuery = true)
	List<ProcessActivity> findByEvents(String events);
}
