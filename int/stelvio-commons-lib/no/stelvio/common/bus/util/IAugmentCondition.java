/*
 * IAugmentCondition.java Created on Dec 18, 2006 Author: persona2c5e3b49756 Schnell
 * (test@example.com)
 * 
 * This is a utility class (interface) that is used within the DataObjectUtil
 */
package no.stelvio.common.bus.util;

import commonj.sdo.DataObject;
import commonj.sdo.Property;

/**
 * Interface for a condition used by DataObjectUtil
 * 
 * @author persona2c5e3b49756 Schnell, test@example.com
 */
public interface IAugmentCondition {
	/**
	 * Should return true if and only if a property should be handled.
	 * 
	 * @param source
	 *            The source DataObject to copy the property's value from
	 * @param property
	 *            The property to copy
	 * @param target
	 *            The target DataObject to copy the property's value to
	 * @return returns true if and only if the property should be handled
	 */
	public boolean evaluate(DataObject source, Property property, DataObject target);

}
