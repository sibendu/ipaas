package coms;

import java.util.HashMap;

import coms.handler.ComsHumanTaskHandler;
import coms.process.ComsEventHandlerDef;
import coms.process.ComsHumanTaskDef;
import coms.process.ComsProcess;
import coms.util.ComsApiUtil;

public class ComsProcessRepository {
	
	private static HashMap<String, ComsProcess> processDefinitions = new HashMap<>();
	
	static {
		try {
			ComsProcess newEnvProcDef = define_NEW_ENV_REQ();
			
			processDefinitions.put(newEnvProcDef.getCode(), newEnvProcDef);
			
			ComsProcess demoProcDef = define_DEMO_PROCESS();
			
			processDefinitions.put(demoProcDef.getCode(), demoProcDef);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static ComsProcess define_DEMO_PROCESS() {
		ComsProcess demo = new ComsProcess("DEMO_PROCESS");
		
		demo.addEvent("START", 
				new ComsEventHandlerDef[] { 
					new ComsEventHandlerDef("New Env Provision", "coms.handler.NewEnvHandler",  null) 
				}, 
				new String[] {"STEP_1"});
		
		demo.addEvent("STEP_1", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Env Configuration", "coms.handler.EnvConfigHandler",null),
						new ComsEventHandlerDef( "Generic handler","coms.handler.EnvConfigHandler",null) 
				}, 
				new String[] {"STEP_2"});

		demo.addEvent("STEP_2", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Notify Customer","coms.handler.CustomerNotificationHandler", null),
						new ComsEventHandlerDef("Generic handler", "coms.handler.GenericHandler", null) 
				}, 
				new String[] {"STEP_3", "STEP_4"});
		
		demo.addEvent("STEP_3", 
				new ComsEventHandlerDef[] { 
						new ComsHumanTaskDef("Review Expense Report", null ,null, new String[] {"STEP_5"}) 
				}, 
				new String[] {"STEP_6"});
		
		demo.addEvent("STEP_4", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Generic handler","coms.handler.GenericHandler", null) 
				}, 
				null);
		
		demo.addEvent("STEP_5", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Generic handler","coms.handler.GenericHandler", null) 
				}, 
				null);
		
		demo.addEvent("STEP_6", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Generic handler","coms.handler.GenericHandler", null) 
				}, 
				null);
		
		demo.setEndEvents(new String[] {"STEP_4", "STEP_5", "STEP_6"});
		
		return demo;
	}
	
	public static ComsProcess define_NEW_ENV_REQ() {
		ComsProcess demo = new ComsProcess("NEW_ENV_REQ");
		
		demo.addEvent("NEW_ENV_PROVISION", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "New Env Provision","coms.handler.NewEnvHandler", null) 
				}, 
				new String[] {"NEW_ENV_CONFIG"});
		
		demo.addEvent("NEW_ENV_CONFIG", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Env Config", "coms.handler.EnvConfigHandler", null) 
				}, 
				new String[] {"NOTIFY_CUSTOMER"});
		
		demo.addEvent("NOTIFY_CUSTOMER", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef( "Notify Customer","coms.handler.CustomerNotificationHandler",  null) 
				}, 
				null);
		
		demo.setEndEvents(new String[] {"NOTIFY_CUSTOMER"});
		
		return demo;
	}
	
	public static ComsProcess MY_ORDER_PROCESS() {
		ComsProcess demo = new ComsProcess("MY_ORDER_PROCESS");
		
		demo.setEndEvents(new String[] {"TEST"});
		
		return demo;
	}
	
	public static ComsProcess getProcessDefinition(String procesCode) {
		return processDefinitions.get(procesCode);
	}
}
