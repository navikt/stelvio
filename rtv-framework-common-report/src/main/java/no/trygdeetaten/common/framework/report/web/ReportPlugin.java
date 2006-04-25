package no.trygdeetaten.common.framework.report.web;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import no.trygdeetaten.common.framework.report.config.ReportConfig;

/**
 * INSERT COMMENT
 * 
 * @author Ole-Kristian Hagen
 * @version $Revision: 2858 $, $Date: 2005-01-07 09:57:12 +0100 (fr, 07 jan
 *          2005) $
 */
public class ReportPlugin implements PlugIn {
	private static final Log log = LogFactory.getLog(ReportConfig.class);

	private static String contextPath;

	private static boolean keepJavaFile;

	private static String imagesURI;

	private static String reportsDirName;

	/**
	 * Creates a new ReportPlugin object.
	 */
	public ReportPlugin() {
		;
	}

	/**
	 * Cleanup
	 */
	public void destroy() {
		;
	}

	/**
	 * <p>
	 * Receive notification that the specified module is being started up.
	 * </p>
	 * 
	 * @param servlet
	 *            ActionServlet that is managing all the modules in this web
	 *            application
	 * @param config
	 *            ModuleConfig for the module with which this plug-in is
	 *            associated
	 * 
	 * @exception ServletException
	 *                if this <code>PlugIn</code> cannot be successfully
	 *                initialized
	 */
	public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
		contextPath = servlet.getServletContext().getRealPath("");
		compileReports();
	}

	/**
	 * DOCUMENT ME!
	 */
	public static void compileReports() {
		boolean compileReports = true;

		String classpath = getFullClassPath();

		if ((classpath == null) || (classpath.length() == 0)) {
			if (log.isWarnEnabled()) {
				log.warn("Plugin must have a classpath property to compile the reports. Reports will not be compiled.");
			}

			compileReports = false;
		}

		if ((imagesURI == null) || (imagesURI.length() == 0)) {
			imagesURI = "/images";

			if (log.isInfoEnabled()) {
				log.info("No 'imagesURI' property defined. Set property to default value " + imagesURI);
			}
		}

		if ((reportsDirName == null) || (reportsDirName.length() == 0)) {
			reportsDirName = contextPath + "reports";

			if (log.isInfoEnabled()) {
				log.info("No 'reportsDirName' property defined. Set property to default value " + reportsDirName);
			}
		}

		ReportConfig.setImagesURI(imagesURI);
		ReportConfig.setClasspath(classpath);
		ReportConfig.setKeepJavaFile(keepJavaFile);
		ReportConfig.setReportsPath(reportsDirName);

		if (compileReports) {
			ReportConfig.compileReports();
		}
	}

	private static String getFullClassPath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		boolean hasParent = true;
		String classLoaderToString;
		StringBuffer classpath = new StringBuffer();
		String parentDelegationMode = "PARENT_FIRST";
		String localClassPath = null;
		String delegationMode = null;

		while (hasParent) {
			classLoaderToString = classLoader.toString();

			if ((classLoaderToString.indexOf("Local Class") > 0) && (classLoaderToString.indexOf("Delegation ") > 0)) {
				localClassPath = StringUtils.substringBetween(classLoaderToString, "Local Class", "Delegation ")
						.substring(5).trim();
				delegationMode = StringUtils.substringAfter(classLoaderToString, "Delegation ").substring(5).trim();
			} else {
				break;
			}

			if (parentDelegationMode.equals("PARENT_FIRST")) {
				if ((classpath.length() > 0) && (classpath.charAt(0) != ';')) {
					classpath.insert(0, ';');
				}

				classpath.insert(0, localClassPath);
			} else {
				if ((classpath.length() > 0) && (classpath.charAt(classpath.length() - 1) != ';')) {
					classpath.append(';');
				}

				classpath.append(localClassPath);
			}

			parentDelegationMode = delegationMode;

			classLoader = classLoader.getParent();

			hasParent = classLoader != null;
		}

		return classpath.toString();
	}

	/**
	 * INSERT COMMENT
	 * 
	 * @param string
	 */
	public void setImagesURI(String string) {
		imagesURI = string;
	}

	/**
	 * INSERT COMMENT
	 * 
	 * @param b
	 */
	public void setKeepJavaFile(boolean b) {
		keepJavaFile = b;
	}

	/**
	 * INSERT COMMENT
	 * 
	 * @param string
	 */
	public void setReportsDirName(String string) {
		reportsDirName = string;
	}

	/**
	 * INSERT COMMENT
	 * 
	 * @return
	 */
	public String getImagesURI() {
		return imagesURI;
	}

	/**
	 * INSERT COMMENT
	 * 
	 * @return
	 */
	public boolean isKeepJavaFile() {
		return keepJavaFile;
	}

	/**
	 * INSERT COMMENT
	 * 
	 * @return
	 */
	public String getReportsDirName() {
		return reportsDirName;
	}
}
