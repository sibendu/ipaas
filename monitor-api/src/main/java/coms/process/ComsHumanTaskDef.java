package coms.process;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsHumanTaskDef extends ComsEventHandlerDef{
	
	private String assignedToGroup;
	private String assignedToUser;
	
	public ComsHumanTaskDef(String name, String assignedToGroup, String assignedToUser,String[] nextEvents) {
		super(name, "coms.handler.ComsHumanTaskHandler", nextEvents);
		this.assignedToGroup = assignedToGroup;
		this.assignedToUser = assignedToUser;
	}
}
