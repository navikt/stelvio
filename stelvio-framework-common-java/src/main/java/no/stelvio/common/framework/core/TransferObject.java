package no.stelvio.common.framework.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * TransferObject is the base class for all implementations
 * of the core J2EE <i>Transfer Object</i> pattern.
 * <p/>
 * A <i>Transfer Object</i> is used to carry multiple data elements across a tier,
 * and is designed to optimize data transfers across tiers. Instead of sending
 * or receiving individual data elements, a <i>Transfer Object</i> contains all the
 * data elements in a single structure required by the request or response.
 * <p/>
 * The <i>Transfer Object</i> is passed by value to the client. Therefore all calls
 * to the <i>Transfer Object</i> are made on copies of the original <i>Transfer Object</i>.
 * <p/>
 * Consequences of using the <i>Transfer Object</i> pattern:
 * <ul>
 * 		<li>Reduced network traffic</li>
 * 		<li>Simplified remote objects and remote interfaces</li>
 * 		<li>More data transfered in fewer remote calls</li>
 * 		<li>Reduced code duplication</li>
 * 		<li>Stale transfer objects introduced</li>
 * </ul>.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2599 $ $Author: jla2920 $ $Date: 2005-10-31 13:42:13 +0100 (Mon, 31 Oct 2005) $
 */
public abstract class TransferObject implements Serializable {

	// Map that contains the data 
	protected Map map = new HashMap();

	/**
	 * Method for adding data-objects to the TransferObject
	 * 
	 * @param key		key that uniquely identifies the data 
	 * @param data		the actual data object
	 */
	public void setData(String key, Object data) {
		map.put(key, data);
	}

	/**
	 * Method for retrieving data from the TransferObject
	 * 
	 * @param key	key that uniquely identifies the data
	 * @return 		data object
	 */
	public Object getData(String key) {
		return map.get(key);
	}

	/**
	 * Compares the content for equality.
	 * 
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof TransferObject)) {
			return false;
		}
		TransferObject castOther = (TransferObject) other;
		return mapEquals(castOther.map);
	}
	
	/**
	 * Check if the TransferObject contains any data. 
	 * 
	 * @return true if the TransferObject is empty
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Compares the specified object with this map for equality.  Returns
	 * <tt>true</tt> if the given object is also a map and the two maps
	 * represent the same mappings.  More formally, two maps <tt>t1</tt> and
	 * <tt>t2</tt> represent the same mappings if
	 * <tt>t1.keySet().equals(t2.keySet())</tt> and for every key <tt>k</tt>
	 * in <tt>t1.keySet()</tt>, <tt> (t1.get(k)==null ? t2.get(k)==null :
	 * t1.get(k).equals(t2.get(k))) </tt>.  This ensures that the
	 * <tt>equals</tt> method works properly across different implementations
	 * of the map interface.<p>
	 *
	 * This implementation first checks if the specified object is this map;
	 * if so it returns <tt>true</tt>.  Then, it checks if the specified
	 * object is a map whose size is identical to the size of this set; if
	 * not, it it returns <tt>false</tt>.  If so, it iterates over this map's
	 * <tt>entrySet</tt> collection, and checks that the specified map
	 * contains each mapping that this map contains.  If the specified map
	 * fails to contain such a mapping, <tt>false</tt> is returned.  If the
	 * iteration completes, <tt>true</tt> is returned.
	 *
	 * @param t object to be compared for equality with this map.
	 * @return <tt>true</tt> if the specified object is equal to this map.
	 */
	private boolean mapEquals(Map t) {

		if (t.size() != map.size()) {
			return false;
		}

		try {
			Iterator i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				Object key = e.getKey();
				Object value = e.getValue();
				if (value == null) {
					if (!(t.get(key) == null && t.containsKey(key))) {
						return false;
					}
				} else if (value.getClass().isArray()) { // Special treatment for arrays
					if (null == t.get(key)) {
						return false; // This value is null, the other is not
					} else if (!t.get(key).getClass().isArray()) {
						return false; // The other value is an arrya, this is not
					} else if (!Arrays.equals((Object[]) t.get(key), (Object[]) value)) {
						return false; // The arrays does'nt match
					}
				} else {
					if (!value.equals(t.get(key))) {
						return false;
					}
				}
			}
		} catch (ClassCastException unused) {
			return false;
		} catch (NullPointerException unused) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the content's hash code.
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(map).toHashCode();
	}

	/**
	 * Returns a String representation of this instance. Will go through the map and handle arrays correctly.
	 * 
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("[map={");

		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			Object key = entry.getKey();
			Object value = entry.getValue();

			final Object realKey = key == this ? "(this Map)" : key;
			final Object realValue = value == this ? "(this Map)" : toString(value);

			buf.append(realKey).append('=').append(realValue);

			if (iterator.hasNext()) {
				buf.append(", ");
			}
		}

		buf.append("}]");

		return buf.toString();
	}

	/**
	 * Returns a String representation of the value handling arrays correctly.
	 *
	 * @param value the value to return a String representation for.
	 * @return Returns a String representation of the value.
	 */
	private String toString(final Object value) {
		if (null != value && value.getClass().isArray()) {
			return Arrays.asList((Object[]) value).toString();
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * Support for better toString() behaviour for transfer object implementations.
	 */
	protected static final class TransferObjectToStringStyle extends ToStringStyle {

		/** The only instance of the inner class. */
		public static final TransferObjectToStringStyle INSTANCE = new TransferObjectToStringStyle();

		/** Inner class should never be instantiated by others. */
		private TransferObjectToStringStyle() {
			setFieldSeparator(", ");
		}
	}
}
