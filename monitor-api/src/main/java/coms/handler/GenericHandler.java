package coms.handler;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import coms.process.ComsAbstractEventHandler;
import coms.process.ComsEvent;
import coms.process.ComsProcessContext;
import coms.process.ComsResult;
import coms.process.ComsVariable;
import coms.process.IComsEventHandler;
import coms.service.ComsProcessService;

@Component
public class GenericHandler extends ComsAbstractEventHandler implements IComsEventHandler{
	
    public GenericHandler(ComsProcessService jobService) {
		super(jobService);
	}

    public ComsResult process(ComsEvent e) {
    	//System.out.println("Generic event handler :: process-instance = "+e.getProcessId()+", event = "+e.getCode());
    	ComsResult result = null;
    	String message = null;
    	try{
    		
    		System.out.println("Generic Handler processing ....");
        	System.out.println("	Context: "+ e.getContext().serializeToString());
            
        	//e.getContext().addVariable(new ComsVariable("VAR-Generic","Val-GenericHandler"));
        	
    		Thread.sleep(2000);
    		
    		message = "GenericHandler completed .. ";
            
            result = new ComsResult(true, message);
            
    	}catch(Exception ex) {
    		message = "Error in event handler "+e.getClass()+" :: Error = "+ex.getMessage();
            result = new ComsResult(false, message);
    	}
        return result;
    }

}
