package coms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_instance")
@Getter @Setter @NoArgsConstructor
public class TaskInstance implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private Date created;
	private Date firstRetrieved;
	private Date updated;
	private String assignedUser;
	private String assignedGroup;	
	private String remark;
	private String status;
	
	private Long processId;
	
	@OneToMany(mappedBy = "taskInstance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<TaskActivity> records = new HashSet<>();
	
	@OneToMany(mappedBy = "taskInstance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<TaskVariable> variables = new HashSet<>();

	public TaskInstance(Long processId, String name, String description, Date created, Date firstRetrieved, Date updated,
			String assignedUser, String assignedGroup, String remark, String status) {
		super();
		this.processId =  processId;
		this.name = name;
		this.description = description;
		this.created = created;
		this.firstRetrieved = firstRetrieved;
		this.updated = updated;
		this.assignedUser = assignedUser;
		this.assignedGroup = assignedGroup;
		this.remark = remark;
		this.status = status;
		this.records = records;
	}
	
}