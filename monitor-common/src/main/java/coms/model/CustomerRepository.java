package coms.model;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	//List<Customer> findByLast_Name(String lastName);

	Customer findById(long id);
}
