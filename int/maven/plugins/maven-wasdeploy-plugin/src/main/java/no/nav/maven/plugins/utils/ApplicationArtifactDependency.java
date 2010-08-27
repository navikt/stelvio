package no.nav.maven.plugins.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;

/**
 * 
 * Class that provides a list of artifacts, and serves as a definition point of 
 * application artifact dependencies and whether the application contains batch.
 * 
 * @author test@example.com
 * 
 */

public class ApplicationArtifactDependency {

	// Definition point of which applications that contains batch
	private final static String[] allSupportedApplications = {"pen", "psak", "pselv", "preg", "popp", "mot", "joark", "medl", "sam", "tp", "komp", "inst"};
	
	// Definition point of which applications that contains batch
	private final static String[] batchApplications = {"pen", "popp", "mot", "joark", "medl", "sam"};
	
	// Returns a list of complete Maven artifact objects for the given application and version
	public static List<Artifact> getApplicationArtifacts(ArtifactFactory artifactory, String application, String version) {

		List<Artifact> artifacts = new ArrayList<Artifact>();
		
		/**
		 * Defines the artifacts (groupId, artifactId) related to each application. 
		 */
		if (application.equals("pen")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.pen-layers.jee", "nav-pensjon-pen-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.pen-layers.config", "nav-config-pensjon-pen", version, null, "jar"));
			// Application batch-client JAR
			artifacts.add(artifactory.createArtifact("no.nav.pen-layers.batch", "nav-batch-pensjon-pen-client", version, null, "jar"));

		} else if (application.equals("psak")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.psak-layers.jee", "nav-pensjon-psak-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.psak-layers.config", "nav-config-pensjon-psak", version, null, "jar"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.pen-layers.config", "nav-config-pensjon-pen", version, null, "jar"));

		} else if (application.equals("pselv")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.pselv-layers.jee", "nav-pensjon-pselv-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.pselv-layers.config", "nav-config-pensjon-pselv", version, null, "jar"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.pen-layers.config", "nav-config-pensjon-pen", version, null, "jar"));

		} else if (application.equals("popp")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.popp-layers.jee", "nav-pensjon-popp-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.popp-layers.config", "nav-config-pensjon-popp", version, null, "jar"));
			// Application batch-client JAR
			artifacts.add(artifactory.createArtifact("no.nav.popp-layers.batch", "nav-batch-pensjon-popp-client", version, null, "jar"));

		} else if (application.equals("preg")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.preg-layers.jee", "nav-service-pensjon-regler-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.preg-layers.config", "nav-config-pensjon-regler", version, null, "jar"));

		} else if (application.equals("medl")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.medl-layers.jee", "nav-service-fellesregister-medl-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.medl-layers.config", "nav-config-pensjon-medl", version, null, "jar"));
			// Application batch-client JAR
			artifacts.add(artifactory.createArtifact("no.nav.medl-layers.batch", "nav-batch-fellesregister-medl-client", version, null, "jar"));

		} else if (application.equals("mot")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.mot-layers.jee", "nav-okonomi-mot-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.mot-layers.config", "nav-config-okonomi-mot", version, null, "jar"));
			// Application batch-client JAR
			artifacts.add(artifactory.createArtifact("no.nav.mot-layers.batch", "nav-batch-okonomi-mot-client", version, null, "jar"));

		} else if (application.equals("tp")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.tp-layers.jee", "nav-fellesregister-tp-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.tp-layers.config", "nav-config-fellesregister-tp", version, null, "jar"));

		} else if (application.equals("joark")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.joark-layers.jee", "nav-dok-joark-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.joark-layers.config", "nav-config-dok-joark", version, null, "jar"));
			// Application batch-client JAR
			artifacts.add(artifactory.createArtifact("no.nav.joark-layers.batch", "nav-batch-dok-joark-client", version, null, "jar"));

		} else if (application.equals("sam")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.sam-layers.jee", "nav-stotte-sam-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.sam-layers.config", "nav-config-stotte-sam", version, null, "jar"));
			// Application batch-client JAR
			artifacts.add(artifactory.createArtifact("no.nav.sam-layers.batch", "nav-batch-stotte-sam-client", version, null, "jar"));

		} else if (application.equals("komp")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.trafikanten-layers.jee", "nav-trafikanten-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.trafikanten-layers.config", "nav-config-trafikanten", version, null, "jar"));

		} else if (application.equals("inst")) {

			// Application EAR
			artifacts.add(artifactory.createArtifact("no.nav.inst-layers.jee", "nav-fellesregister-inst-jee", version, null, "ear"));
			// Application configuration JAR
			artifacts.add(artifactory.createArtifact("no.nav.inst-layers.config", "nav-config-fellesregister-inst", version, null, "jar"));
		}

		return artifacts;
	}
	
	// Returns true or false based on whether the application contains batch
	public static boolean isBatch(String application) {
		
		for (int i = 0; i < batchApplications.length; i++) {
			if (batchApplications[i].equals(application))
				return true;
		}
		return false;
	}
	
	// Returns true or false based on whether the application is supporte
	public static boolean isSupported(String application) {
		
		for (int i = 0; i < allSupportedApplications.length; i++) {
			if (allSupportedApplications[i].equals(application))
				return true;
		}
		return false;
	}
}
