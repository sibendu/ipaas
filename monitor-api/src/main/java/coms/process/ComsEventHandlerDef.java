package coms.process;

import java.io.Serializable;

import coms.util.ComsApiUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsEventHandlerDef implements Serializable{

	private String name;
	private String handlerClass;
	private String[] nextEvents;
	
	public ComsEventHandlerDef(String name, String handlerClass, String[] nextEvents) {
		super();
		this.handlerClass = handlerClass;
		this.name = name;
		this.nextEvents = nextEvents;
	}
}
