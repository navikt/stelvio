package no.stelvio.maven.enforcer;

import java.io.File;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.w3c.dom.Element;

public class RequireJaxWsHandlerRule extends AbstractJaxWsBindingRule {
	private String handlerClass;

	@Override
	protected XPathExpression buildValidatingXPathExpression() throws EnforcerRuleException {
		try {
			String validatingXPathExpression = "//esbBinding[(@xsi:type='jaxws:JaxWsExportBinding') and not(handlerChain/handler/handlerClass/text()='"
					+ handlerClass + "')]";
			return ExportAndImportFileXPathFactory.getXPathExpression(validatingXPathExpression);
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error compiling XPath: ", e);
		}
	}

	@Override
	protected String buildValidationErrorMessage(File file, Element jaxWSBindingElement) {
		// TODO: Extract fully qualified name of service
		return "Binding for service " + jaxWSBindingElement.getAttribute("service") + " defined in " + file.getAbsolutePath()
				+ " does not have required handler (" + handlerClass + ").";
	}

	@Override
	protected String getIncludedFilePattern() {
		return "**/*.export";
	}
}
