package no.nav.datapower.config.freemarker.templates;

import java.net.URL;

import freemarker.cache.TemplateLoader;

public class DPTemplateLib {

	public static URL getResource(String resource) {
		return DPTemplateLib.class.getClass().getResource(resource);
	}
	
	public static TemplateLoader getTemplateLoader() {
//		return new TemplateLoader() {
//			TemplateLoader loader = new ClassTemplateLoader(DPTemplateLib.class.getClass(), "/lib");
//			public void closeTemplateSource(Object arg0) throws IOException {
//				System.out.println("DPLIBTemplateLoder.closeTemplateSource()");
//				loader.closeTemplateSource(arg0);
//			}
//
//			public Object findTemplateSource(String arg0) throws IOException {
//				Object returnValue = loader.findTemplateSource(arg0);
//				System.out.println("DPLIBTemplateLoader.findTemplateSource(), returned '" + returnValue + "' for template '" + arg0 + "'");
//				return returnValue;
//			}
//
//			public long getLastModified(Object arg0) {
//				long returnValue = loader.getLastModified(arg0);
//				System.out.println("DPLIBTemplateLoader.getLastModified(), returned '" + returnValue + "'");
//				return returnValue;
//			}
//
//			public Reader getReader(Object arg0, String arg1) throws IOException {
//				Reader reader = loader.getReader(arg0, arg1);
//				System.out.println("DPLIBTemplateLoader.getReader()");
//				return reader;
//			}
//		};
//		return new StreamTemplateLoader(DPTemplateLib.class.getClass(), "/lib/");
		return new StreamTemplateLoader(DPTemplateLib.class, "/lib/");
	}
}
