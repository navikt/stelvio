package no.stelvio.common.framework.report.design;

import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import org.apache.commons.beanutils.DynaBean;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class JRDynaBeanCollectionDataSource implements JRRewindableDataSource {
    private Collection data = null;
    private Iterator iterator = null;
    private DynaBean currentBean = null;

    /**
     * Creates a new JRDynaBeanCollectionDataSource object.
     *
     * @param beanCollection DOCUMENT ME!
     */
    public JRDynaBeanCollectionDataSource(Collection beanCollection) {
        this.data = beanCollection;

        if (this.data != null) {
            this.iterator = this.data.iterator();
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

        if (this.iterator != null) {
            hasNext = this.iterator.hasNext();

            if (hasNext) {
                this.currentBean = (DynaBean) this.iterator.next();
            }
        }

        return hasNext;
    }

    /**
     * DOCUMENT ME!
     *
     * @param jrField DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JRException
     */
    public Object getFieldValue(JRField jrField) throws JRException {
        Object value = null;

        if (currentBean != null) {
            value = currentBean.get(jrField.getName());
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws JRException
     */
    public void moveFirst() throws JRException {
        if (this.data != null) {
            this.iterator = this.data.iterator();
        }
    }
}
