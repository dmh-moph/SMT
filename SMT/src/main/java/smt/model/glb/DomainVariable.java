package smt.model.glb;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="GLB_VARIABLE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class DomainVariable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5848352716161322763L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="DOMAIN")
	private String domain;
	
	@Basic
	@Column(name="SEQUENCE")
	private Integer sequence;
	
	@Basic
	@Column(name="CODE")
	private String code;
	
	@Basic
	@Column(name="DESCRIPTION")
	private String description;
	
	@Basic 
	@Column(name="REMARK")
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
}

