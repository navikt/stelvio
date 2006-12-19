package no.stelvio.common.context.support;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

import no.stelvio.common.context.Context;

/**
 * Context implementation that uses the Log4j MDC class
 * for storing key-value pairs.
 * <p/>
 * See <a href="http://jakarta.apache.org/log4j/docs/api/org/apache/log4j/MDC.html">
 * http://jakarta.apache.org/log4j/docs/api/org/apache/log4j/MDC.html</a>
 * <p/>
 * This is the implementation of Context that must be used to output Context values 
 * to log4j produced logs. If log4j is on the classpath, this plugin will automatically be loaded.
 * <p/>
 * To configure what Context values should be logged, search for MDC in 
 * <a href="http://jakarta.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html">
 * http://jakarta.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html</a>
 * <p/>
 *
 * @author person7553f5959484
 * @version $Revision: 123 $ $Date: 2004-05-06 20:09:25 +0200 (Thu, 06 May 2004) $
 */
class Log4jContext implements Context {

	private static Log log = LogFactory.getLog(Log4jContext.class);

	/**
	 * Put a context value as identified with the key parameter 
	 * into the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @param value object to store
	 * 
	 * @see no.stelvio.common.context.Context#put(java.lang.String, java.lang.Object)
	 */
	public void put(String key, Object value) {
		if (null != key && null != value) {
			MDC.put(key, value);
		}
	}

	/**
	 * Get the context value identified by the key parameter 
	 * from the current thread's context.
	 * 
	 * @param key key to identify the value
	 * @return the value stored
	 * 
	 * @see no.stelvio.common.context.Context#get(java.lang.String)
	 */
	public Object get(String key) {
		return MDC.get(key);
	}

	/**
	 * Remove all of the context values.
	 * 
	 * @see no.stelvio.common.context.Context#remove()
	 */
	public void remove() {
		Hashtable ht = MDC.getContext();
		if (ht != null) {
			ht.clear();
		}
	}

	/**
	 * Import a new context
	 * 
	 * @param o the context to import
	 * @see no.stelvio.common.context.Context#importContext(java.lang.Object)
	 */
	public void importContext(Object o) {
		if (null == o) {
			// Do nothing
			return;
		} else if (o instanceof Hashtable) {
			// Casted external format to Hashtable,
			// Now, clear the existing context and
			// put all objects in the external hashtable 
			// into the existing context
			Hashtable context = (Hashtable) o;
			Hashtable ht = MDC.getContext();
			if (ht != null) {
				ht.clear();
				ht.putAll(context);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("MDC was null! Re-Initialise it now!");
				}
				Iterator i = context.entrySet().iterator();
				while (i.hasNext()) {
					Entry entry = (Entry) i.next();
					MDC.put((String) entry.getKey(), entry.getValue());
				}
			}
		} else {
			throw new ClassCastException(
				"Can not import object of type "
					+ o.getClass().getName()
					+ " into Context. Object must be of type "
					+ "java.util.Hashtable");
		}

	}

	/**
	 * Export the context
	 * 
	 * @return the exported context
	 * 
	 * @see no.stelvio.common.context.Context#exportContext()
	 */
	public Object exportContext() {
		Hashtable uc = MDC.getContext();
		if (uc == null) {
			return null;
		} else {
			return new Hashtable(uc);
		}
	}

}
