package no.nav.maven.plugin.wsi;

import java.io.File;
import java.util.Collections;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.wsi.WSIException;
import org.wsi.test.analyzer.BasicProfileAnalyzer;
import org.wsi.test.analyzer.config.AnalyzerConfig;
import org.wsi.test.analyzer.config.WSDLElement;
import org.wsi.test.analyzer.config.WSDLReference;
import org.wsi.test.analyzer.config.impl.WSDLElementImpl;
import org.wsi.test.analyzer.config.impl.WSDLReferenceImpl;
import org.wsi.test.common.AddStyleSheet;
import org.wsi.test.common.impl.AddStyleSheetImpl;
import org.wsi.test.document.DocumentFactory;

/**
 * 
 * @author test@example.com
 * 
 * @goal analyze
 */
public class AnalyzeMojo extends AbstractMojo {
	/**
	 * @parameter
	 */
	private boolean verbose;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @readonly
	 */
	private File projectBuildDirectory;

	/**
	 * @parameter expression=${failOnFailure} default-value="true"
	 */
	private boolean failOnFailure;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			DocumentFactory documentFactory = DocumentFactory.newInstance();

			AnalyzerConfig analyzerConfig = documentFactory.newAnalyzerConfig();
			analyzerConfig.setVerboseOption(verbose);

			// TODO: Make selection of profile configurable
			analyzerConfig.setTestAssertionsDocumentLocation("common/profiles/SSBP10_BP11_TAD.xml");

			analyzerConfig.setReportLocation(new File(projectBuildDirectory, "report.xml").getPath());
			analyzerConfig.setReplaceReport(true);

			AddStyleSheet addStyleSheet = new AddStyleSheetImpl();
			addStyleSheet.setHref("common/xsl/report.xsl");
			addStyleSheet.setType("text/xsl");
			analyzerConfig.setAddStyleSheet(addStyleSheet);

			// TODO: Find all applicable port types
			WSDLReference wsdlReference = new WSDLReferenceImpl();
			wsdlReference.setWSDLLocation("no\\nav\\lib\\oko\\inf\\Utbetaling.wsdl");
			WSDLElement wsdlElement = new WSDLElementImpl();
			wsdlReference.setWSDLElement(wsdlElement);
			wsdlElement.setType("portType");
			wsdlElement.setNamespace("http://nav-lib-oko/no/nav/lib/oko/inf");
			wsdlElement.setName("Utbetaling");

			analyzerConfig.setWSDLReference(wsdlReference);

			if (getLog().isDebugEnabled()) {
				getLog().debug(analyzerConfig.toString());
			}

			int result = new BasicProfileAnalyzer(Collections.singletonList(analyzerConfig)).validateConformance();
			if (failOnFailure && result > 0) {
				throw new MojoFailureException("WSI validation failed, check report for details.");
			}
		} catch (WSIException e) {
			throw new MojoExecutionException("WSIException", e);
		}
	}
}
