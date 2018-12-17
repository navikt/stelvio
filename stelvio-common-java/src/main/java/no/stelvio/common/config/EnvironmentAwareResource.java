package no.stelvio.common.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jndi.JndiTemplate;

/**
 * Spring resource which adds functionality for environment specific locations of property files. A Spring bean of this class
 * can for example be passed as a location to a Spring PropertyPlaceholderConfigurer.
 * 
 * The optional property <code>environmentKey</code> can be induced. If this property is set, the environment name is looked up
 * from jndi under specified jndi name. If the jndi name does not exist, the location is used as is. The environment name is
 * appended to the location. The property is appended before the text '.properties'. If the location text does not contain this
 * word, the environment property is appended at the end of the location.
 * 
 * Examples, assume that environment name has value <code>komptest</code>':
 * <ul>
 * <li>location=rep-opptjening-environment.properties gives environment specific location
 * rep-opptjening-environment.komptest.properties</li>
 * <li>location=rep-opptjening-environment gives environment specific location rep-opptjening-environment.komptest</li>
 * </ul>
 * 
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 * 
 * @author person727e2beea31f, Sirius IT
 * @version $Id $
 */
public class EnvironmentAwareResource implements Resource, ResourceLoaderAware {

	private Resource resourceImpl;
	private ResourceLoader resourceLoader;
	private JndiTemplate jndiTemplate;
	private String location;
	private String environmentKey;
	private String environmentName;

	private Log log = LogFactory.getLog(EnvironmentAwareResource.class);

	/**
	 * Sets the JndiTemplate. (SpringDI)
	 * 
	 * @param jndiTemplate
	 *            the jndiTemplate to set
	 */
	public void setJndiTemplate(JndiTemplate jndiTemplate) {
		this.jndiTemplate = jndiTemplate;
	}

	/**
	 * Implementation of ResourceLoaderAware interface. Method is called by Spring upon initialization.
	 * 
	 * @param resourceLoader
	 *            the resource loader
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Injection of location attribute. This attribute is mandatory. (SpringDI)
	 * 
	 * @param location
	 *            the location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Injection of environmentKey attribute. This attribute is optional. (SpringDI)
	 * 
	 * @param environmentKey
	 *            the environment key
	 */
	public void setEnvironmentKey(String environmentKey) {
		this.environmentKey = environmentKey;
	}

	/**
	 * Gets a location specific to the environment. If no environmentName is set, the basic location is returned.
	 * 
	 * @return The environment specific location.
	 */
	private String getEnvironmentSpecificLocation() {
		String result;
		if (environmentKey == null) {
			environmentName = null;
		} else {
			try {
				environmentName = (String) jndiTemplate.lookup(environmentKey);
			} catch (NamingException ex) {
				environmentName = null;
				log.warn("Could not obtain environment specific location for properties file (environmentKey=" + environmentKey
						+ ". Error=" + ex.getMessage());
			}
		}
		if (environmentName == null || location == null) {
			result = location;
		} else {
			final String mask = ".properties";
			StringBuffer resultBuffer;
			int maskIndex = location.lastIndexOf(mask);
			if (maskIndex == -1) {
				// No mask found in location, append environmentName at the end
				resultBuffer = new StringBuffer(location);
				resultBuffer.append(".");
				resultBuffer.append(environmentName);
			} else {
				// Append environmentName before mask.
				resultBuffer = new StringBuffer(location.substring(0, maskIndex));
				resultBuffer.append(".");
				resultBuffer.append(environmentName);
				resultBuffer.append(mask);
			}
			result = resultBuffer.toString();
		}
		return result;
	}

	/**
	 * Called by Spring upon initialization. The method validates that the required dependency injection is properly configured.
	 * In addition, this method configures the correct location.
	 * 
	 * @throws MissingPropertyException
	 *             If a dependency injection property is missing from the configuration.
	 */
	public void init() throws MissingPropertyException {
		List<String> propertyList = new ArrayList<>();
		if (location == null) {
			propertyList.add("location");
		}
		if (resourceLoader == null) {
			propertyList.add("resourceLoader");
		}
		if (jndiTemplate == null) {
			propertyList.add("jndiTemplate");
		}
		if (!propertyList.isEmpty()) {
			throw new MissingPropertyException(
					"Required properties for environment aware resources was not set by configuration", propertyList);
		}

		String environmentSpecificLocation = getEnvironmentSpecificLocation();
		log.info("Configuring application with property file=" + environmentSpecificLocation);
		resourceImpl = resourceLoader.getResource(environmentSpecificLocation);
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @param arg0
	 *            the relative path to the resource
	 * @return the resource
	 * @throws IOException
	 *             if an IOException occurs while creating the resource
	 */
	public Resource createRelative(String arg0) throws IOException {
		return resourceImpl.createRelative(arg0);
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return true if the resource exists, false otherwise
	 */
	public boolean exists() {
		return resourceImpl.exists();
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return the resource's description
	 */
	public String getDescription() {
		return resourceImpl.getDescription();
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return the resource's file
	 * @throws IOException
	 *             if an IOException occurs while getting the file
	 */
	public File getFile() throws IOException {
		return resourceImpl.getFile();
	}

    public long contentLength() throws IOException {
        return getFile().length();
    }

    /**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return the filename for the resource
	 */
	public String getFilename() {
		return resourceImpl.getFilename();
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return the URL for the resource
	 * @throws IOException
	 *             if an IOException occurs while getting the URL
	 */
	public URL getURL() throws IOException {
		return resourceImpl.getURL();
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return true if the resource is open, false otherwise
	 */
	public boolean isOpen() {
		return resourceImpl.isOpen();
	}

	/**
	 * Implementation of org.springframework.core.io.Resource.
	 * 
	 * @return the input stream for the resource
	 * @throws IOException
	 *             if an IOException occurs while getting the input stream
	 */
	public InputStream getInputStream() throws IOException {
		return resourceImpl.getInputStream();
	}

	/**
	 * Return resource's last modified.
	 * 
	 * @return last modified
	 * @throws IOException
	 *             io-exception
	 */
	public long lastModified() throws IOException {
		return resourceImpl.lastModified();
	}

	/**
	 * Return resource's uri.
	 * 
	 * @return uri
	 * @throws IOException
	 *             io-exception
	 */
	public URI getURI() throws IOException {
		return resourceImpl.getURI();
	}

	/**
	 * Return resource's readable.
	 * 
	 * @return readable
	 */
	public boolean isReadable() {
		return resourceImpl.isReadable();
	}

}
