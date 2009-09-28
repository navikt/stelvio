package no.nav.maven.commons.configuration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import no.nav.maven.commons.constants.Constants;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationDocument;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ExclusionType;

import org.apache.xmlbeans.XmlException;

/**
 * @author test@example.com
 */
public final class ArtifactConfiguration {

	private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static final Lock read = readWriteLock.readLock();
	private static final Lock write = readWriteLock.writeLock();
	private static final HashMap<String, ConfigurationType> configuration = new HashMap<String, ConfigurationType>();
	private static final HashMap<String, ConfigurationType> envConfiguration = new HashMap<String, ConfigurationType>();

	public static final boolean isConfigurationLoaded() {

		read.lock();
		try {
			return (!configuration.isEmpty() || !envConfiguration.isEmpty());
		} finally {
			read.unlock();
		}
	}

	public static final void loadConfiguration(final File directory, final String environment) {
		write.lock();
		try {
			if ((configuration.isEmpty() == false) || (envConfiguration.isEmpty() == false)) {
				return;
			}
			loadXML(directory, environment);
		} finally {
			write.unlock();
		}
	}

	public static final ConfigurationType getConfiguration(final String artifactId) {
		read.lock();
		try {
			// TODO: This should be cloned.
			return configuration.get(artifactId);
		} finally {
			read.unlock();
		}
	}

	public static final ConfigurationType getEnvConfiguration(final String artifactId) {
		read.lock();
		try {
			// TODO: This should be cloned.
			return envConfiguration.get(artifactId);
		} finally {
			read.unlock();
		}
	}

	public static final List<ConfigurationType> getAllConfigurations(final String artifactId) {
		read.lock();
		try {
			List<ConfigurationType> allConfigurations = new ArrayList<ConfigurationType>();
			allConfigurations.addAll(getConfigurations(artifactId, configuration));
			allConfigurations.addAll(getConfigurations(artifactId, envConfiguration));
			return allConfigurations;
		} finally {
			read.unlock();
		}
	}

	private static List<ConfigurationType> getConfigurations(final String artifactId,
			HashMap<String, ConfigurationType> configurationsMap) {
		List<ConfigurationType> configurations = new ArrayList<ConfigurationType>();

		ConfigurationType configuration = null;

		StringTokenizer tokenizer = new StringTokenizer(artifactId, Constants.ARTIFACT_MODIFIER_SEPARATOR);
		while (tokenizer.hasMoreTokens()) {
			String tok = tokenizer.nextToken();
			configuration = configurationsMap.get(tok);
			addConfiguration(artifactId, configurations, configuration);
		}

		configuration = configurationsMap.get(artifactId);
		addConfiguration(artifactId, configurations, configuration);

		return configurations;
	}

	private static void addConfiguration(String artifactId, List<ConfigurationType> configurations,
			ConfigurationType configuration) {
		if (configuration != null) {
			if (configuration.getExclusions() != null) {
				for (ExclusionType exclusion : configuration.getExclusions().getExclusionList()) {
					if (exclusion.getArtifactId().equals(artifactId)) {
						// Exclusion for artifact found. Return from method.
						return;
					}
				}
			}
			configurations.add(configuration);
		}
	}

	/* TODO: This is so utterly stupid */
	private final static void loadXML(final File directory, final String environment) {
		ArrayList<File> files = new ArrayList<File>();

		File[] globFiles = directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(".xml");
			}
		});

		File envDirectory = new File(directory.getAbsoluteFile(), environment);
		File[] envFiles = envDirectory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && file.getName().endsWith(".xml");
			}
		});

		if (globFiles != null && globFiles.length > 0) {
			files.addAll(Arrays.asList(globFiles));
		}

		if (envFiles != null && envFiles.length > 0) {
			files.addAll(Arrays.asList(envFiles));
		}

		for (File f : files) {

			ConfigurationType config = null;
			try {
				config = ConfigurationDocument.Factory.parse(f).getConfiguration();
			} catch (XmlException e) {
				throw new RuntimeException("An XML exception occured reading file ", e);
			} catch (IOException e) {
				throw new RuntimeException("An IO exception occured reading file", e);
			}

			if (f.getParent().equals(envDirectory.getAbsolutePath())) {
				envConfiguration.put(f.getName().replace(".xml", ""), config);
			} else {
				configuration.put(f.getName().replace(".xml", ""), config);
			}
		}
	}
}
