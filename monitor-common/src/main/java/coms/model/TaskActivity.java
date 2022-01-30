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
@Table(name = "task_activity")
@Getter @Setter @NoArgsConstructor
public class TaskActivity implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String userId;
	private Date timestamp;
	private String message;
		
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
    private TaskInstance taskInstance;

	public TaskActivity(Long id, String userId, Date timestamp, String message, TaskInstance taskInstance) {
		super();
		this.id = id;
		this.userId = userId;
		this.timestamp = timestamp;
		this.message = message;
		this.taskInstance = taskInstance;
	}
	
	
}
