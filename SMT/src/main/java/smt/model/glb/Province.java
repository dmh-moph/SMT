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
@Table(name="GLB_PROVINCE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Province.class)
public class Province implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1275651369352428923L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="PROVINCE_CODE")
	private String code;
	
	@Basic
	@Column(name="PROVINCE_NAME")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="ZONE_ID")
	private HealthZone zone;

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

	public HealthZone getZone() {
		return zone;
	}

	public void setZone(HealthZone zone) {
		this.zone = zone;
	}
	
	
}
