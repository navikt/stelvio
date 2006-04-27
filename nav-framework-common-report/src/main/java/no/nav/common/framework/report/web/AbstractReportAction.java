package no.nav.common.framework.report.web;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import no.nav.common.framework.report.ReportNotFoundException;
import no.nav.common.framework.report.ReportParameter;
import no.nav.common.framework.report.config.Report;
import no.nav.common.framework.report.config.ReportChart;
import no.nav.common.framework.report.config.ReportConfig;
import no.nav.common.framework.report.design.ChartValue;
import no.nav.common.framework.report.design.CommonChartImageFactory;
import no.nav.common.framework.report.design.JRExcelCsvExporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.FactoryNotFoundException;
import org.apache.struts.tiles.TilesUtil;

/**
 * INSERT COMMENT
 *
 * @author Ole-Kristian Hagen
 * @version $Revision: 2702 $, $Date: 2005-12-12 12:55:12 +0100 (Mon, 12 Dec 2005) $
*/
public abstract class AbstractReportAction extends DispatchAction {
	protected static Log log = LogFactory.getLog(AbstractReportAction.class);
	private static final String ATTR_IMAGES_MAP = "IMAGES_MAP";
	protected static final String FORM_PARAM_REPORT_ID = "reportId";
	protected static final String FORM_PARAM_REPORT_NAME = "reportName";
	protected static final String FORM_PARAM_REPORT_TYPE = "reportType";
	protected static final String FORM_PARAM_REPORT_GROUP = "reportGroup";
	protected static final String FORM_PARAM_REPORT_FORMAT = "rapportFormat";
	protected static final String FORM_PARAM_IMAGE_NAME = "imageName";

	/**
	 * DOCUMENT ME!
	 *
	 * @param mapping DOCUMENT ME!
	 * @param form DOCUMENT ME!
	 * @param request DOCUMENT ME!
	 * @param response DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws ReportNotFoundException
	 */
	public final ActionForward getImage(
		final ActionMapping mapping,
		final ActionForm form,
		final HttpServletRequest request,
		final HttpServletResponse response)
		throws ReportNotFoundException {
		String imageName = request.getParameter(FORM_PARAM_IMAGE_NAME);

		//load image from IMAGES_MAP
		if (imageName != null) {
			Map imagesMap =
				(Map) request.getSession().getAttribute(ATTR_IMAGES_MAP);

			if (imagesMap != null) {
				byte[] imageData = (byte[]) imagesMap.get(imageName);

				try {
					response.setContentLength(imageData.length);

					OutputStream out = response.getOutputStream();
					out.write(imageData, 0, imageData.length);
					out.flush();
					out.close();
				} catch (IOException ioe) {
					log.warn(ioe.toString());
				}
			}
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param mapping DOCUMENT ME!
	 * @param form DOCUMENT ME!
	 * @param request DOCUMENT ME!
	 * @param response DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws ServletException DOCUMENT ME!
	 * @deprecated
	 */
	public final ActionForward getInput(
		final ActionMapping mapping,
		final ActionForm form,
		final HttpServletRequest request,
		final HttpServletResponse response)
		throws ServletException {
		String def = request.getParameter("def");

		if ((def == null) || (def.length() == 0)) {
			throw new ServletException("Paremeter 'def' is required for this command");
		} else {
			request.getSession().setAttribute("def", def);
		}

		try {
			ComponentDefinition definition =
				TilesUtil.getDefinition(
					def,
					request,
					getServlet().getServletContext());
			org.apache.struts.tiles.DefinitionsUtil.setActionDefinition(
				request,
				definition);
		} catch (FactoryNotFoundException e) {
			log.error("Can't get definition factory.");

			return (mapping.findForward("error"));
		} catch (DefinitionsFactoryException e) {
			log.error("General Factory error", e);

			return (mapping.findForward("error"));
		}

		DynaActionForm dynaForm = (DynaActionForm) form;

		Boolean resetForm = (Boolean) request.getAttribute("resetForm");

		if ((resetForm == null) && (resetForm == Boolean.TRUE)) {
			dynaForm.initialize(mapping);
		}

		return mapping.findForward("dummy");
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param mapping DOCUMENT ME!
	 * @param form DOCUMENT ME!
	 * @param request DOCUMENT ME!
	 * @param response DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws ReportNotFoundException
	 */
	public final ActionForward validateForm(
		final ActionMapping mapping,
		final ActionForm form,
		final HttpServletRequest request,
		final HttpServletResponse response)
		throws ReportNotFoundException {
		DynaActionForm dynaForm = (DynaActionForm) form;

		// Validate
		if (!validate(mapping, dynaForm, request)) {
			request.setAttribute("resetForm", Boolean.FALSE);
			dynaForm.set("formValid", "false");
		} else {
			dynaForm.set("formValid", "true");
		}

		return new ActionForward(
			(String) request.getSession().getAttribute("def"));
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param mapping DOCUMENT ME!
	 * @param form DOCUMENT ME!
	 * @param request DOCUMENT ME!
	 * @param response DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws ReportNotFoundException DOCUMENT ME!
	 */
	public final ActionForward getReport(
		final ActionMapping mapping,
		final ActionForm form,
		final HttpServletRequest request,
		final HttpServletResponse response)
		throws ReportNotFoundException {
		DynaActionForm dynaForm = (DynaActionForm) form;
		dynaForm.set("formValid", "false");

		// Make queryparameters
		Map queryParameters = createQueryParameters(request, dynaForm, mapping);

		// Get report
		Report report = lookupReport(queryParameters);

		// Find outputtype
		String outputType =
			(String) queryParameters.get(FORM_PARAM_REPORT_FORMAT);

		if ((outputType == null) || (outputType.length() == 0)) {
			if (log.isWarnEnabled()) {
				log.warn(
					"No reportformat found in form. Setting format to pdf");
			}

			outputType = "pdf";
		}

		try {
			// Execute reportquery
			List list = executeMainReportQuery(queryParameters, report);

			if ((list.size() > 0) || report.getShowEmptyReportAsBoolean()) {
				response.setHeader(
					"Content-Disposition",
					"inline; filename=\""
						+ report.getName()
						+ '.'
						+ outputType
						+ "\"");

				// Create datasource
				JRDataSource dataSource = createDataSource(list);

				// Create report parameters
				Map parameters = createBaseReportParameters(request, report);
				parameters.putAll(createReportParameters(request, report));
				parameters.put(ReportParameter.EXPORT_TYPE, outputType);
				parameters.put(
					ReportParameter.QUERY_PARAMETER_MAP,
					queryParameters);

				// Create print                
				JasperPrint jrPrint =
					createPrint(report, dataSource, parameters);

				// Export print
				JRExporter exporter =
					createExporter(request, response, outputType);

				byte[] bytes = exportReport(jrPrint, exporter);

				response.setContentLength(bytes.length);

				try {
					writeBytesToOutputStream(bytes, response.getOutputStream());
				} catch (IOException e) {
					log.error(e);
				}
			} else {
				return handleNoResult(request, response, form, mapping);
			}
		} catch (JRException e) {
			log.error("Failed to build report", e);
		} finally {
			dynaForm.initialize(mapping);
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param bytes DOCUMENT ME!
	 * @param out DOCUMENT ME!
	 *
	 * @throws IOException
	 */
	public static void writeBytesToOutputStream(byte[] bytes, OutputStream out)
		throws IOException {
		BufferedOutputStream bufferedOut;
		long startStreaming = 0;

		if (log.isInfoEnabled()) {
			startStreaming = System.currentTimeMillis();
		}

		bufferedOut = new BufferedOutputStream(out, bytes.length);
		bufferedOut.write(bytes);
		bufferedOut.flush();
		bufferedOut.close();

		if (log.isInfoEnabled()) {
			log.info(
				"Streaming report to client executed in "
					+ (System.currentTimeMillis() - startStreaming)
					+ " ms");
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param queryParameters DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws IllegalArgumentException
	 * @throws NumberFormatException
	 * @throws ReportNotFoundException
	 */
	public static Report lookupReport(Map queryParameters)
		throws
			IllegalArgumentException,
			NumberFormatException,
			ReportNotFoundException {
		String reportId = (String) queryParameters.get(FORM_PARAM_REPORT_ID);
		String reportName =
			(String) queryParameters.get(FORM_PARAM_REPORT_NAME);
		String reportType =
			(String) queryParameters.get(FORM_PARAM_REPORT_TYPE);
		String reportGroup =
			(String) queryParameters.get(FORM_PARAM_REPORT_GROUP);

		if (((reportId == null) && (reportName == null))
			|| (reportType == null)
			|| (reportGroup == null)) {
			throw new IllegalArgumentException(
				"All of the the parameters "
					+ FORM_PARAM_REPORT_ID
					+ " or "
					+ FORM_PARAM_REPORT_NAME
					+ ", "
					+ FORM_PARAM_REPORT_TYPE
					+ " and "
					+ FORM_PARAM_REPORT_GROUP
					+ " must be set.");
		}

		Report report = null;

		if (reportId != null) {
			report =
				ReportConfig.getReport(
					reportType,
					reportGroup,
					Integer.valueOf(reportId).intValue());
		} else {
			report =
				ReportConfig.getReport(reportType, reportGroup, reportName);
		}

		if (report == null) {
			throw new ReportNotFoundException(
				"Report [type="
					+ reportType
					+ ", group="
					+ reportGroup
					+ ", id="
					+ reportId
					+ ", name="
					+ reportName
					+ "] does not exist");
		}

		return report;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param request DOCUMENT ME!
	 * @param response DOCUMENT ME!
	 * @param outputType DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static JRExporter createExporter(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String outputType) {
		JRExporter exporter = null;

		if (outputType.equals("pdf")) {
			response.setContentType("application/pdf");

			exporter = new JRPdfExporter();
		} else if (outputType.equals("html")) {
			response.setContentType("text/html");

			exporter = new JRHtmlExporter();
			exporter.setParameter(
				JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
			exporter.setParameter(
				JRHtmlExporterParameter.BETWEEN_PAGES_HTML,
				"");

			Map imagesMap = new HashMap();

			request.getSession().setAttribute(ATTR_IMAGES_MAP, imagesMap);
			exporter.setParameter(
				JRHtmlExporterParameter.IMAGES_MAP,
				imagesMap);
			exporter.setParameter(
				JRHtmlExporterParameter.IMAGES_URI,
				request.getRequestURI() + "?command=getImage&imageName=");
		} else if (outputType.equals("xls")) {
			response.setContentType("application/vnd.ms-excel");

			exporter = new JRXlsExporter();
			exporter.setParameter(
				JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
				Boolean.TRUE);
			exporter.setParameter(
				JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
			exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		} else if (outputType.equals("csv")) {
			response.setContentType("application/vnd.ms-excel");

			exporter = new JRExcelCsvExporter();
			exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "\t");
			exporter.setParameter(
				JRCsvExporterParameter.RECORD_DELIMITER,
				"\r\n");
		}

		exporter.setParameter(
			JRExporterParameter.CHARACTER_ENCODING,
			"ISO-8859-1");

		return exporter;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param report DOCUMENT ME!
	 * @param dataSource DOCUMENT ME!
	 * @param parameters DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JRException
	 */
	public static JasperPrint createPrint(
		Report report,
		JRDataSource dataSource,
		Map parameters)
		throws JRException {
		long startFilling = 0;

		if (log.isInfoEnabled()) {
			startFilling = System.currentTimeMillis();
		}

		JasperPrint jrPrint;

		if (report.getDatasourceAsParameterBoolean()) {
			parameters.put(
				ReportParameter.CONTENT_REPORT_DATA_SOURCE,
				dataSource);
			jrPrint =
				JasperFillManager.fillReport(
					report.getJRReport(Report.ROOTDESIGN_NAME),
					parameters,
					new JREmptyDataSource(1));
		} else {
			jrPrint =
				JasperFillManager.fillReport(
					report.getJRReport(Report.ROOTDESIGN_NAME),
					parameters,
					dataSource);
		}

		if (log.isInfoEnabled()) {
			log.info(
				"Filling report executed in "
					+ (System.currentTimeMillis() - startFilling)
					+ " ms");
		}

		return jrPrint;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param jasperPrint DOCUMENT ME!
	 * @param exporter DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws JRException
	 */
	public static byte[] exportReport(
		JasperPrint jasperPrint,
		JRExporter exporter)
		throws JRException {
		byte[] output;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

		long start = 0;

		if (log.isInfoEnabled()) {
			start = System.currentTimeMillis();
		}

		exporter.exportReport();

		if (log.isInfoEnabled()) {
			log.info(
				"Exporting report executed in "
					+ (System.currentTimeMillis() - start)
					+ " ms");
		}

		output = baos.toByteArray();

		return output;
	}

	protected static boolean validate(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request) {
		ActionErrors errors = form.validate(mapping, request);

		if ((errors == null) || errors.isEmpty()) {
			return (true);
		}

		request.setAttribute(Globals.ERROR_KEY, errors);

		return false;
	}

	private Map createBaseReportParameters(
		final HttpServletRequest request,
		Report report)
		throws JRException {
		Map parameters = new HashMap();

		// JasperReports
		Map compiledDesigns = report.getCompiledDesigns();
		Iterator compiledDesignsIterator =
			compiledDesigns.entrySet().iterator();
		Map.Entry entry;

		while (compiledDesignsIterator.hasNext()) {
			entry = (Map.Entry) compiledDesignsIterator.next();
			parameters.put(
				(String) entry.getKey(),
				report.getJRReport((String) entry.getKey()));
		}

		// Images (awt)
		Map images = report.getImages();
		Iterator imagesIterator = images.entrySet().iterator();

		while (imagesIterator.hasNext()) {
			entry = (Map.Entry) imagesIterator.next();

			try {
				parameters.put(
					(String) entry.getKey(),
					ImageIO.read(
						new File(
							servlet.getServletContext().getRealPath(
								ReportConfig.getImagesURI())
								+ (String) entry.getValue())));
			} catch (IOException e) {
				log.error(e);
			}
		}

		// Params defined in config.
		parameters.putAll(report.getParameters());

		// Create chart (This part is not tested!!!)
		ReportChart reportChart = report.getReportChart();

		if (reportChart != null) {
			// Execute chart query
			ChartValue[] values = null;

			// Create jfreechart image
			Image chartImage =
				CommonChartImageFactory.create(reportChart, values);

			// Set chartimage as parameter 'ChartImage'
			parameters.put(ReportParameter.CHART_IMAGE, chartImage);
		}

		// Reportname
		parameters.put(ReportParameter.REPORT_NAME, report.getName());

		return parameters;
	}

	protected abstract ActionForward handleNoResult(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final ActionForm form,
		final ActionMapping mapping);

	protected abstract Map createQueryParameters(
		final HttpServletRequest request,
		final ActionForm form,
		final ActionMapping mapping);

	protected abstract Map createReportParameters(
		final HttpServletRequest request,
		Report report)
		throws JRException;

	protected abstract List executeMainReportQuery(
		Map queryParameters,
		Report report);

	protected abstract JRDataSource createDataSource(List resultList);
}
