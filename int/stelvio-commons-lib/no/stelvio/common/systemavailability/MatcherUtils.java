package no.stelvio.common.systemavailability;

import java.util.Date;
import java.util.Iterator;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;

import commonj.sdo.DataObject;
import commonj.sdo.Property;

class MatcherUtils {
	private MatcherUtils() {
	}

	static boolean match(DataObject criteriaObject, DataObject testObject) {
		for (Iterator i = criteriaObject.getType().getProperties().iterator(); i.hasNext();) {
			Property property = (Property) i.next();
			Object criteriaSubObject = criteriaObject.get(property);
			if (criteriaSubObject != null) {
				Object testSubObject = testObject.get(property);
				if (testSubObject == null)
					return false;
				if (criteriaSubObject instanceof DataObject) {
					if (!(testSubObject instanceof DataObject))
						return false;
					if (!match((DataObject) criteriaSubObject, (DataObject) testSubObject))
						return false;
				} else if (criteriaSubObject instanceof EObjectContainmentEList) {
					if (!(testSubObject instanceof EObjectContainmentEList)) {
						return false;
					}
					// The list in criteria has size 0 if nothing is provided.
					// This should match always.
					if (((EObjectContainmentEList) criteriaSubObject).basicList().size() != 0) {
						Iterator criteriaIterator = ((EObjectContainmentEList) criteriaSubObject).basicList().iterator();
						while (criteriaIterator.hasNext()) {
							boolean foundmatchingelement = false;
							DataObject criteriaElement = (DataObject) criteriaIterator.next();
							Iterator testIterator = ((EObjectContainmentEList) testSubObject).basicList().iterator();
							while (testIterator.hasNext()) {
								if (match(criteriaElement, (DataObject) testIterator.next()))
									foundmatchingelement = true;
							}
							if (!foundmatchingelement)
								return false;
						}
					}
				} else if (!matchPrimitive(testSubObject, criteriaSubObject))
					return false;
			}
		}
		return true;
	}

	private static boolean matchPrimitive(Object testSubObject, Object criteriaSubObject) {
		if (testSubObject instanceof Date && criteriaSubObject instanceof Date) {
			// Accept up to an hour difference, due to problems with time
			// zone/DST and serialization'
			if (!(Math.abs(((Date) testSubObject).getTime() - ((Date) criteriaSubObject).getTime()) < 1000 * 4000))
				return false;
		} else if (criteriaSubObject instanceof Integer && ((Integer) criteriaSubObject).intValue() == 0) {
			// When criteria of these types are removed,
			// they are still read up as 0 values (or false). This is a hack so
			// that criteria value 0 matches everything
			return true;
		} else if (criteriaSubObject instanceof Long && ((Long) criteriaSubObject).longValue() == 0) {
			return true;
		} else if (criteriaSubObject instanceof Short && ((Short) criteriaSubObject).shortValue() == 0) {
			return true;
		} else if (criteriaSubObject instanceof Boolean && ((Boolean) criteriaSubObject).booleanValue() == false) {
			// Special case: Booleans that are not included in the Stored
			// Criteria are not Null, they are recorded as false. So we must
			// accept this as always match until further notice
			return true;
		} else if (criteriaSubObject instanceof Float && ((Float) criteriaSubObject).floatValue() == 0) {
			return true;
		} else if (criteriaSubObject instanceof Double && ((Double) criteriaSubObject).doubleValue() == 0) {
			return true;
		}
		if (!testSubObject.equals(criteriaSubObject)) {
			return false;
		}
		return true;
	}
}
