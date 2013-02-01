package no.nav.serviceregistry.test;

import static org.junit.Assert.*;

import no.nav.aura.appconfig.exposed.Service;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.serviceregistry.exception.ApplicationConfigException;
import no.nav.serviceregistry.util.MvnArtifact;

import org.junit.Test;

public class MvnArtifactTest {
	String artifactId = "test-ArtifactId";
	String groupId = "test-groupId";
	String version = "0.0.0";
	String type = "test-type";

	@Test
	public void testMvnArtifactStringStringStringString() {
		MvnArtifact mvnArtifact = new MvnArtifact(artifactId, groupId, version, type);
		assertEquals(artifactId, mvnArtifact.getArtifactId());
		assertEquals(groupId, mvnArtifact.getGroupId());
		assertEquals(version, mvnArtifact.getVersion());
		assertEquals(type, mvnArtifact.getType());
	}

	@Test
	public void testMvnArtifactApplicationInfoString() {
		ApplicationInfo applicationInfo = new ApplicationInfo();
		applicationInfo.setAppConfigArtifactId(artifactId);
		applicationInfo.setAppConfigGroupId(groupId);
		applicationInfo.setVersion(version);
		MvnArtifact mvnArtifact = new MvnArtifact(applicationInfo, type);
		assertEquals(artifactId, mvnArtifact.getArtifactId());
		assertEquals(groupId, mvnArtifact.getGroupId());
		assertEquals(version, mvnArtifact.getVersion());
		assertEquals(type, mvnArtifact.getType());
	}

	@Test
	public void testMvnArtifactServiceString() {
		Service service = new Service();
		service.setWsdlArtifactId(artifactId);
		service.setWsdlGroupId(groupId);
		service.setWsdlVersion(version);
		MvnArtifact mvnArtifact = new MvnArtifact(service, type);
		assertEquals(artifactId, mvnArtifact.getArtifactId());
		assertEquals(groupId, mvnArtifact.getGroupId());
		assertEquals(version, mvnArtifact.getVersion());
		assertEquals(type, mvnArtifact.getType());
	}
	
	@Test(expected=ApplicationConfigException.class)
	public void testMvnArtifactMissingInput() {
		Service service = new Service();
		service.setWsdlArtifactId("");
		service.setWsdlGroupId(groupId);
		service.setWsdlVersion(version);
		new MvnArtifact(service, type);
	}

}
