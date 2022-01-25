package coms;

import java.util.HashMap;

import coms.process.ComsEventHandlerDef;
import coms.process.ComsProcess;

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
					new ComsEventHandlerDef("coms.handler.NewEnvHandler", "New Env Provision", null) 
				}, 
				new String[] {"STEP_1"});
		
		demo.addEvent("STEP_1", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.EnvConfigHandler","Env Configuration", null),
						new ComsEventHandlerDef("coms.handler.GenericHandler", "Generic handler",null) 
				}, 
				new String[] {"STEP_2"});

		demo.addEvent("STEP_2", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.CustomerNotificationHandler","Notify Customer", null),
						new ComsEventHandlerDef("coms.handler.GenericHandler","Generic handler", null) 
				}, 
				new String[] {"STEP_3", "STEP_4"});
		
		demo.addEvent("STEP_3", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.GenericHandler", "Generic handler",null) 
				}, 
				new String[] {"STEP_5"});
		
		demo.addEvent("STEP_4", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.GenericHandler","Generic handler", null) 
				}, 
				null);
		
		demo.addEvent("STEP_5", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.GenericHandler", "Generic handler",null) ,
						new ComsEventHandlerDef("coms.handler.GenericHandler","Generic handler", null) 
				}, 
				null);
		
		demo.setEndEvents(new String[] {"STEP_4", "STEP_5"});
		
		return demo;
	}
	
	public static ComsProcess define_NEW_ENV_REQ() {
		ComsProcess demo = new ComsProcess("NEW_ENV_REQ");
		
		demo.addEvent("NEW_ENV_PROVISION", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.NewEnvHandler","New Env Provision", null) 
				}, 
				new String[] {"NEW_ENV_CONFIG"});
		
		demo.addEvent("NEW_ENV_CONFIG", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.EnvConfigHandler","Env Config", null) 
				}, 
				new String[] {"NOTIFY_CUSTOMER"});
		
		demo.addEvent("NOTIFY_CUSTOMER", 
				new ComsEventHandlerDef[] { 
						new ComsEventHandlerDef("coms.handler.CustomerNotificationHandler", "Notify Customer", null) 
				}, 
				null);
		
		demo.setEndEvents(new String[] {"NOTIFY_CUSTOMER"});
		
		return demo;
	}
	
	public static ComsProcess getProcessDefinition(String procesCode) {
		return processDefinitions.get(procesCode);
	}
}
