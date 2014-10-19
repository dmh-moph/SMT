package smt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import smt.auth.model.SecurityUser;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_BEHAVIOR_IMPACT")
@SequenceGenerator(name="SMT_BEHAVIOR_IMPACT_SEQ", sequenceName="SMT_BEHAVIOR_IMPACT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class BehaviorImpact implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6698056221899997962L;



	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_BEHAVIOR_IMPACT_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="IMPACT_DESC")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="BEHAVIOR_ID")
	private Behavior behavior;
	
	@ManyToOne
	@JoinColumn(name="CREATE_BY")
	private SecurityUser createBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	@ManyToOne
	@JoinColumn(name="LAST_UPDATE_BY")
	private SecurityUser lastUpdateBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	
	
}
