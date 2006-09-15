package no.nav.common.framework.report.design.chart;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

import no.nav.common.framework.report.design.ChartValue;
import no.nav.common.framework.report.design.DataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class PieChartDataSetFactory implements DataSetFactory {
    /**
     * DOCUMENT ME!
     *
     * @param values
     *
     * @return
     */
    public Dataset create(ChartValue[] values) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (int i = 0; i < values.length; i++) {
            PieChartValue value = (PieChartValue) values[i];
            dataset.setValue(value.getKey(), value.getValue());
        }

        return dataset;
    }
}
