package no.nav.common.framework.report.design.chart;

import org.jfree.data.general.Dataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import no.nav.common.framework.report.design.ChartValue;
import no.nav.common.framework.report.design.DataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class TimeChartDataSetFactory implements DataSetFactory {
    /**
     * DOCUMENT ME!
     *
     * @param values
     *
     * @return
     */
    public Dataset create(ChartValue[] values) {
        TimeSeries series = null;
        TimeSeriesCollection seriesCollection = new TimeSeriesCollection();

        for (int i = 0; i < values.length; i++) {
            TimeChartValue value = (TimeChartValue) values[i];

            if ((series == null) || !series.getName().equals(value.getSeries())) {
                if (series != null) {
                    seriesCollection.addSeries(series);
                }

                series = new TimeSeries(value.getSeries(), Second.class);
            }

            series.add(new Second(value.getTime()), value.getValue());
        }

        seriesCollection.addSeries(series);

        return seriesCollection;
    }
}
