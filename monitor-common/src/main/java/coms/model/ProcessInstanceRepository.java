package coms.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProcessInstanceRepository extends CrudRepository<ProcessInstance, Long> {

	ProcessInstance findById(long id);
	
	List<ProcessInstance> findByCodeAndStatus(String code, String status);
	
	List<ProcessInstance> findByCreatedAfter(Date birthDate);
}
