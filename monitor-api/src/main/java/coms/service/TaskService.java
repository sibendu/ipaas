package coms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coms.model.ProcessActivity;
import coms.model.ProcessActivityRepository;
import coms.model.ProcessInstance;
import coms.model.TaskActivity;
import coms.model.TaskInstance;
import coms.process.ComsEvent;
import coms.process.ComsEventHandlerDef;
import coms.process.ComsProcess;
import coms.process.ComsProcessContext;
import coms.process.ComsVariable;
import coms.process.ProcessSearchRequest;
import coms.task.TaskAction;
import coms.util.ComsApiUtil;

import coms.model.TaskInstanceRepository;
import coms.model.TaskVariable;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;

@Component
public class TaskService {
	
	@Autowired
	ComsMessageService messageService;
	
	@Autowired
	ProcessActivityRepository processActivityRepo;
	
	@Autowired
	public TaskInstanceRepository taskRepository;
	
//	@Autowired
//	private Queue queue;
	
//	public JobService(Queue queue) {
//        this.queue = queue;
//    }
	
	public Iterable<TaskInstance> getTasks() {
		//System.out.println("JobService.getJob(id)");
		return taskRepository.findAll();
	}
	
	public TaskInstance getTask(long id) {
		//System.out.println("JobService.getJob(id)");
		return taskRepository.findById(id);
	}
	
	public TaskInstance save(TaskInstance t) {
		//System.out.println("JobService.save(job)");
		return taskRepository.save(t);
	}
	
	public TaskInstance createTask(TaskInstance task) {
		System.out.println("TaskService.createTask()");

		//Create a new process instance record
		Date dt = new Date();
		task.setCreated(dt);
		
		if(task.getVariables() != null) {
			for (TaskVariable var : task.getVariables()) {
				var.setTaskInstance(task);
			}
		}
		
		task = taskRepository.save(task);
				
		return task;
	}
	
	public TaskInstance claimTask(long taskId, String user) {
		System.out.println("TaskService.claimTask()");

		TaskInstance instance = taskRepository.findById(taskId);
		
		//To do: need to check if user belongs to right group
		instance.setAssignedUser(user);
		instance.setStatus(ComsApiUtil.TASK_ACTION_CLAIM);
		instance.setUpdated(new Date());
		
		TaskInstance inst = taskRepository.save(instance);
				
		return inst;
	}

	public TaskInstance completeTask(long taskId, TaskAction action) throws Exception{
		System.out.println("TaskService.completeTask(): id="+taskId+", user="+action.getUser());

		TaskInstance instance = taskRepository.findById(taskId);
		System.out.println("Found task id "+instance.getId()+", assigned to user("+instance.getAssignedUser()+") and group("+instance.getAssignedGroup()+")");
		
		if(instance.getAssignedUser() == null || instance.getAssignedUser().equals("")) {
			throw new Exception("Task not yet assigned to a user; first claim the task.");
		}else if(!instance.getAssignedUser().equalsIgnoreCase(action.getUser())) {
			throw new Exception("Task id "+taskId+" assigned to user "+instance.getAssignedUser()+"; "+action.getUser()+" cannot change it. First reassign the task.");
		}else {
			System.out.println("Updating task id "+instance.getId());
			
			instance.setStatus(ComsApiUtil.TASK_ACTION_COMPLETE);
			instance.setUpdated(new Date());
			
			//Record an activity on the task by current user
			TaskActivity actitivity = new TaskActivity(null, action.getUser(), new Date(), action.getRemarks(), instance);
			instance.addActivity(actitivity);
			
			//Add more variables for context of the task if passed in payload
			if(action.getVariables() != null && action.getVariables().size() > 0) {
				for (ComsVariable thisVar : action.getVariables()) {
					TaskVariable var = new TaskVariable(thisVar.getName(), thisVar.getValue());
					var.setTaskInstance(instance);
					instance.addVariable(var);
				}
			}
			
			//And finally, save it all
			instance = taskRepository.save(instance);
			
			if(instance.getProcessId() != null && instance.getActivityId() != null) {
				
				ProcessActivity act = processActivityRepo.findById(instance.getActivityId().longValue());
				act.setFinish(new Date());
				
				ProcessActivity updatedAct = processActivityRepo.save(act);						
			}
			
			
			//Task completed, check if there are any events to trigger
			String[] nextEvents = instance.getDeserializeNextEvents();
    		if(instance.getProcessId() != null && nextEvents != null && nextEvents.length > 0) {
    			
    			System.out.println("Task "+instance.getName()+" complete; resuming process-id "+instance.getProcessId());
    			
    			Set<TaskVariable> vars = instance.getVariables();
    			List<ComsVariable> comVars = new ArrayList<>();
    			for (TaskVariable tv : vars) {
					comVars.add(new ComsVariable(tv.getName(), tv.getValue()));
				}
    			
        		// The process definition has next events are defined for this task. Trigger them all
            	for (int i = 0; i < nextEvents.length; i++) {
					String nextEvent = nextEvents[i];    							
					ComsProcessContext processCtx = new ComsProcessContext();
					processCtx.setVariables(comVars);
					ComsEvent ev = new ComsEvent(nextEvent, new Date(), instance.getProcessId(), processCtx);
					
					messageService.sendMessage(ev);
				}
    		}
    		
		}		
		return instance;
	}
		
	public List<TaskInstance> findByAssignedUser(String user) {
		System.out.println("JobService.findByAssignedUser()");
		List<TaskInstance> instances = taskRepository.findByAssignedUser(user);
		System.out.println("Result: "+instances.size());
		return instances;
	}
	
	public void cleanAll() {
		taskRepository.deleteAll();
	}
}
