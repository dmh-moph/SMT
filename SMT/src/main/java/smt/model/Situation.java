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

import org.neo4j.cypher.internal.compiler.v2_1.perty.docbuilders.scalaDocBuilder;

import smt.auth.model.SecurityUser;
import smt.model.glb.EducationLevel;
import smt.model.glb.HealthZone;
import smt.model.glb.Province;
import smt.model.glb.Sex;
import smt.model.glb.SituationType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SMT_SITUATION")
@SequenceGenerator(name="SMT_SITUATION_SEQ", sequenceName="SMT_SITUATION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Situation.class)
public class Situation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5608380002832435844L;

	
	@Override
	public int hashCode() {
		if(this.id !=null) return this.id.hashCode();
		
		return super.hashCode();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SMT_SITUATION_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="CODE")
	private String code;
	
	@ManyToOne
	@JoinColumn(name="DV_SITUATION_TYPE")
	private SituationType situationType;
	
	@Basic
	@Column(name="TYPE")
	private String type;
	
	@Basic
	@Column(name="NAME")
	private String name;


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

	public SituationType getSituationType() {
		return situationType;
	}

	public void setSituationType(SituationType situationType) {
		this.situationType = situationType;
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
	
	
	
	
}
