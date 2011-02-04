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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RequireScaSecurityPermission implements EnforcerRule {

	private File basedir;

	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		MavenProject project = getProject(helper);

		// This rule is meaningless for POM artifacts
		if (!"pom".equals(project.getPackaging())) {
			basedir = project.getBasedir();
			if (hasJaxWsExport()) {
				validateScaSecurityPermission();
			}
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

	private boolean hasJaxWsExport() throws EnforcerRuleException {
		try {
			XPathExpression xPathExpression = ExportAndImportFileXPathFactory
					.getXPathExpression("//esbBinding[@xsi:type='jaxws:JaxWsExportBinding']");
			for (File file : getFiles(basedir, "*.export")) {
				NodeList jaxWSBindingNodeList = getNodeList(xPathExpression, file);
				if (jaxWSBindingNodeList.getLength() > 0) {
					return true;
				}
			}
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error compiling XPath: ", e);
		}
		return false;
	}

	private List<File> getFiles(File directory, String includedFilePattern) throws EnforcerRuleException {
		try {
			return FileUtils.getFiles(directory, includedFilePattern, FileUtils.getDefaultExcludesAsString());
		} catch (IOException e) {
			throw new EnforcerRuleException("Unable to list files: ", e);
		}
	}

	private void validateScaSecurityPermission() throws EnforcerRuleException {
		try {
			XPathExpression xPathExpression = ExportAndImportFileXPathFactory
					.getXPathExpression("//scdl:interfaceQualifier[@xsi:type='scdl:SecurityPermission']");
			for (File file : getFiles(basedir, "*.component")) {
				NodeList jaxWSBindingNodeList = getNodeList(xPathExpression, file);
				if (jaxWSBindingNodeList.getLength() > 0) {
					return;
				}
			}
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error compiling XPath: ", e);
		}
		throw new EnforcerRuleException(
				"SCA module with JAX-WS export does not have a SCA component with security permission set");
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
