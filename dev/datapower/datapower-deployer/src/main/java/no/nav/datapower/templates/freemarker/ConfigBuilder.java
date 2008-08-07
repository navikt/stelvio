package no.nav.datapower.templates.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;

import no.nav.datapower.util.DPFileUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ConfigBuilder {

	public static final Factory FACTORY;
		
	static {
		//FACTORY = new Factory(DPFileUtils.getResource(ConfigBuilder.class.getClass(), "/templates"));
		FACTORY = new Factory();
	}
		
	public static class Factory {
		private Configuration freemarker;

		private static final TemplateLoader DEFAULT_TEMPLATE_LOADER = new ClassTemplateLoader(ConfigBuilder.class, "");
		
		public Factory() {
			freemarker = configureFreemarker(DEFAULT_TEMPLATE_LOADER);
		}
		
		public Factory(File templateDir) {
			FileTemplateLoader ftl = null;
			try {
				ftl = new FileTemplateLoader(templateDir);
			} catch (IOException e) {
				throw new IllegalArgumentException();
			}
			freemarker = configureFreemarker(DEFAULT_TEMPLATE_LOADER, ftl);
		}
		
		public Factory(TemplateLoader... loaders) {
			freemarker = configureFreemarker(loaders);
		}
		
		private Configuration configureFreemarker(TemplateLoader... loaders) {
			Configuration cfg = new Configuration();		
			cfg.setTemplateLoader(new MultiTemplateLoader(loaders));
			cfg.setWhitespaceStripping(true);
			cfg.setObjectWrapper(new DefaultObjectWrapper());			
			return cfg;
		}
		
		public ConfigBuilder newConfigBuilder(String templateName) {
			return new ConfigBuilder(templateName, freemarker);
		}
	}
	
	private final String templateName;
	private final Configuration freemarker;
	
	private ConfigBuilder(final String templateName, final Configuration freemarker) {
		this.templateName = templateName;
		this.freemarker = freemarker;
	}
	
	public void build(Hashtable props, Writer writer) throws IOException, TemplateException {
		Template template = freemarker.getTemplate(templateName);
		template.process(props, writer);
		writer.flush();				
	}	
}
