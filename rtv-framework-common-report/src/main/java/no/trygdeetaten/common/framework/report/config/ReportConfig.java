package no.trygdeetaten.common.framework.report.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import no.trygdeetaten.common.framework.report.util.FileUtils;


/**
 * DOCUMENT ME!
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 2398 $, $Date: 2005-07-05 13:24:31 +0200 (Tue, 05 Jul 2005) $
*/
public class ReportConfig {
    private static final Log log = LogFactory.getLog(ReportConfig.class);
    private static final String DEFAULT_RESOURCEFILE = "report-config.xml";
    private static String resourceFile;
    private static Map reportTypes = new HashMap();
    private static String imagesURI;
    private static String classpath;
    private static String reportsPath;
    private static boolean keepJavaFile;

    // Singleton instance
    private static ReportConfig instance = new ReportConfig();

    /**
     * Creates a new ReportConfig object.
     */
    private ReportConfig() {
        this(DEFAULT_RESOURCEFILE);
    }

    /**
     * Creates a new ReportConfig object.
     *
     * @param resFile DOCUMENT ME!
     */
    private ReportConfig(String resFile) {
        resourceFile = resFile;

        try {
            build();
        } catch (IOException e) {
            log.error(e);
        } catch (SAXException e) {
            log.error(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ReportConfig getInstance() {
        return instance;
    }

    /**
     * DOCUMENT ME!
     */
    public static void compileReports() {
        List reportList = getAllReports();
        String reportBasePath = reportsPath;
        String reportJasperPath = reportBasePath + File.separatorChar + "jasper";
        String reportJavaPath = reportBasePath + File.separatorChar + "java";

        System.setProperty("jasper.reports.compile.keep.java.file", keepJavaFile ? "true" : "false");
        System.setProperty("jasper.reports.compile.temp", reportJavaPath);
        System.setProperty("jasper.reports.compile.class.path", classpath);

        if (log.isInfoEnabled()) {
            log.info("jasper.reports.compile.class.path: '" + classpath + "'");
            log.info("jasper.reports.compile.temp: '" + reportJavaPath + "'");
            log.info("jasper.reports.compile.keep.java.file: '" + keepJavaFile + "'");
        }

        // Create jasper dir if necessary
        File jasperDir = new File(reportJasperPath);
        jasperDir.delete();
        jasperDir.mkdir();

        // Create java dir if necessary
        File javaDir = new File(reportJavaPath);
        javaDir.delete();
        javaDir.mkdir();

        int numReports = reportList.size();

        Map reportFilesToCompile = new HashMap();
        Iterator reportDesignIterator = null;
        Map.Entry entry = null;
        String designName = null;
        String designFilename = null;
        Report report = null;

        for (int i = 0; i < numReports; i++) {
            report = (Report) reportList.get(i);

            reportDesignIterator = report.getDesigns().entrySet().iterator();

            String reportJasperFile;

            while (reportDesignIterator.hasNext()) {
                entry = (Map.Entry) reportDesignIterator.next();
                designName = (String) entry.getKey();
                designFilename = (String) entry.getValue();

                reportJasperFile = reportJasperPath + File.separatorChar + FileUtils.changeFileExtention((new File(designFilename)).getName(), "jasper");

                // Collect unique files to compile
                reportFilesToCompile.put(reportBasePath + designFilename, reportJasperFile);

                report.setCompiledDesignFile(designName, reportJasperFile);
            }
        }

        String sourceFileName;
        String destFileName;

        for (Iterator iter = reportFilesToCompile.entrySet().iterator(); iter.hasNext();) {
            entry = (Map.Entry) iter.next();
            sourceFileName = (String) entry.getKey();
            destFileName = (String) entry.getValue();

            try {
                System.out.print("Compiling design " + sourceFileName + "... ");

                JasperCompileManager.compileReportToFile(sourceFileName, destFileName);

                System.out.println("OK");
            } catch (JRException e) {
                System.out.println("FAILED");
                log.error(e);
            }
        }

        for (int i = 0; i < numReports; i++) {
            report = (Report) reportList.get(i);
            reportDesignIterator = report.getCompiledDesigns().entrySet().iterator();

            while (reportDesignIterator.hasNext()) {
                entry = (Map.Entry) reportDesignIterator.next();
                designName = (String) entry.getKey();
                designFilename = (String) entry.getValue();

                try {
                    report.setJRReport(designName, (JasperReport) JRLoader.loadObject(designFilename));
                } catch (JRException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException
     * @throws SAXException
     */
    private void build() throws IOException, SAXException {
        final Digester digester = new Digester();
        digester.setUseContextClassLoader(true);

        digester.push(this);

        // Configuration
        digester.addObjectCreate("report-config/report-group", ReportGroup.class.getName());
        digester.addSetProperties("report-config/report-group");

        digester.addObjectCreate("report-config/report-group/report", Report.class.getName());
        digester.addSetProperties("report-config/report-group/report");

        digester.addCallMethod("report-config/report-group/report/description", "setReportDescription", 0);

        digester.addCallMethod("report-config/report-group/report/report-designs/design", "addDesign", 2, new String[] {
                String.class.getName(), String.class.getName()
            });
        digester.addCallParam("report-config/report-group/report/report-designs/design", 0, "name");
        digester.addCallParam("report-config/report-group/report/report-designs/design", 1, "file");

        digester.addCallMethod("report-config/report-group/report/images/image", "addImage", 2, new String[] {
                String.class.getName(), String.class.getName()
            });
        digester.addCallParam("report-config/report-group/report/images/image", 0, "name");
        digester.addCallParam("report-config/report-group/report/images/image", 1, "file");

        digester.addCallMethod("report-config/report-group/report/parameters/add", "addParameter", 2, new String[] {
                String.class.getName(), String.class.getName()
            });
        digester.addCallParam("report-config/report-group/report/parameters/add", 0, "name");
        digester.addCallParam("report-config/report-group/report/parameters/add", 1, "value");

        digester.addObjectCreate("report-config/report-group/report/report-chart", ReportChart.class.getName());
        digester.addSetProperties("report-config/report-group/report/report-chart");
        digester.addCallMethod("report-config/report-group/report/report-chart/description", "setDescription", 0);
        digester.addCallMethod("report-config/report-group/report/report-chart/xaxislabel", "setXAxisLabel", 0);
        digester.addCallMethod("report-config/report-group/report/report-chart/yaxislabel", "setYAxisLabel", 0);

        digester.addSetNext("report-config/report-group/report/report-chart", "setReportChart");

        digester.addCallMethod("report-config/report-group/report/sortable-fields/field", "addSortableField", 2, new String[] {
                String.class.getName(), String.class.getName()
            });
        digester.addCallParam("report-config/report-group/report/sortable-fields/field", 0, "name");
        digester.addCallParam("report-config/report-group/report/sortable-fields/field", 1, "label");

        digester.addCallMethod("report-config/report-group/report/disabled-fields/field", "addDisabledField", 0);

        digester.addSetNext("report-config/report-group/report", "addReport");
        digester.addSetNext("report-config/report-group", "addReportGroup");

        digester.parse(Thread.currentThread().getContextClassLoader().getResource(resourceFile).toString());

        log.info(resourceFile + " loaded sucessfully");
    }

    /**
     * DOCUMENT ME!
     *
     * @param reportGroup DOCUMENT ME!
     */
    public void addReportGroup(ReportGroup reportGroup) {
        if (reportTypes.get(reportGroup.getType()) == null) {
            reportTypes.put(reportGroup.getType(), new HashMap());
        }

        ((HashMap) reportTypes.get(reportGroup.getType())).put(reportGroup.getName(), reportGroup);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ReportGroup getReportGroup(String name, String type) {
        return (ReportGroup) ((Map) reportTypes.get(type)).get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Collection getReportGroups(String type) {
        return ((Map) reportTypes.get(type)).values();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Set getReportTypes() {
        return reportTypes.keySet();
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param reportId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Report getReport(String type, String group, int reportId) {
        return ((ReportGroup) (((HashMap) reportTypes.get(type)).get(group))).getReport(reportId);
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param reportName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Report getReport(String type, String group, String reportName) {
        List reportList = getReports(type, group);
        Report report = null;

        for (Iterator iter = reportList.iterator(); iter.hasNext();) {
            report = (Report) iter.next();

            if (report.getName().equals(reportName)) {
                break;
            }
        }

        return report;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static List getReports(String type) {
        Collection groups = ((HashMap) reportTypes.get(type)).values();
        int numGroups = groups.size();
        List result = new ArrayList();
        ReportGroup group = null;
        Iterator groupIterator = groups.iterator();

        for (int i = 0; i < numGroups; i++) {
            group = (ReportGroup) groupIterator.next();
            result.addAll(group.getReports());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     * @param group DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static List getReports(String type, String group) {
        return ((ReportGroup) ((HashMap) reportTypes.get(type)).get(group)).getReports();
    }       

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static List getAllReports() {
        List allReports = new ArrayList();

        Iterator typeIterator = reportTypes.values().iterator();

        while (typeIterator.hasNext()) {
            Collection groups = ((HashMap) typeIterator.next()).values();
            Iterator groupIterator = groups.iterator();

            while (groupIterator.hasNext()) {
                ReportGroup group = (ReportGroup) groupIterator.next();
                allReports.addAll(group.getReports());
            }
        }

        return allReports;
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public static String getImagesURI() {
        return imagesURI;
    }

    /**
     * INSERT COMMENT
     *
     * @param string
     */
    public static void setImagesURI(String string) {
        imagesURI = string;
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public static String getClasspath() {
        return classpath;
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public static boolean isKeepJavaFile() {
        return keepJavaFile;
    }

    /**
     * INSERT COMMENT
     *
     * @return
     */
    public static String getReportsPath() {
        return reportsPath;
    }

    /**
     * INSERT COMMENT
     *
     * @param string
     */
    public static void setClasspath(String string) {
        classpath = string;
    }

    /**
     * INSERT COMMENT
     *
     * @param b
     */
    public static void setKeepJavaFile(boolean b) {
        keepJavaFile = b;
    }

    /**
     * INSERT COMMENT
     *
     * @param string
     */
    public static void setReportsPath(String string) {
        reportsPath = string;
    }
}
