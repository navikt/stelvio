package no.stelvio.common.framework.report.design.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

import no.stelvio.common.framework.report.config.ReportChart;
import no.stelvio.common.framework.report.design.ChartValue;
import no.stelvio.common.framework.report.design.CommonDataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class XYChartFactory extends AbstractChartFactory {
    /**
     * DOCUMENT ME!
     *
     * @param reportChart
     * @param values
     *
     * @return
     */
    protected JFreeChart createChart(ReportChart reportChart, ChartValue[] values) {
        JFreeChart chart = ChartFactory.createXYLineChart(reportChart.getDescription(), reportChart.getXAxisLabel(), reportChart.getYAxisLabel(), (XYDataset) CommonDataSetFactory.create(values, reportChart.getTypeAsReportChartType()), PlotOrientation.VERTICAL, true, true, false);

        return chart;
    }
}
