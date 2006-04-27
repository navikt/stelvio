package no.nav.common.framework.report.design;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $
 */
public class JRArrayMapDataSource implements JRDataSource {
    private Map records;
    private Map.Entry currentRecord;
    private Iterator iterator;

    /**
     * Creates a new JRArrayMapDataSource object.
     *
     * @param map DOCUMENT ME!
     */
    public JRArrayMapDataSource(Map map) {
        records = map;

        if (records != null) {
            iterator = records.entrySet().iterator();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JRException
     */
    public boolean next() throws JRException {
        boolean hasNext = false;

        if (iterator != null) {
            hasNext = iterator.hasNext();

            if (hasNext) {
                currentRecord = (Map.Entry) iterator.next();
            }
        }

        return hasNext;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JRException
     */
    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;

        if (currentRecord != null) {
            String fieldName = field.getName();

            if (fieldName.equals("KEY")) {
                value = currentRecord.getKey();
            } else {
                int fieldNum = Integer.valueOf(field.getName()).intValue();
                value = ((BigDecimal[]) currentRecord.getValue())[fieldNum - 1];
            }
        }

        return value;
    }
}
