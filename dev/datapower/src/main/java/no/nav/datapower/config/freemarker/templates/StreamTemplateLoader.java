package no.nav.datapower.config.freemarker.templates;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import freemarker.cache.TemplateLoader;

public class StreamTemplateLoader implements TemplateLoader {

	private Class loaderClass;
	private String path;

	public StreamTemplateLoader(Class loaderClass, String path) {
		this.loaderClass = loaderClass;
		this.path = canonicalizePath(path);
	}
	
	
	public void closeTemplateSource(Object templateSource) throws IOException {
		//System.out.println("StreamTemplateLoader.closeTemplateSource(Object)");
		InputStream src = (InputStream) templateSource;
		src.close();
	}

	public Object findTemplateSource(String name) throws IOException {
		//System.out.println("StreamTemplateLoader.findTemplateSource(String), name = " + name);
		return loaderClass.getResourceAsStream(path + name);
	}

	public long getLastModified(Object templateSource) {
		//System.out.println("StreamTemplateLoader.getLastModified(Object)");
		return -1;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		//System.out.println("StreamTemplateLoader.getReader(Object, String)");
		return new InputStreamReader((InputStream) templateSource, encoding);
	}
	
	private String canonicalizePath(String path) {
		path = path.replace('\\', '/');
		if (path.length() > 0 && !path.endsWith("/"))
			path += "/";
		return path;
	}

}
