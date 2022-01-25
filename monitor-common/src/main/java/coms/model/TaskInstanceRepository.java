package coms.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TaskInstanceRepository extends CrudRepository<TaskInstance, Long> {

	TaskInstance findById(long id);
	
	TaskInstance findByAssignedUser(String user);

	TaskInstance findByAssignedGroup(String group);

}
