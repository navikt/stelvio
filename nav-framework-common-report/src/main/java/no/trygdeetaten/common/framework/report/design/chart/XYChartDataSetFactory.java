package no.trygdeetaten.common.framework.report.design.chart;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import no.trygdeetaten.common.framework.report.design.ChartValue;
import no.trygdeetaten.common.framework.report.design.DataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class XYChartDataSetFactory implements DataSetFactory {
    /**
     * DOCUMENT ME!
     *
     * @param values
     *
     * @return
     */
    public Dataset create(ChartValue[] values) {
        XYSeries series = null;
        XYSeriesCollection seriesCollection = new XYSeriesCollection();

        for (int i = 0; i < values.length; i++) {
            XYChartValue value = (XYChartValue) values[i];

            if ((series == null) || !series.getName().equals(value.getSeries())) {
                if (series != null) {
                    seriesCollection.addSeries(series);
                }

                series = new XYSeries(value.getSeries());
            }

            series.add(value.getValue(), value.getSecondValue());
        }

        seriesCollection.addSeries(series);

        return seriesCollection;
    }
}
