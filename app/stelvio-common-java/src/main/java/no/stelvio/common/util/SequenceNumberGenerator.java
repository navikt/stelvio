package no.stelvio.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * SequenceNumberGenerator is a utility class for generating unique ids in a sequence.
 * <p/>
 * The sequence number is most likely to be unique across all class loaders in a cluster
 * and sequential per class loader.
 * <p/>
 * The sequence number is made up of 3 parts:
 * <ul>
 *    <li> a hashcode of the local host address</li>
 *    <li> a hascode of an anonymous object </li>
 *    <li> a seed based on the current system time </li>
 * </ul>
 * 
 * @author person7553f5959484
 * @version $Revision: 916 $ $Author: psa2920 $ $Date: 2004-07-14 08:24:41 +0200 (Wed, 14 Jul 2004) $
 */
public final class SequenceNumberGenerator {

	private static final Object LOCK = new Object();

	// Hash code of of the local host
	private static int localHostHash;

	// Hash code of an object created in the local JVM
	private static int identityHash;

	// Seed
	private static long seed = System.currentTimeMillis();

	// Do the heavy computing once ;)
	static {
		try {
			// The server adress as byte array
			localHostHash = InetAddress.getLocalHost().hashCode();
		} catch (UnknownHostException uhe) {
			// This should never happen, but anyway
			localHostHash = ("localhost").hashCode();
		}
		identityHash = System.identityHashCode(new Object());
	}

	/**
	 * Get the next unique id in current sequence.
	 * 
	 * @return the next sequence number
	 */
	public static long getNextId() {
		synchronized (LOCK) {
			++seed;
		}
		return localHostHash + identityHash + seed;
	}

	/**
	 * Get the next unique id in current sequence for current sub system.
	 * 
	 * @param	subsystem name of current sub system
	 * @return 	the next sequence number
	 */
	public static long getNextId(String subsystem) {
		if (null == subsystem) {
			return getNextId();
		} else {
			synchronized (LOCK) {
				++seed;
			}
			return localHostHash + identityHash + seed + subsystem.hashCode();
		}
	}

	/**
	 * Enforcing non instabillity using a private constructor.
	 */
	private SequenceNumberGenerator() {
		super();
	}
}
