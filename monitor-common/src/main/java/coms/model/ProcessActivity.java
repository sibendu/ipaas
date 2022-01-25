package coms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "process_activity")
@Getter @Setter @NoArgsConstructor
public class ProcessActivity implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String event;
	private String handler;
	private String description;
	private Date start;
	private Date finish;
	private String success;	
	private String message;
	
	@Column(name = "variables", length = 2000)
	private String variables;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
    private ProcessInstance processInstance;
	
	public ProcessActivity(Long id, String event, String handler, String description, Date start, Date finish, String variables, ProcessInstance processInstance) {
		super();
		this.id = id;
		this.event =  event;
		this.handler = handler;
		this.description = description;
		this.start = start;
		this.finish = finish;
		this.variables = variables;
		this.processInstance = processInstance;
	}
}
