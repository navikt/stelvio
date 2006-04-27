package no.nav.common.framework.context;

import java.util.Hashtable;

/**
 * <code>ThreadLocalMap</code> extends {@link InheritableThreadLocal}
 * to bequeath a copy of the AbstractContext of the parent
 * thread.
 *
 * @author person7553f5959484
 * @version $Revision: 123 $ $Date: 2004-05-06 20:09:25 +0200 (Thu, 06 May 2004) $
 */
class ThreadLocalMap extends InheritableThreadLocal {

	/**
	 * Computes the child's initial value for this inheritable 
	 * thread-local variable as a function of the parent's value 
	 * at the time the child thread is created. 
	 * <p/>
	 * This method is called from within the parent thread 
	 * before the child is started. This method merely returns 
	 * its input argument, and should be overridden if a different 
	 * behavior is desired.
	 * 
	 * @param parentValue the parent thread's value 
	 * @return the child thread's initial value
	 */
	protected Object childValue(Object parentValue) {
		Hashtable ht = (Hashtable) parentValue;
		if (ht != null) {
			return ht.clone();
		} else {
			return null;
		}
	}
}