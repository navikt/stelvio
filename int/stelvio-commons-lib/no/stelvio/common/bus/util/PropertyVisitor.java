package no.stelvio.common.bus.util;

/**
 * @author test@example.com
 */
public interface PropertyVisitor {
	/**
	 * Callback method that will be invoked for all (simple) properties in a structure of data objects.
	 * 
	 * @param propertyValue the value of the property
	 * @return the new value of the property (changed or unchanged)
	 */
	Object visitProperty(Object propertyValue);
}
