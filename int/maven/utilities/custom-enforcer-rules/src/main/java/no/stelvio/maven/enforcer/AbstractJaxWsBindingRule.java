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

public abstract class AbstractJaxWsBindingRule implements EnforcerRule {
	private XPathExpression validatingXPathExpression;

	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		MavenProject project = getProject(helper);

		// This rule is meaningless for POM artifacts
		if (!"pom".equals(project.getPackaging())) {
			File basedir = project.getBasedir();
			validateJaxWsBindings(getFiles(basedir, getIncludedFilePattern()), getValidatingXPathExpression());
		}
	}

	protected abstract String getIncludedFilePattern();

	protected abstract XPathExpression buildValidatingXPathExpression() throws EnforcerRuleException;

	protected abstract String buildValidationErrorMessage(File file, Element jaxWSBindingElement);

	private XPathExpression getValidatingXPathExpression() throws EnforcerRuleException {
		if (validatingXPathExpression == null) {
			validatingXPathExpression = buildValidatingXPathExpression();
		}
		return validatingXPathExpression;
	}

	private void validateJaxWsBindings(List<File> files, XPathExpression xPathExpression) throws EnforcerRuleException {
		for (File file : files) {
			NodeList jaxWSBindingNodeList = getNodeList(xPathExpression, file);
			if (jaxWSBindingNodeList.getLength() > 0) {
				// TODO: Make validation report multiple errors
				throw new EnforcerRuleException(buildValidationErrorMessage(file, (Element) jaxWSBindingNodeList.item(0)));
			}
		}
	}

	private List<File> getFiles(File directory, String includedFilePattern) throws EnforcerRuleException {
		try {
			return FileUtils.getFiles(directory, includedFilePattern, FileUtils.getDefaultExcludesAsString());
		} catch (IOException e) {
			throw new EnforcerRuleException("Unable to list files: ", e);
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

	private NodeList getNodeList(XPathExpression xPathExpression, File file) throws EnforcerRuleException {
		try {
			return (NodeList) xPathExpression.evaluate(new InputSource(new FileInputStream(file)), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error evauating XPath: ", e);
		} catch (FileNotFoundException e) {
			throw new EnforcerRuleException("File not found (should not happen...): ", e);
		}
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