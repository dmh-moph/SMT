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
@Table(name="SMT_ORGANIZATION_PERSON")
@SequenceGenerator(name="SMT_ORGANIZATION_PERSON_SEQ", sequenceName="SMT_ORGANIZATION_PERSON_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class OrganizationPerson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7529785437671030659L;


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_ORGANIZATION_PERSON_SEQ")
	@Column(name="ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="ORGANIZATION_ID")
	private OrganizationNetwork organizationNetwork;
	
	@Basic
	@Column(name="PERSON_NAME")
	private String name;
	
	@Basic
	@Column(name="PERSON_TYPE")
	private String type;
	
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

	public OrganizationNetwork getOrganizationNetwork() {
		return organizationNetwork;
	}

	public void setOrganizationNetwork(OrganizationNetwork organizationNetwork) {
		this.organizationNetwork = organizationNetwork;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
