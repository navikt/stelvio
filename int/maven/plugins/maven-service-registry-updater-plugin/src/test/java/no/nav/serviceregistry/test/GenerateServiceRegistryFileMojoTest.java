package no.nav.serviceregistry.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import no.nav.maven.plugins.GenerateServiceRegistryFileMojo;
import no.nav.serviceregistry.mocker.MyMocker;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class GenerateServiceRegistryFileMojoTest {
    // Helper method for get the file content
	private static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	@Test
	public void testTestableMojoExecutor() throws MojoExecutionException {
		String serviceRegistryFile = this.getClass().getResource("/serviceregistry-simple.xml").getFile();
		String orgServiceRegistryFile = this.getClass().getResource("/serviceregistry-simple.result.xml").getFile();
		MyMocker mavenMocker = new MyMocker();
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();

		mojo.testableMojoExecutor("u3", "http://test.url", "autodeploy-test", serviceRegistryFile, mavenMocker);
		
		List<String> orgServiceLines = fileToLines(orgServiceRegistryFile);
		List<String> serviceLines = fileToLines(serviceRegistryFile);
		System.out.println(orgServiceRegistryFile + serviceRegistryFile);
		assertArrayEquals(orgServiceLines.toArray(), serviceLines.toArray());
	}
	
	@Test(expected=RuntimeException.class) //TODO: her burde Platform teamet ha sendt en egendefinert exception
	public void testEnvConfigNotAccesible() throws MojoExecutionException, FileNotFoundException{
		String serviceRegistryFile = this.getClass().getResource("/serviceregistry-simple.xml").getFile();
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();
		mojo.testableMojoExecutor("u3", "http://vg.no", "autodeploy-test", serviceRegistryFile, null);	
	}
}
