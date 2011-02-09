package no.nav.maven.plugin.serviceregistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentProperties extends Properties {

	private static final long serialVersionUID = 1L;

	public EnvironmentProperties(File file) throws FileNotFoundException, IOException {
		load(new FileInputStream(file));
	}

}
