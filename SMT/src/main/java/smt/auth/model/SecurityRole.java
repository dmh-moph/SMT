package smt.auth.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SEC_ROLE")
@SequenceGenerator(name="SEC_ROLE_SEQ", sequenceName="SEC_ROLE_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SecurityRole implements Role, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6684395870677941937L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SEC_ROLE_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
	
}
