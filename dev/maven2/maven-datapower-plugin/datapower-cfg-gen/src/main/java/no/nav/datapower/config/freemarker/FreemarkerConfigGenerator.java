package no.nav.datapower.config.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;

import no.nav.datapower.config.ConfigGenerator;
import no.nav.datapower.config.freemarker.templates.DPTemplateLib;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class FreemarkerConfigGenerator extends ConfigGenerator {

	private static final TemplateLoader DPLIB_TEMPLATE_LOADER = DPTemplateLib.getTemplateLoader();

	private Configuration freemarker;
	
	public FreemarkerConfigGenerator() {
		super();
	}
	
	public FreemarkerConfigGenerator(String name, Properties requiredProperties, TemplateLoader templateLoader) {
		this(name, requiredProperties, DPLIB_TEMPLATE_LOADER, templateLoader);
	}
	
	public FreemarkerConfigGenerator(String name, Properties requiredProperties, TemplateLoader... templateLoaders) {
		super(name, requiredProperties);
		getFreemarker().setTemplateLoader(new MultiTemplateLoader(templateLoaders));
	}	
	
	protected Configuration getFreemarker() {
		if (freemarker == null) {
			freemarker = new Configuration();
			freemarker.setWhitespaceStripping(true);
			freemarker.setObjectWrapper(new DefaultObjectWrapper());
			freemarker.setLocalizedLookup(false);
		}
		return freemarker;
	}

	protected void setTemplateLoader(Class clazz, String resourcePath) {
		TemplateLoader[] loaders = { DPLIB_TEMPLATE_LOADER, new ClassTemplateLoader(clazz, resourcePath) };
		getFreemarker().setTemplateLoader(new MultiTemplateLoader(loaders));
	}
	
	protected Template getTemplate(String templateName) throws IOException {
		return freemarker.getTemplate(templateName);
	}

	protected void processTemplate(String templateName, Hashtable properties, Writer writer) throws IOException, TemplateException {
		Template template = freemarker.getTemplate(templateName);
		template.process(properties, writer);
		writer.flush();				
	}

	//public abstract ConfigPackage generate();

}
