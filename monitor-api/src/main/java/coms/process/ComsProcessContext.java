package coms.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsProcessContext implements Serializable{

	private List<ComsVariable> variables;

	public ComsProcessContext(List<ComsVariable> variables) {
		super();
		if(variables != null) {
			this.variables = variables;			
		}else {
			this.variables =  new ArrayList<>();
		}
	}
	
	public void addVariable(ComsVariable var) {
		if(this.variables == null) {
			this.variables =  new ArrayList<>();
		}
		this.variables.add(var);
	}
	
	public String serializeToString() {
		String val = "{";
		for (ComsVariable var : variables) {
			val = val + "\"" + var.getName() + "\":"+ (var.getValue() == null? "null": "\"" + var.getValue() + "\"") + ",";  
		}
		if(val.length() > 1){
			val = val.substring(0, val.length()-1);
		}
		val = val + "}";
		return val;
	}
}
