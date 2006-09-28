package no.stelvio.common.core;

import java.io.Serializable;

/**
 * DomainObject is the base class that all objects representing an
 * application or enterprise domain object should extend.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2565 $ $Author: psa2920 $ $Date: 2005-10-19 17:19:43 +0200 (Wed, 19 Oct 2005) $
 */
public abstract class DomainObject implements Serializable {

	private Behaviour behaviour = null;

	/**
	 * Get the behaviour
	 * @return the behaviour
	 */
	public final Behaviour getBehaviour() {
		return behaviour;
	}

	/**
	 * Set the behaviour
	 * @param behaviour the behaviour.
	 */
	public final void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
		if (this.behaviour != null) {
			this.behaviour.setDomainObject(this);
		}
	}

}
