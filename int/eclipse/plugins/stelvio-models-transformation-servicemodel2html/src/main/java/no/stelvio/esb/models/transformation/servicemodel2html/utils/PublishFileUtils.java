package no.stelvio.esb.models.transformation.servicemodel2html.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class PublishFileUtils {

	public Logger logger = Logger.getLogger("PublishFileUtils");
	public static final String TEMPLATE_FOLDER_NAME = "src/main/resources/html-template";
	public static final String SHARED_IMAGES_FOLDER_NAME = "images";
	public static final String SHARED_JAVASCRIPT_FOLDER_NAME = "js";
	public static final String SHARED_CSS_FOLDER_NAME = "stylesheets";
	public static final String SHARED_DIAGRAM_FOLDER_NAME = "diagrams";
	public static final String SHARED_ATTACHMENT_FOLDER_NAME = "attachments";
	public static final String DIAGRAM_IMAGE_FILE_FORMAT = "png";
	//public static final String
	public static Bundle bundle = Platform.getBundle("no.stelvio.esb.models.transformation.servicemodel2html");
	
	/****************** CSS *********************/

	private static IPath getCssDirPath()
	{
		IPath cssDirPath = new Path(TEMPLATE_FOLDER_NAME);
		cssDirPath = cssDirPath.append(SHARED_CSS_FOLDER_NAME);
		return cssDirPath;
	}
	
	private static IPath getOutputCssDirPath(File outputDirectory)
	{
		IPath outputDirPath = new Path(outputDirectory.getAbsolutePath());
		outputDirPath = outputDirPath.append(SHARED_CSS_FOLDER_NAME);
		
		return outputDirPath;
	}
	
	public static void copyCssDirectory(File outputDirectory)
			throws IOException, URISyntaxException {
		IPath outputDirPath = getOutputCssDirPath(outputDirectory);
		IPath cssDirPath = getCssDirPath();

		File cssDirFile = getBoundleFileForPath(cssDirPath);
		FileUtils.copyDirectory(cssDirFile, outputDirPath.toFile());
	}

	public static String getRelativePathToCssDir(String outputDirPath, String currentFilePath) 
	{
		File outputDirectory = new File(outputDirPath);
		IPath cssDirPath = getOutputCssDirPath(outputDirectory);
		
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile,cssDirPath).toString();
	}

	/******************** JAVASCRIPT *********************/

	private static IPath getJavascriptDirPath()
	{
		IPath javascriptDirPath = new Path(TEMPLATE_FOLDER_NAME);
		javascriptDirPath = javascriptDirPath.append(SHARED_JAVASCRIPT_FOLDER_NAME);
		return javascriptDirPath;
	}
	
	private static IPath getOutputJavascriptDirPath(File outputDirectory)
	{
		IPath outputDirPath = new Path(outputDirectory.getAbsolutePath());
		outputDirPath = outputDirPath.append(SHARED_JAVASCRIPT_FOLDER_NAME);
		
		return outputDirPath;
	}
	
	public static void copyJavascriptDirectory(File outputDirectory)throws IOException 
	{
		IPath outputDirPath = getOutputJavascriptDirPath(outputDirectory);
		IPath javascriptDirPath = getJavascriptDirPath();
		
		File javascriptDirFile = getBoundleFileForPath(javascriptDirPath);	
		FileUtils.copyDirectory(javascriptDirFile, outputDirPath.toFile());
	}

	public static String getRelativePathToJavascriptDir(String outputDirPath, String currentFilePath) 
	{	
		File outputDirectory = new File(outputDirPath);
		IPath javascriptDirPath = getOutputJavascriptDirPath(outputDirectory);
		
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile,javascriptDirPath).toString();
	}
	
	/******************** IMAGES *********************/
	
	private static IPath getImagesDirPath()
	{
		IPath cssDirPath = new Path(TEMPLATE_FOLDER_NAME);
		cssDirPath = cssDirPath.append(SHARED_IMAGES_FOLDER_NAME);
		return cssDirPath;
	}
	
	private static IPath getOutputImagesDirPath(File outputDirectory)
	{
		IPath outputDirPath = new Path(outputDirectory.getAbsolutePath());
		outputDirPath = outputDirPath.append(SHARED_IMAGES_FOLDER_NAME);
		
		return outputDirPath;
	}
	
	public static void copyImagesDirectory(File outputDirectory)throws IOException 
	{
		IPath outputDirPath = getOutputImagesDirPath(outputDirectory);
		IPath imagesDirPath = getImagesDirPath();
		
		File imagesDirFile = getBoundleFileForPath(imagesDirPath);	
		FileUtils.copyDirectory(imagesDirFile, outputDirPath.toFile());
	}

	public static String getRelativePathToImagesDir(String outputDirPath, String currentFilePath) 
	{	
		File outputDirectory = new File(outputDirPath);
		IPath imagesDirPath = getOutputImagesDirPath(outputDirectory);
		
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile,imagesDirPath).toString();
	}

	/******************** WEB LINKS *********************/
	public static IPath makeRelativeWebLink(File fromFile, File toFile) {
		IPath fromPath = new Path(fromFile.getPath());
		IPath toPath = new Path(toFile.getPath());

		return makeRelativeWebLink(fromPath, toPath);
	}

	public static IPath makeRelativeWebLink(IPath fromPath, IPath toPath) {
		if (fromPath.getFileExtension() != null)
			fromPath = fromPath.removeLastSegments(1);

		return toPath.makeRelativeTo(fromPath);
	}

	/********************* Service operation *************************/

	protected static IPath getOperationFilePath(File outputDirectory,
			String interfaceNamespace, String interfaceName,
			String operationName) throws URISyntaxException {
		URI interfaceNamespaceURI = new URI(interfaceNamespace);

		IPath operationFilePath = new Path(outputDirectory.getAbsolutePath());
		operationFilePath = operationFilePath.append(interfaceNamespaceURI
				.getSchemeSpecificPart());
		operationFilePath = operationFilePath.append(interfaceName);
		operationFilePath = operationFilePath.append(operationName);
		operationFilePath = operationFilePath.addFileExtension("html");

		return operationFilePath;
	}

	public static File createOperationFile(File outputDirectory,
			String interfaceNamespace, String interfaceName,
			String operationName) throws IOException, URISyntaxException 
	{
		IPath operationFilePath = getOperationFilePath(outputDirectory,
				interfaceNamespace, interfaceName, operationName);
		
		File operationFile = operationFilePath.toFile();
		File operationDir = new File(FileUtils.getFullPath(operationFilePath.toString()));
		
		if(!operationDir.isDirectory())
		{
			operationDir.mkdirs();
		}

		operationFile.createNewFile();
		
		return operationFile;
	}
	
	public static String getLinkToOperation(String outputDirPath, String currentFilePath, 
			String interfaceNamespace, String interfaceName, String operationName) throws URISyntaxException
	{
		File outputDirectory = new File(outputDirPath);
		IPath toOperationFilePath = getOperationFilePath(outputDirectory, interfaceNamespace, interfaceName, operationName);
		
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toOperationFilePath).toString();
	}
	
	/********************** Complex types *******************************/
	
	public static IPath getComplextypeFilePath(File outputDirectory, String namespace, String name) 
	throws URISyntaxException
	{
		URI complextypeNamespaceURI = new URI(namespace);
		
		IPath complextypeFilePath = new Path(outputDirectory.getAbsolutePath());
		complextypeFilePath = complextypeFilePath.append(complextypeNamespaceURI.getSchemeSpecificPart());
		complextypeFilePath = complextypeFilePath.append(name);
		complextypeFilePath = complextypeFilePath.addFileExtension("html");
		
		return complextypeFilePath;
	}
	
	public static File createComplextypeFile(File outputDirectory, String namespace, String name) 
	throws IOException, URISyntaxException 
	{
		IPath complextypeFilePath = getComplextypeFilePath(outputDirectory, namespace, name);
		
		File complextypeFile = complextypeFilePath.toFile();
		File complextypeDir = new File(FileUtils.getFullPath(complextypeFilePath.toString()));
		
		if(!complextypeDir.isDirectory())
		{
			complextypeDir.mkdirs();
		}

		complextypeFile.createNewFile();
		
		return complextypeFile;
	}
	
	public static String getLinkToComplextype(String outputDirPath, String currentFilePath, 
			String namespace, String name) throws URISyntaxException
	{
		File outputDirectory = new File(outputDirPath);
		IPath toComplextypeFilePath = getComplextypeFilePath(outputDirectory, namespace, name);
		
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toComplextypeFilePath).toString();
	}
	
	/********************** Diagrams ******************************************************/
	public static IPath getDiagramFilePath(File outputDirectory, String diagramUUID)
	{	
		IPath diagramFilePath = new Path(outputDirectory.getAbsolutePath());
		diagramFilePath = diagramFilePath.append(SHARED_DIAGRAM_FOLDER_NAME);
		diagramFilePath = diagramFilePath.append(diagramUUID);
		diagramFilePath = diagramFilePath.addFileExtension(DIAGRAM_IMAGE_FILE_FORMAT);
		
		return diagramFilePath;
	}
	
	public static File createDiagramFile(File outputDirectory, String diagramUUID) throws IOException
	{
		IPath diagramFilePath = getDiagramFilePath(outputDirectory, diagramUUID);
		
		File diagramFile = diagramFilePath.toFile();
		File diagramDir = new File(FileUtils.getFullPath(diagramFilePath.toString()));
		
		if(!diagramDir.isDirectory())
		{
			diagramDir.mkdirs();
		}

		diagramFile.createNewFile();
		
		return diagramFile;
	}
	
	public static String getLinkToDiagram(String outputDirPath, String currentFilePath, String diagramUUID)
	{
		File outputDirectory = new File(outputDirPath);
		IPath toDiagramFilePath = getDiagramFilePath(outputDirectory, diagramUUID);
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toDiagramFilePath).toString();
	}
	
	/********************** Attachments ******************************************************/
	public static IPath getAttachmentFilePath(File outputDirectory, String filePath)
	{	
		IPath attachementFilePath = new Path(outputDirectory.getAbsolutePath());
		attachementFilePath = attachementFilePath.append(SHARED_ATTACHMENT_FOLDER_NAME);
		attachementFilePath = attachementFilePath.append(filePath);
		return attachementFilePath;
	}
	
	public static String getLinkToAttachment(String outputDirPath, String currentFilePath, String filePath)
	{
		File outputDirectory = new File(outputDirPath);
		IPath toAttachmentFilePath = getAttachmentFilePath(outputDirectory, filePath);
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toAttachmentFilePath).toString();
	}
	
	/********************** Front pages ******************************************************/
	public static IPath getFrontpageFilePath(File outputDirectory)
	{
		IPath frontpageFilePath = new Path(outputDirectory.getAbsolutePath());
		frontpageFilePath = frontpageFilePath.append("index");
		frontpageFilePath = frontpageFilePath.addFileExtension("html");
		
		return frontpageFilePath;
	}
	
	public static IPath getServicemodelFrontpageFilePath(File outputDirectory)
	{
		IPath frontpageFilePath = new Path(outputDirectory.getAbsolutePath());
		frontpageFilePath = frontpageFilePath.append("servicemodels");
		frontpageFilePath = frontpageFilePath.addFileExtension("html");
		
		return frontpageFilePath;
	}
	
	public static IPath getInformationmodelFrontpageFilePath(File outputDirectory)
	{
		IPath frontpageFilePath = new Path(outputDirectory.getAbsolutePath());
		frontpageFilePath = frontpageFilePath.append("informationmodels");
		frontpageFilePath = frontpageFilePath.addFileExtension("html");
		
		return frontpageFilePath;
	}
	
	/**
	 * Creates HTML-file for the frontpage (including output/destination folders if they doesn't exist)
	 * 
	 * @param outputDirectory the directory where to place the frontpage file
	 * @return created file
	 * @throws IOException
	 */
	public static File createFrontpageFile(File outputDirectory) throws IOException
	{
		IPath frontpageFilePath = getFrontpageFilePath(outputDirectory);
		
		File frontpageFile = frontpageFilePath.toFile();
		File frontpageDir = new File(FileUtils.getFullPath(frontpageFilePath.toString()));
		
		if(!frontpageDir.isDirectory())
		{
			frontpageDir.mkdirs();
		}

		frontpageFile.createNewFile();
		
		return frontpageFile;
	}

	
	/**
	 * Creates HTML-file for the servicemodel frontpage (including output/destination folders if they doesn't exist)
	 * 
	 * @param outputDirectory the directory where to place the frontpage file
	 * @return created file
	 * @throws IOException
	 */
	public static File createServicemodelFrontpageFile(File outputDirectory) throws IOException
	{
		IPath frontpageFilePath = getServicemodelFrontpageFilePath(outputDirectory);
		
		File frontpageFile = frontpageFilePath.toFile();
		File frontpageDir = new File(FileUtils.getFullPath(frontpageFilePath.toString()));
		
		if(!frontpageDir.isDirectory())
		{
			frontpageDir.mkdirs();
		}

		frontpageFile.createNewFile();
		
		return frontpageFile;
	}
	
	/**
	 * Creates HTML-file for the informationmodel frontpage (including output/destination folders if they doesn't exist)
	 * 
	 * @param outputDirectory the directory where to place the frontpage file
	 * @return created file
	 * @throws IOException
	 */
	public static File createInformationmodelFrontpageFile(File outputDirectory) throws IOException
	{
		IPath frontpageFilePath = getInformationmodelFrontpageFilePath(outputDirectory);
		
		File frontpageFile = frontpageFilePath.toFile();
		File frontpageDir = new File(FileUtils.getFullPath(frontpageFilePath.toString()));
		
		if(!frontpageDir.isDirectory())
		{
			frontpageDir.mkdirs();
		}

		frontpageFile.createNewFile();
		
		return frontpageFile;
	}
	
	public static String getLinkToFrontpage(String outputDirPath, String currentFilePath)
	{
		File outputDirectory = new File(outputDirPath);
		IPath toFrontpageFilePath = getFrontpageFilePath(outputDirectory);
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toFrontpageFilePath).toString();
	}
	
	public static String getLinkToServicemodelFrontpage(String outputDirPath, String currentFilePath)
	{
		File outputDirectory = new File(outputDirPath);
		IPath toFrontpageFilePath = getServicemodelFrontpageFilePath(outputDirectory);
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toFrontpageFilePath).toString();
	}
	
	public static String getLinkToInformationmodelFrontpage(String outputDirPath, String currentFilePath)
	{
		File outputDirectory = new File(outputDirPath);
		IPath toFrontpageFilePath = getInformationmodelFrontpageFilePath(outputDirectory);
		IPath currentFile = new Path(currentFilePath);
		
		return makeRelativeWebLink(currentFile, toFrontpageFilePath).toString();
	}
	
	/********************** Eclipse Plug-in boundle resources ******************************************************/
	
	public static File getBoundleFileForPath(IPath filePath) throws IOException
	{
		URL fileUrl = bundle.getResource(filePath.toString());		
		URL boundleFileUrl = FileLocator.toFileURL(fileUrl);
		File boundleFile = new File(boundleFileUrl.getFile());
		
		return boundleFile;
	}
	
}
