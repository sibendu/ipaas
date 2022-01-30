package coms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

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

import coms.util.ComsUtil;
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
	private Date updated;
	private String assignedUser;
	private String assignedGroup;	
	private String remark;
	private String nextEvents;
	private String status;
	
	private Long processId;
	private Long activityId;
	
	@OneToMany(mappedBy = "taskInstance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<TaskActivity> activities = new HashSet<>();
	
	@OneToMany(mappedBy = "taskInstance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<TaskVariable> variables = new HashSet<>();

	public TaskInstance( String name, String description, String assignedGroup, String assignedUser, Long processId,Long activityId) {
		super();
		this.name = name;
		this.description = description;
		this.assignedGroup = assignedGroup;
		this.assignedUser = assignedUser;

		this.processId =  processId;
		this.activityId =  activityId;
		
		this.created = new Date();
		this.status = ComsUtil.TASK_STATE_NEW;
	}
	
	public void addActivity(TaskActivity activity) {
		this.activities.add(activity);
	}
	
	public void addVariable(TaskVariable var) {
		this.variables.add(var);
	}
	
	public TaskVariable getVariable(String variableName) throws Exception{
		for (TaskVariable var : this.variables) {
			if(var.getName().equalsIgnoreCase(variableName)) {
				return var;
			}
		}
		throw new Exception("Task id "+this.getId()+"does not contain variable "+variableName);
	}
	
	public void serializeSetNextEvents(String[] events) {
		String str = null;
		if(events != null && events.length > 0) {
			str = "";
			for (int i = 0; i < events.length; i++) {
				str = str + events[i] + "#";
			}
			str = str.substring(0, str.length() -1);
		}
		this.nextEvents = str;
	}
	
	public String[] getDeserializeNextEvents() {
		ArrayList<String> events = new ArrayList<>();
		if(this.nextEvents != null) {
			StringTokenizer stkn = new StringTokenizer(this.nextEvents, "#");
			while (stkn.hasMoreElements()) {
				events.add((String)stkn.nextElement());				
			}
		}
		String[] evs = new String[events.size()];
		for (int i = 0; i < events.size(); i++) {
			evs[i] =  events.get(i);	
		} 
		return evs;		
	}
}