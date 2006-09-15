package no.nav.common.framework.report.config;

/**
 * DOCUMENT ME!
 *
 * @author CHANGE THIS
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class ReportChart {
    private ReportChartType type;
    private String query;
    private String width;
    private String height;
    private String description;
    private String xAxisLabel;
    private String yAxisLabel;

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public ReportChartType getTypeAsReportChartType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setQuery(String string) {
        query = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type
     */
    public void setType(String type) {
        this.type = ReportChartType.findType(type);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeightAsInt() {
        return Integer.valueOf(this.height).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidthAsInt() {
        return Integer.valueOf(this.width).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setHeight(String string) {
        height = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setWidth(String string) {
        width = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getXAxisLabel() {
        return xAxisLabel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getYAxisLabel() {
        return yAxisLabel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setXAxisLabel(String string) {
        xAxisLabel = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setYAxisLabel(String string) {
        yAxisLabel = string;
    }
}
