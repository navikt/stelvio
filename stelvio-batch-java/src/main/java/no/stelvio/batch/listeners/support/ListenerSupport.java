package no.stelvio.batch.listeners.support;

/**
 * Utility class for Spring batch listeners
 * 
 *
 */
public final class ListenerSupport {

	/**
	 * Hidden constructor
	 */
	private ListenerSupport(){
	}
	
	/**
	 * Format a duration given in milliseconds as hours, minutes, seconds and milliseconds.
	 * 
	 * @param duration
	 *            the duration in milliseconds
	 * @return a formatted string representing the duration in human readable format
	 */
	public static String formatMillisecondsDurationAsHumanReadableString(long duration) {
		int seconds = (int) (duration / 1000) % 60;
		int minutes = (int) ((duration / 1000) % 3600) / 60;
		int hours = (int) (duration / 1000) / 3600;
		int milliseconds = (int) duration % 1000;
		
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));

		return sb.toString();
	}	
}