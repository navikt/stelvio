package no.nav.maven.plugin.wpsdeploy.plugin.utils;

import java.io.File;

public class JarExtractor {

	public static void extractJar(File jarFile, File outputDir, String desiredFileOrFolder){
		if(outputDir.isDirectory())
			outputDir.mkdir();
		String cmd = "unzip "+jarFile.getAbsolutePath()+" "+desiredFileOrFolder +" -d "+outputDir.getAbsolutePath();
		CommandUtil.runCommand(cmd);
	}
	
	public static void extractJar(File jarFile, File outputDir){
		extractJar(jarFile, outputDir, "");
	}
}
