package no.stelvio.common.framework.report.design;

import java.awt.Image;

import no.stelvio.common.framework.report.config.ReportChart;
import no.stelvio.common.framework.report.config.ReportChartType;
import no.stelvio.common.framework.report.design.chart.BarChartFactory;
import no.stelvio.common.framework.report.design.chart.PieChartFactory;
import no.stelvio.common.framework.report.design.chart.TimeChartFactory;
import no.stelvio.common.framework.report.design.chart.XYChartFactory;


/**
 * DOCUMENT ME!
 *
 * @author CHANGE THIS
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class CommonChartImageFactory {
    private static final ChartImageFactory BAR_CHART_FACTORY = new BarChartFactory();
    private static final ChartImageFactory PIE_CHART_FACTORY = new PieChartFactory();
    private static final ChartImageFactory TIME_CHART_FACTORY = new TimeChartFactory();
    private static final ChartImageFactory XY_CHART_FACTORY = new XYChartFactory();

    private CommonChartImageFactory() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param reportChart DOCUMENT ME!
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static Image create(ReportChart reportChart, ChartValue[] values) {
        ChartImageFactory factory = null;
        ReportChartType chartType = reportChart.getTypeAsReportChartType();

        if (chartType == ReportChartType.BAR_CHART) {
            factory = BAR_CHART_FACTORY;
        } else if (chartType == ReportChartType.PIE_CHART) {
            factory = PIE_CHART_FACTORY;
        } else if (chartType == ReportChartType.TIME_CHART) {
            factory = TIME_CHART_FACTORY;
        } else if (chartType == ReportChartType.XY_CHART) {
            factory = XY_CHART_FACTORY;
        }

        if (factory == null) {
            throw new IllegalArgumentException("Chart type " + chartType + " is not supported");
        }

        return factory.create(reportChart, values);
    }
}
