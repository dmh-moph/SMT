package smt.model.glb;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@DiscriminatorValue("SITUATION_TYPE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SituationType.class)
public class SituationType extends DomainVariable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1512848338264227387L;

	

}
