package no.stelvio.esb.models.transformation.diagram2img.utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class PublishFileUtils {

	public Logger logger = Logger.getLogger("PublishFileUtils");
	public static final String SHARED_DIAGRAM_FOLDER_NAME = "diagrams";
	public static final String DIAGRAM_IMAGE_FILE_FORMAT = "png";

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
}
