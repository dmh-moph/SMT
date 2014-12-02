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
	private String preventiveGuideline;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Integer getStartAge() {
		return startAge;
	}

	public void setStartAge(Integer startAge) {
		this.startAge = startAge;
	}

	public Integer getEndAge() {
		return endAge;
	}

	public void setEndAge(Integer endAge) {
		this.endAge = endAge;
	}

	public EducationLevel getTargetEducationLevel() {
		return targetEducationLevel;
	}

	public void setTargetEducationLevel(EducationLevel targetEducationLevel) {
		this.targetEducationLevel = targetEducationLevel;
	}

	public SituationType getSituationType() {
		return situationType;
	}

	public void setSituationType(SituationType situationType) {
		this.situationType = situationType;
	}

	public String getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(String riskFactor) {
		this.riskFactor = riskFactor;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSymptom() {
		return symptom;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPreventiveGuideline() {
		return preventiveGuideline;
	}

	public void setPreventiveGuideline(String preventiveGuideline) {
		preventiveGuideline = preventiveGuideline;
	}

	public String getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public List<BehaviorImpact> getImpacts() {
		return impacts;
	}

	public void setImpacts(List<BehaviorImpact> impacts) {
		this.impacts = impacts;
	}

	public SecurityUser getCreateBy() {
		return createBy;
	}

	public void setCreateBy(SecurityUser createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SecurityUser getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(SecurityUser lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
}
