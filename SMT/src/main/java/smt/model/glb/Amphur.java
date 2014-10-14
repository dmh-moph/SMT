package smt.model.glb;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="GLB_AMPHUR")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Amphur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2752819600355932373L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="AMPHUR_CODE")
	private String code;
	
	@Basic
	@Column(name="AMPHUR_NAME")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private Province province;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	
	
	
}
