package coms.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsProcess {
	
	private String code;
	private HashMap<String, String[]> eventMap;
	private HashMap<String, ComsEventHandlerDef[]> eventHandlers; 
	private String[] endEvents;
	
	public ComsProcess(String code) {
		super();
		this.code =  code;
		this.eventHandlers = new LinkedHashMap<String, ComsEventHandlerDef[]>();
		this.eventMap = new LinkedHashMap<String, String[]>();
	}
	
	public String[] getNextEvents(String currentEvent) {
		return this.eventMap.get(currentEvent);
	}
	
	public ComsEventHandlerDef[] getEventHandlers(String currentEvent) {
		return this.eventHandlers.get(currentEvent);
	}
	
	public void addEvent(String event, ComsEventHandlerDef[] handlers, String[] nextEvents) {
		this.eventHandlers.put(event, handlers);
		this.eventMap.put(event, nextEvents);
	}
	
	public String getStartEvent() {
		Map.Entry<String,String[]> entry = eventMap.entrySet().iterator().next();
		String key= entry.getKey();
		return key;
	}
	
	public boolean validateProcessDefinition() throws Exception{
		
		//End events must be defined
		if(endEvents == null || endEvents.length == 0 ) {
			throw new Exception("Process definition must have end events defined");
		}
		
		//For end events, there should not be further events
		for (int i = 0; i < endEvents.length; i++) {
			
			ComsEventHandlerDef[] hs = this.eventHandlers.get(endEvents[i]);
			for (int j = 0; j < hs.length; j++) {
				if(hs[i].getNextEvents() != null || hs[i].getNextEvents().length > 0) {
					throw new Exception("Event "+ endEvents[i]+ "is an end event, but its handler "+hs[i].getHandlerClass()+" has next event defined");
				}
			}
			
			String[] nextEvents = eventMap.get(endEvents[i]);
			if(nextEvents != null || nextEvents.length > 0) {
				throw new Exception("Next events are defined for the end event "+endEvents[i]);
			}
		}
		
		return true;
	}
	
	public boolean isEndEvent(String event) {
		for (int i = 0; i < endEvents.length; i++) {
			if(event.equalsIgnoreCase(endEvents[i])) return true;
		}
		return false;
	}
}
