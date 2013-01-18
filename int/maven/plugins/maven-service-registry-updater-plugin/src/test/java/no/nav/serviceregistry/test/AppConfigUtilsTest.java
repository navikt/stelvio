package no.nav.serviceregistry.test;

import static org.junit.Assert.*;
import static no.nav.serviceregistry.test.util.TestUtils.getResource;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import no.nav.aura.appconfig.Application;
import no.nav.aura.appconfig.artifact.Artifact;
import no.nav.serviceregistry.util.AppConfigUtils;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

public class AppConfigUtilsTest {
	
	@Test
	public void testReadAppConfig() throws JAXBException {
		File appConfigFile = new File(getResource("/app-config.xml"));
		Application app = AppConfigUtils.readAppConfig(appConfigFile);
		assertEquals("autodeploy-test", app.getName());
	}

	@Test
	public void testParseApplicationsString() throws MojoExecutionException {
		HashSet<String> mockApps = new HashSet<String>();
		mockApps.add("pselv");
		mockApps.add("norg");
		mockApps.add("joark");
		Set<String> apps = AppConfigUtils.parseApplicationsString("pselv, norg,Joark");
		assertEquals(mockApps, apps);
	}

	@Test
	public void testEmpty() throws MojoExecutionException {
		assertTrue(AppConfigUtils.empty(""));
		assertTrue(AppConfigUtils.empty(null));
		assertTrue(AppConfigUtils.empty(new String()));
		assertFalse(AppConfigUtils.empty("sl"));
	}

	@Test
	public void testUnmarshalAppConfig() throws MojoExecutionException {
		Application app = AppConfigUtils.unmarshalAppConfig(getResource("/app-config.xml"));
		assertEquals("autodeploy-test", app.getName());
		Collection<Artifact> artifacts = app.getArtifacts();
		for (Artifact artifact : artifacts) {
			assertEquals("autodeploy-test-ear", artifact.getArtifactId());
			assertEquals("no.nav.aura.test", artifact.getGroupId());
		}
	}
}
