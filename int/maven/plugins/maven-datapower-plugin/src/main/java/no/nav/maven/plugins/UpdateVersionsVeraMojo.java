package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONValue;


/**
 * Goal which updates Vera with version numbers.
 *
 * @goal updateVersionsVera
 * 
 */
public class UpdateVersionsVeraMojo extends AbstractDeviceMgmtMojo {
	
	/**
	 * @parameter
	 */
	private String envConfigVersion;

    /**
     * @parameter
     */
    private String nonEnvConfigVersion;

    /**
     * @parameter
     */
    private String deployPomVersion;

    /**
     * @parameter
     */
    private String environment;
    /**
     * @parameter
     */
    private String veraURL;
    /**
     * @parameter
     */
    private String envDomain;


    /**
     * @parameter
     */
    private String deployedBy;

    /**
     * @parameter
     */
    private Boolean testRun;

    private String testApplication;

	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
            updateVera(getDomain(), envConfigVersion, nonEnvConfigVersion, deployPomVersion, environment, deployedBy, veraURL, envDomain, testRun);
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to write config version for domain " + getDomain() + "'",e);
		}
	}
    protected void updateVera(String thisDomain, String envConfigVersion, String nonEnvConfigVersion, String deployPomVersion, String environment, String deployedBy, String veraURL, String envDomain, boolean testRun){
        /*
        This method updates three rows in Vera, one for each of the versionnumbers (environment-config-version, nonenvironment-config-version, deploy-pom-version).
        The application names for a domain are dp-domain-env, dp-domain-nonenv, dp-domain-pom.
        Vera is updated by three HTTP Post call, where the body is JSON.
         */
        //Identify the sone, and decide if the application name should contain the sone.
        String veraApplicationPrefix = buildVeraApplicationPrefix(thisDomain,envDomain);

        //Need to make three POST calls towards vera.
        //For convenience we put the info in a map called allVersions
        Map<String, String> allVersions = new LinkedHashMap<String, String>();
        allVersions.put("env", envConfigVersion);
        allVersions.put("nonenv", nonEnvConfigVersion);
        allVersions.put("pom", deployPomVersion);

        //Iterate the map allVersions and for each iteration make a POST
        for(Map.Entry<String, String> entry : allVersions.entrySet()){
            ////////////////////  TEST  start/////////////////////////
            if(testRun){
                //Create the JSON string as if it was not a test, but do not make the post call
                String application = veraApplicationPrefix + "-" + entry.getKey();
                //Create the body in JSON as if not a test
                Map<String,String> deployInfo = createMap(application, entry.getValue(), environment, deployedBy);
                String deployInfoJson = createJsonFromMap(deployInfo);
                getLog().debug("Actual domain info: " + deployInfoJson);

                //Create JSON string for test
                environment = "t1";
                testApplication = "vera-test";
                //Create the testbody in JSON
                Map<String,String> deployInfoTest = createMap(testApplication, entry.getValue(), environment, deployedBy);
                String deployInfoTestJson = createJsonFromMap(deployInfoTest);
                getLog().debug(deployInfoTestJson);

                //Do the POST call
                connectToURLAndPost(deployInfoTestJson, veraURL);
             ////////////////////  TEST  stop/////////////////////////
            }else{
                //Build the application name
                String application = veraApplicationPrefix + "-" + entry.getKey();
                //Create the body in JSON
                Map<String,String> deployInfo = createMap(application, entry.getValue(), environment, deployedBy);
                String deployInfoJson = createJsonFromMap(deployInfo);
                getLog().debug(deployInfoJson);
                //Do the POST call
                connectToURLAndPost(deployInfoJson, veraURL);
            }
        }
    }

    protected String buildVeraApplicationPrefix(String thisDomain, String envDomain) {
        /*
        This method builds the prefix to the application name that is shown in Vera.
        For a generic domain the prefix should be "dp-domain", but a few of the domains can be in both sones (fss, sbs).
        This needs to be represented in the application name.
        The prefix for domains that exists in both sones should be "dp-domain-sone".
        The 3gen domains can be in both sones (sbs and fss).
        The 1or2gen domains get the name "dp-domain", the 3gen domains get the name "dp-domain-sone".
         */
        boolean domainIs3Gen = checkIfDomainIs3Gen(envDomain);
        String veraPrefix = "dp-" + thisDomain;
        if(domainIs3Gen){
            String sone = identifySone(thisDomain, envDomain);
            veraPrefix += "-" + sone;
        }
        return veraPrefix;
    }

    protected boolean checkIfDomainIs3Gen(String envDomain){
        boolean domainIs3Gen = (envDomain != null);
        return domainIs3Gen;
    }
    protected String identifySone(String thisDomain, String envDomain){
        /*
        This method identifies which sone the domain is deployed to. Either fss or sbs.
         */
        String sone = "";
        boolean domainIs3Gen = (envDomain != null);

        // only 3gen domains can be in both sones, have to check envDomain
        if(domainIs3Gen){
            //check if envDomain contains the word oera
            if(envDomain.toLowerCase().contains("oera".toLowerCase())){
                sone = "sbs";
            }else{
                sone = "fss";
            }
        }else{
            //sikkerhetslytter is the only 1or2gen domain in fss sone
            if((thisDomain.toLowerCase()).equals("sikkerhetslytter")){
                sone = "fss";
            }else{
                sone = "sbs";
            }
        }
        return sone;
    }

    protected Map<String,String> createMap(String application, String version, String environment, String deployedBy){
        /*
        This method creates a LinkedHashMap of the information that goes in the body of the HTTP POST call.
        The body should for instance look like this:
        {
         "application": "dp-service-gw-sbs-env",
         "version": "1.0.0.1",
         "environment": "t1",
         "deployedBy": "Kamperud, Mathilde"
        }
         */
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("application", application);
        map.put("version",version);
        map.put("environment", environment);
        map.put("deployedBy", deployedBy);
        return map;
    }


    protected String createJsonFromMap(Map <String, String> map){
        /*
        This method takes a simple map <String,String> and converts it to a JSON string.
         */
        StringWriter out = new StringWriter();
        String jsonText = "";
        try {
            JSONValue.writeJSONString(map, out);
            jsonText = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    protected HttpURLConnection connectToURLAndPost(String generatedJsonString, String path){
        /*
        This method connects to the path and HTTP POSTs a JSON body.
         */
        URL url;
        HttpURLConnection conn = null;
        try{
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //header
            conn.setRequestProperty("Content-Type", "application/json");


            //send HTTP-request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(generatedJsonString);
            wr.flush();
            wr.close();

            getLog().debug("Made a Http-post towards " + path + ". Response code = " + conn.getResponseCode() + ", Response message = " + conn.getResponseMessage());
        }catch(IOException e){
            e.printStackTrace();
        }
        return conn;
    }
}
