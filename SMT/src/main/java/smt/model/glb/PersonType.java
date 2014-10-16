package smt.model.glb;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PERSON_TYPE")
public class PersonType extends DomainVariable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7542858489287444276L;

}
