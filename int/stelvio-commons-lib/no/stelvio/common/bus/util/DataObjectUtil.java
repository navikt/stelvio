/*
 * DataObjectUtil.java Created on Sep 28, 2006 Author: persona2c5e3b49756 Schnell
 * (test@example.com)
 * 
 * This is a utility class that provides different capability for SDOs
 * (DataObjects), useful for copy DataObjectProperties with different Namespaces
 * or other functionality
 * 
 * @Updated:
 *  18-12-2006 -> new Version with more functionality
 * 
 */

package no.stelvio.common.bus.util;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import com.ibm.websphere.bo.BOXMLSerializer;
import com.ibm.websphere.sca.ServiceManager;
import com.ibm.websphere.sca.sdo.DataFactory;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/*
 * <p> This is a utility class that provides helper methods around SDOs, useful for different purpose as copy the SDO properties
 * from Source to Target or other funtionality around SDO </p>
 * 
 * @usage <p>
 * 
 * </p>
 * 
 * @author persona2c5e3b49756 Schnell, test@example.com
 * @author Erik Godding Boye, test@example.com
 */

public class DataObjectUtil {

	public static final int EXACT_TYPE = 0;
	public static final int MATCH_NAME = 1;
	public static final int MATCH_TYPENAME = 2;
	public static final int MATCH_BOTH = MATCH_NAME | MATCH_TYPENAME;

	/**
	 * Remove all leading and trailing whitespace from all data object string properties (recursively).
	 * 
	 * @param dataObject
	 *            dataObject
	 * @return dataObject
	 */
	public static DataObject trimWhitespace(DataObject dataObject) {
		visitProperties(dataObject, new PropertyVisitor() {
			@Override
			public Object visitProperty(Object propertyValue) {
				Object newPropertyValue = propertyValue;
				if (propertyValue != null & propertyValue instanceof String) {
					newPropertyValue = ((String) propertyValue).trim();
				}
				return newPropertyValue;
			}
		});
		return dataObject;
	}

	/**
	 * Nullify all data object strings properties that are whitespace only (recursively).
	 * 
	 * @param dataObject
	 *            dataObject
	 * @return dataObject
	 */
	public static DataObject nullifyBlankStrings(DataObject dataObject) {
		visitProperties(dataObject, new PropertyVisitor() {
			@Override
			public Object visitProperty(Object propertyValue) {
				if (propertyValue != null & propertyValue instanceof String) {
					for (char character : ((String) propertyValue).toCharArray()) {
						if (!Character.isWhitespace(character)) {
							return propertyValue;
						}
					}
					// All characters are whitespace - return null;
					return null;
				}
				return propertyValue;
			}
		});
		return dataObject;
	}

	/**
	 * Utility method that can be used to visit all (simple properties) in a data object structure.
	 * 
	 * @param dataObject
	 *            dataObject
	 * @param propertyVisitor
	 *            propertyVisitor
	 */
	public static void visitProperties(DataObject dataObject, PropertyVisitor propertyVisitor) {
		visitProperties((Object) dataObject, propertyVisitor);
	}

	private static Object visitProperties(Object object, PropertyVisitor propertyVisitor) {
		if (object == null) {
			return object;
		} else if (object instanceof DataObject) {
			DataObject dataObject = (DataObject) object;
			for (Property property : (List<Property>) dataObject.getType().getProperties()) {
				Object propertyValue = dataObject.get(property);
				if (property.isMany()) {
					List list = (List) propertyValue;
					for (ListIterator listIterator = list.listIterator(); listIterator.hasNext();) {
						Object listElement = listIterator.next();
						Object newListElement = visitProperties(listElement, propertyVisitor);
						listIterator.set(newListElement);
					}
				} else {
					Object newPropertyValue = visitProperties(propertyValue, propertyVisitor);
					dataObject.set(property, newPropertyValue);
				}
			}
			return object;
		} else {
			return propertyVisitor.visitProperty(object);
		}
	}

	/**
	 * <p>
	 * Copy the SDO properties from SourceToTarget DataObject (SDO)
	 * </p>
	 * 
	 * @param source
	 *            The SDO object to copy
	 * @param target
	 *            The target SDO object
	 */
	public static DataObject deepCopyDataObjectProperties(DataObject source, DataObject target) {
		if ((source != null) && (target != null)) {
			for (Iterator i = source.getType().getProperties().iterator(); i.hasNext();) {
				Property property = (Property) i.next();
				Object object = source.get(property);
				if (object != null) {
					if (object instanceof DataObject) {
						object = deepCopyDataObjectProperties((DataObject) object, ((DataObject) target)
								.createDataObject(property));
					}
					if (object instanceof String) {
						object = ((String) object).trim();
					}
				}
				target.set(property, object);
			}
		}
		return target;
	}

	/**
	 * <p>
	 * Copy the SDO properties from SourceToTarget without NS DataObject (SDO)
	 * </p>
	 * 
	 * @param source
	 *            The SDO object to copy
	 * @param target
	 *            The target SDO object
	 */
	public static DataObject deepCopyDataObjectNoNS(DataObject source, DataObject target) {
		if ((source != null) && (target != null)) {
			for (Iterator i = source.getType().getProperties().iterator(); i.hasNext();) {
				Property property = (Property) i.next();
				Object object = source.get(property);
				if (object != null) {
					if (object instanceof DataObject) {
						object = deepCopyDataObjectNoNS((DataObject) object, ((DataObject) target).createDataObject(property
								.getName()));
					}
					if (object instanceof String) {
						object = ((String) object).trim();
					}
				}
				target.set(property.getName(), object);
			}
		}
		return target;
	}

	/**
	 * <p>
	 * Copy the SDO object from SourceToTarget DataObject (SDO)
	 * </p>
	 * 
	 * @param source
	 *            The SDO object to copy
	 * @param target
	 *            The target SDO object
	 */
	public static DataObject augmentDataObject(DataObject source, DataObject target) {
		if (source != null) {
			if (target == null) {
				target = DataFactory.INSTANCE.create(source.getType());
			}
			for (Iterator i = source.getType().getProperties().iterator(); i.hasNext();) {
				Property property = (Property) i.next();
				Object object = source.get(property);
				if (object != null) {
					if (object instanceof DataObject) {
						object = augmentDataObject((DataObject) object, (DataObject) target.get(property));
					}
					target.set(property, object);
				}
			}
		}
		return target;
	}

	/**
	 * IAugmentCondition returns always true.
	 */
	public static final IAugmentCondition alwaysCondition = new IAugmentCondition() {
		public boolean evaluate(DataObject source, Property property, DataObject target) {
			return true;
		}
	};

	/**
	 * IAugmentCondition returns true if the source property's set flag is set.
	 */
	public static final IAugmentCondition isSetCondition = new IAugmentCondition() {
		public boolean evaluate(DataObject source, Property property, DataObject target) {
			return source.isSet(property);
		}
	};

	/**
	 * Copies hierachical the value of each property in source to the same property in target if the condition function called
	 * returns true. It does not clone the properties values. It creates property objects if they don't already exist and the
	 * parameter is set to true. List items are replaced, and additional items are created if createProps is set to true.
	 * 
	 * @param source
	 *            The source data object to copy the data from
	 * @param target
	 *            The target object to copy the data to, will be modified
	 * @param exactProps
	 *            Set this to EXACT_TPYE if a property's value should only be copied if it's type is exactly the same than the
	 *            target property's type; if set to MATCH_NAME a property's value will also be copied if only the property's
	 *            name, isContainment flag, and isMany flag are matching; if set to MATCH_TYPENAME a property's value will also
	 *            be copied if only the property's type name, isContainment flag, and isMany flag are matching; if set to
	 *            MATCH_NAME|MATCH_TYPENAME a property's value will also be copied if only the property's name, type name,
	 *            isContainment flag, and isMany flag are matching
	 * @param createProps
	 *            Set this to true to create DataObjects for empty properties
	 * @param condition
	 *            This condition is called for each property to evaluate if it should be copied or not
	 * @return returns the target object
	 */
	@SuppressWarnings("unchecked")
	public static DataObject deepCopyDataObject(DataObject source, DataObject target, int exactProps, boolean createProps,
			IAugmentCondition condition) {
		if (source != null) {
			if ((target != null) || createProps) {
				if (target == null)
					target = DataFactory.INSTANCE.create(source.getType());
				for (Iterator i = source.getType().getProperties().iterator(); i.hasNext();) { // iterate over source
					// properties
					Property sProp = (Property) i.next();
					Object object = source.get(sProp);
					Property tProp;
					boolean canHoldProp;
					if ((exactProps & (MATCH_NAME | MATCH_TYPENAME)) <= 0) {
						canHoldProp = target.getType().getProperties().contains(sProp);
						tProp = sProp;
					} else {
						canHoldProp = hasProperty(target, ((exactProps & MATCH_NAME) > 0) ? sProp.getName() : null,
								((exactProps & MATCH_TYPENAME) > 0) ? sProp.getType().getName() : null, new Boolean(sProp
										.isContainment()), new Boolean(sProp.isMany()));
						tProp = target.getType().getProperty(sProp.getName());
					}
					if (canHoldProp) { // target can hold property
						if (condition.evaluate(source, tProp, target)) { // condition
							if (object == null) { // is null
								target.set(tProp, object);
							} else if (!sProp.isMany()) { // is not an array
								if (!sProp.isContainment()) { // is a simple type
									target.set(tProp, object);
								} else { // is a DataObject
									target.set(tProp, deepCopyDataObject((DataObject) object, (DataObject) target.get(tProp),
											exactProps, createProps, condition));
								}
							} else { // is an array
								if (createProps && (target.getList(tProp) == null))
									target.set(tProp, new Vector());
								if (target.getList(tProp) != null) {
									List sl = (List) object;
									List tl = target.getList(tProp);
									for (int j = 0; j < sl.size(); j++) { // iterate over source array items
										if (j < tl.size()) { // replace item
											if (!sProp.isContainment()) { // simple type
												tl.set(j, sl.get(j));
											} else { // DataObject
												tl.set(j, deepCopyDataObject((DataObject) sl.get(j), (DataObject) tl.get(j),
														exactProps, createProps, condition));
											}
										} else if (createProps) { // append item
											if (!sProp.isContainment()) { // simple type
												tl.add(sl.get(j));
											} else { // DataObject
												tl.add(deepCopyDataObject((DataObject) sl.get(j), null, exactProps, true,
														condition));
											}
										}
									}
								}
							}
							if (!source.isSet(sProp))
								target.unset(tProp);
						}
					}
				}
			}
		}
		return target;
	}

	/**
	 * <p>
	 * Copies hierachical the value of each property in source to the target.
	 * </p>
	 * 
	 * @param source
	 *            The source data object to copy the data from
	 * @param target
	 *            The target object to copy the data to, can also be null
	 * @return returns the target object
	 */
	public static DataObject cloneDataObject(DataObject source, DataObject target) {
		// return deepCopyDataObject(source,target,EXACT_TYPE,true,alwaysCondition);
		return deepCopyDataObject(source, target, MATCH_TYPENAME, true, alwaysCondition);
	}

	/**
	 * <p>
	 * Copies hierachical the value of each property in source to the same property in target if the condition function
	 * isSetCondition returns true. It creates property objects if they don't already exist.
	 * </p>
	 * 
	 * @param source
	 *            The source data object to copy the data from
	 * @param target
	 *            The target object to copy the data to, can also be null
	 * @return returns the target object
	 * 
	 */
	public static DataObject augmentDataObjectTest(DataObject source, DataObject target) {
		return deepCopyDataObject(source, target, MATCH_NAME | MATCH_TYPENAME, true, isSetCondition);
	}

	/**
	 * <p>
	 * Copies hierachical the value of each property in source to the same property in target if it exists.
	 * </p>
	 * 
	 * @param source
	 *            The source data object to copy the data from
	 * @param target
	 *            The target object to copy the data to
	 * @return returns the target object
	 */
	public static DataObject deepCopyDataObjectPropertiesTest(DataObject source, DataObject target) {
		return deepCopyDataObject(source, target, MATCH_NAME | MATCH_TYPENAME, false, alwaysCondition);
	}

	/**
	 * <p>
	 * Convert an SDO object to XML
	 * </p>
	 * 
	 * @param source
	 *            The SDO object to copy
	 * @return String SDO as XML String
	 */
	public static String convertDataObjectToXML(DataObject sdo) {
		if (sdo == null)
			return null;
		try {
			BOXMLSerializer xmlSerializer = (BOXMLSerializer) new ServiceManager()
					.locateService("com/ibm/websphere/bo/BOXMLSerializer");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Type type = sdo.getType();
			xmlSerializer.writeDataObject(sdo, type.getURI(), type.getName(), baos);
			String boxml = new String(baos.toByteArray());
			return boxml;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * Returns true if and only if a property with the criterias is found.
	 * </p>
	 * 
	 * @param bo
	 *            The DataObject to search in
	 * @param nameRegex
	 *            The name of the property to search for, can be a regular expression, can be null to disable this criteria
	 * @param typenameRegex
	 *            The name of the property's type to search for, can be a regular expression, can be null to disable this
	 *            criteria
	 * @param isBo
	 *            Criteria to search for either a DataObject or a simple type, can be null to disable this criteria
	 * @param isArray
	 *            Criteria to search for either an Array or a flat type, can be null to disable this criteria
	 * @return returns true if and only if a property with the criterias is found
	 */
	protected static boolean hasProperty(DataObject bo, String nameRegex, String typenameRegex, Boolean isBo, Boolean isArray) {
		for (Iterator i = bo.getType().getProperties().iterator(); i.hasNext();) { // iterate over bo properties
			Property property = (Property) i.next();
			if (nameRegex != null ? property.getName().matches(nameRegex) : true && typenameRegex != null ? property.getType()
					.getName().matches(typenameRegex) : true && isBo != null ? property.isContainment() == isBo.booleanValue()
					: true && isArray != null ? property.isMany() == isArray.booleanValue() : true)
				return true;
		}
		return false;
	}

}
