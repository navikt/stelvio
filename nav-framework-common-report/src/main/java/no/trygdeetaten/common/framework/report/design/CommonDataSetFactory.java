package no.trygdeetaten.common.framework.report.design;

import org.jfree.data.general.Dataset;

import no.trygdeetaten.common.framework.report.config.ReportChartType;
import no.trygdeetaten.common.framework.report.design.chart.BarChartDataSetFactory;
import no.trygdeetaten.common.framework.report.design.chart.PieChartDataSetFactory;
import no.trygdeetaten.common.framework.report.design.chart.TimeChartDataSetFactory;
import no.trygdeetaten.common.framework.report.design.chart.XYChartDataSetFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class CommonDataSetFactory {
    private static final DataSetFactory BAR_DATASET_FACTORY = new BarChartDataSetFactory();
    private static final DataSetFactory PIE_DATASET_FACTORY = new PieChartDataSetFactory();
    private static final DataSetFactory TIME_DATASET_FACTORY = new TimeChartDataSetFactory();
    private static final DataSetFactory XY_DATASET_FACTORY = new XYChartDataSetFactory();

    private CommonDataSetFactory() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     * @param chartType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static Dataset create(ChartValue[] values, ReportChartType chartType) {
        DataSetFactory factory = null;

        if (chartType == ReportChartType.BAR_CHART) {
            factory = BAR_DATASET_FACTORY;
        } else if (chartType == ReportChartType.PIE_CHART) {
            factory = PIE_DATASET_FACTORY;
        } else if (chartType == ReportChartType.TIME_CHART) {
            factory = TIME_DATASET_FACTORY;
        } else if (chartType == ReportChartType.XY_CHART) {
            factory = XY_DATASET_FACTORY;
        }

        if (factory == null) {
            throw new IllegalArgumentException("Chart type " + chartType + " is not supported");
        }

        return factory.create(values);
    }
}
