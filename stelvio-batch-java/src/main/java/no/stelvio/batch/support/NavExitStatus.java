package no.stelvio.batch.support;

import org.springframework.batch.core.ExitStatus;

/**
 * Simple wrapper to hold custom NAV {@link ExitStatus}'s.
 *
 */
public final class NavExitStatus {
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private NavExitStatus() {}

	/** The batch executed, but with warnings. */
	public static final ExitStatus WARNING = new ExitStatus("WARNING");
	
	/** The batch failed. */
	public static final ExitStatus ERROR = new ExitStatus("ERROR");
}
