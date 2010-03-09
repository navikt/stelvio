package no.nav.datapower.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class FileUtilsTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File wsdlArchive = new File("E:\\Develop\\wsdl\\wsdl-pselv-kjempen.zip");
		File outputDirectory = new File("E:\\Develop\\wsdl\\tmp\\");
		//File importDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());
		//File wsdlDirectory = new File(importDirectory.getAbsolutePath() + "\\wsdl");
		File wsdlDirectory = new File(outputDirectory.getAbsolutePath() + "\\" + new Date().getTime());

		try {
			//deviceProps.load(new FileInputStream(new File("")));
			//envProps.load(new FileInputStream(new File("")));
			System.out.println("Extracting ZIP archive to directory '" + wsdlDirectory + "'");
			DPFileUtils.extractArchive(wsdlArchive, wsdlDirectory);
			File wsdlDir = DPFileUtils.findDirectory(wsdlDirectory, "wsdl");
			List<File> folders = DPFileUtils.getRelativeFolderPaths(wsdlDirectory, wsdlDir);
			for (File folder : folders) {
				System.out.println("Folder relativePath = " + DPFileUtils.replaceSeparator(folder, '/'));
			}
//			List<File> files = FileUtils.getRelativeFilePaths(wsdlDirectory, wsdlDir);
//			for (File file : files) {
//				System.out.println("File relativePath = " + file);
//			}
			File[] wsdlFiles = wsdlDir.listFiles();
			System.out.println("Listing " + wsdlFiles.length + " files in directory");		
			for (File wsdlFile : wsdlFiles) {
				System.out.println("Listing files in wsdlDirectory, " + wsdlFile.getName());
//				if (wsdlFile.getName().endsWith(".wsdl")) {
//					Definition def = WSDLUtils.getDefinition(wsdlFile.getPath());
//					System.out.println("Binding = " + def.getAllBindings().entrySet().iterator().next());
//					System.out.println("PortType = " + def.getAllPortTypes().entrySet().iterator().next());
//					System.out.println("Service = " + def.getAllServices().entrySet().iterator().next());
//				}
			}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
