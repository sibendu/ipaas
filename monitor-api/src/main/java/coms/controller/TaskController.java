package coms.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coms.model.ProcessInstance;
import coms.process.ComsEvent;
import coms.process.ComsProcess;
import coms.process.ComsProcessContext;
import coms.process.ComsVariable;
import coms.process.ProcessSearchRequest;
import coms.model.ProcessActivity;
import coms.ComsProcessRepository;
import coms.model.ProcessInstanceRepository;
import coms.model.TaskInstance;
import coms.model.TaskVariable;
import coms.service.ComsProcessService;
import coms.service.TaskService;
import coms.task.TaskAction;
import coms.util.ComsApiUtil;
import coms.service.ComsMessageService;


@RestController
@RequestMapping("/task")
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@GetMapping("/")
	public Iterable<TaskInstance> findJobs() {
		return taskService.getTasks();
	}
	
	@GetMapping("/{id}")
	public TaskInstance findJob(@PathVariable long id) {
		return taskService.getTask(id);
	}
	
//	@PostMapping("/")
//	public ProcessInstance save(@RequestBody ProcessInstance job) {
//		return jobService.save(job);
//	}
	
	@PostMapping("/search")
	public List<TaskInstance> search(@RequestBody String user) {
		System.out.println("TaskController.findByCodeAndStatus()");
		return taskService.findByAssignedUser(user);
	}	
	
	@GetMapping("/cleanall")
	public String clean() {
		taskService.cleanAll();
		return "All records removed successfully";
	}
	
	@PostMapping("/start")
	public TaskInstance createTask(@RequestBody TaskInstance task) {
		System.out.println("TaskController.createTask()");
		String message = null;
		TaskInstance instance = null;
		try {
			instance = taskService.createTask(task);
			message = "Task created: id = "+instance.getId();
		}catch(Exception e) {
			message = "Error: "+e.getMessage();
			e.printStackTrace();
		}
		return instance;
	}
	
	@PutMapping("/{id}/{action}")
	public String action(@PathVariable long id, @PathVariable String action, @RequestBody TaskAction taskAction) {
		System.out.println("TaskController.action(): action="+action);
		String message = "";
		if(action.equalsIgnoreCase(ComsApiUtil.TASK_ACTION_CLAIM)) {
			System.out.println("Assigning task "+ id + " to user "+taskAction.getUser());
			TaskInstance inst = taskService.claimTask(id, taskAction.getUser());
			message = "Task id "+id+" claimed by user "+taskAction.getUser();
		}else if(action.equalsIgnoreCase(ComsApiUtil.TASK_ACTION_COMPLETE)) {
			try {
				System.out.println("Completing task: "+id);
				TaskInstance inst = taskService.completeTask(id, taskAction);
				message = "Task id "+id+" completed by user "+taskAction.getUser();
			} catch (Exception e) {
				message = "Error in task completion: "+ e.getMessage();
			}
		}else {
			message = action + "is not a valid action and cannot be performd on task id = "+id;
		}
						
		return message;
	}

}
