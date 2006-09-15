package no.nav.common.framework.report.design.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

import no.nav.common.framework.report.config.ReportChart;
import no.nav.common.framework.report.design.ChartValue;
import no.nav.common.framework.report.design.CommonDataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class PieChartFactory extends AbstractChartFactory {
    /**
     * DOCUMENT ME!
     *
     * @param reportChart
     * @param values
     *
     * @return
     */
    protected JFreeChart createChart(ReportChart reportChart, ChartValue[] values) {
        JFreeChart chart = ChartFactory.createPieChart3D(reportChart.getDescription(), (PieDataset) CommonDataSetFactory.create(values, reportChart.getTypeAsReportChartType()), true, true, false);

        return chart;
    }
}
