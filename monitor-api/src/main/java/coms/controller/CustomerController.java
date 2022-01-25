package coms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import coms.model.*;
import coms.service.*;


@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	public CustomerService customerService;
	
	@Autowired
	public ComsProcessService jobService;
	
	@GetMapping("/")
	public Iterable<Customer> findCustomers() {
		return customerService.findCustomers();
	}
	
	@GetMapping("/{id}")
	public Customer findCustomer(@PathVariable Long id) {
		return customerService.findCustomer(id);
	}
	
	@PostMapping("/")
	public Customer saveCustomer(@RequestBody Customer cust) {
		return customerService.save(cust);
	}
	
}
