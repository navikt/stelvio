package no.trygdeetaten.common.framework.report.design.chart;

import no.trygdeetaten.common.framework.report.design.ChartValue;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $
 */
public class XYChartValue extends ChartValue {
    private String series;
    private double secondValue;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSecondValue() {
        return secondValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param secondValue DOCUMENT ME!
     */
    public void setSecondValue(double secondValue) {
        this.secondValue = secondValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSeries() {
        return series;
    }

    /**
     * DOCUMENT ME!
     *
     * @param series DOCUMENT ME!
     */
    public void setSeries(String series) {
        this.series = series;
    }
}
