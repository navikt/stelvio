package no.stelvio.web.taglib;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the <code>BeanInfo</code> descriptor for the
 * {@link CTSelectTag} class. It is needed to override the default mapping
 * of custom tag attribute names to class attribute names.
 * <p>
 * This is because the value of the unevaluated EL expression has to be kept
 * separately from the evaluated value, which is stored in the base class. This
 * is related to the fact that the JSP compiler can choose to reuse different
 * tag instances if they received the same original attribute values, and the
 * JSP compiler can choose to not re-call the setter methods, because it can
 * assume the same values are already set.
 *
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class CTSelectTagBeanInfo extends SimpleBeanInfo {

	private static final Log LOG = LogFactory.getLog(CTSelectTag.class);

	/**
	 * Returns an array of <code>PropertyDescriptor</code>s for mapping property name and method
	 * in the <code>CTSelectTag</code>.
	 *
	 * @return an array of <code>PropertyDescriptor</code>s.
	 * @see PropertyDescriptor
	 * @see CTSelectTag
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		ArrayList proplist = new ArrayList();

		try {
			proplist.add(new PropertyDescriptor("name", CTSelectTag.class, null, "setName"));
		} catch (IntrospectionException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Failed to map setter method for property named 'name'", ex);
			}
		}

		try {
			proplist.add(new PropertyDescriptor("property", CTSelectTag.class, null, "setProperty"));
		} catch (IntrospectionException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Failed to map setter method for property named 'property'", ex);
			}
		}

		try {
			proplist.add(new PropertyDescriptor("value", CTSelectTag.class, null, "setValue"));
		} catch (IntrospectionException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Failed to map setter method for property named 'value'", ex);
			}
		}

		try {
			proplist.add(new PropertyDescriptor("indexed", CTSelectTag.class, null, "setIndexed"));
		} catch (IntrospectionException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Failed to map setter method for property named 'indexed'", ex);
			}
		}

		try {
			proplist.add(new PropertyDescriptor("readonly", CTSelectTag.class, null, "setElReadonly"));
		} catch (IntrospectionException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Failed to map setter method for property named 'readonly'", ex);
			}
		}

		try {
			proplist.add(new PropertyDescriptor("codestable", CTSelectTag.class, null, "setCodestable"));
		} catch (IntrospectionException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Failed to map setter method for property named 'codestable'", ex);
			}
		}

		PropertyDescriptor[] result = new PropertyDescriptor[proplist.size()];
		return ((PropertyDescriptor[]) proplist.toArray(result));
	}
}
