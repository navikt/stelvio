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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RequirePolicySetRule implements EnforcerRule {
	private XPathExpression jaxWSExportBindingExpression;

	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		MavenProject project = getProject(helper);

		File basedir = project.getBasedir();
		validateJaxWsBindings(getExportFiles(basedir), getJaxWSExportBindingExpression());
	}

	private void validateJaxWsBindings(List<File> files, XPathExpression xPathExpression) throws EnforcerRuleException {
		for (File file : files) {
			NodeList jaxWSBindingNodeList = getNodeList(xPathExpression, file);
			jaxWSBinding: for (int i = 0; i < jaxWSBindingNodeList.getLength(); i++) {
				Node jaxWSBindingNode = jaxWSBindingNodeList.item(i);
				NodeList jaxWSBindingChildNodeList = jaxWSBindingNode.getChildNodes();
				for (int j = 0; j < jaxWSBindingChildNodeList.getLength(); j++) {
					Node childNode = jaxWSBindingChildNodeList.item(j);
					if ("policySetRef".equals(childNode.getLocalName())) {
						// Found required policy set - continue to next export
						continue jaxWSBinding;
					}
				}
				throw new EnforcerRuleException(buildErrorMessage(file, jaxWSBindingNode));
			}
		}
	}

	private String buildErrorMessage(File file, Node jaxWSBindingNode) {
		return file.getName() + " contains errors...";
	}

	private NodeList getNodeList(XPathExpression xPathExpression, File file) throws EnforcerRuleException {
		try {
			NodeList nodeList = (NodeList) xPathExpression.evaluate(new InputSource(new FileInputStream(file)),
					XPathConstants.NODESET);
			return nodeList;
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error evauating XPath: ", e);
		} catch (FileNotFoundException e) {
			throw new EnforcerRuleException("File not found (should not happen): ", e);
		}
	}

	private XPathExpression getJaxWSExportBindingExpression() throws EnforcerRuleException {
		try {
			if (jaxWSExportBindingExpression == null) {
				String expression = "//esbBinding[@xsi:type='jaxws:JaxWsExportBinding']";
				jaxWSExportBindingExpression = ExportFileXPathFactory.getXPathExpression(expression);
			}
			return jaxWSExportBindingExpression;
		} catch (XPathExpressionException e) {
			throw new EnforcerRuleException("Error compiling XPath: ", e);
		}
	}

	private List<File> getExportFiles(File basedir) throws EnforcerRuleException {
		try {
			return FileUtils.getFiles(basedir, "**/*.export", FileUtils.getDefaultExcludesAsString());
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
