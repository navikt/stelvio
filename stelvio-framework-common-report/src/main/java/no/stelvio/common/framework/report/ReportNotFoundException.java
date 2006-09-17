package no.stelvio.common.framework.report;

/**
* Class comment goes here.
*
* <pre>
* Date $$Date: 2005-01-04 09:25:28 +0100 (Tue, 04 Jan 2005) $$
* CVS History:
* $$Log$
* $Revision 1.2  2005/01/04 08:20:26  ohb2930
* $*** empty log message ***
* $$
* </pre>
* @author $$Author: ohb2930 $$
* @version $$Revision: 1751 $$
*/
public class ReportNotFoundException extends Exception {
    /**
     * Creates a new ReportNotFoundException object.
     */
    public ReportNotFoundException() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param message
     */
    public ReportNotFoundException(String message) {
        super(message);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cause
     */
    public ReportNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message
     * @param cause
     */
    public ReportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
