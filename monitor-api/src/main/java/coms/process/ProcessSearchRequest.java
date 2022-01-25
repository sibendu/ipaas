package coms.process;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProcessSearchRequest implements Serializable {
	private String code;
	private String status;
}
