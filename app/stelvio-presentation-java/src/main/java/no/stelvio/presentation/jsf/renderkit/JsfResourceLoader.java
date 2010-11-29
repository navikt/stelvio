package no.stelvio.presentation.jsf.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.ResourceLoader;
import org.apache.myfaces.shared_tomahawk.util.ClassUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.net.URL;
import java.net.URLConnection;

/**
 * A ResourceLoader capable of fetching resources from the classpath, but only
 * for classes under package no.stelvio.presentation.jsf.taglib. This
 * implementation is based on the myfaces tomahawk MyFacesResourceLoader
 * implementation
 * 
 * @author personf0169700350c, myfaces
 * @author person6045563b8dec, Accenture
 * @version $Id$
 */
public class JsfResourceLoader implements ResourceLoader {
	/** Logger. */
	protected static final Log LOG = LogFactory
			.getLog(JsfResourceLoader.class);

	static final String NO_NAV_PRESENTATION_PENSJON_JSF_TAGLIB = "no.nav.presentation.pensjon.common.taglib";

	private static long lastModified = 0;

	/**
	 * Get the last-modified time of the resource.
	 * <p>
	 * Unfortunately this is not possible with files inside jars. Instead, the
	 * MyFaces build process ensures that there is a file AddResource.properties
	 * which has the datestamp of the time the build process was run. This
	 * method simply gets that value and returns it.
	 * <p>
	 * Note that this method is not related to the generation of "cache key"
	 * values by the AddResource class, nor does it affect the caching behaviour
	 * of web browsers. This value simply goes into the http headers as the
	 * last-modified time of the specified resource.
	 * 
	 * @return last modified time 
	 */
	private static long getLastModified() {
		if (lastModified == 0) {
			final String format = "yyyy-MM-dd HH:mm:ss Z"; // Must match the
			// one used in the
			// build file
			final String bundleName = AddResource.class.getName();
			ResourceBundle resources = ResourceBundle.getBundle(bundleName);
			String sLastModified = resources.getString("lastModified");
			try {
				lastModified = new SimpleDateFormat(format)
						.parse(sLastModified).getTime();
			} catch (ParseException e) {
				lastModified = new Date().getTime();
				LOG.warn("Unparsable lastModified : " + sLastModified);
			}
		}

		return lastModified;
	}

	/**
	 * Given a URI of form "{partial.class.name}/{resourceName}", locate the
	 * specified file within the current classpath and write it to the response
	 * object.
	 * <p>
	 * The partial class name has "no.stelvio.presentation.jsf.taglib."
	 * prepended to it to form the fully qualified classname. This class object
	 * is loaded, and Class.getResourceAsStream is called on it, passing a uri
	 * of "resource/" + {resourceName}.
	 * <p>
	 * The data written to the response stream includes http headers which
	 * define the mime content-type; this is deduced from the filename suffix of
	 * the resource.
	 * <p>
	 * 
	 * @param context the servlet context
	 * @param request the current http request
	 * @param response the current http response
	 * @param resourceUri the uri of the resource to locate
	 * @throws IOException if an error occurs during input/output
	 * 
	 * @see org.apache.myfaces.renderkit.html.util.ResourceLoader#serveResource(javax.servlet.ServletContext,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void serveResource(ServletContext context,
								HttpServletRequest request, 
								HttpServletResponse response,
								String resourceUri) throws IOException {
		String[] uriParts = resourceUri.split("/", 2);

		String component = uriParts[0];
		if (component == null || component.trim().length() == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid request");
			LOG
					.error("Could not find parameter for component to load a resource.");
			return;
		}
		Class componentClass;
		String className = NO_NAV_PRESENTATION_PENSJON_JSF_TAGLIB + "." + component;
		try {
			componentClass = loadComponentClass(className);
		} catch (ClassNotFoundException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e
					.getMessage());
			LOG.error("Could not find the class for component " + className
					+ " to load a resource.");
			return;
		}
		String resource = uriParts[1];
		if (resource == null || resource.trim().length() == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"No resource defined");
			LOG.error("No resource defined component class " + className);
			return;
		}
		resource = "resource/" + resource;

		InputStream is = null;

		try {

			URL url = componentClass.getResource(resource);

			if (url == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND,
						"Unable to find resource " + resource
								+ " for component " + component
								+ ". Check that this file is available "
								+ "in the classpath in sub-directory "
								+ "/resource of the package-directory.");
				LOG.error("Unable to find resource " + resource
						+ " for component " + component
						+ ". Check that this file is available "
						+ "in the classpath in sub-directory "
						+ "/resource of the package-directory.");
			} else {
				URLConnection con = url.openConnection();
				int contentLength = con.getContentLength();

				is = con.getInputStream();

				defineContentHeaders(request, response, resource, contentLength);
				defineCaching(request, response, resource);
				writeResource(request, response, is);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Copy the content of the specified input stream to the servlet response.
	 * 
	 * @param request the current http request
	 * @param response the current http response object
	 * @param in the inputstream to write the resource to
	 * @throws IOException if an error occurs when the resource is written 
	 */
	protected void writeResource(HttpServletRequest request,
								HttpServletResponse response, 
								InputStream in) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		try {
			byte[] buffer = new byte[1024];
			for (int size = in.read(buffer); size != -1; size = in.read(buffer)) {
				out.write(buffer, 0, size);
			}
		} finally {
			out.close();
		}
	}

	/**
	 * Output http headers telling the browser (and possibly intermediate
	 * caches) how to cache this data.
	 * <p>
	 * The expiry time in this header info is set to 7 days. This is not a
	 * problem as the overall URI contains a "cache key" that changes whenever
	 * the webapp is redeployed (see AddResource.getCacheKey), meaning that all
	 * browsers will effectively reload files on webapp redeploy.
	 * 
	 * @param request the current http request object
	 * @param response the current http response object
	 * @param resource the resource which is beeing loaded
	 */
	protected void defineCaching(HttpServletRequest request,
								HttpServletResponse response, 
								String resource) {
		response.setDateHeader("Last-Modified", getLastModified());

		Calendar expires = Calendar.getInstance();
		expires.add(Calendar.DAY_OF_YEAR, 7);
		response.setDateHeader("Expires", expires.getTimeInMillis());

		// 12 hours: 43200 = 60s * 60 * 12
		response.setHeader("Cache-Control", "max-age=43200");
		response.setHeader("Pragma", "");
	}

	/**
	 * Output http headers indicating the mime-type of the content being served.
	 * The mime-type output is determined by the resource filename suffix.
	 * 
	 * @param request the current http request object
	 * @param response the current http response object
	 * @param resource the resource beeing loaded
	 * @param contentLength the length of the resource beeing loaded
	 */
	protected void defineContentHeaders(HttpServletRequest request,
										HttpServletResponse response, 
										String resource, 
										int contentLength) {
		if (contentLength > -1) {
			response.setContentLength(contentLength);
		}

		if (resource.endsWith(".js")) {
			response
					.setContentType(org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT);
		} else if (resource.endsWith(".css")) {
			response
					.setContentType(org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.STYLE_TYPE_TEXT_CSS);
		} else if (resource.endsWith(".gif")) {
			response.setContentType("image/gif");
		} else if (resource.endsWith(".png")) {
			response.setContentType("image/png");
		} else if (resource.endsWith(".jpg") || resource.endsWith(".jpeg")) {
			response.setContentType("image/jpeg");
		} else if (resource.endsWith(".xml") || resource.endsWith(".xsl")) {
			response.setContentType("text/xml"); // XSL has to be served as
			// XML.
		}
	}

	/**
	 * Loads the component class.
	 * 
	 * @param componentClass
	 *            the class to be loaded
	 * @return the Class loaded
	 * @throws ClassNotFoundException if the component class is not found
	 */
	protected Class loadComponentClass(String componentClass)
			throws ClassNotFoundException {
		return ClassUtils.classForName(componentClass);
	}


	/**
	 * Gets the current class loader.
	 * 
	 * @param defaultObject the default object
	 * @return the ClassLoader
	 */
	protected static ClassLoader getCurrentLoader(Object defaultObject) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}
		return loader;
	}

}