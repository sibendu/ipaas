package coms.process;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsEventHandlerDef implements Serializable{

	private String handlerClass;
	private String description;
	private String[] nextEvents;
	
	public ComsEventHandlerDef(String handlerClass,String description, String[] nextEvents) {
		super();
		this.handlerClass = handlerClass;
		this.description = description;
		this.nextEvents = nextEvents;
	}
	
	
}
