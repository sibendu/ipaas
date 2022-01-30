package coms.task;

import java.io.Serializable;
import java.util.List;

import coms.process.ComsVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TaskAction implements Serializable {
	
	private String user;
	private String remarks;
	private List<ComsVariable> variables;
}
