package no.stelvio.common.error.support;

/**
 * Error severity enumeration. The severity levels reflect 
 * the most severe log levels used in Jakarta Commons Logging where 
 * <code>FATAL</code> has highest severity and 
 * <code>INFO</code> the lowest, se the ordered list below:
 * <ol>
 * 		<li> <code> FATAL </code> </li>
 * 		<li> <code> ERROR </code> </li>
 * 		<li> <code> WARN </code> </li>
 * 		<li> <code> INFO </code> </li>
 * </ol>
 * Another way to se this is how the severity increases from 
 * left to right: 
 * <p/>
 * <code>INFO &lt; WARN &lt; ERROR &lt; FATAL</code>
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: Severity.java 1954 2005-02-08 13:35:42Z psa2920 $
 */
public enum Severity {
    FATAL(5),
    ERROR(4),
    WARN(3),
    INFO(2);

    private int level;

    Severity(int level) {
        this.level = level;
    }

    public boolean isMoreFatalThan(Severity other) {
        return level > other.level;
    }
}
