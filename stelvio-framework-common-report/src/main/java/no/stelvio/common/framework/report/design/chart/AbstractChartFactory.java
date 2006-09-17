package no.stelvio.common.framework.report.design.chart;

import java.awt.Image;

import org.jfree.chart.JFreeChart;

import no.stelvio.common.framework.report.config.ReportChart;
import no.stelvio.common.framework.report.design.ChartImageFactory;
import no.stelvio.common.framework.report.design.ChartValue;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $
 */
public abstract class AbstractChartFactory implements ChartImageFactory {
    /**
     * DOCUMENT ME!
     *
     * @param reportChart DOCUMENT ME!
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image create(ReportChart reportChart, ChartValue[] values) {
        JFreeChart chart = chart = createChart(reportChart, values);

        return chart.createBufferedImage(reportChart.getWidthAsInt(), reportChart.getHeightAsInt());
    }

    protected abstract JFreeChart createChart(ReportChart reportChart, ChartValue[] values);
}
