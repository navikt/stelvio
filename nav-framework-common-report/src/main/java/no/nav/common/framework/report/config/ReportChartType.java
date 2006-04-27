package no.nav.common.framework.report.config;

/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 2860 $, $Date: 2006-04-25 13:14:40 +0200 (Tue, 25 Apr 2006) $
 */
public class ReportChartType {
    /** Describe me! */
    public static final ReportChartType BAR_CHART = new ReportChartType("Bar");

    /** Describe me! */
    public static final ReportChartType PIE_CHART = new ReportChartType("Pie");

    /** Describe me! */
    public static final ReportChartType XY_CHART = new ReportChartType("XY");

    /** Describe me! */
    public static final ReportChartType TIME_CHART = new ReportChartType("Time");

    private ReportChartType(String type) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param strType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static ReportChartType findType(String strType) {
        if (strType.equals("Bar")) {
            return BAR_CHART;
        } else if (strType.equals("Pie")) {
            return PIE_CHART;
        } else if (strType.equals("XY")) {
            return XY_CHART;
        } else if (strType.equals("Time")) {
            return TIME_CHART;
        } else {
            throw new IllegalArgumentException("Type not supported");
        }
    }
}
