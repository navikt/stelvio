package no.nav.maven.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import freemarker.template.TemplateException;
import no.nav.aura.service.discovery.EnvConfigServiceDiscovery;
import no.nav.aura.service.discovery.domain.WebServiceResource;
import no.nav.datapower.config.WSDLFile;
import no.nav.datapower.config.WSProxy;
import no.nav.datapower.config.freemarker.Freemarker;
import no.nav.datapower.util.DPCollectionUtils;
import no.nav.datapower.util.DPPropertiesUtils;
import no.nav.datapower.util.PropertiesBuilder;

/**
 * Goal which generates a DataPower configuration based on services discovered by using environment configuration in given
 * environment
 * 
 * @goal generateFromEnvConfig
 * @phase compile
 * 
 * @author Ismar Slomic, Accenture
 */
public class GenerateConfigFromEnvConfigMojo extends AbstractMojo {
    /**
     * The endpoint URL to environment configuration
     * 
     * @parameter expression="${envConfigBaseUrlString}"
     * @required
     * @readonly
     */
    private String envConfigBaseUrlString;
    /**
     * The name of the environment where to discover web services
     * 
     * @parameter expression="${env}"
     * @required
     * @readonly
     */
    private String env;

    /**
     * The domain of the environment where to discover web services
     * 
     * Example: devillo.no, test.local, oera.t
     * 
     * @parameter expression="${envDomain}"
     * @required
     * @readonly
     */
    private String envDomain;

    /**
     * The username to authenticate to environment configuration
     * 
     * @parameter expression="${username}"
     * @required
     * @readonly
     */
    private String username;

    /**
     * The password to authenticate to environment configuration
     * 
     * 
     * @parameter expression="${password}"
     * @required
     * @readonly
     */
    private String password;

    // ========================================

    /**
     * The maven project
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * secgw or partner-gw
     * 
     * @parameter expression="${gateway-name}"
     * @readonly
     * @required
     */
    private String gatewayName;

    /**
     * @parameter expression="${envclass}"
     * @required
     */
    protected String envclass;

    /** Output files and directories **/
    private File outputDirectory;
    private File wsdlOutputDirectory;
    private File certFile;
    private File configFile;
    private File mainTemplateFile;

    private final String PROXIES_PROPERTY_KEY_NAME = "GeneralProxies";

    /**
     * The mojo method doing the actual work when goal is invoked
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // 1. Initialize paths for directories and files
        initializeDirAndFiles();

        // 2. Load properties from certificate file
        Properties properties = loadProperties(certFile);

        // 3. Expand trust ceriticate property previously loaded
        expandTrustCertificateProperty(properties);

        // 4. Discover and map wsdls to wsdl proxies
        mapWsdlsToProxies(properties);

        // 5. Merge templates with properties and output to config file
        try {
            processFreemarkerTemplates(mainTemplateFile, configFile, properties);
        } catch (IOException e) {
            throw new IllegalStateException("Caught IOException while generating DataPower configuration", e);
        } catch (TemplateException e) {
            throw new IllegalStateException("Caught IllegalStateException while generating DataPower configuration", e);
        }
    }

    private void mapWsdlsToProxies(Properties properties) {
        // 1. Discover services and get WSDLFile objects
        URI envConfigBaseUrl = null;
        try {
            envConfigBaseUrl = new URI(envConfigBaseUrlString);
        } catch (URISyntaxException e) {
            getLog().error("URL to environment configuration is not valid URI", e);
        }
        Set<WSDLFile> wsdlFiles = discover(envConfigBaseUrl, env, envDomain, username, password, wsdlOutputDirectory);
        
        // 2. Create WSProxy objects based on WSDLFile objects and put them in properties
        Collection<WSProxy> proxies = getProxies(wsdlFiles);
        String proxiesPropertyKeyName = PROXIES_PROPERTY_KEY_NAME;
        properties.put(proxiesPropertyKeyName, proxies);

        getLog().info("Added " + proxies.size() + " proxies to template variable " + proxiesPropertyKeyName);
    }

    private void initializeDirAndFiles()
    {
        outputDirectory = new File(project.getBuild().getOutputDirectory());
        wsdlOutputDirectory = new File(outputDirectory, "files/local/wsdl");
        // TODO: I GenerateConfigMojo sjekkes det om gateway er Partner eller Dialog for å sette riktig certFile path. Jeg har
        // tatt utgangspunkt at det er en av disse to gateways i pathen nedenfor
        certFile = new File(project.getBasedir(), "target/dependency/" + gatewayName + "-environment-configuration-jar/" + envclass + "/trustCerts.properties");
        File tempDir = new File(project.getBasedir(), "target/temp");
        tempDir.mkdir();
        configFile = new File(tempDir, "configuration.xml");
        mainTemplateFile = new File(project.getBasedir(), "target/dependency/" + gatewayName + "-nonenvironment-configuration-jar/freemarker-templates/main.ftl");
    }

    // TODO: GENERISK METODE SOM KAN SKILLES UT I EGEN KLASSE OG GJENBRUKES PÅ TVERS
    /**
     * Discovers services in given environment and returns WSDLFile objects
     * 
     * @param envConfigBaseUrl
     * @param env
     * @param envDomain
     * @param username
     * @param password
     * @param wsdlOutputDirectory
     * @return
     */
    public Set<WSDLFile> discover(URI envConfigBaseUrl, String env, String envDomain, String username, String password, File wsdlOutputDirectory)
    {
        getLog().info("Discovering web services in environment: " + env);

        // Discover web services by using environment configuration
        EnvConfigServiceDiscovery serviceDiscovery = new EnvConfigServiceDiscovery(envConfigBaseUrl, env, envDomain, username, password);
        WebServiceResource[] webServiceResources = serviceDiscovery.discoverAndDownload(wsdlOutputDirectory);
        getLog().info("Discovered and downloaded " + webServiceResources.length + " web service(s) from environment config");

        // Create WSDLFile objects
        Set<WSDLFile> wsdlFiles = new LinkedHashSet<WSDLFile>();
        for (WebServiceResource webServiceResource : webServiceResources) {
            WSDLFile wsdlFile = new WSDLFile(webServiceResource.getWsdlFile(), wsdlOutputDirectory.getParentFile());
            
            // OBS! TEKNISK GJELD!!!
            //
            // Setter frontside URI til samme URI som tjenesten bruker på egen host
            //
            // MULIG FOR 2 TJENESTER Å FÅ SAMME FRONTSIDE URI
            getLog().info("Frontside URI: " + webServiceResource.getEndpointUrl().getPath());
            wsdlFile.setFrontsideURI(webServiceResource.getEndpointUrl().getPath());
            
            
            wsdlFiles.add(wsdlFile);
        }

        return wsdlFiles;
    }

    // TODO: GENERISK METODE SOM KAN SKILLES UT I EGEN KLASSE OG GJENBRUKES PÅ TVERS
    private Collection<WSProxy> getProxies(Set<WSDLFile> wsdlFiles) {
        Map<String, WSProxy> proxies = new LinkedHashMap<String, WSProxy>();

        for (WSDLFile wsdl : wsdlFiles) {
            WSProxy proxy = new WSProxy(wsdl.getProxyName());
            if (proxies.containsKey(proxy.getName())) {
                proxy = proxies.get(proxy.getName());
                getLog().debug("Found existing new proxy " + proxy.getName() + " in WSDL " + wsdl.getPortType());
            } else {
                proxies.put(proxy.getName(), proxy);
                getLog().debug("Found new proxy " + proxy.getName() + " in WSDL " + wsdl.getPortType());
            }
            proxy.addWsdl(wsdl);
        }
        return proxies.values();
    }

    // TODO: GENERISK METODE SOM KAN SKILLES UT I EGEN KLASSE OG GJENBRUKES PÅ TVERS
    /**
     * Load properties from property file
     * 
     * @param propertiesFile
     *            the property file to read from
     * @return properties loaded
     */
    private Properties loadProperties(File propertiesFile) {
        getLog().info("Loading properties from file " + propertiesFile);
        Properties properties = DPPropertiesUtils.load(propertiesFile);
        PropertiesBuilder builder = new PropertiesBuilder();
        builder.putAll(properties);
        builder.interpolate();
        return builder.buildProperties();
    }

    // TODO: GENERISK METODE SOM KAN SKILLES UT I EGEN KLASSE OG GJENBRUKES PÅ TVERS
    // TODO: VIL PROPERTY NAVNET ALLTID VÆRE partnerTrustCerts, burde dette sendes inn som metodeparameter?
    /**
     * Expand trust certificate property and inject new expanded property
     * 
     * @param properties
     *            the properties where the trust certificate property to expand is defined
     */
    private void expandTrustCertificateProperty(Properties properties) {
        getLog().info("Expanding trust certification property");

        String trustCertPropertyKeyName = "partnerTrustCerts";
        String trustedCertPropertyKeyName = "partnerTrustedCerts";
        String trustCertProperty = properties.getProperty(trustCertPropertyKeyName);

        if (trustCertProperty != null) {
            List<String> trustCertList = DPCollectionUtils.listFromString(trustCertProperty);
            List<Map<String, String>> trustCertMapList = DPCollectionUtils.newArrayList();
            for (String trustCert : trustCertList) {
                Map<String, String> cert = DPCollectionUtils.newHashMap();
                cert.put("name", trustCert.substring(trustCert.lastIndexOf("/") + 1));
                cert.put("file", trustCert);
                trustCertMapList.add(cert);
            }
            properties.put(trustedCertPropertyKeyName, trustCertMapList);
        }
    }

    // TODO: GENERISK METODE SOM KAN SKILLES UT I EGEN KLASSE OG GJENBRUKES PÅ TVERS
    /**
     * 
     * @param freemarkerTemplateFile
     *            the freemarker template file
     * @param configFile
     *            the configuration file
     * @param properties
     *            properties
     * @throws IOException
     * @throws TemplateException
     */
    private void processFreemarkerTemplates(File freemarkerTemplateFile, File configFile, Properties properties) throws IOException, TemplateException {
        getLog().info("Processing freemarker teamplate");
        Freemarker freemarker = new Freemarker(freemarkerTemplateFile.getParentFile());
        FileWriter writer = new FileWriter(configFile);
        freemarker.processTemplate(freemarkerTemplateFile.getName(), properties, writer);
        writer.flush();
        writer.close();
    }
}