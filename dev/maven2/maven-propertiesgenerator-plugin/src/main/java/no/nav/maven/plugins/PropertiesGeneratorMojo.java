package no.nav.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import no.nav.maven.plugins.common.utils.FileUtils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal generate
 * 
 * @phase process-sources
 */
public class PropertiesGeneratorMojo extends AbstractMojo {
	/**
	 * Location Velocity templates
	 * 
	 * @parameter
	 * @required
	 */
	private String templateDir;

	/**
	 * 
	 * The created propertiesfiles are put in outputdir/environmentName
	 * 
	 * @parameter
	 * @required
	 */
	private String outputDir;

	/**
	 * 
	 * @parameter
	 * @required
	 */
	private String environmentProperties;

	public String getEnvironmentDir() {
		return environmentProperties;
	}

	public void setEnvironmentDir(String environmentDir) {
		this.environmentProperties = environmentDir;
	}

	/**
	 * @return Returns the outputDir.
	 */
	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * @param outputDir
	 *            The outputDir to set.
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * @return Returns the templateDir.
	 */
	public String getTemplateDir() {
		return templateDir;
	}

	/**
	 * @param templateDir
	 *            The templateDir to set.
	 */
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public void execute() throws MojoExecutionException {

		getLog().info("Taking properties from " +environmentProperties+ "and templates from " +templateDir+ ", combining them in "+ outputDir);

		try {
			Properties p = new Properties();
			p.setProperty("file.resource.loader.path", templateDir);
			Velocity.init(p);
			VelocityContext context = new VelocityContext();
			Properties props = new Properties();
			File propertiesFileOrFolder = new File(environmentProperties);


			if(propertiesFileOrFolder.isDirectory()){			
				String[] propertiesFileNames = (propertiesFileOrFolder).list();
				for(int i = 0; i < propertiesFileNames.length; i++){
					String propertiesFileName = propertiesFileNames[i];
					if (propertiesFileName.toLowerCase().endsWith(".properties")){
						java.io.InputStream is = new FileInputStream(environmentProperties + "/" + propertiesFileName);
						props.load(is);
					}
				}
			} else {
				props.load(new FileInputStream(propertiesFileOrFolder));
			}


			Set keys = props.keySet();
			String key;
			for (Iterator iterator = keys.iterator(); iterator.hasNext(); context
					.put(key, props.get(key)))
				key = (String) iterator.next();

			File output = new File(outputDir);
			getLog().info("Target folder: " + output.getAbsolutePath());
			if (output.exists()) {
				getLog().info("Deleting " + output.getAbsolutePath());
				FileUtils.recursiveDelete(output);
				output.mkdirs();
			}
			File tempTemplates[] = (new File(templateDir)).listFiles();
			File templates[] = new File[tempTemplates.length];
			ArrayList tempArrayList = new ArrayList();
			for (int j = 0; j < tempTemplates.length; j++) {
				File tempFile = tempTemplates[j];
				String tempName = tempFile.getName();

				if (tempFile.isFile()
						&& (tempName.endsWith(".vm") || tempName
								.endsWith(".properties"))) {
					String newName;
					if (tempName.endsWith(".properties")) {
						tempArrayList.add(0, tempFile);
					} else {
						if (tempArrayList.size() == 0) {
							tempArrayList.add(tempFile);
						} else if (tempArrayList.size() == 1) {
							tempArrayList.add(1, tempFile);
						} else {
							tempArrayList.add(tempArrayList.size() - 1,
									tempFile);
						}
					}
				} else {
					tempArrayList.add(tempFile);
				}
			}
			for (int j = 0; j < tempArrayList.size(); j++) {
				templates[j] = (File) tempArrayList.get(j);
			}
			for (int i = 0; i < templates.length; i++) {
				File template = templates[i];
				String name = template.getName();
				if (template.isFile()
						&& (name.endsWith(".vm") || name
								.endsWith(".properties"))) {
					String newName;
					if (name.endsWith(".vm"))
						newName = name.substring(0, name.length() - 3)
						+ ".properties";
					else
						newName = name;
					String newOut = outputDir;
					File tmp = new File(newOut);
					tmp.mkdirs();
					FileWriter fileWriter = new FileWriter(newOut + "/"
							+ newName);
					Velocity.mergeTemplate(name, context, fileWriter);
					fileWriter.flush();
					fileWriter.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException(e.getMessage());
		}
	}
}
