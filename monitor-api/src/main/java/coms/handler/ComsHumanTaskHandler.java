package coms.handler;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import coms.model.TaskInstance;
import coms.model.TaskVariable;
import coms.process.ComsAbstractEventHandler;
import coms.process.ComsEvent;
import coms.process.ComsHumanTaskDef;
import coms.process.ComsProcessContext;
import coms.process.ComsResult;
import coms.process.ComsVariable;
import coms.process.IComsEventHandler;
import coms.service.ComsProcessService;
import coms.service.TaskService;

@Component
public class ComsHumanTaskHandler extends ComsAbstractEventHandler implements IComsEventHandler{
	
	private TaskService taskService;
	
    public ComsHumanTaskHandler(ComsProcessService jobService, TaskService taskService) {
		super(jobService);
		this.taskService = taskService;
	}

    public ComsResult process(ComsEvent e) {
    	return new ComsResult(false, "**FATAL ERROR: This should have never been called");
    }
    
    public ComsResult process(ComsEvent e, Long processActivityId, ComsHumanTaskDef humanTaskDef) {
    	System.out.println("Human Task handler :: process-instance = "+e.getProcessId()+", event = "+e.getCode());
    	ComsResult result = null;
    	String message = null;
    	try{
    		
    		TaskInstance task = new TaskInstance(humanTaskDef.getName(), null,humanTaskDef.getAssignedToGroup(),humanTaskDef.getAssignedToUser(), e.getProcessId(), processActivityId);
    		
    		task.serializeSetNextEvents(humanTaskDef.getNextEvents());
    		
    		//Add process context variables as task variables
    		List<ComsVariable> vars = e.getContext().getVariables();
    		for (ComsVariable comsVar : vars) {
    			TaskVariable taskVar = new TaskVariable(comsVar.getName(), comsVar.getValue());
    			taskVar.setTaskInstance(task);
    			
    			task.addVariable(taskVar);
			}
    		
    		//Save them all
    		task = taskService.createTask(task);
    		
    		//Add task id to process context
    		e.getContext().addVariable(new ComsVariable("TASKID_"+ humanTaskDef.getName().toUpperCase(), task.getId().toString()));
    		
        	System.out.println("	Context: "+ e.getContext().serializeToString());
            
        	message = "Task created successfully: id = "+task.getId();
            
            result = new ComsResult(true, message);
            
    	}catch(Exception ex) {
    		message = "Error in event handler "+e.getClass()+" :: Error = "+ex.getMessage();
            result = new ComsResult(false, message);
    	}
        return result;
    }

}
