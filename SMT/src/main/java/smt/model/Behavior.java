package smt.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import smt.auth.model.SecurityUser;
import smt.model.glb.EducationLevel;
import smt.model.glb.Province;
import smt.model.glb.Sex;
import smt.model.glb.SituationType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_BEHAVIOR")
@SequenceGenerator(name="SMT_BEHAVIOR_SEQ", sequenceName="SMT_BEHAVIOR_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Behavior implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8188407395419621249L;

	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_BEHAVIOR_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="YEAR")
	private Integer year;
	
	@Basic
	@Column(name="BEHAVIOR_NAME")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private Province province;
	
	@Enumerated(EnumType.STRING)
	@Column(name="SEX")
	private Sex sex;
	
	@Basic
	@Column(name="START_AGE")
	private Integer startAge;
	
	@Basic
	@Column(name="END_AGE")
	private Integer endAge;
	
	@ManyToOne
	@JoinColumn(name="DV_EDUCATION_LEVEL")
	private EducationLevel targetEducationLevel;
	
	@ManyToOne
	@JoinColumn(name="DV_SITUATION_TYPE")
	private SituationType situationType;
	
	@Basic
	@Column(name="RISK_FACTOR")
	private String riskFactor;
	
	@Basic
	@Column(name="BEHAVIOR_CAUSE")
	private String cause;
	
	@Basic
	@Column(name="BEHAVIOR_DESC")
	private String description;
	
	@Basic
	@Column(name="BEHAVIOR_SYMPTOM")
	private String symptom;
	
	@Basic
	@Column(name="BEHAVIOR_PLACE")
	private String place;
	
	@Basic
	@Column(name="PREVENTIVE_GUIDELINE")
	private String PreventiveGuideline;
	
	@Basic
	@Column(name="ATTACH_FILE")
	private String attachFile;
	
	@Basic
	@Column(name="REFERENCE")
	private String reference;
	
	@OneToMany(mappedBy="behavior", cascade=CascadeType.ALL)
	
	 @OrderColumn(name="behavior_index")
	 List<BehaviorImpact> impacts;
	
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
