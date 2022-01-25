package coms.process;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsVariable implements Serializable{

	private String name;
	private String value;
	
	public ComsVariable(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}	
}
