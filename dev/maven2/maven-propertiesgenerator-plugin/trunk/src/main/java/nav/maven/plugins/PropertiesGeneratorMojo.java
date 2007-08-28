package nav.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

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
	 * This is used to decide which subfolder to put the created properties in.
	 * @parameter
	 * @required
	 */
	private String environmentName;
	
	/**
	 * 
	 * The created propertiesfiles are put in outputdir/environmentName
	 * @parameter
	 * @required
	 */
	private String outputDir;
	
	/**
	 * 
	 * @parameter
	 * @required
	 */
	private String environmentDir;
	
	
	
	

	public String getEnvironmentDir() {
		return environmentDir;
	}
	public void setEnvironmentDir(String environmentDir) {
		this.environmentDir = environmentDir;
	}
	/**
	 * @return Returns the environmentName.
	 */
	public String getEnvironmentName() {
		return environmentName;
	}
	/**
	 * @param environmentName The environmentName to set.
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	/**
	 * @return Returns the outputDir.
	 */
	public String getOutputDir() {
		return outputDir;
	}
	/**
	 * @param outputDir The outputDir to set.
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
	 * @param templateDir The templateDir to set.
	 */
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}
	public void execute() throws MojoExecutionException {

		try {
			
			Properties p = new Properties();
			p.setProperty("file.resource.loader.path",templateDir);
			Velocity.init(p);
			VelocityContext context = new VelocityContext();
			
			InputStream is = new FileInputStream(environmentDir+"/"+environmentName+".properties");
			Properties props = new Properties();
			props.load(is);
			Set keys = props.keySet();
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				context.put(key,props.get(key));
			}
			
			File dir = new File(templateDir);
			File[] templates = dir.listFiles();
			for (int i = 0; i < templates.length; i++) {
				File template = templates[i];
				String name = template.getName();
				String newName;
				if (name.endsWith(".vm")) {
					newName = name.substring(0,name.length()-3)+".properties";
				} else {
					newName = name;
				}
				String newOut = outputDir+"/"+environmentName;
				File tmp = new File(newOut);
				tmp.mkdirs();
				FileWriter fileWriter = new FileWriter(newOut+"/"+newName);
				System.out.println("parset filen "+name);
				Velocity.mergeTemplate(name, context, fileWriter);
				fileWriter.flush();
				fileWriter.close();
			}							
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException(e.getMessage());
		}
		
	}
}
