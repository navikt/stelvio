package no.stelvio.maven.enforcer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.util.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RequirePolicySetRule implements EnforcerRule {
	private static final String MISSING_POLICYSET_JAXWS_BINDING_XPATH_EXPRESSION = "//esbBinding[(@xsi:type='jaxws:JaxWsExportBinding' or @xsi:type='jaxws:JaxWsImportBinding') and not(policySetRef)]";
	private XPathExpression missingPolicySetJaxWSBindingXPathExpression;

	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		MavenProject project = getProject(helper);

		// This rule is meaningless for POM artifacts
		if (!"pom".equals(project.getPackaging())) {
			File basedir = project.getBasedir();
			validateJaxWsBindings(getExportAndImportFiles(basedir), getMissingPolicySetJaxWSBindingXPathExpression());
		}
	}

	private void validateJaxWsBindings(List<File> files, XPathExpression xPathExpression) throws EnforcerRuleException {
		for (File file : files) {
			NodeList jaxWSBindingNodeList = getNodeList(xPathExpression, file);
			if (jaxWSBindingNodeList.getLength() > 0) {
				// TODO: Make validation report multiple errors
				throw new EnforcerRuleException(buildErrorMessage(file, (Element) jaxWSBindingNodeList.item(0)));
			}
		}
	}

	private String buildErrorMessage(File file, Element jaxWSBindingElement) {
		// TODO: Extract fully qualified name of service
		return "Binding for service " + jaxWSBindingElement.getAttribute("service") + " defined in " + file.getAbsolutePath()
				+ " does not have required policy set.";
	}

	private NodeList getNodeList(XPathExpression xPathExpression, File file) throws EnforcerRuleException {
		try {
			return (NodeList) xPathExpression.evaluate(new InputSource(new FileInputStream(file)), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error evauating XPath: ", e);
		} catch (FileNotFoundException e) {
			throw new EnforcerRuleException("File not found (should not happen...): ", e);
		}
	}

	private XPathExpression getMissingPolicySetJaxWSBindingXPathExpression() throws EnforcerRuleException {
		if (missingPolicySetJaxWSBindingXPathExpression == null) {
			missingPolicySetJaxWSBindingXPathExpression = getXPathExpression(MISSING_POLICYSET_JAXWS_BINDING_XPATH_EXPRESSION);
		}
		return missingPolicySetJaxWSBindingXPathExpression;
	}

	private XPathExpression getXPathExpression(String expression) throws EnforcerRuleException {
		try {
			return ExportAndImportFileXPathFactory.getXPathExpression(expression);
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error compiling XPath: ", e);
		}
	}

	private List<File> getExportAndImportFiles(File basedir) throws EnforcerRuleException {
		try {
			return FileUtils.getFiles(basedir, "**/*.export,**/*.import", FileUtils.getDefaultExcludesAsString());
		} catch (IOException e) {
			throw new EnforcerRuleException("Unable to list export files: ", e);
		}
	}

	private MavenProject getProject(EnforcerRuleHelper helper) throws EnforcerRuleException {
		MavenProject project;
		try {
			project = (MavenProject) helper.evaluate("${project}");
		} catch (ExpressionEvaluationException e) {
			throw new EnforcerRuleException("Unable to retrieve the MavenProject: ", e);
		}
		return project;
	}

	public String getCacheId() {
		return "0";
	}

	public boolean isCacheable() {
		return false;
	}

	public boolean isResultValid(EnforcerRule cachedRule) {
		return false;
	}
}
