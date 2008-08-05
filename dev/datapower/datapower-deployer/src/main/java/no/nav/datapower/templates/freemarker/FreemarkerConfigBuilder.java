package no.nav.datapower.templates.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.List;

import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.util.DPCollectionUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerConfigBuilder {

	private Configuration freemarker;

	private static final TemplateLoader DEFAULT_TEMPLATE_LOADER = new ClassTemplateLoader(FreemarkerConfigBuilder.class, "");
	
	public FreemarkerConfigBuilder() {
		freemarker = configureFreemarker(DEFAULT_TEMPLATE_LOADER);
	}
	
	public FreemarkerConfigBuilder(File templateDir) {
		FileTemplateLoader ftl = null;
		try {
			ftl = new FileTemplateLoader(templateDir);
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
		freemarker = configureFreemarker(DEFAULT_TEMPLATE_LOADER, ftl);
	}
	
	public FreemarkerConfigBuilder(TemplateLoader... loaders) {
		freemarker = configureFreemarker(loaders);
	}
	
	private Configuration configureFreemarker(TemplateLoader... loaders) {
		Configuration cfg = new Configuration();		
		cfg.setTemplateLoader(new MultiTemplateLoader(loaders));
		cfg.setWhitespaceStripping(true);
		cfg.setObjectWrapper(new DefaultObjectWrapper());			
		return cfg;
	}

	public void buildConfig(String templateFile, Hashtable props, List<File> wsdlFiles, Writer writer) throws IOException, TemplateException {
		props.put("wsdls", getWsdlFileList(wsdlFiles));
		processTemplate(templateFile, props, writer);
	}
	
	public void buildAAAInfoFile(String templateFile, Hashtable props, Writer writer) throws IOException, TemplateException {
		processTemplate(templateFile, props, writer);
	}
	
	private void processTemplate(String templateFile, Hashtable props, Writer writer) throws IOException, TemplateException {
		Template template = freemarker.getTemplate(templateFile);
		template.process(props, writer);
		writer.flush();				
	}
	
	private List<WSDLFile> getWsdlFileList(List<File> wsdlFiles) {
		List<WSDLFile> proxyList = DPCollectionUtils.newArrayList();
		for (File wsdl : wsdlFiles) {
			proxyList.add(new WSDLFile(wsdl));
		}
		return proxyList;
	}
}
