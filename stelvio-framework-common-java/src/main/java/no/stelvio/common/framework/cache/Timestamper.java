package no.stelvio.common.framework.cache;

/**
 * Generates increasing identifiers (in a single VM only).
 * Not valid across multiple VMs. Identifiers are not necessarily
 * strictly increasing, but usually are.
 * 
 * <p/>
 * 
 * This class is a replica of {@link net.sf.hibernate.cache.Timestamper} 
 * from Hibernate 2.1.6.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: Timestamper.java 2145 2005-03-22 10:24:03Z skb2930 $
 */
public final class Timestamper {

	private static short counter = 0;
	private static long time;
	private static final int BIN_DIGITS = 12;
	
	/** Representation of one millisecond. */
	public static final short ONE_MS = 1 << BIN_DIGITS;

	/**
	 * Returns the next timestamp.
	 * 
	 * @return timestamp in milliseconds.
	 */
	public static long next() {
		
		synchronized (Timestamper.class) {
			long newTime = System.currentTimeMillis() << BIN_DIGITS;

			if (time < newTime) {
				time = newTime;
				counter = 0;
			} else if (counter < ONE_MS - 1) {
				counter++;
			}
			
			return time + counter;
		}
	}

	/** This class is not to be instantiated. */
	private Timestamper() {
	}
}
