package no.nav.common.framework.report.design.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import no.nav.common.framework.report.config.ReportChart;
import no.nav.common.framework.report.design.ChartValue;
import no.nav.common.framework.report.design.CommonDataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author CHANGE THIS
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class BarChartFactory extends AbstractChartFactory {
    /**
     * DOCUMENT ME!
     *
     * @param reportChart
     * @param values
     *
     * @return
     */
    protected JFreeChart createChart(ReportChart reportChart, ChartValue[] values) {
        return ChartFactory.createBarChart3D(reportChart.getDescription(), reportChart.getXAxisLabel(), reportChart.getYAxisLabel(), (CategoryDataset) CommonDataSetFactory.create(values, reportChart.getTypeAsReportChartType()), PlotOrientation.VERTICAL, true, true, false);
    }
}
