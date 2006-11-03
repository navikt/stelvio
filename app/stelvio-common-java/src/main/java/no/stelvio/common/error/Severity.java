package no.stelvio.common.error;

/**
 * Error severity constants. The severity levels reflect 
 * the most severe log levels used in Jakarta Commons Logging where 
 * <code>FATAL</code> has highest severity and 
 * <code>WARN</code> the lowest, se the ordered list below:
 * <ol>
 * 		<li> <code> FATAL </code> </li>
 * 		<li> <code> ERROR </code> </li>
 * 		<li> <code> WARN </code> </li>
 * </ol>
 * Another way to se this is how the severity increases from 
 * left to right: 
 * <p/>
 * <code>WARN &lt; ERROR &lt; FATAL</code>
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: Severity.java 1954 2005-02-08 13:35:42Z psa2920 $
 * @todo should be an Enum
 */
public final class Severity {

	/** Constant representing severity of type FATAL. Constant value is 5. */
	public static final Integer FATAL = new Integer(5);

	/** Constant representing severity of type ERROR. Constant value is 4. */
	public static final Integer ERROR = new Integer(4);

	/** Constant representing severity of type WARN. Constant value is 3. */
	public static final Integer WARN = new Integer(3);
}
