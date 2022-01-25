package coms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coms.model.ProcessInstance;
import coms.model.TaskInstance;
import coms.process.ComsEvent;
import coms.process.ComsEventHandlerDef;
import coms.process.ComsProcess;
import coms.process.ComsProcessContext;
import coms.process.ComsVariable;
import coms.process.ProcessSearchRequest;
import coms.util.ComsUtil;

import coms.model.TaskInstanceRepository;
import coms.model.TaskVariable;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;

@Component
public class TaskService {
	
//	@Autowired
//	ComsMessageService messageService;
	
	@Autowired
	public TaskInstanceRepository taskRepository;
	
//	@Autowired
//	private Queue queue;
	
//	public JobService(Queue queue) {
//        this.queue = queue;
//    }
	
	public Iterable<TaskInstance> getJobs() {
		//System.out.println("JobService.getJob(id)");
		return taskRepository.findAll();
	}
	
	public TaskInstance getJob(Long id) {
		//System.out.println("JobService.getJob(id)");
		return taskRepository.findById(id).get();
	}
	
	public TaskInstance save(TaskInstance t) {
		//System.out.println("JobService.save(job)");
		return taskRepository.save(t);
	}
	
	public TaskInstance createTask(Long processId, String name, String description, String assignedUser, String assignedGroup, String remark, List<ComsVariable> vars) {
		System.out.println("TaskService.createTask()");

		//Create a new process instance record
		Date dt = new Date();
		TaskInstance instance = new TaskInstance(processId, name, description , dt, null, null, 
				assignedUser, assignedGroup, remark, ComsUtil.TASK_STATE_NEW);
		
		if(vars != null && vars.size() > 0) {
			for (ComsVariable v : vars) {
				TaskVariable tv = new TaskVariable(v.getName(), v.getValue());
				tv.setTaskInstance(instance);
				
				instance.getVariables().add(tv);
			}
		}
		
		instance = taskRepository.save(instance);
				
		return instance;
	}
	
	public TaskInstance claimTask(long taskId, String user) {
		System.out.println("TaskService.claimTask()");

		TaskInstance instance = taskRepository.findById(taskId);
		
		//To do: need to check if user belongs to right group
		instance.setAssignedUser(user);
		instance.setUpdated(new Date());
		
		TaskInstance inst = taskRepository.save(instance);
				
		return inst;
	}

	public TaskInstance completeTask(long taskId, String user, String remarks) throws Exception{
		System.out.println("TaskService.completeTask()");

		TaskInstance instance = taskRepository.findById(taskId);
		
		if(instance.getAssignedUser() == null) {
			throw new Exception("Task not yet assigned to a user; first claim the task.");
		}
		
		//To do: need to check if user belongs to right group
		if(user != null && !user.equalsIgnoreCase(instance.getAssignedUser())) {
			throw new Exception("Task not assigned to user ("+user+"); first reassign the task.");
		}
		
		instance.setStatus(ComsUtil.TASK_STATE_CLOSED);
		instance.setRemark(remarks);
		instance.setUpdated(new Date());
		
		TaskInstance inst = taskRepository.save(instance);
				
		return inst;
	}
}
