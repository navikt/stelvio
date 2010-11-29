package no.stelvio.common.util;

/**
 * Helper methods for working with objects.
 *
 * @author personf8e9850ed756, Accenture
 * @version $Id: ObjectUtil.java 1949 2005-02-08 11:33:09Z psa2920 $
 */
public final class ObjectUtil {

	/** Should not be instantiated. */
	private ObjectUtil() {
	}

	/**
	 * Null safe method for cloning a <code>Cloneable</code>.
	 *
	 * @param object the object to clone.
	 * @return null if input is null, otherwise a clone of the object.
	 */
	public static Object nullSafeClone(Cloneable object) {
		return null == object ? null : ReflectUtil.callMethodOnInstance(object, "clone");
	}
}
