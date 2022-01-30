package coms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "process_instance")
@Getter @Setter @NoArgsConstructor
public class ProcessInstance implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String status;
	private Date created;
	private Date updated;
	private int noEndEvents = 0;
	
	@OneToMany(mappedBy = "processInstance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<ProcessActivity> records = new HashSet<>();
	
	public ProcessInstance(Long id, String code, String status, Date created, Date updated) {
		super();
		this.id = id;
		this.code = code;
		this.status = status;
		this.created =created;
		this.updated =updated;
	}	
	
	public void addActivity(ProcessActivity act) {
		this.records.add(act);
	}
}
