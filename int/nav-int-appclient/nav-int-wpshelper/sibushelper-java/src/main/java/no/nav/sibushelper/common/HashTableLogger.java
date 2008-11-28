/**
 * 
 */
package no.nav.sibushelper.common;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.nav.sibushelper.SIBUSHelper;

/**
 * @author persona2c5e3b49756 Schnell
 *
 */
public class HashTableLogger {
	
	private static Logger logger = Logger.getLogger(SIBUSHelper.class.getName());
	private String className = HashTableLogger.class.getName();
	
	private Hashtable ht = null;

	 /**
	 * @param ht
	 */
	public HashTableLogger(Hashtable<Object,Object> ht)
	 {
		 if (!ht.isEmpty()) 
		 {
			this.ht = ht;
			logger.logp(Level.INFO, className, "list()", "Size="+ ht.size());
		 }
		 else
		 {
			 logger.logp(Level.WARNING, className, "constructor()", "Hashtable is empty! Don't log content!");
		 }
	 }

	 /**
	 * 
	 */
	public void list()
	 {
		 for (Enumeration e=ht.keys(); e.hasMoreElements();)
		 {
			 String name = (String) e.nextElement();
			 logger.logp(Level.INFO, className, "list()", "HashtableInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + "name=" + name + ", " + "value=" + ht.get(name)  + "}");
		 
		 }
	 }
	
	
}
