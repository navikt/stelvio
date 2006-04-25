package no.trygdeetaten.common.framework.report.design;

import java.util.Iterator;
import java.util.Set;

import net.sf.jasperreports.engine.JRDefaultScriptlet;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class JRBaseScriptlet extends JRDefaultScriptlet {
    /**
     * DOCUMENT ME!
     *
     * @param set DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(Set set) {
        StringBuffer result = new StringBuffer();
        Iterator setIterator = set.iterator();
        String value;

        for (int i = 0; setIterator.hasNext(); i++) {
            value = String.valueOf(setIterator.next());

            if (i > 0) {
                result.append(", ");
            }

            result.append(value);
        }

        return result.toString();
    }
}
