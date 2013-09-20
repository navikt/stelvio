package no.nav.datapower.config.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Represent the Freemarker-specific configuration and processing steps
 * 
 * @author Øystein Gisnås, Accenture
 *
 */
public class Freemarker {

	private static final String DATAPOWER_CFG_GEN_RESOURCE_PREFIX = "/freemarker-templates/";
	private Configuration configuration;

	/**
	 * Initialize Freemarker for processing Datapower configuration-related templates
	 *  
	 * @throws IOException
	 */
	public Freemarker(File templateDirectory) throws IOException {
		configuration = new Configuration();
		configuration.setWhitespaceStripping(true);
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		configuration.setLocalizedLookup(false);
		
		FileTemplateLoader localLoader = new FileTemplateLoader(templateDirectory);
		ClassTemplateLoader pluginLoader = new ClassTemplateLoader(this.getClass(), DATAPOWER_CFG_GEN_RESOURCE_PREFIX);
		TemplateLoader[] loaders = new TemplateLoader[] {localLoader, pluginLoader};
		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
		configuration.setTemplateLoader(mtl);
	}

	/**
	 * Process the main template, merge in properties and output to writer
	 * 
	 * @param templateName
	 * @param properties
	 * @param writer
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void processTemplate(String templateName, Hashtable properties, Writer writer) throws IOException, TemplateException {
		Template template = configuration.getTemplate(templateName);
		template.process(properties, writer);
	}

}
