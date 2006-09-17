package no.stelvio.common.framework.report.design;

import java.awt.Image;


/**
 * DOCUMENT ME!
 *
 * @author CHANGE THIS
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public interface ChartImageFactory {
    /**
     * DOCUMENT ME!
     *
     * @param reportChart DOCUMENT ME!
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image create(no.stelvio.common.framework.report.config.ReportChart reportChart, ChartValue[] values);
}
