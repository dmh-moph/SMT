package smt.auth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import smt.model.glb.Occupation;
import smt.model.glb.Position;
import smt.model.glb.Sex;
import smt.model.glb.UserInfoObjective;

@Entity
@Table(name="SMT_USERINFO")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6170207935552849374L;
	

	public static Logger logger = LoggerFactory.getLogger(UserInfo.class);
	


	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="EMAIL")
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(name="SEX")
	private Sex sex;
	
	@Column(name="DEPARTMENT")
	private String department;
	
	@ManyToOne
	@JoinColumn(name="OCCUPATION")
	private Occupation occupation;
	
	@Column(name="OCCUPATION_OTHER")
	private String OccupationOther;
	
	@ManyToOne
	@JoinColumn(name="POSITION")
	private Position position;
	
	@Column(name="POSITION_OTHER")
	private String positionOther;
	
	@ManyToOne
	@JoinColumn(name="OBJECTIVE")
	private UserInfoObjective objective;
	
	@Column(name="OBJECTIVE_OTHER")
	private String ObjectiveOther;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public String getOccupationOther() {
		return OccupationOther;
	}

	public void setOccupationOther(String occupationOther) {
		OccupationOther = occupationOther;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getPositionOther() {
		return positionOther;
	}

	public void setPositionOther(String positionOther) {
		this.positionOther = positionOther;
	}

	public UserInfoObjective getObjective() {
		return objective;
	}

	public void setObjective(UserInfoObjective objective) {
		this.objective = objective;
	}

	public String getObjectiveOther() {
		return ObjectiveOther;
	}

	public void setObjectiveOther(String objectiveOther) {
		ObjectiveOther = objectiveOther;
	}
	
	
	
	
	
}
