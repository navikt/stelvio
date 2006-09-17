package no.stelvio.common.framework.report.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 1595 $, $Date: 2004-11-25 15:14:24 +0100 (Thu, 25 Nov 2004) $
 */
public class ReportGroup {
    private String name;
    private String type;
    private HashMap reportsMap = new HashMap();
    private List reportsList = new ArrayList();
    private int numReports = 0;

    /**
     * Creates a new ReportGroup object.
     */
    public ReportGroup() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param report DOCUMENT ME!
     */
    public void addReport(Report report) {
        numReports++;
        report.setReportId(numReports);
        this.reportsMap.put(new Integer(report.getReportId()), report);
        this.reportsList.add(this.reportsMap.get(new Integer(report.getReportId())));
    }

    /**
     * DOCUMENT ME!
     *
     * @param reportId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Report getReport(int reportId) {
        return (Report) this.reportsMap.get(new Integer(reportId));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getReports() {
        return this.reportsList;
    }
}
