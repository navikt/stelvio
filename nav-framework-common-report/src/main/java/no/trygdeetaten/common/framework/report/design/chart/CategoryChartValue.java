package no.trygdeetaten.common.framework.report.design.chart;

import no.trygdeetaten.common.framework.report.design.ChartValue;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $
 */
public class CategoryChartValue extends ChartValue {
    private String series;
    private String category;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCategory() {
        return category;
    }

    /**
     * DOCUMENT ME!
     *
     * @param category DOCUMENT ME!
     */
    public void setCategory(String category) {
        this.category = category;
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
