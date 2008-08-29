package no.nav.datapower.config.freemarker.templates;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.log4j.Logger;

import freemarker.cache.TemplateLoader;

public class StreamTemplateLoader implements TemplateLoader {

	private static final Logger LOG = Logger.getLogger(StreamTemplateLoader.class);
	private Class loaderClass;
	private String path;

	public StreamTemplateLoader(Class loaderClass, String path) {
		this.loaderClass = loaderClass;
		this.path = canonicalizePath(path);
	}
	
	
	public void closeTemplateSource(Object templateSource) throws IOException {
		LOG.trace("closeTemplateSource(Object)");
		InputStream src = (InputStream) templateSource;
		src.close();
	}

	public Object findTemplateSource(String name) throws IOException {
		LOG.trace("findTemplateSource(String), name = " + name);
		return loaderClass.getResourceAsStream(path + name);
	}

	public long getLastModified(Object templateSource) {
		LOG.trace("getLastModified(Object)");
		return -1;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		LOG.trace("getReader(Object, String)");
		return new InputStreamReader((InputStream) templateSource, encoding);
	}
	
	private String canonicalizePath(String path) {
		path = path.replace('\\', '/');
		if (path.length() > 0 && !path.endsWith("/"))
			path += "/";
		return path;
	}

}
