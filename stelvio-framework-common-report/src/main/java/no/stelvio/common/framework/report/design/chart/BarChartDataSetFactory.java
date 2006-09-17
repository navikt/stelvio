package no.stelvio.common.framework.report.design.chart;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import no.stelvio.common.framework.report.design.ChartValue;
import no.stelvio.common.framework.report.design.DataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author CHANGE THIS
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class BarChartDataSetFactory implements DataSetFactory {
    /**
     * DOCUMENT ME!
     *
     * @param values
     *
     * @return
     */
    public Dataset create(ChartValue[] values) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < values.length; i++) {
            CategoryChartValue value = (CategoryChartValue) values[i];
            dataset.addValue(value.getValue(), value.getSeries(), value.getCategory());
        }

        return dataset;
    }
}
