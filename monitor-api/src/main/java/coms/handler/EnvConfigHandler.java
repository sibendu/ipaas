package coms.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import coms.model.ProcessInstance;
import coms.process.ComsAbstractEventHandler;
import coms.process.ComsEvent;
import coms.process.ComsProcessContext;
import coms.process.ComsResult;
import coms.process.ComsVariable;
import coms.process.IComsEventHandler;
import coms.service.ComsProcessService;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EnvConfigHandler extends ComsAbstractEventHandler implements IComsEventHandler{
	
    public EnvConfigHandler(ComsProcessService jobService) {
		super(jobService);
	}

    public ComsResult process(ComsEvent e) {
    	//System.out.println("Event Handler fired: "+e.getCode()+", processor invoked:"+this.getClass());
    	ComsResult result = null;
    	String message = null;
        try {
            System.out.println("New environment provisioned, configuring it now ....");
            
        	System.out.println("	Context: "+ e.getContext().serializeToString());

            e.getContext().addVariable(new ComsVariable("VAR-EnvConfig","Val-EnvConfigHandler"));
            
            Thread.sleep(2000);
            
            message = "Environment configured";
            
            result = new ComsResult(true, message);
            
        }catch(Exception ex) {
    		message = "Error in event handler "+e.getClass()+" :: Error = "+ex.getMessage();
            result = new ComsResult(false, message);
    	}
    	//System.out.println("Event Handler completed: "+this.getClass()+" ::: Result = "+result);
        return result;
    }

}
