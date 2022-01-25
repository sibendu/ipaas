package coms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import coms.model.Customer;
import coms.model.CustomerRepository;

@Component
public class CustomerService {
	
	@Autowired
	public CustomerRepository repository;
	
	public Iterable<Customer> findCustomers() {
		System.out.println("CustomerService.findCustomers()");
		return repository.findAll();
	}
	
	public Customer findCustomer(long id) {
		System.out.println("CustomerService.findCustomer(id)");
		return repository.findById(id);
	}
	
	public Customer save(Customer cust) {
		System.out.println("CustomerService.save(cust)");
		System.out.println("Savings: "+cust.getId()+" - "+cust.getFirstName()+" - "+cust.getLastName());
		cust = repository.save(cust);
		System.out.println("Saved: "+cust.getId()+" - "+cust.getFirstName()+" - "+cust.getLastName());
		return cust;
	}
}
