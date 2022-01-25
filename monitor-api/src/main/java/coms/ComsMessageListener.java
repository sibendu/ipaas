package coms;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import coms.handler.NewEnvHandler;
import coms.model.ProcessActivity;
import coms.model.ProcessInstance;
import coms.process.ComsEvent;
import coms.process.ComsEventHandlerDef;
import coms.process.ComsProcess;
import coms.process.ComsResult;
import coms.process.IComsEventHandler;
import coms.service.ComsMessageService;
import coms.service.ComsProcessService;
import coms.util.ComsUtil;
import io.kubemq.sdk.event.Channel;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.Transaction;
import io.kubemq.sdk.queue.TransactionMessagesResponse;
import io.kubemq.sdk.tools.Converter;

@Component
public class ComsMessageListener {

	@Autowired
	public ComsProcessService jobService;
	
	@Autowired
	public ComsMessageService messageService;
	
	private Queue queue;
	private Channel channel;
	private NewEnvHandler orderProcessor;
	private TaskExecutor taskExecutor;
	
	
	public ComsMessageListener(Queue queue, Channel channel, NewEnvHandler orderProcessor, TaskExecutor taskExecutor) {
		this.queue = queue;
		this.channel = channel;
		this.orderProcessor = orderProcessor;
		this.taskExecutor = taskExecutor;		
	}

	@PostConstruct
	public void listen() {
		taskExecutor.execute(() -> {
			while (true) {
			    try {
                    Transaction transaction = queue.CreateTransaction();
                    TransactionMessagesResponse response = transaction.Receive(10, 10);
                    if (response.getMessage().getBody().length > 0) {
                    	
                    	transaction.AckMessage();  
                    	
                    	ComsEvent event = (ComsEvent)Converter.FromByteArray(response.getMessage().getBody());
                    	
                    	String eventCode = event.getCode();
                    	
                    	ProcessInstance currentProcessInstance = jobService.getJob(event.getProcessId());
                    	String processCode = currentProcessInstance.getCode();
                    	
                    	System.out.println("Event "+ eventCode+" received for Process "+processCode+ " :: process-instance "+currentProcessInstance.getId());
                    	
                    	ComsProcess processDef = ComsProcessRepository.getProcessDefinition(processCode);
                    	                    
                    	//Fire handlers to process this event
                    	ComsEventHandlerDef[] handlers = processDef.getEventHandlers(event.getCode());
                    	
                    	boolean handlersSuccessful = true;
                    	String success = "N";
                    	ComsResult thisHandlerResult = null;
                    	String processVars =  null;
                    	
                    	if(handlers != null && handlers.length > 0) {    
                    		
                    		//Let all handlers do their job 
	                    	for (int i = 0; i < handlers.length; i++) {
	                    		ComsEventHandlerDef thisHandler = handlers[i];
								IComsEventHandler handler = (IComsEventHandler)Class.forName(thisHandler.getHandlerClass()).getConstructor(ComsProcessService.class).newInstance(jobService);
	
								if(event.getContext() != null) {
									processVars =  event.getContext().serializeToString();
								}
								ProcessActivity activityRecord = jobService.markActivityStart(currentProcessInstance, eventCode, thisHandler.getHandlerClass(), thisHandler.getDescription(), processVars);
								
								thisHandlerResult = handler.process(event);
								
								success = "N";
								if(thisHandlerResult.isSuccess()) {
									success = "Y";
								}else {
									handlersSuccessful = false; // At least one handler for current event did not finish successfully
								}
								
								//Update process activity with the outcome of processing for this handler
								activityRecord.setSuccess(success);
								activityRecord.setMessage(thisHandlerResult.getResult());
								
								if(event.getContext() != null) {
									activityRecord.setVariables( event.getContext().serializeToString());
								}
								
								activityRecord = jobService.markActivityEnd(activityRecord);
								
								//Fire next set of events for this handler, if defined
	                    		String[] nextEventsFromHandler = thisHandler.getNextEvents();
	                    		if(nextEventsFromHandler != null) {                    			                    	
		                        	for (int k = 0; k < nextEventsFromHandler.length; k++) {
		    							String nextHandlerEvent = nextEventsFromHandler[k];    							
		    							ComsEvent eve = new ComsEvent(nextHandlerEvent, new Date(), event.getProcessId(), event.getContext());
		    							messageService.sendMessage(eve);
		    						}
	    				    		//System.out.println("Next events triggered : "+ nextEvents.toString());
	                    		}
							}
	                    	
	                    	String[] nextEvents = processDef.getNextEvents(event.getCode());
	                    	
	                    	// All handlers have successfully processed without error, decide what to do next 
	                    	if(handlersSuccessful) {
	                    		
	                    		if(nextEvents != null && nextEvents.length > 0) {
		                    		// next events are defined. Trigger them all
		                        	for (int i = 0; i < nextEvents.length; i++) {
		    							String nextEvent = nextEvents[i];    							
		    							ComsEvent ev = new ComsEvent(nextEvent, new Date(), event.getProcessId(), event.getContext());
		    							messageService.sendMessage(ev);
		    						}
	                    		}else {
	                    			//current event does not have any next event defined
	                    			// check if it is one of the end event, and if yes, if process instance has reached completion 
	                    			
	                    			if(processDef.isEndEvent(eventCode)) {
										//It is (one of the) end events, update count of end Events completed for this process instance 
	                    				currentProcessInstance = jobService.updateEndEventCompletedCount(currentProcessInstance, processDef);	                    					                    				                    			
	                    			}
	                    		}	               	
	                    	}else {
	                    		//This case needs more thought. What to do in case one (or more) handler resulted in erroneous result 
	                    		
	                    		System.out.println("Error in processing event: "+event.getCode()+" , Process-Instance = "+event.getProcessId());                   		
	                    	}     
                    	
                    	}else { // No handler defined, this denotes end of process
                    		throw new Exception("***FATAL Error *** No handler defined for event "+eventCode);
                    	}                    	                  	                                               
                    	
                    } else {
                        System.out.println("No messages");
                    }
                    
                    Thread.sleep(10000);
                    
                } catch (Exception e) {         
                	System.out.println("Error: "+e.getMessage());
					e.printStackTrace();
                }
			}
		});

	}

}
