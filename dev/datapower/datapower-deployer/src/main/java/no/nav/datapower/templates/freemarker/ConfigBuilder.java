package no.nav.datapower.templates.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Hashtable;

import no.nav.datapower.util.DPFileUtils;

import org.apache.commons.io.IOUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ConfigBuilder {

//	public static final Factory FACTORY;
//		
//	static {
////		FACTORY = new Factory(DPFileUtils.getResource(ConfigBuilder.class, "/"));
//		FACTORY = new Factory();
//	}
		
	public static class Factory {
		private Configuration freemarker;

		private static final TemplateLoader DEFAULT_TEMPLATE_LOADER = new ClassTemplateLoader(ConfigBuilder.class, "/");
		
		public Factory() {
//			freemarker = configureFreemarker(DEFAULT_TEMPLATE_LOADER);
			this(DEFAULT_TEMPLATE_LOADER, new ClassTemplateLoader(ConfigBuilder.class.getClass(), ""));
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
			try {
				IOUtils.writeLines(Arrays.asList(loaders), IOUtils.LINE_SEPARATOR, System.out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		System.out.println("ClassLoader for ConfigBuilder.class = " + DPFileUtils.getResource(ConfigBuilder.class, ""));
		System.out.println("ClassLoader for ConfigBuilder.class.getClass() = " + DPFileUtils.getResource(ConfigBuilder.class.getClass(), "/"));
	}
	
	public void build(Hashtable props, Writer writer) throws IOException, TemplateException {
		Template template = freemarker.getTemplate(templateName);
		template.process(props, writer);
		writer.flush();				
	}	
}
