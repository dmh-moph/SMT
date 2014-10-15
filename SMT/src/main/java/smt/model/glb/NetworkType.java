package smt.model.glb;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ORG_TYPE")
public class NetworkType extends DomainVariable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8882781559614363637L;

}
