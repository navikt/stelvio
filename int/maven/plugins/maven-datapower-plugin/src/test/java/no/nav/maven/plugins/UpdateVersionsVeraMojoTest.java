package no.nav.maven.plugins;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by K143569 on 22.09.14.
 */
public class UpdateVersionsVeraMojoTest{

    @Test @Ignore
    public void testNullDeployedBy(){
        UpdateVersionsVeraMojo mj = new UpdateVersionsVeraMojo();
        String domain = "partner-gw";
        String envConfigVersion = "1.0.2";
        String nonEnvConfigVersion = "1.0.3";
        String deployPomVersion = "1.0.4";
        String environment = "t2";
        String deployedBy = null;
        String veraURL = "http://confluence.adeo.no/dashboard.action";
        String envDomain = "trlala";
//        mj.updateVera(domain, envConfigVersion, nonEnvConfigVersion, deployPomVersion, environment, deployedBy, veraURL, envDomain);
    }
    @Test @Ignore
    public void testVera(){
        UpdateVersionsVeraMojo mj = new UpdateVersionsVeraMojo();
        String domain = "partner-gw";
        String envConfigVersion = "1.0.99";
        String nonEnvConfigVersion = "1.0.3";
        String deployPomVersion = "1.0.4";
        String environment = "u5";
        String deployedBy = "Mathilde ÆØÅ";

        System.out.println("Orginal: " + deployedBy);

       // byte[] tt = deployedBy.getBytes(Charset.forName("ISO-8859-1"));
        byte[] tt = deployedBy.getBytes(Charset.forName("UTF-8"));
        String newString = new String(tt);
        System.out.println("Ny: " + newString);
        System.out.println("Charset: " + Charset.defaultCharset());
//        byte[] tt = deployedBy.getBytes("UTF-8");
//        byte[] tt2 = deployedBy.getBytes(Charset.defaultCharset());


        String veraURL = "http://vera.adeo.no/version/";
        String envDomain = null;

//        mj.updateVera(domain, envConfigVersion, nonEnvConfigVersion, deployPomVersion, environment, deployedBy, veraURL, envDomain);
    }

    @Test @Ignore
    public void testSendNullVera(){
        UpdateVersionsVeraMojo mj = new UpdateVersionsVeraMojo();


//        String veraURL = "http://b27isvl001.preprod.local:9080/version";
//        String veraURL = "http://vera.adeo.no/version/";
        String application = "vera-test";
        String environment = "t1";
        String versionNumber = "1.2.3.4";
        String deployedBy = "Ã¦Ã¸Ã¥";
        System.out.print(deployedBy);
        Map<String,String> deployInfo = mj.createMap(application, versionNumber, environment, deployedBy);
        String deployInfoJson = mj.createJsonFromMap(deployInfo);
        System.out.println(deployInfoJson);
        //Do the POST call
//        mj.connectToURLAndPost(deployInfoJson, veraURL);
    }
}
