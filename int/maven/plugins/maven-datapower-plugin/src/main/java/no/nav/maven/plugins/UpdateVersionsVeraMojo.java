package no.nav.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONValue;


/**
 * Goal which updates Vera with version numbers.
 *
 * @goal updateVersionsVera
 *
 * @author Mathilde Nygaard Kamperud
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


	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		try {
            updateVera(getDomain(), envConfigVersion, nonEnvConfigVersion, deployPomVersion, environment, deployedBy, veraURL, envDomain);
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to write config version for domain " + getDomain() + "'",e);
		}
	}
    protected void updateVera(String thisDomain, String envConfigVersion, String nonEnvConfigVersion, String deployPomVersion, String environment, String deployedBy, String veraURL, String envDomain){
        /*
        This method updates three rows in Vera, one for each of the version number (environment-config-version, nonenvironment-config-version, deploy-pom-version).
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
            String versionType = entry.getKey(); // env, nonenv or pom
            String versionNumber = entry.getValue();
            //Build the application name
            String application = veraApplicationPrefix + "-" + versionType;
            //Create the body in JSON
            Map<String,String> deployInfo = createMap(application, versionNumber, environment, deployedBy);
            String deployInfoJson = createJsonFromMap(deployInfo);
            //Do the POST call
            connectToURLAndPost(deployInfoJson, veraURL);

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
        String veraPrefix = "dp-" + thisDomain;
        String nameAddition = getNameAdditionSone(envDomain); //if the domain exists in both sones
        veraPrefix += nameAddition;

        return veraPrefix;
    }


    protected String getNameAdditionSone(String envDomain){
        /*
            This method identifies which sone the domain is deployed to, and decides if the application name
            should contain the sone as a suffix.
            Either "-fss", "-sbs" or "".
         */
        String nameAddition = "";
        boolean domainIs3Gen = (envDomain != null);
        // only 3gen domains can be in both sones, have to check envDomain
        if(domainIs3Gen){
            //check if envDomain contains the word oera
            if(envDomain.toLowerCase().contains("oera".toLowerCase())){
                nameAddition = "-sbs";
            }else{
                nameAddition = "-fss";
            }
        }
        return nameAddition;

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
            conn.getOutputStream().write(generatedJsonString.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();



            getLog().debug("Made a Http-post towards " + path);
            getLog().debug("    JSON-body:" + generatedJsonString);
            getLog().debug("    Response code: " + conn.getResponseCode());
            getLog().debug("    Response message: " + conn.getResponseMessage());


            conn.disconnect();
        }catch(IOException e){
            e.printStackTrace();
        }
        return conn;
    }
}
