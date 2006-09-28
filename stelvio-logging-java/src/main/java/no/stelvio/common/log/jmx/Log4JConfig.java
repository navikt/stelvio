package no.stelvio.common.log.jmx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.apache.log4j.helpers.LogLog;

/**
 * Log4j configuration that may be managed through JMX.
 * 
 * @author person356941106810, Accenture
 * @version $Id: Log4JConfig.java 2796 2006-02-28 19:40:23Z skb2930 $
 */
public class Log4JConfig implements Log4JConfigMBean {
	private String currentConfig = null;
	private String fileName = null;

	/**
	 * Creates a new Log4J MBean.
	 * @param fileName the filename which Log4J reads
	 */
	public Log4JConfig(String fileName) {
		this.fileName = fileName;
		register();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLogConfig() {
		return currentConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLogConfig(String newConfig) {
		currentConfig = newConfig;

	}

	/**
	 * {@inheritDoc}
	 */
	public void load() throws Exception {
		final StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (null != line) {
				buffer.append(line).append("\n");
				line = reader.readLine();
			}
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				LogLog.error("Error closing reader reading from " + fileName);
			}
		}

		currentConfig = buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void save() throws Exception {
		// retrieve the location of the file to persist the bean configuration
		FileWriter writer = null;

		try {
			writer = new FileWriter(fileName, false);
			writer.write(currentConfig);
		} finally {
			try {
				if (null != writer) {
					writer.close();
				}
			} catch (IOException e) {
				LogLog.error("Error closing writer for " + fileName + ": " + e);
			}
		}

	}

	/**
	 * Register this Log4j configuraion in the JMX server 
	 */
	private void register() {
		List servers = MBeanServerFactory.findMBeanServer(null);
		MBeanServer server = null;

		if (servers == null || servers.size() == 0) {
			LogLog.debug("No MBeanServer was found, creating new MBeanServer");
			server = MBeanServerFactory.createMBeanServer();
		} else {
			server = (MBeanServer) servers.get(0);
		}

		ObjectName mbeanName = null;
		// make sure we only register the last portion of the filename
		String newFileName = fileName.substring(fileName.lastIndexOf('/') + 1);

		try {
			mbeanName = new ObjectName("Config:type=Log4j,filename=" + newFileName);
		} catch (MalformedObjectNameException e) {
			LogLog.error("Error creating ObjectName=\"Config:type=Log4j,filename=" + newFileName + "\"", e);
		}

		if (!server.isRegistered(mbeanName)) {
			LogLog.debug("Registering MBean with name:" + mbeanName);

			try {
				server.registerMBean(this, mbeanName);
			} catch (InstanceAlreadyExistsException e) {
				LogLog.error("MBean " + mbeanName + " already exists.", e);
			} catch (MBeanRegistrationException e) {
				LogLog.error("Error registering MBean " + mbeanName + ".", e);
			} catch (NotCompliantMBeanException e) {
				LogLog.error("MBean " + mbeanName + " does not comply to JMX standard.", e);
			}
		}
	}
}
