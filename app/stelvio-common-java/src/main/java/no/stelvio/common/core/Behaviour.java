package no.stelvio.common.core;

/**
 * Base implementation class for Behaviour. 
 * 
 * @author Jonas Lindholm, Accenture
 * @version $Id: Behaviour.java 2565 2005-10-19 15:19:43Z psa2920 $
 */
public abstract class Behaviour {

	private DomainObject domainObject;

	/**
	 * Sets the DomainObject representation that this behaviour is connected to.
	 * 
	 * @param obj the DomainObject
	 */
	public final void setDomainObject(DomainObject obj) {
		this.domainObject = obj;
	}

	/**
	 * Returns the DomainObject representation that this behaviour is connected to.
	 * 
	 * @return the DomainObject
	 */
	public final DomainObject getDomainObject() {
		return domainObject;
	}

}
