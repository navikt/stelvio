package no.trygdeetaten.common.framework.report.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 2494 $, $Date: 2005-09-23 10:52:47 +0200 (Fri, 23 Sep 2005) $
*/
public class Report {
    private static DynaClass dynaClass;
    private static final transient Log log = LogFactory.getLog(Report.class);

    static {
        dynaClass = new BasicDynaClass("sortableFieldBean", null, new DynaProperty[] {
                    new DynaProperty("name", String.class), new DynaProperty("label", String.class)
                });
    }

    /** Describe me! */
    public static final String ROOTDESIGN_NAME = "ROOT";
    private String name;
    private String query;
    private ReportChart reportChart;
    private String reportDescription;
    private int reportId;
    private boolean datasourceAsParameter = false;
    private boolean showEmptyReport = false;
    private List sortableFields = new ArrayList();
    private Map designs = new HashMap();
    private Map images = new HashMap();
    private Map compiledDesigns = new HashMap();
    private List disabledFields = new ArrayList();
    private Map parameters = new HashMap();
    private Map jRReportMap = new HashMap();
    private String[] roles;

    /**
     * Creates a new Report object.
     */
    public Report() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param file DOCUMENT ME!
     */
    public void addDesign(String name, String file) {
        this.designs.put(name, file);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDesign(String name) {
        return (String) designs.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getDesigns() {
        return this.designs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void addDisabledField(String name) {
        this.disabledFields.add(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getDisabledFileds() {
        return this.disabledFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void addParameter(String name, String value) {
        this.parameters.put(name, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getParameters() {
        return this.parameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param file DOCUMENT ME!
     */
    public void addImage(String name, String file) {
        this.images.put(name, file);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getImage(String name) {
        return (String) this.images.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getImages() {
        return this.images;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRootDesign() {
        return (String) designs.get(ROOTDESIGN_NAME);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRootCompiledDesign() {
        return (String) this.compiledDesigns.get(ROOTDESIGN_NAME);
    }

    /**
     * DOCUMENT ME!
     *
     * @param designName DOCUMENT ME!
     * @param compFilename DOCUMENT ME!
     */
    public void setCompiledDesignFile(String designName, String compFilename) {
        this.compiledDesigns.put(designName, compFilename);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getCompiledDesigns() {
        return this.compiledDesigns;
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
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param label DOCUMENT ME!
     */
    public void addSortableField(String name, String label) {
        try {
            DynaBean bean = dynaClass.newInstance();
            bean.set("name", name);
            bean.set("label", label);

            this.sortableFields.add(bean);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InstantiationException e) {
            log.error(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getSortableFields() {
        return this.sortableFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getReportDescription() {
        return reportDescription;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setReportDescription(String string) {
        reportDescription = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getReportId() {
        return this.reportId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id
     */
    public void setReportId(int id) {
        this.reportId = id;
    }

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
     * @param string
     */
    public void setQuery(String string) {
        query = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public ReportChart getReportChart() {
        return reportChart;
    }

    /**
     * DOCUMENT ME!
     *
     * @param chart
     */
    public void setReportChart(ReportChart chart) {
        reportChart = chart;
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public boolean getDatasourceAsParameterBoolean() {
        return datasourceAsParameter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDatasourceAsParameter() {
        return String.valueOf(this.datasourceAsParameter);
    }

    /**
     * INSERT COMMENT
     *
     * @param b
     */
    public void setDatasourceAsParameter(String b) {
        this.datasourceAsParameter = Boolean.valueOf(b).booleanValue();
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public Map getJRReportMap() {
        return jRReportMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param designName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JasperReport getJRReport(String designName) {
        return (JasperReport) jRReportMap.get(designName);
    }

    /**
     * INSERT COMMENT
     *
     * @param designName DOCUMENT ME!
     * @param report
     */
    public void setJRReport(String designName, JasperReport report) {
        jRReportMap.put(designName, report);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String[] getRolesAsArr() {
        return roles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRoles() {
        return roles.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setRoles(String string) {
        roles = StringUtils.split(string, ',');
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public boolean getShowEmptyReportAsBoolean() {
        return this.showEmptyReport;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getShowEmptyReport() {
        return String.valueOf(this.showEmptyReport);
    }

    /**
     * INSERT COMMENT
     *
     * @param b
     */
    public void setShowEmptyReport(String b) {
        this.showEmptyReport = Boolean.valueOf(b).booleanValue();
    }
}
