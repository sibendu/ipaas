package coms.model;

import java.io.Serializable;

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
@Table(name = "task_variable")
@Getter @Setter @NoArgsConstructor
public class TaskVariable implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String value;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
    private TaskInstance taskInstance;
	
	public TaskVariable(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}	
}
