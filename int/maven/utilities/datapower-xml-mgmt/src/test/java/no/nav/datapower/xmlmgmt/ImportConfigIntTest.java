package no.nav.datapower.xmlmgmt;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

/**
 * Created by G133480 on 04.03.2015.
 */
public class ImportConfigIntTest {
    final Logger log = LoggerFactory.getLogger(ImportConfigIntTest.class);

    /** Integration test for uploading policy
     *
     */
    @Ignore
    @Test
    public void runImportConfigWithDeploymentPolicyIntegrationTest() throws URISyntaxException, XMLMgmtException {

        String domain = "t4_simpleloopbackservice";
        String response;

        XMLMgmtSession xmlMgmtSession = getXMLMgmtSession(domain);

        response = xmlMgmtSession.deleteDomain(domain);
        System.out.println(response);
        log.info(response);

        response = xmlMgmtSession.createDomain(domain);
        System.out.println(response);
        log.info(response);
        response = xmlMgmtSession.saveConfig();
        System.out.println(response);
        log.info(response);

        File depFile = new File(getClass().getClassLoader().getResource("deployment-policy.xml").toURI());
        response = xmlMgmtSession.importConfig(depFile, ImportFormat.XML);

        System.out.println(response);
        log.info(response);

//        response = xmlMgmtSession.saveConfig();
//        System.out.println(response);

        File configFile = new File(getClass().getClassLoader().getResource("configuration.xml").toURI());
        response = xmlMgmtSession.importConfig(configFile, ImportFormat.XML, "domain-policy");

        System.out.println(response);
        log.info(response);
    }

    /** Integration test for uploading policy
     *
     */
    @Ignore
    @Test
    public void runImportDeploymentPolicyIntegrationTest() throws URISyntaxException, XMLMgmtException {

        String domain = "utv-g133480";
        String response;

        XMLMgmtSession xmlMgmtSession = getXMLMgmtSession(domain);

        File depFile = new File(getClass().getClassLoader().getResource("testdeploymentpolicy.xml").toURI());
        response = xmlMgmtSession.importConfig(depFile, ImportFormat.XML);

        System.out.println(response);
        log.info(response);
    }


    /** Integration test for uploading policy
     *
     */
    @Ignore
    @Test
    public void runImportConfigDeploymentPolicyIntegrationTest() throws URISyntaxException, XMLMgmtException {

        String domain = "t4_simpleloopbackservice";
        String response;

        XMLMgmtSession xmlMgmtSession = getXMLMgmtSession(domain);

        File configFile = new File(getClass().getClassLoader().getResource("configuration.xml").toURI());
//        response = xmlMgmtSession.importConfig(configFile, ImportFormat.XML);
        response = xmlMgmtSession.importConfig(configFile, ImportFormat.XML, "domain-policy");

        System.out.println(response);
        log.info(response);
    }


    private XMLMgmtSession getXMLMgmtSession(String domain) {
        return new XMLMgmtSession.Builder("https://dp-tst-01.adeo.no:5550/service/mgmt/3.0").user("mavendeployer").password("Test1234").domain(domain).build();
//        return new XMLMgmtSession.Builder("https://a34dpva001.devillo.no:5550/service/mgmt/3.0").user("sysadmin").password("poc1234").domain(domain).build();
    }
}
