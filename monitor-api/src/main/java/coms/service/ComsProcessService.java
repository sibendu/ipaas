package coms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coms.model.ProcessInstance;
import coms.process.ComsAbstractEventHandler;
import coms.process.ComsEvent;
import coms.process.ComsEventHandlerDef;
import coms.process.ComsProcess;
import coms.process.ComsProcessContext;
import coms.process.IComsEventHandler;
import coms.process.ProcessSearchRequest;
import coms.util.ComsApiUtil;
import coms.model.ProcessActivity;
import coms.ComsProcessRepository;
import coms.model.ProcessActivityRepository;
import coms.model.ProcessInstanceRepository;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;

@Component
public class ComsProcessService {
	
	@Autowired
	ComsMessageService messageService;
	
	@Autowired
	public ProcessInstanceRepository repository;
	
	@Autowired
	public ProcessActivityRepository recordRepository;
	
	@Autowired
	private Queue queue;
	
//	public JobService(Queue queue) {
//        this.queue = queue;
//    }
	
	public Iterable<ProcessInstance> getJobs() {
		//System.out.println("JobService.getJobs()");
		return repository.findAll();
	}
	
	public ProcessInstance getJob(Long id) {
		//System.out.println("JobService.getJob(id)");
		return repository.findById(id).get();
	}
	
	public ProcessInstance save(ProcessInstance job) {
		//System.out.println("JobService.save(job)");
		return repository.save(job);
	}
	
	public ProcessActivity getJobRecord(Long id) {
		//System.out.println("JobService.getJobRecord(id)");
		return recordRepository.findById(id).get();
	}
	
	public ProcessActivity save(ProcessActivity jobRecord) {
		//System.out.println("JobService.save(jobRecord)");
		return recordRepository.save(jobRecord);
	}
	
	public ProcessInstance startProcess(String processCode, ComsProcessContext context) {
		//System.out.println("JobService.startProcess(event)");

		//Create a new process instance record
		Date dt = new Date();
		ProcessInstance processInStance = new ProcessInstance(null, processCode, ComsApiUtil.PROCESS_STATUS_NEW, dt, dt);
		processInStance = repository.save(processInStance);
		
		// Trigger first event to start processing
		ComsProcess process = ComsProcessRepository.getProcessDefinition(processCode);
		String startEvent = process.getStartEvent();
		ComsEvent event = new ComsEvent(startEvent, new Date(), processInStance.getId(), context);
        
		messageService.sendMessage(event);
				
		return processInStance;
	}
	
	public ProcessActivity markActivityStart(ProcessInstance pi, ComsEvent event,ComsEventHandlerDef handler, ComsProcess processDef) {// String eventCode, String handler, String description, String variables) {

		//Create a new activity record for current process instance
		
		String variables = event.getContext() != null ? event.getContext().serializeToString(): null;		
		ProcessActivity act = new ProcessActivity(null,event.getCode(), handler.getHandlerClass(), handler.getName(), new Date(), null, variables, pi);		
		
		ProcessActivity activity = recordRepository.save(act);
		
		if(processDef.getStartEvent().equalsIgnoreCase(event.getCode())) {
			//This is first event. Mark instance status as WIP
			pi.setStatus(ComsApiUtil.PROCESS_STATUS_WIP);
			pi = repository.save(pi);
			System.out.println("PI updated");
		}
		
		
		return activity;
	}
	
	public ProcessActivity markActivityEnd(ProcessActivity rec) {
		rec.setFinish(new Date());
		ProcessActivity rec1 = recordRepository.save(rec);
		return rec1;
	}
	
	public List<ProcessInstance> findByCodeAndStatus(ProcessSearchRequest request) {
		System.out.println("JobService.findByCodeAndStatus()");
		List<ProcessInstance> instances = repository.findByCodeAndStatus(request.getCode(), request.getStatus());
		System.out.println("Result: "+instances.size());
		return instances;
	}
	
	public List<ProcessInstance> findByCreatedAfter(Date birthDate) {
		return repository.findByCreatedAfter(birthDate);
	}
	
	public void cleanAll() {
		repository.deleteAll();
	}
	
	public ProcessInstance updateEndEventCompletedCount(ProcessInstance in,ComsProcess processDef) {
		
		ProcessInstance inst = repository.findById(in.getId()).get();//Latest record fetced
		
		inst.setNoEndEvents(inst.getNoEndEvents()+1);
		
		if(inst.getNoEndEvents() == processDef.getEndEvents().length) {
			//This was the last end event; process instance can be marked completed
			System.out.println("Marking process completed: process-id "+ inst.getId());
			inst.setStatus(ComsApiUtil.PROCESS_STATUS_COMPLETE);
		}
		inst.setUpdated(new Date());
		
		ProcessInstance newInst = repository.save(inst);
		return newInst;
	}
	
}
