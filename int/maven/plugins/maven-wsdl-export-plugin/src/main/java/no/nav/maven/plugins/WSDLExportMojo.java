package no.nav.maven.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Goal which touches a timestamp file.
 * 
 * @goal exportwsdl
 * 
 * @phase package
 */
public class WSDLExportMojo extends AbstractMojo {
	private static final String ZIP_SUFFIX = "zip";

	private static final String WAR_SUFFIX = "war";

	private static final String WSDL_PATH_IN_WAR = "/META-INF/wsdl";

	private static final String WSDL_INTERFACE_ARTIFACT_TYPE = "wsdl-interface";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private Archiver archiver;

	/**
	 * @component roleHint="wsdl-interface"
	 */
	private ArtifactHandler wsdlInterfaceArtifactHandler;

	/**
	 * @component
	 */
	private ArtifactHandlerManager artifactHandlerManager;

	/**
	 * @component roleHint="zip"
	 * @required
	 * @readonly
	 */
	private UnArchiver unArchiver;

	
	


	/**
	 * EAR-file.
	 * 
	 * @parameter expression="${project.artifact.file}"
	 */
	private File earFile;

	public void execute() throws MojoExecutionException {
		// This "stupid" if test is here because I want to configure the plugin
		// execution element i parent POMs
		if (!"pom".equals(project.getPackaging())) {
			executeInternal();
		}
	}

	private void executeInternal() throws MojoExecutionException {
		try {    
			if (!wsdlInterfaceArtifactHandler.equals(artifactHandlerManager
					.getArtifactHandler(WSDL_INTERFACE_ARTIFACT_TYPE))) {
				getLog()
						.debug(
								"Adding wsdlif interchange artifact handler to artifact handler manager");
				artifactHandlerManager.addHandlers(Collections.singletonMap(
						WSDL_INTERFACE_ARTIFACT_TYPE,
						wsdlInterfaceArtifactHandler));
			}
			unArchiver.setSourceFile(earFile);
			String tempDir = project.getBuild().getDirectory() + "/wsdltemp/"
					+ System.currentTimeMillis() + "/";
			File tempDirfile = new File(tempDir);
			tempDirfile.mkdirs();

			unArchiver.setDestDirectory(tempDirfile);
			try {
				unArchiver.extract();
			} catch (Exception e) {
				throw new RuntimeException("Unable to unzip ear file "
						+ earFile.getPath() + " to directory "
						+ unArchiver.getDestDirectory(), e);
			}
			String[] warFileNames = tempDirfile.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(WAR_SUFFIX);
				}
			});
			if (warFileNames.length != 0) { // Does this module have WSDL
				// exports?
				String warFileName = tempDir + warFileNames[0];
				File warFile = new File(warFileName);
				unArchiver.setSourceFile(warFile);
				unArchiver.extract();
				
				File wsdlDirectory=new File(tempDir+WSDL_PATH_IN_WAR);				
				Map<String, String> namespaceToPackageMap=createNameSpaceToPackageMapFromWSDLDirectory(wsdlDirectory);
				File nameSpaceToPackageFile=new File(project.getBuild().getDirectory(), project.getBuild().getFinalName()+"-NStoPkg.properties");			
				PrintWriter pw=new PrintWriter(new BufferedOutputStream(new FileOutputStream(nameSpaceToPackageFile)));
				for (String namespace:namespaceToPackageMap.keySet()){
					pw.write(namespace+"="+namespaceToPackageMap.get(namespace)+"\n");
				}
				pw.close();
				
				
				File wsdlZipArtifactFile = new File(project.getBuild().getDirectory(), project.getBuild().getFinalName()+"-"+wsdlInterfaceArtifactHandler.getClassifier()
						+ "." + ZIP_SUFFIX);
				archiver.setDestFile(wsdlZipArtifactFile);

				archiver.addDirectory(wsdlDirectory);
				archiver.addFile(nameSpaceToPackageFile,"NStoPkg.properties"); // project.getBuild().getDirectory()+"/"+ project.getBuild().getFinalName()+"-
				archiver.createArchive();
				projectHelper.attachArtifact(project,
						WSDL_INTERFACE_ARTIFACT_TYPE,
						wsdlInterfaceArtifactHandler.getClassifier(),
						wsdlZipArtifactFile);
			}
		} catch (Exception e) {
			throw new MojoExecutionException(
					"Creating wsdl export file failed ", e);
		}

		   	
    }

    public Map<String,String> createNameSpaceToPackageMapFromWSDLDirectory(File wsdlDirectory){
    	HashMap<String, String> map=new HashMap<String,String>();
    	try {
			createNameSpaceToPackageMap(wsdlDirectory, map);
		} catch (IOException e) {			
			throw new RuntimeException(e);
		}
    	return map;
    }
    
	private void createNameSpaceToPackageMap(File file, Map<String,String> nameSpaceMap) throws IOException {
		if (file.isDirectory()){
			for (File f:file.listFiles()){
				createNameSpaceToPackageMap(f, nameSpaceMap);
			}
		}else
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			
			final byte[] bytes=new byte[(int) file.length()];
			bis.read(bytes);
			bis.close();
			String fileString=new String(bytes);
			Pattern p=Pattern.compile("\"http://([^\"]+)\"");
			Matcher m=p.matcher(fileString);
			while (m.find()){							
				String nameSpace=m.group(1);
				String packageName=generatePackageNameFromNamespace(nameSpace);
				if (packageName!=null){
					String escapedNameSpaceUrl="http\\://"+nameSpace;
					nameSpaceMap.put(escapedNameSpaceUrl, packageName);
				}
			}	
		}
		
	}
	
	private String generatePackageNameFromNamespace(String nameSpace){
		String[] parts = nameSpace.split("/");
		//Check if this is something else than a namespace
		if (parts[0].startsWith("www")|| parts[0].startsWith("localhost")||parts[0].endsWith(".org")) return null;
		
		String packageName=null;
		//Skip parts[0], since this is the module name. Add the other parts, dot-separated;
		for (int i=1;i<parts.length;i++){
			if (packageName==null){
				packageName=parts[i];
			}else{
				packageName=packageName+"."+parts[i];
			}
		}
		return packageName;
			
	}
}
