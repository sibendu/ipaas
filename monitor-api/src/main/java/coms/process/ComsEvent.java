package coms.process;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComsEvent implements Serializable {

	private String code;
	private Date time;
	private Long processId;
	private ComsProcessContext context;

	public ComsEvent(String code, Date time, Long processId, ComsProcessContext context) {
		super();
		this.code = code;
		this.time=time;
		this.processId = processId;
		
		if(context != null) {
			this.context = context;
		}else {
			this.context= new ComsProcessContext();
		}
	}	
}
