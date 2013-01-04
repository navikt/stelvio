import java.io.File;
import no.nav.maven.plugins.GenerateServiceRegistryFileMojo;
import no.nav.serviceregistry.ServiceRegistry;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Tester {
	
	//@Test
	public void test1() {
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		serviceRegistry.addServiceInstance("popp", "https://hostnavn.test.local:9443/", 
				"C:/Users/F133008/Kode/trunk/stelvio-int-maven/poms/maven-service-gw-provisioning-pom/wsdl-popp/nav-opptjening-tjeneste-1.1.2-wsdlif/nav-opptjening-tjeneste_OpptjeningWSEXP.wsdl");
		serviceRegistry.addServiceInstance("pselv", "https://hostnavn2.test.local:9443/", 
		"C:/Users/F133008/Kode/trunk/stelvio-int-maven/poms/maven-service-gw-provisioning-pom/wsdl-pselv/nav-tjeneste-oppholdsstatus-1.0.4-wsdlif/no/nav/virksomhet/tjenester/oppholdsstatus/v1/Binding.wsdl");
		serviceRegistry.addServiceInstance("tsys", "https://hostnavn.test.local:9443/", 
				"C:/Users/F133008/Kode/trunk/stelvio-int-maven/poms/maven-service-gw-provisioning-pom/wsdl-tsys/nav-fim-brukerprofil-tjenestespesifikasjon-0.0.1-alpha001/Brukerprofil.wsdl");
		File testfil = new File("C:/Users/F133008/Kode/trunk/stelvio-int-maven/poms/maven-service-gw-provisioning-pom/testFil");
		File oldServiceRegistryFile;
		
		try {
			testfil.mkdir();
			oldServiceRegistryFile = new File(testfil, "service-registry.xml");

			serviceRegistry.writeToFile(oldServiceRegistryFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		ServiceRegistry sr = new ServiceRegistry();
		try {
			sr = serviceRegistry.readServiceRegistry(oldServiceRegistryFile.toString());
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
		System.out.println(sr.toString());
	}
	
	//@Test
	public void test() throws MojoExecutionException, MojoFailureException {
		GenerateServiceRegistryFileMojo mojo = new GenerateServiceRegistryFileMojo();
		mojo.setServiceRegistryFile("C:/Users/F133008/Kode/trunk/stelvio-int-maven/poms/maven-service-gw-provisioning-pom/serviceregistry-simple.xml");
		mojo.execute();
		
	}
	

}
