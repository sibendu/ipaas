package coms.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coms.model.ProcessInstance;
import coms.process.ComsEvent;
import coms.process.ComsProcess;
import coms.process.ComsProcessContext;
import coms.process.ProcessSearchRequest;
import coms.model.ProcessActivity;
import coms.ComsProcessRepository;
import coms.model.ProcessInstanceRepository;
import coms.service.ComsProcessService;
import coms.service.ComsMessageService;


@RestController
@RequestMapping("/process")
public class ProcessController {
	
	@Autowired
	ComsProcessService jobService;
	
	@GetMapping("/")
	public Iterable<ProcessInstance> findJobs() {
		return jobService.getJobs();
	}
	
	@GetMapping("/{id}")
	public ProcessInstance findJob(@PathVariable Long id) {
		return jobService.getJob(id);
	}
	
//	@PostMapping("/")
//	public ProcessInstance save(@RequestBody ProcessInstance job) {
//		return jobService.save(job);
//	}
	
	@PostMapping("/search")
	public List<ProcessInstance> findByCodeAndStatus(@RequestBody ProcessSearchRequest request) {
		System.out.println("ProcessController.findByCodeAndStatus()");
		return jobService.findByCodeAndStatus(request);
	}
	
	@PostMapping("/start/{processCode}")
	public String startEnv(@PathVariable String processCode, @RequestBody ComsProcessContext context) {
		
		//System.out.println("JobController.createNewEnv()");
				
		ProcessInstance processInStance = jobService.startProcess( processCode, context);		
		System.out.println("Process instantiated: "+processCode+", Instance-Id = "+processInStance.getId());
				
		return "Request accepted: Job Id = "+processInStance.getId();
		
	}
	
	@GetMapping("/cleanall")
	public String clean() {
		jobService.cleanAll();
		return "All records removed successfully";
	}
	
}
