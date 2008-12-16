package no.nav.maven.plugins.fixers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.maven.plugins.common.utils.EarUtils;
import no.nav.maven.plugins.common.utils.XMLUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;

/**
 * Maven Goal that alters the META-INF/ibm-webservicesclient-bnd.xmi file and
 * adds the following tag: <basicAuth xmi:id="BasicAuth_1187868680921"
 * userid="perskort" password="{xor}LzotLDQwLSs="/>
 * 
 * @goal changeDisplayName2
 */
public class ChangeDisplayName extends AbstractMojo {

	/**
	 * targetDir containing all extracted ear archives
	 * 
	 * @parameter
	 * @required
	 */
	private File targetDir = null;

	public void execute() throws MojoExecutionException, MojoFailureException {
		File[] modules = targetDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		getLog().info("-------------------- Start ChangeDisplayName --------------------");
		for (File module : modules) {
			try {
				editApplicationXml(module);
			} catch (IOException e) {
				throw new MojoExecutionException("An IO exception occured while processing application.xml!", e);
			} catch (DocumentException e) {
				throw new MojoExecutionException("A DocumentException was thrown while processing application.xml!", e);
			}
		}
		getLog().info("-------------------- End ChangeDisplayName --------------------");
	}

	private void editApplicationXml(File module) throws IOException, DocumentException {
		File appxml;
		Document doc;
		SAXReader reader;
		Element app, desc;
		XPath search;
		Map<String, String> uris = new HashMap<String, String>();

		appxml = new File(module.getAbsolutePath()
				+ "/META-INF/application.xml");
		if (appxml.exists()) {
			reader = new SAXReader();
			doc = reader.read(appxml);
			uris.put("ns", "http://java.sun.com/xml/ns/j2ee");

			search = doc.createXPath("/ns:application/ns:description");
			search.setNamespaceURIs(uris);
			desc = (Element) search.selectSingleNode(doc);
			if (desc != null) {
				String value = EarUtils.getTagLog().get(module.getName() + ".ear");
				if(value == null) throw new IOException("Unable to determine tag for '" + module.getName() + "'");
				desc.setText(value);
				getLog().info("[" + module.getName() + "] description element changed to " + EarUtils.getTagLog().get(module.getName() + ".ear"));
			}else{
				app = doc.getRootElement();
				desc = DOMDocumentFactory.getInstance().createElement("description", "http://java.sun.com/xml/ns/j2ee");
				desc.setText(EarUtils.getTagLog().get(module.getName() + ".ear"));				
				List list = app.content();
				list.add(0, desc);	
				getLog().info("[" + module.getName() + "] description element with text '" + EarUtils.getTagLog().get(module.getName() + ".ear") + "' added");
			}
			
			XMLUtils.writeXMLDocument(doc, appxml);
		} else
			getLog().warn(
					module.getName() + " does not contain application.xml!");

	}

	public File getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}

}
