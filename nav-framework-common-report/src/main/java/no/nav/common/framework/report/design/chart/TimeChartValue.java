package no.nav.common.framework.report.design.chart;

import java.util.Date;

import no.nav.common.framework.report.design.ChartValue;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $
 */
public class TimeChartValue extends ChartValue {
    private String series;
    private Date time;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTime(Date time) {
        this.time = time;
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
