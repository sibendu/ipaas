package coms.process;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsResult implements Serializable{

	private boolean success;
	private String result;
	private ComsProcessContext context;
	
	public ComsResult(boolean success, String result) {
		super();
		this.success = success;
		this.result = result;
	} 
	
}
