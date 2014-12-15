package smt.model.glb;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="GLB_ZONE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=HealthZone.class)
public class HealthZone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1756158173268695687L;

	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="ZONE_CODE")
	private String code;
	
	@Basic
	@Column(name="ZONE_NAME")
	private String name;
	
	@OneToMany(mappedBy="zone")
	Set<Province> provinces;

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
	
	
	
	
}
