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
public enum Severity {
    FATAL(5),
    ERROR(4),
    WARN(3);

    private int level;

    Severity(int level) {
        this.level = level;
    }

    public boolean isMoreFatalThan(Severity other) {
        return level > other.level;
    }
}
